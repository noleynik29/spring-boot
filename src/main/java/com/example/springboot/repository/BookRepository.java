package com.example.springboot.repository;

import com.example.springboot.entity.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book,Long> {
    @Query("""
        SELECT b FROM Book b
        WHERE (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')))
          AND (:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%')))
          AND (:isbn IS NULL OR b.isbn = :isbn)
    """)
    List<Book> search(
            @Param("title") String title,
            @Param("author") String author,
            @Param("isbn") String isbn
    );
}
