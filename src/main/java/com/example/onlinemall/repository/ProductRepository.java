package com.example.onlinemall.repository;

import com.example.onlinemall.model.Product;
import com.example.onlinemall.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStore(Store store);
    List<Product> findByNameContainingIgnoreCase(String keyword);

}
