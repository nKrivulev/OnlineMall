package com.example.onlinemall.controller;

import com.example.onlinemall.dto.ReviewResponse;
import com.example.onlinemall.model.Product;
import com.example.onlinemall.model.Review;
import com.example.onlinemall.model.User;
import com.example.onlinemall.repository.ProductRepository;
import com.example.onlinemall.service.ProductService;
import com.example.onlinemall.service.ReviewService;
import com.example.onlinemall.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final ProductRepository productRepository;
    private final ProductService productService; // ❗️ Добавено

    // ✅ Вземане на всички отзиви за продукт
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponse>> getReviews(@PathVariable Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Продуктът не е намерен."));

        List<Review> reviews = reviewService.getReviewsForProduct(product);
        List<ReviewResponse> responses = reviews.stream().map(r -> {
            ReviewResponse dto = new ReviewResponse();
            dto.setRating(r.getRating());
            dto.setComment(r.getComment());
            dto.setUsername(r.getUser().getUsername());
            return dto;
        }).toList();

        return ResponseEntity.ok(responses);
    }

    // ✅ Добавяне на нов отзив за продукт
    @PostMapping
    public ResponseEntity<String> addReview(
            @RequestBody ReviewInput input,
            Principal principal
    ) {
        User user = userService.getByUsername(principal.getName());
        Product product = productRepository.findById(input.productId())
                .orElseThrow(() -> new RuntimeException("Продуктът не е намерен."));

        reviewService.addReview(user, product, input.rating(), input.comment());
        return ResponseEntity.ok("Отзивът е добавен успешно.");
    }

    // ✅ Средна оценка за продукт
    @GetMapping("/products/{productId}/average-rating")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long productId) {
        Product product = productService.getById(productId);
        double average = reviewService.getAverageRating(product);
        return ResponseEntity.ok(average);
    }

    // ✅ Клас за входни данни
    public record ReviewInput(Long productId, int rating, String comment) {}
}
