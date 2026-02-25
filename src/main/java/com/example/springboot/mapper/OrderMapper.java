package com.example.springboot.mapper;

import com.example.springboot.config.MapperConfig;
import com.example.springboot.dto.order.CreateOrderRequestDto;
import com.example.springboot.dto.order.OrderDto;
import com.example.springboot.dto.order.OrderItemDto;
import com.example.springboot.entity.Order;
import com.example.springboot.entity.OrderItem;
import com.example.springboot.entity.ShoppingCart;
import org.mapstruct.Mapper;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {

    OrderDto toDto(Order order);

    OrderItemDto toDto(OrderItem item);

    default Order toModel(ShoppingCart cart, CreateOrderRequestDto dto) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setStatus(Order.Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(dto.getShippingAddress());

        Set<OrderItem> items = cart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem item = new OrderItem();
                    item.setOrder(order);
                    item.setBook(cartItem.getBook());
                    item.setQuantity(cartItem.getQuantity());
                    item.setPrice(cartItem.getBook().getPrice());
                    return item;
                })
                .collect(Collectors.toSet());

        order.setOrderItems(items);

        return order;
    }
}
