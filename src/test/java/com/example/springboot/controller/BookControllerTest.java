package com.example.springboot.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @MockitoBean
    private BookService bookService;

    @Autowired
    private CategoryRepository categoryRepository;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    private Book createTestBook() {
        Category category = new Category();
        category.setName("Test Category");
        category.setDescription("Test Description");
        categoryRepository.save(category);

        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setIsbn("1234567890");
        book.setPrice(BigDecimal.valueOf(10));
        book.setCategories(Set.of(category));

        return bookRepository.save(book);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get all books - returns page of books")
    void getAllBooks_ReturnsPage() throws Exception {
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Test Book");
        bookDto.setAuthor("Test Author");
        bookDto.setPrice(new BigDecimal("19.99"));

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
    @DisplayName("Get book by id")
    @WithMockUser(roles = "USER")
    void getBookById_ReturnsBook() throws Exception {
        Book book = createTestBook();

        mockMvc.perform(get("/books/{id}", book.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(book.getTitle()))
                .andExpect(jsonPath("$.author").value(book.getAuthor()));
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

        BookDto responseDto = new BookDto();
        responseDto.setId(1L);
        responseDto.setTitle("Test Book");
        responseDto.setAuthor("Test Author");
        responseDto.setPrice(new BigDecimal("19.99"));

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
    @DisplayName("Update book")
    @WithMockUser(roles = "ADMIN")
    void updateBook_ReturnsUpdatedBook() throws Exception {
        Book book = createTestBook();

        String json = """
                {
                  "title": "Updated Title",
                  "author": "Updated Author",
                  "isbn": "1234567890",
                  "price": 20,
                  "categoryIds": []
                }
                """;

        mockMvc.perform(put("/books/{id}", book.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    @DisplayName("Delete book")
    @WithMockUser(roles = "ADMIN")
    void deleteBook_ReturnsNoContent() throws Exception {
        Book book = createTestBook();

        mockMvc.perform(delete("/books/{id}", book.getId())
                        .with(csrf()))
                .andExpect(status().isNoContent());

        assertFalse(bookRepository.findById(book.getId()).isPresent());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Search books returns list of books")
    void searchBooks_ReturnsBooks() throws Exception {
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Test Title");
        bookDto.setAuthor("Author");
        bookDto.setPrice(BigDecimal.TEN);

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
}