package com.example.onlinemall.controller;

import com.example.onlinemall.dto.UserResponse;
import com.example.onlinemall.model.User;
import com.example.onlinemall.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ✅ Връща информация за текущия потребител
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyProfile(Principal principal) {
        User user = userService.getByUsername(principal.getName());
        return ResponseEntity.ok(userService.toResponse(user));
    }

    // ✅ Добавяне на средства (само за логнат потребител)
    @PostMapping("/add-funds")
    public ResponseEntity<String> addFunds(Principal principal, @RequestParam double amount) {
        if (amount <= 0) {
            return ResponseEntity.badRequest().body("Сумата трябва да е положителна.");
        }

        if (amount > 10000) {
            return ResponseEntity.badRequest().body("Превишена максимална сума за еднократно добавяне.");
        }

        User user = userService.getByUsername(principal.getName());
        user.setBalance(user.getBalance() + amount);
        userService.saveUser(user);

        return ResponseEntity.ok("Успешно добавени " + amount + " лв. Нов баланс: " + user.getBalance() + " лв.");
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(userService::toResponse)
                .toList();
    }

    @GetMapping("/{email}")
    public UserResponse getUserByEmail(@PathVariable String email) {
        return userService.toResponse(
                userService.getByEmail(email)
                        .orElseThrow(() -> new RuntimeException("Потребителят не е намерен"))
        );
    }
}
