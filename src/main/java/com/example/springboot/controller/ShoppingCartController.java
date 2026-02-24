package com.example.springboot.controller;

import com.example.springboot.dto.cart.AddToCartRequestDto;
import com.example.springboot.dto.cart.ShoppingCartDto;
import com.example.springboot.dto.cart.UpdateCartItemRequestDto;
import com.example.springboot.service.cart.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name = "ShoppingCart")
public class ShoppingCartController {
    private final ShoppingCartService cartService;

    @Operation(summary = "Get current user's shopping cart",
            description = "Get current user's shopping cart")
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartDto getCart(Principal principal) {
        return cartService.getCartByUserEmail(principal.getName());
    }

    @Operation(summary = "Add book to shopping cart",
            description = "Add book to shopping cart")
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartDto addBookToCart(
            @RequestBody @Valid AddToCartRequestDto dto,
            Principal principal
    ) {
        return cartService.addBookToCartByUserEmail(principal.getName(), dto);
    }

    @Operation(summary = "Update cart item in shopping cart",
            description = "Update cart item in shopping cart")
    @PutMapping("/items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartDto updateCartItem(
            @PathVariable Long cartItemId,
            @RequestBody @Valid UpdateCartItemRequestDto dto,
            Principal principal
    ) {
        return cartService.updateCartItem(principal.getName(), cartItemId, dto);
    }

    @Operation(summary = "Remove book from shopping cart",
            description = "Remove book from shopping cart")
    @DeleteMapping("/items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    public void removeCartItem(@PathVariable Long cartItemId) {
        cartService.removeCartItem(cartItemId);
    }
}
