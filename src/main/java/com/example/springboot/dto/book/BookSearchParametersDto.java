package com.example.springboot.dto.book;

public record BookSearchParametersDto(
        String[] titles,
        String[] authors,
        String[] isbns
) {
}
