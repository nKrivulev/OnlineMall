package com.example.onlinemall.controller;

import com.example.onlinemall.dto.OrderResponse;
import com.example.onlinemall.model.User;
import com.example.onlinemall.service.OrderService;
import com.example.onlinemall.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getMyOrders(Principal principal) {
        User user = userService.getByUsername(principal.getName());
        List<OrderResponse> orders = orderService.getOrdersByUser(user).stream()
                .map(orderService::toResponse)
                .toList();

        return ResponseEntity.ok(orders);
    }
}
