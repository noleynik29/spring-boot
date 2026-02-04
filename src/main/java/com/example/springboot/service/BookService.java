package com.example.springboot.service;

import com.example.springboot.dto.BookDto;
import com.example.springboot.dto.BookSearchParametersDto;
import com.example.springboot.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto book);

    List<BookDto> findAll();

    BookDto findById(Long id);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    void delete(Long id);

    List<BookDto> searchBooks(BookSearchParametersDto parametersDto);
}
