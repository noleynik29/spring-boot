package com.example.springboot.service.cart;

import com.example.springboot.dto.cart.AddToCartRequestDto;
import com.example.springboot.dto.cart.CartItemDto;
import com.example.springboot.dto.cart.ShoppingCartDto;
import com.example.springboot.dto.cart.UpdateCartItemRequestDto;
import com.example.springboot.entity.ShoppingCart;
import com.example.springboot.entity.User;

public interface ShoppingCartService {
    CartItemDto updateCartItem(Long cartItemId, UpdateCartItemRequestDto dto);

    void removeCartItem(Long cartItemId);

    ShoppingCart createCartForUser(User user);

    ShoppingCartDto getCartByUserEmail(String email);

    CartItemDto addBookToCartByUserEmail(String email, AddToCartRequestDto dto);
}
