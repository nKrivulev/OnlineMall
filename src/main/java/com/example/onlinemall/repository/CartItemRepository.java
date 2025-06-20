package com.example.onlinemall.repository;

import com.example.onlinemall.model.CartItem;
import com.example.onlinemall.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
    void deleteByUser(User user);
}
