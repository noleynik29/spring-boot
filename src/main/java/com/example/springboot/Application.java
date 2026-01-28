package com.example.springboot;

import com.example.springboot.entity.Book;
import com.example.springboot.service.BookService;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner runner(BookService bookService) {
        return args -> {
            Book book = new Book();
            book.setTitle("Clean Code");
            book.setAuthor("Robert Martin");
            book.setIsbn("9780132350884");
            book.setPrice(BigDecimal.valueOf(30));

            bookService.save(book);

            bookService.findAll().forEach(System.out::println);
        };
    }
}
