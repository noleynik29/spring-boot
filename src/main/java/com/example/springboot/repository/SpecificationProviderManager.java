package com.example.springboot.repository;

import com.example.springboot.entity.Book;

public interface SpecificationProviderManager<T> {
    SpecificationProvider<Book> getSpecificationProvider(String key);
}
