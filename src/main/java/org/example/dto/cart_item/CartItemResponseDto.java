package org.example.dto.cart_item;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CartItemResponseDto {
    private Long id;
    private Long wineId;
    private int quantity;
}
