package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.payment.PaymentIntentRequestDto;
import org.example.dto.payment.PaymentIntentResponseDto;
import org.example.dto.payment.PaymentSessionResponseDto;
import org.example.service.payment.PaymentService;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Payment management",
        description = "Endpoints for processing order payments")
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{orderId}")
    @Operation(summary = "Create payment session",
            description = "Generate a Stripe checkout session URL for an order")
    public PaymentSessionResponseDto createPaymentSession(@PathVariable Long orderId) {
        return paymentService.createPaymentSession(orderId);
    }

    @GetMapping("/success")
    @Operation(summary = "Confirm payment",
            description = "Callback endpoint triggered after successful checkout")
    public void confirmPayment(@RequestParam("order_id") Long orderId) {
        paymentService.handlePaymentSuccess(orderId);
    }

    @PostMapping("/create-payment-intent")
    @Operation(summary = "Create payment intent",
            description = "Generate a Stripe client secret for custom frontend payment flow")
    public PaymentIntentResponseDto createPaymentIntent(@RequestBody @Valid
                                                            PaymentIntentRequestDto request) {
        return paymentService.createPaymentIntent(request);
    }
}
