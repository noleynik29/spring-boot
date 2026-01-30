package com.example.springboot.repository;

import com.example.springboot.entity.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
