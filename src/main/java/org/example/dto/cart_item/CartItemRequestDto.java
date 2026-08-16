package org.example.dto.cart_item;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CartItemRequestDto {
    @Positive
    private Long wineId;
    @Positive
    @NotNull
    private int quantity;
}
