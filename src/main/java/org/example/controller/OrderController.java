package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.order.OrderResponseDto;
import org.example.dto.order_item.OrderItemResponseDto;
import org.example.dto.order.OrderRequestDto;
import org.example.dto.order.UpdateOrderDto;
import org.example.service.order.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Orders management",
        description = "Endpoints for managing user's orders")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Add order", description = "Place an order for completing")
    public OrderResponseDto addOrder(@RequestBody @Valid OrderRequestDto request) {
        return orderService.addOrder(request);
    }

    @GetMapping
    @Operation(summary = "Get orders", description = "Retrieve user's order history")
    public Page<OrderResponseDto> getOrders(Pageable pageable) {
        return orderService.getOrders(pageable);
    }

    @PatchMapping("/{orderId}")
    @Operation(summary = "Update order status", description = "Update order status")
    public OrderResponseDto updateStatus(@PathVariable Long orderId,
                                         @RequestBody @Valid UpdateOrderDto request) {
        return orderService.updateStatus(orderId, request);
    }

    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get order items", description = "Retrieve all order items for a specific order")
    public Page<OrderItemResponseDto> getOrderItems(@PathVariable Long orderId,
                                                    Pageable pageable) {
        return orderService.getOrderItems(orderId, pageable);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Get order item", description = "Retrieve a specific order item within an order")
    public OrderItemResponseDto getItem(@PathVariable Long orderId,
                                        @PathVariable Long itemId) {
        return orderService.getItem(orderId, itemId);
    }
}
