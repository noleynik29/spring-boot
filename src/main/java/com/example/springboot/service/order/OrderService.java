package com.example.springboot.service.order;

import com.example.springboot.dto.order.CreateOrderRequestDto;
import com.example.springboot.dto.order.OrderDto;
import com.example.springboot.dto.order.OrderItemDto;
import com.example.springboot.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto placeOrder(String userEmail, CreateOrderRequestDto dto);

    Page<OrderDto> getOrders(String userEmail, Pageable pageable);

    OrderDto updateStatus(Long orderId, Order.Status status);

    Page<OrderItemDto> getOrderItems(Long orderId, Pageable pageable);

    OrderItemDto getOrderItem(Long orderId, Long itemId);
}
