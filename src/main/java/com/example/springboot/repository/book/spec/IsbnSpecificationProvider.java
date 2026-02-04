package com.example.springboot.repository.book.spec;

import com.example.springboot.entity.Book;
import com.example.springboot.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class IsbnSpecificationProvider implements SpecificationProvider<Book> {
    public static final String key = "isbn";

    @Override
    public String getKey() {
        return key;
    }

    public Specification<Book> getSpecification(String[] params) {
        return ((root, query, criteriaBuilder) -> root
                .get(key)
                .in(Arrays
                        .stream(params)
                        .toArray()
                )
            );
    }
}
