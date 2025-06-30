package com.example.onlinemall.service;

import com.example.onlinemall.dto.OrderResponse;
import com.example.onlinemall.dto.ProductResponse;
import com.example.onlinemall.model.Order;
import com.example.onlinemall.model.Product;
import com.example.onlinemall.model.User;
import com.example.onlinemall.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    public Order createOrder(User user, List<Product> products, double totalAmount) {
        Order order = Order.builder()
                .user(user)
                .products(products)
                .totalAmount(totalAmount)
                .createdAt(LocalDateTime.now())
                .build();

        return orderRepository.save(order);
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    public OrderResponse toResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setTotalAmount(order.getTotalAmount());
        response.setCreatedAt(order.getCreatedAt());

        List<ProductResponse> productResponses = order.getProducts().stream()
                .map(productService::toResponse)
                .toList();

        response.setProducts(productResponses);
        return response;
    }
}
