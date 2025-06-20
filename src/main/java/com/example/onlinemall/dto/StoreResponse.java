package com.example.onlinemall.dto;

import lombok.Data;

@Data
public class StoreResponse {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private int floor;
    private String category;
    private String ownerUsername;
}
