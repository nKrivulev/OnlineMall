package com.example.onlinemall.dto;

import lombok.Data;

@Data
public class UserResponse {
    private String username;
    private String email;
    private double balance;
    private String role;
}
