package org.example.mapper;

import org.example.config.MapperConfig;
import org.example.dto.order.OrderResponseDto;
import org.example.dto.order.UpdateOrderDto;
import org.example.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderItems", source = "orderItems")
    OrderResponseDto toDto(Order order);

    void updateFromDto(UpdateOrderDto request, @MappingTarget Order order);
}
