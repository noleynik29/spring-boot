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
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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

    @Test
    @DisplayName("Save book - should persist book correctly")
    void save_validBook_persistsSuccessfully() {
        Book newBook = new Book();
        newBook.setTitle("New Book");
        newBook.setAuthor("New Author");
        newBook.setIsbn("2143658709");
        newBook.setPrice(BigDecimal.valueOf(50));
        newBook.setCategories(new HashSet<>(List.of(category)));

        Book savedBook = bookRepository.save(newBook);

        assertNotNull(savedBook.getId());

        Book fromDb = bookRepository.findById(savedBook.getId()).orElse(null);
        assertNotNull(fromDb);
        assertEquals("New Book", fromDb.getTitle());
        assertEquals("New Author", fromDb.getAuthor());
        assertEquals("2143658709", fromDb.getIsbn());
        assertEquals(0, BigDecimal.valueOf(50).compareTo(fromDb.getPrice()));
    }

    @Test
    @DisplayName("Save book - duplicate ISBN should fail")
    void save_duplicateIsbn_throwsException() {
        Book duplicate = new Book();
        duplicate.setTitle("Duplicate");
        duplicate.setAuthor("Author");
        duplicate.setIsbn("1234567890");
        duplicate.setPrice(BigDecimal.valueOf(10));
        duplicate.setCategories(new HashSet<>(List.of(category)));

        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            bookRepository.saveAndFlush(duplicate);
        });
    }

    @Test
    @DisplayName("Delete book - should remove book from database")
    void delete_validId_removesBook() {
        Long id = book.getId();

        bookRepository.deleteById(id);

        boolean exists = bookRepository.findById(id).isPresent();
        assertTrue(!exists);
    }

    @Test
    @DisplayName("Delete book - non existing id should not throw exception")
    void delete_invalidId_doesNothing() {
        Long nonExistingId = 999L;

        bookRepository.deleteById(nonExistingId);

        List<Book> books = bookRepository.findAll();
        assertEquals(1, books.size());
    }
}
