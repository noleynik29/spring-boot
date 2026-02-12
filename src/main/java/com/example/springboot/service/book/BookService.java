package com.example.springboot.service.book;

import com.example.springboot.dto.book.BookDto;
import com.example.springboot.dto.book.BookSearchParametersDto;
import com.example.springboot.dto.book.CreateBookRequestDto;
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
