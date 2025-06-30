package com.example.onlinemall.dto;

import com.example.onlinemall.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Потребителското име е задължително.")
    private String username;

    @Email(message = "Невалиден имейл.")
    @NotBlank(message = "Имейлът е задължителен.")
    private String email;

    @NotBlank(message = "Паролата е задължителна.")
    @Size(min = 6, message = "Паролата трябва да е поне 6 символа.")
    private String password;

    @NotNull(message = "Ролята е задължителна.")
    private Role role; // enum: USER, SELLER, ADMIN
}
