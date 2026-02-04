package com.example.springboot.repository.book;

import com.example.springboot.dto.BookSearchParametersDto;
import com.example.springboot.entity.Book;
import com.example.springboot.repository.SpecificationBuilder;
import com.example.springboot.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    public static final String titleKey = "title";
    public static final String authorKey = "author";
    public static final String isbnKey = "isbn";

    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParametersDto) {
        Specification<Book> specification = Specification.where((Specification<Book>) null);
        if (searchParametersDto.titles() != null && searchParametersDto.titles().length > 0) {
            specification = specification.and(specificationProviderManager
                    .getSpecificationProvider(titleKey)
                    .getSpecification(searchParametersDto.titles()));
        }
        if (searchParametersDto.authors() != null && searchParametersDto.authors().length > 0) {
            specification = specification.and(specificationProviderManager
                    .getSpecificationProvider(authorKey)
                    .getSpecification(searchParametersDto.authors()));
        }
        if (searchParametersDto.isbns() != null && searchParametersDto.isbns().length > 0) {
            specification = specification.and(specificationProviderManager
                    .getSpecificationProvider(isbnKey)
                    .getSpecification(searchParametersDto.isbns()));
        }
        return specification;
    }
}
