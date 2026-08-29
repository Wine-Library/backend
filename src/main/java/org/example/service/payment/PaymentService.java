package org.example.service.payment;

import org.example.dto.payment.PaymentIntentRequestDto;
import org.example.dto.payment.PaymentIntentResponseDto;
import org.example.dto.payment.PaymentSessionResponseDto;

public interface PaymentService {

    PaymentSessionResponseDto createPaymentSession(Long orderId);

    void handlePaymentSuccess(Long orderId);

    PaymentIntentResponseDto createPaymentIntent(PaymentIntentRequestDto request);
}
