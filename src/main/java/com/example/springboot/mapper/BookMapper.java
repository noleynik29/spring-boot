package com.example.springboot.mapper;

import com.example.springboot.config.MapperConfig;
import com.example.springboot.dto.book.BookDto;
import com.example.springboot.dto.book.BookDtoWithoutCategoryIds;
import com.example.springboot.dto.book.CreateBookRequestDto;
import com.example.springboot.entity.Book;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toEntity(CreateBookRequestDto bookDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
    }

    void updateBookFromDto(
            CreateBookRequestDto requestDto,
            @MappingTarget Book book
    );
}
