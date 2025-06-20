package com.example.onlinemall.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductRequest {

    @NotBlank(message = "Името на продукта е задължително.")
    private String name;

    @NotBlank(message = "Описанието е задължително.")
    private String description;

    @Min(value = 0, message = "Цената трябва да е положително число.")
    private double price;

    @NotBlank(message = "URL на снимката е задължителен.")
    private String imageUrl;
}
