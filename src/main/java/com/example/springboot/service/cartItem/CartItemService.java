package com.example.springboot.service.cartItem;

import com.example.springboot.dto.cart.AddToCartRequestDto;
import com.example.springboot.dto.cart.CartItemDto;
import com.example.springboot.dto.cart.UpdateCartItemRequestDto;
import com.example.springboot.entity.ShoppingCart;

public interface CartItemService {
    CartItemDto updateCartItem(Long cartItemId, UpdateCartItemRequestDto dto);

    void removeCartItem(Long cartItemId);

    CartItemDto addOrUpdateCartItem(ShoppingCart cart, AddToCartRequestDto dto);
}
