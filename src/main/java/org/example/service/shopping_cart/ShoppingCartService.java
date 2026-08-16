package org.example.service.shopping_cart;

import org.example.dto.cart_item.CartItemRequestDto;
import org.example.dto.cart_item.UpdateCartItemDto;
import org.example.dto.shopping_cart.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto getCart();

    ShoppingCartDto addWine(CartItemRequestDto request);

    ShoppingCartDto updateQuantity(Long cartItemId, UpdateCartItemDto request);

    void deleteWine(Long cartItemId);
}
