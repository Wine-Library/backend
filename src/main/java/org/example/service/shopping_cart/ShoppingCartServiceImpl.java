package org.example.service.shopping_cart;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.dto.cart_item.CartItemRequestDto;
import org.example.dto.cart_item.CartItemWithCounterResponseDto;
import org.example.dto.cart_item.UpdateCartItemDto;
import org.example.dto.shopping_cart.ShoppingCartDto;
import org.example.dto.shopping_cart.ShoppingCartWithCountersDto;
import org.example.dto.cart_item.UpdateCartItemDto;
import org.example.dto.shopping_cart.ShoppingCartDto;
import org.example.mapper.ShoppingCartMapper;
import org.example.model.CartItem;
import org.example.model.ShoppingCart;
import org.example.model.User;
import org.example.model.Wine;
import org.example.repository.CartItemRepository;
import org.example.repository.ShoppingCartRepository;
import org.example.repository.WineRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Transactional
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemRepository cartItemRepository;
    private final WineRepository wineRepository;

    @Override
    public ShoppingCartDto getCart() {
        Long currentUserId = getCurrentUserId();

        ShoppingCart cart = shoppingCartRepository
                .findByUserId(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart not found for user " + currentUserId));

        return shoppingCartMapper.toDto(cart);
    }

    @Override
    public ShoppingCartWithCountersDto getCartWithCounters() {
        Long currentUserId = getCurrentUserId();

        ShoppingCart cart = shoppingCartRepository
                .findByUserId(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart not found for user " + currentUserId));
        Set<CartItemWithCounterResponseDto> cartItemsWithCounter = new HashSet<>();
        for (CartItem cartItem : cart.getCartItems()) {
            CartItemWithCounterResponseDto cartItemWithCounterResponseDto
                    = new CartItemWithCounterResponseDto()
                    .setId(cartItem.getId())
                            .setWineId(cartItem.getWine().getId())
                                    .setQuantity(cartItem.getQuantity())
                                            .setTotalPrice(cartItem
                                                    .getWine()
                                                    .getPrice()
                                                    .multiply(BigDecimal.valueOf(
                                                            cartItem.getQuantity())));
            cartItemsWithCounter.add(cartItemWithCounterResponseDto);
        }

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartItemWithCounterResponseDto item : cartItemsWithCounter) {
            totalPrice = totalPrice.add(item.getTotalPrice());
        }

        return new ShoppingCartWithCountersDto()
                .setId(cart.getId())
                .setUserId(currentUserId)
                .setCartItems(cartItemsWithCounter)
                .setTotalPrice(totalPrice);
    }

    @Override
    public ShoppingCartDto addWine(CartItemRequestDto request) {
        Long currentUserId = getCurrentUserId();

        ShoppingCart cart = shoppingCartRepository
                .findByUserId(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart not found for user " + currentUserId));

        Wine wine = wineRepository.findById(request.getWineId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Wine not found: " + request.getWineId()));

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getWine()
                        .getId().equals(request.getWineId()))
                .findFirst()
                .orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(
                    cartItem.getQuantity() + request.getQuantity()
            );
        } else {
            CartItem newItem = new CartItem()
                    .setWine(wine)
                    .setQuantity(request.getQuantity())
                    .setShoppingCart(cart);
            cart.getCartItems().add(newItem);
        }

        shoppingCartRepository.save(cart);
        return shoppingCartMapper.toDto(cart);
    }

    @Override
    public ShoppingCartDto updateQuantity(
            Long cartItemId,
            UpdateCartItemDto request
    ) {
        Long currentUserId = getCurrentUserId();

        final ShoppingCart cart = shoppingCartRepository
                .findByUserId(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart not found for user " + currentUserId));

        CartItem item = cartItemRepository
                .findByIdAndShoppingCartId(cartItemId, cart.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Invalid cart item id: " + cartItemId));

        item.setQuantity(request.getQuantity());
        cartItemRepository.save(item);

        return shoppingCartMapper.toDto(cart);
    }

    @Override
    public void deleteWine(Long cartItemId) {
        Long currentUserId = getCurrentUserId();

        final ShoppingCart cart = shoppingCartRepository
                .findByUserId(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart not found for user " + currentUserId));

        CartItem item = cartItemRepository
                .findByIdAndShoppingCartId(cartItemId, cart.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart item not found by id: " + cartItemId));

        cart.getCartItems().remove(item);
        cartItemRepository.delete(item);
        shoppingCartRepository.save(cart);
    }

    @Override
    public void addUser(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    private Long getCurrentUserId() throws SecurityException {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !(authentication.getPrincipal() instanceof User)) {
            throw new SecurityException("Unauthorized");
        }

        return ((User) authentication.getPrincipal()).getId();
    }
}
