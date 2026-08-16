package org.example.mapper;

import org.example.config.MapperConfig;
import org.example.dto.cart_item.CartItemRequestDto;
import org.example.dto.cart_item.CartItemResponseDto;
import org.example.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = WineMapper.class)
public interface CartItemMapper {

    @Mapping(target = "wineId", source = "wine.id")
    CartItemRequestDto toDto(CartItem cartItem);

    @Mapping(target = "wine", source = "wineId",
            qualifiedByName = "wineFromId")
    CartItem toEntity(CartItemResponseDto cartItemDto);
}
