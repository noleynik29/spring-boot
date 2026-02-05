package com.example.springboot.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotNull(message = "This field cannot be null")
    private Long id;
    @NotBlank(message = "This field cannot be blank")
    private String title;
    @NotBlank(message = "This field cannot be blank")
    private String author;
    @NotBlank(message = "This field cannot be blank")
    private String isbn;
    @NotNull(message = "This field cannot be null")
    @Min(0)
    private BigDecimal price;
    @NotBlank(message = "This field cannot be blank")
    private String description;
    @NotBlank(message = "This field cannot be blank")
    private String coverImage;
}
