package com.example.springboot.dto.order;

import com.example.springboot.entity.Order;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequestDto(
        @NotNull
        Order.Status status
) {
}
