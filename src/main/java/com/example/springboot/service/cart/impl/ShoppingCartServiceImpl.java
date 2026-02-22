package com.example.springboot.service.cart.impl;

import com.example.springboot.dto.cart.AddToCartRequestDto;
import com.example.springboot.dto.cart.CartItemDto;
import com.example.springboot.dto.cart.ShoppingCartDto;
import com.example.springboot.dto.cart.UpdateCartItemRequestDto;
import com.example.springboot.entity.ShoppingCart;
import com.example.springboot.entity.User;
import com.example.springboot.exception.EntityNotFoundException;
import com.example.springboot.mapper.ShoppingCartMapper;
import com.example.springboot.repository.cart.ShoppingCartRepository;
import com.example.springboot.service.cart.ShoppingCartService;
import com.example.springboot.service.item.CartItemService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository cartRepository;
    private final CartItemService cartItemService;
    private final ShoppingCartMapper cartMapper;

    @Override
    public ShoppingCartDto getCartByUserEmail(String email) {
        return cartMapper.toDto(findCartByUserEmail(email));
    }

    @Override
    public CartItemDto addBookToCartByUserEmail(String email, AddToCartRequestDto dto) {
        ShoppingCart cart = findCartByUserEmail(email);
        return cartItemService.addOrUpdateCartItem(cart, dto);
    }

    @Override
    public CartItemDto updateCartItem(Long cartItemId, UpdateCartItemRequestDto dto) {
        return cartItemService.updateCartItem(cartItemId, dto);
    }

    @Override
    public void removeCartItem(Long cartItemId) {
        cartItemService.removeCartItem(cartItemId);
    }

    @Override
    public ShoppingCart createCartForUser(User user) {
        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    private ShoppingCart findCartByUserEmail(String email) {
        return cartRepository.findByUserEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart not found for user: " + email
                ));
    }
}
