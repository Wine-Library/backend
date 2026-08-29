package org.example.service.payment;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.example.dto.payment.PaymentIntentRequestDto;
import org.example.dto.payment.PaymentIntentResponseDto;
import org.example.dto.payment.PaymentSessionResponseDto;
import org.example.exception.PaymentProcessingException;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.example.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;

    @Value("${stripe.api-key}")
    private String stripeApiKey;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    @Override
    @Transactional
    public PaymentSessionResponseDto createPaymentSession(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order not found with id: " + orderId));

        SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(frontendUrl
                        + "/payments/success?session_id={CHECKOUT_SESSION_ID}&order_id="
                        + order.getId())
                .setCancelUrl(frontendUrl
                        + "/payments/cancel?order_id=" + order.getId());

        for (OrderItem item : order.getOrderItems()) {
            long unitAmountInCents = item.getPrice()
                    .multiply(BigDecimal.valueOf(100))
                    .longValue();

            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                    .setQuantity((long) item.getQuantity())
                    .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("usd")
                                    .setUnitAmount(unitAmountInCents)
                                    .setProductData(
                                            SessionCreateParams
                                                    .LineItem
                                                    .PriceData
                                                    .ProductData
                                                    .builder()
                                                    .setName(item.getWine().getWineName())
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

            paramsBuilder.addLineItem(lineItem);
        }

        try {
            Session session = Session.create(paramsBuilder.build());
            return new PaymentSessionResponseDto(session.getId(), session.getUrl());
        } catch (StripeException e) {
            throw new RuntimeException("Failed to create Stripe payment session", e);
        }
    }

    @Override
    @Transactional
    public void handlePaymentSuccess(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + orderId));

        order.setStatus(Order.Status.COMPLETED);
        orderRepository.save(order);
    }

    @Override
    public PaymentIntentResponseDto createPaymentIntent(PaymentIntentRequestDto request) {
        long amountInCents = request.amount().multiply(BigDecimal.valueOf(100)).longValue();

        String currency = (request.currency() != null && !request.currency().isBlank())
                ? request.currency().toLowerCase()
                : "usd";

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency(currency)
                .build();

        try {
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            return new PaymentIntentResponseDto(paymentIntent.getClientSecret());
        } catch (StripeException e) {
            throw new PaymentProcessingException("Failed to create Stripe PaymentIntent for "
                    + request);
        }
    }
}
