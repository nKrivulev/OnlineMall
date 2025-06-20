package com.example.onlinemall.dto;

import lombok.Data;

@Data
public class ReviewResponse {
    private int rating;
    private String comment;
    private String username;
}
