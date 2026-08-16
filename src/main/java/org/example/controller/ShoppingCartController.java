package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.cart_item.CartItemRequestDto;
import org.example.dto.cart_item.UpdateCartItemDto;
import org.example.dto.shopping_cart.ShoppingCartDto;
import org.example.dto.shopping_cart.ShoppingCartWithCountersDto;
import org.example.service.shopping_cart.ShoppingCartService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management",
        description = "Endpoints for managing user's shopping cart")
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @Operation(summary = "Get shopping cart",
            description = "Retrieve user's shopping cart")
    public ShoppingCartDto getShoppingCart() {
        return shoppingCartService.getCart();
    }

    @GetMapping("/manage")
    @Operation(summary = "Get shopping cart with subtotal per item and a total price counter",
            description = "Retrieve user's shopping cart with subtotal per item and a total price counter")
    public ShoppingCartWithCountersDto getShoppingCartWithCounters() {
        return shoppingCartService.getCartWithCounters();
    }

    @PostMapping
    @Operation(summary = "Add wine to cart",
            description = "Add wine to the shopping cart")
    public ShoppingCartDto addWineToTheCart(@RequestBody @Valid CartItemRequestDto request) {
        return shoppingCartService.addWine(request);
    }

    @PutMapping("/items/{cartItemId}")
    @Operation(summary = "Update quantity of wine in the shopping cart",
            description = "Update quantity of wine in the shopping cart  by its id")
    public ShoppingCartDto updateWineQuantityInCart(@PathVariable Long cartItemId,
                                                    @RequestBody @Valid UpdateCartItemDto request) {
        return shoppingCartService.updateQuantity(cartItemId, request);
    }

    @DeleteMapping("/items/{cartItemId}")
    @Operation(summary = "Remove wine from the shopping cart",
            description = "Remove wine from the shopping cart by its id")
    public void deleteWineFromTheCart(@PathVariable Long cartItemId) {
        shoppingCartService.deleteWine(cartItemId);
    }
}
