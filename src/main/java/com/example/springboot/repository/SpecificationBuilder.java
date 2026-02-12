package com.example.springboot.repository;

import com.example.springboot.dto.book.BookSearchParametersDto;
import com.example.springboot.entity.Book;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<Book> build(BookSearchParametersDto searchParametersDto);
}
