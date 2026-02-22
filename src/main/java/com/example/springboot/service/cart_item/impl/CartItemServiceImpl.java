package com.example.springboot.service.cart_item.impl;

import com.example.springboot.dto.cart.AddToCartRequestDto;
import com.example.springboot.dto.cart.CartItemDto;
import com.example.springboot.dto.cart.UpdateCartItemRequestDto;
import com.example.springboot.entity.CartItem;
import com.example.springboot.entity.ShoppingCart;
import com.example.springboot.exception.EntityNotFoundException;
import com.example.springboot.mapper.ShoppingCartMapper;
import com.example.springboot.repository.cartItem.CartItemRepository;
import com.example.springboot.service.cart_item.CartItemService;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ShoppingCartMapper cartMapper;

    @Override
    public CartItemDto updateCartItem(Long cartItemId, UpdateCartItemRequestDto dto) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "CartItem not found with id " + cartItemId
                ));
        item.setQuantity(dto.getQuantity());
        return cartMapper.toDto(item);
    }

    @Override
    public void removeCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public CartItemDto addOrUpdateCartItem(ShoppingCart cart, AddToCartRequestDto dto) {
        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(dto.getBookId()))
                .findFirst();

        CartItem cartItem;
        if (existingItem.isPresent()) {
            cartItem = existingItem.get();
            return updateCartItem(
                    cartItem.getId(),
                    new UpdateCartItemRequestDto(cartItem.getQuantity() + dto.getQuantity())
            );
        } else {
            cartItem = new CartItem();
            cartItem.setShoppingCart(cart);
            cartMapper.updateCartItemFromDto(dto, cartItem);
            cart.getCartItems().add(cartItem);
            cartItemRepository.save(cartItem);
            return cartMapper.toDto(cartItem);
        }
    }
}
