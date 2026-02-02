package com.example.springboot.dto;

public record BookSearchParametersDto(
        String title,
        String author,
        String isbn
) {
}
