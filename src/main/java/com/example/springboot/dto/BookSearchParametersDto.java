package com.example.springboot.dto;

public record BookSearchParametersDto(
        String[] titles,
        String[] authors,
        String[] isbns
) {
}
