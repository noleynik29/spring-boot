package com.example.springboot.mapper;

import com.example.springboot.config.MapperConfig;
import com.example.springboot.dto.cart.ShoppingCartDto;
import com.example.springboot.entity.ShoppingCart;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    ShoppingCartDto toDto(ShoppingCart cart);

    ShoppingCart toEntity(ShoppingCartDto dto);
}
