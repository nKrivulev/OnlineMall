package com.example.onlinemall.dto;

import lombok.Data;

@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private String storeName;

    private double averageRating; // ⭐
    private int reviewCount;      // 📝
}
