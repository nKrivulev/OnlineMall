package com.example.onlinemall.service;

import com.example.onlinemall.dto.ProductResponse;
import com.example.onlinemall.model.Product;
import com.example.onlinemall.model.Store;
import com.example.onlinemall.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewService reviewService;

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getProductsByStore(Store store) {
        return productRepository.findByStore(store);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Продуктът не съществува"));
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void delete(Product product) {
        productRepository.delete(product);
    }

    public List<Product> searchByName(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    public List<Product> getByStore(Store store) {
        return productRepository.findByStore(store);
    }

    public ProductResponse toResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setImageUrl(product.getImageUrl());
        response.setStoreName(product.getStore().getName());

        // 👉 Добавяме рейтинг и брой ревюта
        double averageRating = reviewService.getAverageRating(product);
        int reviewCount = reviewService.getReviewsForProduct(product).size();

        response.setAverageRating(averageRating);
        response.setReviewCount(reviewCount);

        return response;
    }

}
