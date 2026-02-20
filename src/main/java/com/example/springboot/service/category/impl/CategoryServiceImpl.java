package com.example.springboot.service.category.impl;

import com.example.springboot.dto.book.BookDtoWithoutCategoryIds;
import com.example.springboot.dto.category.CategoryDto;
import com.example.springboot.dto.category.CategoryRequestDto;
import com.example.springboot.entity.Category;
import com.example.springboot.exception.EntityNotFoundException;
import com.example.springboot.mapper.BookMapper;
import com.example.springboot.mapper.CategoryMapper;
import com.example.springboot.repository.book.BookRepository;
import com.example.springboot.repository.category.CategoryRepository;
import com.example.springboot.service.category.CategoryService;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public Page<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toDto);
    }

    @Override
    public CategoryDto getById(Long id) {
        return categoryMapper.toDto(
                categoryRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException("Category not found: " + id))
        );
    }

    @Override
    public CategoryDto save(CategoryRequestDto dto) {
        return categoryMapper.toDto(
                categoryRepository.save(categoryMapper.toEntity(dto))
        );
    }

    @Override
    public CategoryDto update(Long id, CategoryRequestDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Category not found: " + id)
                );
        categoryMapper.updateCategoryFromDto(dto, category);
        return categoryMapper.toDto(category);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id) {
        return bookRepository.findAllByCategoriesId(id).stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }

    @Override
    public Set<Category> getCategoriesByIds(Set<Long> categoryIds) {
        Set<Category> categories = new HashSet<>(categoryRepository.findAllById(categoryIds));

        if (categories.size() != categoryIds.size()) {
            throw new EntityNotFoundException("One or more categories not found");
        }

        return categories;
    }
}
