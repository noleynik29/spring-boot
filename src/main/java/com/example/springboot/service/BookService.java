package com.example.springboot.service;

import com.example.springboot.dto.BookDto;
import com.example.springboot.dto.BookSearchParametersDto;
import com.example.springboot.dto.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto book);

    Page<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    void delete(Long id);

    List<BookDto> searchBooks(BookSearchParametersDto parametersDto);
}
