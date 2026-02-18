package com.example.springboot.controller;

import com.example.springboot.dto.book.BookDtoWithoutCategoryIds;
import com.example.springboot.dto.category.CategoryDto;
import com.example.springboot.dto.category.CategoryRequestDto;
import com.example.springboot.service.category.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Create a new category", description = "Accessible only by ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody CategoryRequestDto dto) {
        return categoryService.save(dto);
    }

    @Operation(summary = "Get all categories", description = "Accessible by USER")
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public Page<CategoryDto> getAll(@PageableDefault Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @Operation(summary = "Get category by ID", description = "Accessible by USER")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public CategoryDto getById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @Operation(summary = "Update a category", description = "Accessible only by ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public CategoryDto update(@PathVariable Long id,
                              @Valid @RequestBody CategoryRequestDto dto) {
        return categoryService.update(id, dto);
    }

    @Operation(summary = "Delete a category", description = "Accessible only by ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @Operation(summary = "Get books by category ID", description = "Accessible by USER")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}/books")
    public List<BookDtoWithoutCategoryIds> getBooks(@PathVariable Long id) {
        return categoryService.getBooksByCategoryId(id);
    }
}
