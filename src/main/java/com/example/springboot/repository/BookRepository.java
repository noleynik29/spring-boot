package com.example.springboot.repository;

import com.example.springboot.entity.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Long> {
    List<Book> search(String title, String author, String isbn);
}
