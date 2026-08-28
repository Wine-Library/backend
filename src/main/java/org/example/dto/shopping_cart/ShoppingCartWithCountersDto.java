package org.example.dto.shopping_cart;

import java.math.BigDecimal;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.example.dto.cart_item.CartItemWithCounterResponseDto;

@Getter
@Setter
@Accessors(chain = true)
public class ShoppingCartWithCountersDto {
    private Long id;
    private Long userId;
    private Set<CartItemWithCounterResponseDto> cartItems;
    private BigDecimal totalPrice;
}
