package com.example.springboot.service.cart;

import com.example.springboot.dto.cart.AddToCartRequestDto;
import com.example.springboot.dto.cart.ShoppingCartDto;
import com.example.springboot.dto.cart.UpdateCartItemRequestDto;
import com.example.springboot.entity.ShoppingCart;
import com.example.springboot.entity.User;

public interface ShoppingCartService {
    ShoppingCartDto updateCartItem(String email, Long cartItemId, UpdateCartItemRequestDto dto);

    void removeCartItem(Long cartItemId);

    ShoppingCart createCartForUser(User user);

    ShoppingCartDto getCartByUserEmail(String email);

    ShoppingCartDto addBookToCartByUserEmail(String email, AddToCartRequestDto dto);

    void clearCart(ShoppingCart cart);
}
