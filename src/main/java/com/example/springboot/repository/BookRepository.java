package com.example.springboot.repository;

import com.example.springboot.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookRepository extends JpaRepository<Book,Long> {
    List<Book> search(String title, String author, String isbn);
}
