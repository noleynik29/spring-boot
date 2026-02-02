package com.example.springboot.mapper;

import com.example.springboot.config.MapperConfig;
import com.example.springboot.dto.BookDto;
import com.example.springboot.dto.CreateBookRequestDto;
import com.example.springboot.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toBookDto(Book book);

    Book toBook(CreateBookRequestDto bookDto);

    void updateBookFromDto(
            CreateBookRequestDto requestDto,
            @MappingTarget Book book
    );
}
