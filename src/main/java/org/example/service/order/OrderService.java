package org.example.service.order;

import org.example.dto.order.OrderRequestDto;
import org.example.dto.order.OrderResponseDto;
import org.example.dto.order.UpdateOrderDto;
import org.example.dto.order_item.OrderItemResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto addOrder(OrderRequestDto request);

    Page<OrderResponseDto> getOrders(Pageable pageable);

    OrderResponseDto updateStatus(Long orderId, UpdateOrderDto request);

    Page<OrderItemResponseDto> getOrderItems(Long orderId, Pageable pageable);

    OrderItemResponseDto getItem(Long orderId, Long itemId);
}
