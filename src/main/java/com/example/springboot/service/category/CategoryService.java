package com.example.springboot.service.category;

import com.example.springboot.dto.book.BookDtoWithoutCategoryIds;
import com.example.springboot.dto.category.CategoryDto;
import com.example.springboot.dto.category.CategoryRequestDto;
import java.util.List;
import java.util.Set;
import com.example.springboot.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Page<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CategoryRequestDto dto);

    CategoryDto update(Long id, CategoryRequestDto dto);

    void deleteById(Long id);

    List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id);

    Set<Category> getCategoriesByIds(Set<Long> categoryIds);
}
