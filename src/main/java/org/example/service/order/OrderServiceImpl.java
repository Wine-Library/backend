package org.example.service.order;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.example.dto.order.OrderRequestDto;
import org.example.dto.order.OrderResponseDto;
import org.example.dto.order.UpdateOrderDto;
import org.example.dto.order_item.OrderItemResponseDto;
import org.example.exception.OrderProcessingException;
import org.example.mapper.OrderItemMapper;
import org.example.mapper.OrderMapper;
import org.example.model.CartItem;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.example.model.ShoppingCart;
import org.example.model.User;
import org.example.repository.CartItemRepository;
import org.example.repository.OrderItemRepository;
import org.example.repository.OrderRepository;
import org.example.repository.ShoppingCartRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public OrderResponseDto addOrder(OrderRequestDto request) {
        User user = getCurrentUser();
        ShoppingCart cart = shoppingCartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found for user: " + user.getId()));

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new OrderProcessingException(
                    "Shopping cart is empty for user: " + user.getId());
        }

        Order order = new Order()
                .setUser(user)
                .setStatus(Order.Status.PENDING)
                .setOrderDate(LocalDateTime.now())
                .setStreet(request.getStreet())
                .setCity(request.getCity())
                .setZipCode(request.getZipCode());

        BigDecimal total = BigDecimal.ZERO;
        Set<OrderItem> orderItems = new HashSet<>();

        for (CartItem cartItem : cart.getCartItems()) {
            BigDecimal price = cartItem.getWine().getPrice();
            int quantity = cartItem.getQuantity();

            BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(quantity));
            total = total.add(itemTotal);

            OrderItem orderItem = new OrderItem()
                    .setOrder(order)
                    .setWine(cartItem.getWine())
                    .setQuantity(quantity)
                    .setPrice(price);

            orderItems.add(orderItem);
        }

        order.setTotal(total);
        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        cartItemRepository.deleteAll(cart.getCartItems());

        return orderMapper.toDto(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponseDto> getOrders(Pageable pageable) {
        Long userId = getCurrentUser().getId();
        return orderRepository.findAllByUserIdWithOrderItems(userId, pageable)
                .map(orderMapper::toDto);
    }

    @Override
    @Transactional
    public OrderResponseDto updateStatus(Long orderId, UpdateOrderDto request) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Cannot update order by id: " + orderId));

        orderMapper.updateFromDto(request, order);
        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toDto(updatedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderItemResponseDto> getOrderItems(Long orderId, Pageable pageable) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Cannot find order by id: " + orderId));

        verifyOrderBelongsToUser(order);

        return orderItemRepository.findAllByOrder_Id(orderId, pageable)
                .map(orderItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderItemResponseDto getItem(Long orderId, Long itemId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Cannot get order by id: " + orderId));

        verifyOrderBelongsToUser(order);

        OrderItem item = orderItemRepository.findByIdAndOrderId(itemId, orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cannot get item " + itemId + " in order " + orderId));

        return orderItemMapper.toDto(item);
    }

    private void verifyOrderBelongsToUser(Order order) {
        User user = getCurrentUser();
        if (!order.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You don't have permission to access this order");
        }
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new SecurityException("Unauthorized");
        }
        return (User) authentication.getPrincipal();
    }
}
