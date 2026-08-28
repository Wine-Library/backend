package org.example.service.payment;

import org.example.dto.order.PaymentSessionResponseDto;

public interface PaymentService {

    PaymentSessionResponseDto createPaymentSession(Long orderId);

    void handlePaymentSuccess(Long orderId);


}
