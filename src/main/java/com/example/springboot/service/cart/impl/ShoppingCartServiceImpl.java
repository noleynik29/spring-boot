package com.example.springboot.service.cart.impl;

import com.example.springboot.dto.cart.AddToCartRequestDto;
import com.example.springboot.dto.cart.ShoppingCartDto;
import com.example.springboot.dto.cart.UpdateCartItemRequestDto;
import com.example.springboot.entity.CartItem;
import com.example.springboot.entity.ShoppingCart;
import com.example.springboot.entity.User;
import com.example.springboot.exception.EntityNotFoundException;
import com.example.springboot.mapper.CartItemMapper;
import com.example.springboot.mapper.ShoppingCartMapper;
import com.example.springboot.repository.cart.CartItemRepository;
import com.example.springboot.repository.cart.ShoppingCartRepository;
import com.example.springboot.service.cart.ShoppingCartService;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartMapper cartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartDto getCartByUserEmail(String email) {
        return cartMapper.toDto(findCartByUserEmail(email));
    }

    @Override
    public ShoppingCartDto addBookToCartByUserEmail(String email, AddToCartRequestDto dto) {
        ShoppingCart cart = findCartByUserEmail(email);
        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(dto.getBookId()))
                .findFirst();

        CartItem cartItem;

        if (existingItem.isPresent()) {
            cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + dto.getQuantity());
        } else {
            cartItem = new CartItem();
            cartItem.setShoppingCart(cart);

            cartItemMapper.updateCartItemFromDto(dto, cartItem);

            cart.getCartItems().add(cartItem);
        }

        cartItemRepository.save(cartItem);
        cartItemMapper.toDto(cartItem);
        return cartMapper.toDto(cart);
    }

    @Override
    public ShoppingCartDto updateCartItem(
            String email,
            Long cartItemId,
            UpdateCartItemRequestDto dto
    ) {
        ShoppingCart cart = findCartByUserEmail(email);

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "CartItem not found with id " + cartItemId
                ));

        item.setQuantity(dto.getQuantity());
        cartItemMapper.toDto(item);

        return cartMapper.toDto(cart);
    }

    @Override
    public void removeCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
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

    @Override
    public void clearCart(ShoppingCart cart) {
        cart.getCartItems().clear();
    }
}
