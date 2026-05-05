package com.example.springboot.util;

import com.example.springboot.dto.book.BookDto;
import com.example.springboot.dto.book.CreateBookRequestDto;
import com.example.springboot.dto.category.CategoryDto;
import com.example.springboot.dto.category.CategoryRequestDto;
import com.example.springboot.entity.Book;
import com.example.springboot.entity.Category;

import java.math.BigDecimal;
import java.util.Set;

public class TestDataHelper {

    public static CategoryRequestDto createCategoryRequestDto() {
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Fantasy");
        requestDto.setDescription("Fantasy books");
        return requestDto;
    }

    public static CategoryRequestDto createUpdatedCategoryRequestDto() {
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Updated Fantasy");
        requestDto.setDescription("Updated Description");
        return requestDto;
    }

    public static CategoryDto createCategoryDto() {
        CategoryDto expectedDto = new CategoryDto();
        expectedDto.setId(1L);
        expectedDto.setName("Fantasy");
        expectedDto.setDescription("Fantasy books");
        return expectedDto;
    }

    public static CreateBookRequestDto createBookRequestDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("The Lord of the Rings");
        requestDto.setAuthor("J.R.R. Tolkien");
        requestDto.setIsbn("123456789");
        requestDto.setPrice(BigDecimal.valueOf(100));
        requestDto.setCategoriesId(Set.of(1L));
        return requestDto;
    }

    public static CreateBookRequestDto createSpecificBookRequestDto(String title, String author, String isbn, BigDecimal price, Set<Long> categoryIds) {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle(title);
        requestDto.setAuthor(author);
        requestDto.setIsbn(isbn);
        requestDto.setPrice(price);
        requestDto.setCategoriesId(categoryIds);
        return requestDto;
    }

    public static CreateBookRequestDto createUpdatedBookRequestDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Updated Title");
        requestDto.setAuthor("Updated Author");
        requestDto.setIsbn("9876543210");
        requestDto.setPrice(BigDecimal.valueOf(2000));
        requestDto.setCategoriesId(Set.of(1L));
        return requestDto;
    }

    public static BookDto createBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("The Lord of the Rings");
        bookDto.setAuthor("J.R.R. Tolkien");
        return bookDto;
    }

    public static BookDto createSpecificBookDto(Long id, String title, String author, BigDecimal price) {
        BookDto bookDto = new BookDto();
        bookDto.setId(id);
        bookDto.setTitle(title);
        bookDto.setAuthor(author);
        bookDto.setPrice(price);
        return bookDto;
    }

    public static Category createCategory(String name, String description) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        return category;
    }

    public static Book createBook(String title, String author, String isbn, BigDecimal price, Category category) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setPrice(price);
        if (category != null) {
            book.getCategories().add(category);
        }
        return book;
    }
}
