package com.example.springboot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotBlank(message = "This field cannot be blank")
    private String title;
    @NotBlank(message = "This field cannot be blank")
    private String author;
    @NotBlank(message = "This field cannot be blank")
    private String isbn;
    @NotNull(message = "This field cannot be null")
    @Positive
    private BigDecimal price;
    private String description;
    private String coverImage;
}
