package com.example.springboot.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get all books - returns page of books")
    void getAllBooks_ReturnsPage() throws Exception {
        Long bookId = 1L;
        BookDto bookDto = TestDataHelper.createSpecificBookDto(bookId, "Test Book",
                "Test Author", BigDecimal.valueOf(19.99));

        Page<BookDto> page = new PageImpl<>(List.of(bookDto));

        when(bookService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Test Book"))
                .andExpect(jsonPath("$.content[0].author").value("Test Author"))
                .andExpect(jsonPath("$.content[0].price").value(19.99));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get book by id - returns book")
    void getBookById_ReturnsBook() throws Exception {
        Long bookId = 1L;
        BookDto bookDto = TestDataHelper.createSpecificBookDto(bookId, "Test Book",
                "Test Author", BigDecimal.valueOf(19.99));

        when(bookService.findById(1L)).thenReturn(bookDto);

        mockMvc.perform(get("/books/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Book"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andExpect(jsonPath("$.price").value(19.99));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Create book - returns created book")
    void createBook_ReturnsBook() throws Exception {
        String requestJson = "{ " +
                "\"title\": \"Test Book\", " +
                "\"author\": \"Test Author\", " +
                "\"isbn\": \"1234567890\", " +
                "\"price\": 19.99, " +
                "\"categoriesId\": []" +
                " }";

        Long bookId = 1L;
        BookDto responseDto = TestDataHelper.createSpecificBookDto(bookId, "Test Book",
                "Test Author", BigDecimal.valueOf(19.99));


        when(bookService.save(any(CreateBookRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Book"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andExpect(jsonPath("$.price").value(19.99));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Update book - returns updated book")
    void updateBook_ReturnsUpdatedBook() throws Exception {
        CreateBookRequestDto requestDto = TestDataHelper.createUpdatedBookRequestDto();

        Long bookId = 1L;
        BookDto updatedBook = TestDataHelper.createSpecificBookDto(bookId, "Updated Title",
                "Updated Author", BigDecimal.valueOf(29.99));

        when(bookService.update(eq(1L), any(CreateBookRequestDto.class))).thenReturn(updatedBook);

        mockMvc.perform(put("/books/{id}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Title\",\"author\":\"Updated Author\","
                                + "\"isbn\":\"9876543210\",\"price\":29.99}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.author").value("Updated Author"))
                .andExpect(jsonPath("$.price").value(29.99));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Delete book - returns no content")
    void deleteBook_ReturnsNoContent() throws Exception {
        doNothing().when(bookService).delete(1L);

        mockMvc.perform(delete("/books/{id}", 1L).with(csrf()))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).delete(1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Search books returns list of books")
    void searchBooks_ReturnsBooks() throws Exception {
        Long bookId = 1L;
        BookDto bookDto = TestDataHelper.createSpecificBookDto(bookId, "Test Book",
                "Test Author", BigDecimal.valueOf(19.99));

        BookSearchParametersDto searchParams =
                new BookSearchParametersDto(
                        new String[]{"Title"},
                        new String[]{"Author"},
                        new String[]{"1234567890"}
                );

        when(bookService.searchBooks(eq(searchParams))).thenReturn(List.of(bookDto));

        mockMvc.perform(get("/books/search"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get book by id - not found")
    void getBookById_NotFound() throws Exception {
        when(bookService.findById(999L))
                .thenThrow(new RuntimeException("Book not found"));

        mockMvc.perform(get("/books/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Create book - invalid input returns 400")
    void createBook_InvalidInput() throws Exception {
        String invalidJson = "{ \"title\": \"\" }";

        mockMvc.perform(post("/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
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
                "\"categoriesId\": []" +
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
        when(bookService.update(eq(999L), any(CreateBookRequestDto.class)))
                .thenThrow(new RuntimeException("Book not found"));

        mockMvc.perform(put("/books/{id}", 999L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Delete book - not found")
    void deleteBook_NotFound() throws Exception {
        doNothing().when(bookService).delete(999L);

        mockMvc.perform(delete("/books/{id}", 999L).with(csrf()))
                .andExpect(status().isNotFound());
    }
}