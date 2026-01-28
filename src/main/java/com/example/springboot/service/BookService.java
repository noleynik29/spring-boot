package com.example.springboot.service;

import com.example.springboot.entity.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);
    List<Book> findAll();
}
