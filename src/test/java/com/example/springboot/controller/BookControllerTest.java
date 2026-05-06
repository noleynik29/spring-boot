package com.example.springboot.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.springboot.dto.book.BookDto;
import com.example.springboot.dto.book.BookSearchParametersDto;
import com.example.springboot.dto.book.CreateBookRequestDto;
import com.example.springboot.entity.Book;
import com.example.springboot.entity.Category;
import com.example.springboot.exception.EntityNotFoundException;
import com.example.springboot.repository.book.BookRepository;
import com.example.springboot.repository.category.CategoryRepository;
import com.example.springboot.security.CustomUserDetailsService;
import com.example.springboot.service.book.BookService;
import com.example.springboot.util.JwtUtil;
import com.example.springboot.util.TestDataHelper;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get all books - returns page of books")
    void getAllBooks_ReturnsPage() throws Exception {
        Category category = categoryRepository.save(
                TestDataHelper.createCategory("Fantasy", "Fantasy books")
        );

        Book book = TestDataHelper.createSpecificBook(
                "Test Book",
                "Test Author",
                "1234567890",
                BigDecimal.valueOf(19.99),
                category
        );

        book = bookRepository.save(book);

        mockMvc.perform(get("/books")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(book.getId()))
                .andExpect(jsonPath("$.content[0].title").value("Test Book"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get book by id - returns book")
    void getBookById_ReturnsBook() throws Exception {
        Category category = categoryRepository.save(
                TestDataHelper.createCategory("Fantasy", "Fantasy books")
        );

        Book book = bookRepository.save(
                TestDataHelper.createSpecificBook(
                        "Test Book",
                        "Test Author",
                        "123",
                        BigDecimal.TEN,
                        category
                )
        );

        mockMvc.perform(get("/books/" + book.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(book.getId()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Create book - returns created book")
    void createBook_ReturnsBook() throws Exception {
        Category category = categoryRepository.save(
                TestDataHelper.createCategory("Fantasy", "Fantasy books")
        );

        String json = """
        {
          "title":"The Lord of the Rings",
          "author":"J.R.R. Tolkien",
          "isbn":"123456789",
          "price":100,
          "categoriesId":[%d]
        }
        """.formatted(category.getId());

        mockMvc.perform(post("/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("The Lord of the Rings"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Update book - returns updated book")
    void updateBook_ReturnsUpdatedBook() throws Exception {
        Category category = categoryRepository.save(
                TestDataHelper.createCategory("Fantasy", "Fantasy books")
        );

        Book book = bookRepository.save(
                TestDataHelper.createSpecificBook(
                        "Old Title",
                        "Old Author",
                        "111",
                        BigDecimal.TEN,
                        category
                )
        );

        String json = """
        {
          "title":"Updated Title",
          "author":"Updated Author",
          "isbn":"9876543210",
          "price":2000,
          "categoriesId":[%d]
        }
        """.formatted(category.getId());

        mockMvc.perform(put("/books/" + book.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Delete book - returns no content")
    void deleteBook_ReturnsNoContent() throws Exception {
        Category category = categoryRepository.save(
                TestDataHelper.createCategory("Fantasy", "Fantasy books")
        );

        Book book = bookRepository.save(
                TestDataHelper.createSpecificBook(
                        "Test",
                        "Author",
                        "123",
                        BigDecimal.TEN,
                        category
                )
        );

        mockMvc.perform(delete("/books/" + book.getId()).with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Search books returns list of books")
    void searchBooks_ReturnsBooks() throws Exception {
        Category category = categoryRepository.save(
                TestDataHelper.createCategory("Fantasy", "Fantasy books")
        );

        bookRepository.save(
                TestDataHelper.createSpecificBook(
                        "Test Book",
                        "Test Author",
                        "1234567890",
                        BigDecimal.valueOf(19.99),
                        category
                )
        );

        mockMvc.perform(get("/books/search")
                        .param("titles", "Test Book")
                        .param("authors", "Test Author")
                        .param("isbns", "1234567890"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Book"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get book by id - not found")
    void getBookById_NotFound() throws Exception {
        mockMvc.perform(get("/books/999"))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Create book - invalid input returns 400")
    void createBook_InvalidInput() throws Exception {
        mockMvc.perform(post("/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Create book - service throws exception")
    void createBook_ServiceException() throws Exception {
        String requestJson = "{ " +
                "\"title\": \"Test Book\", " +
                "\"author\": \"Test Author\", " +
                "\"isbn\": \"1234567890\", " +
                "\"price\": 19.99, " +
                "\"categoriesId\": [1]" +
                " }";

        when(bookService.save(any(CreateBookRequestDto.class)))
                .thenThrow(new RuntimeException("Error saving book"));

        mockMvc.perform(post("/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Update book - not found")
    void updateBook_NotFound() throws Exception {
        String json = """
        {
          "title":"Updated",
          "categoriesId":[1]
        }
        """;

        mockMvc.perform(put("/books/999")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Delete book - not found")
    void deleteBook_NotFound() throws Exception {
        mockMvc.perform(delete("/books/999").with(csrf()))
                .andExpect(status().isNotFound());
    }

}