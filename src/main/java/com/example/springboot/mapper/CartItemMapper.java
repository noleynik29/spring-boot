package com.example.springboot.mapper;

import com.example.springboot.config.MapperConfig;
import com.example.springboot.dto.cart.AddToCartRequestDto;
import com.example.springboot.dto.cart.CartItemDto;
import com.example.springboot.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {

    CartItemDto toDto(CartItem item);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shoppingCart", ignore = true)
    @Mapping(target = "book", ignore = true)
    void updateCartItemFromDto(
            AddToCartRequestDto dto,
            @MappingTarget CartItem item
    );
}
