package org.example.mapper;

import org.example.config.MapperConfig;
import org.example.dto.shopping_cart.ShoppingCartRequestDto;
import org.example.dto.shopping_cart.ShoppingCartResponseDto;
import org.example.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "cartItems", source = "cartItems")
    ShoppingCartRequestDto toDto(ShoppingCart shoppingCart);

    ShoppingCart toEntity(ShoppingCartResponseDto shoppingCartDto);
}
