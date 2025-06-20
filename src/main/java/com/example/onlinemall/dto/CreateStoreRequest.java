package com.example.onlinemall.dto;

import com.example.onlinemall.model.StoreCategory;
import lombok.Data;

@Data
public class CreateStoreRequest {
    private String name;
    private String description;
    private String imageUrl;
    private int floor;
    private StoreCategory category;
}
