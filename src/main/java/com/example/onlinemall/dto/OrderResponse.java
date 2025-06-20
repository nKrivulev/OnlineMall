package com.example.onlinemall.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private double totalAmount;
    private LocalDateTime createdAt;
    private List<ProductResponse> products;
}
