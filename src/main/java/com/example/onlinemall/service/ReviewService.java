package com.example.onlinemall.service;

import com.example.onlinemall.model.Product;
import com.example.onlinemall.model.Review;
import com.example.onlinemall.model.User;
import com.example.onlinemall.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public Review addReview(User user, Product product, int rating, String comment) {
        if (reviewRepository.existsByProductAndUser(product, user)) {
            throw new RuntimeException("Вече сте оставили отзив за този продукт.");
        }

        Review review = Review.builder()
                .user(user)
                .product(product)
                .rating(rating)
                .comment(comment)
                .build();

        return reviewRepository.save(review);
    }

    public List<Review> getReviewsForProduct(Product product) {
        return reviewRepository.findByProduct(product);
    }

    public double getAverageRating(Product product) {
        List<Review> reviews = getReviewsForProduct(product);
        if (reviews.isEmpty()) return 0.0;

        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }
}
