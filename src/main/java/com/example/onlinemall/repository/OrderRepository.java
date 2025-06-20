package com.example.onlinemall.repository;

import com.example.onlinemall.model.Order;
import com.example.onlinemall.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
