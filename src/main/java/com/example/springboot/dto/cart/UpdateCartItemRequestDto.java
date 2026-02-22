package com.example.springboot.dto.cart;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateCartItemRequestDto {
    @Positive
    private int quantity;
}
