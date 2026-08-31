package org.example.mapper;

import org.example.config.MapperConfig;
import org.example.dto.order_item.OrderItemResponseDto;
import org.example.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = WineMapper.class)
public interface OrderItemMapper {

    @Mapping(source = "wine.id", target = "wineId")
    OrderItemResponseDto toDto(OrderItem orderItem);
}
