package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.dto.order.PaymentSessionResponseDto;
import org.example.service.payment.PaymentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
