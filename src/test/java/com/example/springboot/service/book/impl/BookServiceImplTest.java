package com.example.springboot.service.book.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.springboot.dto.book.BookDto;
import com.example.springboot.dto.book.BookSearchParametersDto;
import com.example.springboot.dto.book.CreateBookRequestDto;
import com.example.springboot.entity.Book;
import com.example.springboot.entity.Category;
import com.example.springboot.exception.EntityNotFoundException;
import com.example.springboot.mapper.BookMapper;
import com.example.springboot.repository.book.BookRepository;
import com.example.springboot.repository.book.BookSpecificationBuilder;
import com.example.springboot.service.category.CategoryService;
import com.example.springboot.util.TestDataHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Save book: should return BookDto when request is valid")
    void save_ValidRequest_ReturnBookDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        Book book = new Book();
        Book savedBook = new Book();
        BookDto expectedDto = new BookDto();

        when(bookMapper.toEntity(requestDto)).thenReturn(book);
        when(categoryService.getCategoriesByIds(any())).thenReturn(Set.of(new Category()));
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.toDto(savedBook)).thenReturn(expectedDto);

        BookDto result = bookService.save(requestDto);

        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(bookRepository).save(book);
    }

    @Test
    @DisplayName("Find book by id: should return BookDto when id exists")
    void findById_ValidId_ReturnBookDto() {
        Long id = 1L;
        Book book = new Book();
        BookDto dto = new BookDto();

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(dto);

        BookDto result = bookService.findById(id);

        assertEquals(dto, result);
        verify(bookRepository).findById(id);
    }

    @Test
    @DisplayName("Find book by id: should throw exception when book not found")
    void findById_InvalidId_ThrowException() {
        Long id = 1L;

        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> bookService.findById(id));
    }

    @Test
    @DisplayName("Find all books: should return page of BookDto")
    void findAll_ReturnPageOfBookDto() {
        Pageable pageable = Pageable.unpaged();
        Book book = new Book();
        BookDto dto = new BookDto();

        when(bookRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(book)));
        when(bookMapper.toDto(book)).thenReturn(dto);

        Page<BookDto> result = bookService.findAll(pageable);

        assertEquals(1, result.getContent().size());
        verify(bookRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Delete book: should call repository delete when id exists")
    void delete_ValidId_CallRepositoryDelete() {
        Long id = 1L;
        Book book = new Book();

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        bookService.delete(id);

        verify(bookRepository).delete(book);
    }

    @Test
    @DisplayName("Delete book: should throw exception when id does not exist")
    void delete_InvalidId_ThrowException() {
        Long id = 1L;

        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> bookService.delete(id));
    }

    @Test
    @DisplayName("Search books: should return list of BookDto based on specification")
    void searchBooks_ReturnListOfBookDto() {
        BookSearchParametersDto params =
                new BookSearchParametersDto(
                        new String[]{"Title"},
                        new String[]{"Author"},
                        new String[]{"1234567890"}
                );
        Specification<Book> specification = mock(Specification.class);

        Book book = new Book();
        BookDto dto = new BookDto();

        when(bookSpecificationBuilder.build(params)).thenReturn(specification);
        when(bookRepository.findAll(specification)).thenReturn(List.of(book));
        when(bookMapper.toDto(book)).thenReturn(dto);

        List<BookDto> result = bookService.searchBooks(params);

        assertEquals(1, result.size());
        verify(bookRepository).findAll(specification);
    }

    @Test
    @DisplayName("Update book: should update and return BookDto when id exists")
    void update_ValidRequest_ReturnUpdatedBookDto() {
        Long id = 1L;

        CreateBookRequestDto requestDto = TestDataHelper.createUpdatedBookRequestDto();

        Category category = TestDataHelper.createCategory("Fantasy", "Fantasy books");

        Book existingBook = TestDataHelper.createBook(
                "Old Title",
                "Old Author",
                "1111111111",
                requestDto.getPrice(),
                category
        );

        Book updatedBook = TestDataHelper.createBook(
                requestDto.getTitle(),
                requestDto.getAuthor(),
                requestDto.getIsbn(),
                requestDto.getPrice(),
                category
        );

        BookDto expectedDto = TestDataHelper.createSpecificBookDto(
                id,
                requestDto.getTitle(),
                requestDto.getAuthor(),
                requestDto.getPrice()
        );

        when(bookRepository.findById(id)).thenReturn(Optional.of(existingBook));
        when(categoryService.getCategoriesByIds(requestDto.getCategoriesId()))
                .thenReturn(Set.of(category));

        when(bookRepository.save(existingBook)).thenReturn(updatedBook);
        when(bookMapper.toDto(updatedBook)).thenReturn(expectedDto);

        BookDto result = bookService.update(id, requestDto);

        assertNotNull(result);
        assertEquals(expectedDto, result);

        verify(bookRepository).findById(id);
        verify(bookRepository).save(existingBook);
        verify(bookMapper).toDto(updatedBook);
    }
}