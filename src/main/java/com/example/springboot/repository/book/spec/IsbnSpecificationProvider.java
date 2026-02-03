package com.example.springboot.repository.book.spec;

import com.example.springboot.entity.Book;
import com.example.springboot.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class IsbnSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "isbn";
    }

    public Specification<Book> getSpecification(String[] params) {
        return ((root, query, criteriaBuilder) -> root
                .get("isbn")
                .in(Arrays
                        .stream(params)
                        .toArray()
                )
            );
    }
}
