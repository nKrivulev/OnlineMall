package com.example.onlinemall.repository;

import com.example.onlinemall.model.Product;
import com.example.onlinemall.model.Review;
import com.example.onlinemall.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProduct(Product product);
    boolean existsByProductAndUser(Product product, User user);
}
