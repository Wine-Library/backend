package org.example.mapper;

import org.example.config.MapperConfig;
import org.example.dto.order_item.OrderItemResponseDto;
import org.example.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface OrderItemMapper {

    @Mapping(target = "wineId", source = "wine.id")
    OrderItemResponseDto toDto(OrderItem orderItem);
}
