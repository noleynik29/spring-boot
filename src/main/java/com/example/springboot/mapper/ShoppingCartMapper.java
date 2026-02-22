package com.example.springboot.mapper;

import com.example.springboot.config.MapperConfig;
import com.example.springboot.dto.cart.AddToCartRequestDto;
import com.example.springboot.dto.cart.CartItemDto;
import com.example.springboot.dto.cart.ShoppingCartDto;
import com.example.springboot.entity.Book;
import com.example.springboot.entity.CartItem;
import com.example.springboot.entity.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    ShoppingCartDto toDto(ShoppingCart cart);

    CartItemDto toDto(CartItem item);

    @Named("bookFromId")
    default Book bookFromId(Long id) {
        Book book = new Book();
        book.setId(id);
        return book;
    }

    default void updateCartItemFromDto(AddToCartRequestDto dto, @MappingTarget CartItem item) {
        if (dto.getBookId() != null) {
            item.setBook(bookFromId(dto.getBookId()));
        }
        item.setQuantity(dto.getQuantity());
    }
}
