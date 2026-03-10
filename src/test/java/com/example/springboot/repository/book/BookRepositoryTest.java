package com.example.springboot.repository.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.springboot.entity.Book;
import com.example.springboot.entity.Category;
import com.example.springboot.repository.category.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;
    private Book book;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setName("Test Category");
        category.setDescription("Test Category Description");
        categoryRepository.save(category);

        book = new Book();
        book.setTitle("Test Title");
        book.setAuthor("Test Author");
        book.setIsbn("1234567890");
        book.setPrice(BigDecimal.valueOf(100));
        book.setCategories(new HashSet<>(List.of(category)));
        bookRepository.save(book);
    }

    @Test
    @DisplayName("findAllByCategoriesId should return books for valid category")
    void findAllByCategoriesId_validCategoryId_returnsBooks() {
        List<Book> books = bookRepository.findAllByCategoriesId(category.getId());
        assertNotNull(books);
        assertEquals(1, books.size());
        assertEquals(book.getTitle(), books.get(0).getTitle());
    }

    @Test
    @DisplayName("findAllByCategoriesId should return empty list for invalid category")
    void findAllByCategoriesId_invalidCategoryId_returnsEmptyList() {
        List<Book> books = bookRepository.findAllByCategoriesId(999L);
        assertNotNull(books);
        assertTrue(books.isEmpty());
    }
}
