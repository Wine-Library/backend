package org.example.dto.payment;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record PaymentIntentRequestDto(
        @NotNull(message = "${validation.payment-intent.invalid}")
        @DecimalMin(value = "0.50", message = "${validation.payment-amount.min}")
        BigDecimal amount,

        String currency
) {
}
