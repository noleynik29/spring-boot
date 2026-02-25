package com.example.springboot.service.order.impl;

import com.example.springboot.dto.cart.ShoppingCartDto;
import com.example.springboot.dto.order.CreateOrderRequestDto;
import com.example.springboot.dto.order.OrderDto;
import com.example.springboot.dto.order.OrderItemDto;
import com.example.springboot.entity.Order;
import com.example.springboot.entity.OrderItem;
import com.example.springboot.entity.ShoppingCart;
import com.example.springboot.exception.EntityNotFoundException;
import com.example.springboot.exception.OrderProcessingException;
import com.example.springboot.mapper.OrderMapper;
import com.example.springboot.mapper.ShoppingCartMapper;
import com.example.springboot.repository.order.OrderItemRepository;
import com.example.springboot.repository.order.OrderRepository;
import com.example.springboot.service.cart.ShoppingCartService;
import com.example.springboot.service.order.OrderService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final ShoppingCartService shoppingCartService;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final OrderMapper orderMapper;

    @Override
    public OrderDto placeOrder(String email, CreateOrderRequestDto dto) {
        ShoppingCartDto cartDto = shoppingCartService.getCartByUserEmail(email);
        ShoppingCart cart = shoppingCartMapper.toEntity(cartDto);
        validateCart(cart);
        Order order = createOrder(cart, dto);
        orderRepository.save(order);
        shoppingCartService.clearCart(cart);
        return orderMapper.toDto(order);
    }

    @Override
    public Page<OrderDto> getOrders(String userEmail, Pageable pageable) {
        return orderRepository.findAllByUserEmail(userEmail, pageable)
                .map(orderMapper::toDto);
    }

    @Override
    public OrderDto updateStatus(Long orderId, Order.Status status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Order not found: " + orderId));
        order.setStatus(status);
        return orderMapper.toDto(order);
    }

    @Override
    public Page<OrderItemDto> getOrderItems(Long orderId, Pageable pageable) {
        Page<OrderItem> items = orderItemRepository
                .findAllByOrderId(orderId, pageable);
        if (items.isEmpty()) {
            throw new EntityNotFoundException(
                    "Order items not found for orderId: " + orderId);
        }
        return items.map(orderMapper::toDto);
    }

    @Override
    public OrderItemDto getOrderItem(Long orderId, Long itemId) {
        OrderItem item = orderItemRepository
                .findByIdAndOrderId(itemId, orderId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                String.format(
                                        "OrderItem not found. orderId=%s, itemId=%s",
                                        orderId,
                                        itemId
                                )
                        )
                );
        return orderMapper.toDto(item);
    }

    private BigDecimal calculateTotal(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> item.getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validateCart(ShoppingCart cart) {
        if (cart.getCartItems().isEmpty()) {
            throw new OrderProcessingException("Shopping cart " + cart.getId() + " is empty");
        }
    }

    private Order createOrder(ShoppingCart cart, CreateOrderRequestDto dto) {
        Order order = orderMapper.toEntity(cart, dto);
        order.setTotal(calculateTotal(order.getOrderItems()));
        return order;
    }
}
