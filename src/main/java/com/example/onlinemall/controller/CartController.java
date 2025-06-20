package com.example.onlinemall.controller;

import com.example.onlinemall.model.CartItem;
import com.example.onlinemall.service.CartService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            Principal principal,
            @RequestParam Long productId,
            @RequestParam @Min(1) int quantity
    ) {
        return cartService.addToCart(principal, productId, quantity);
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(Principal principal) {
        return ResponseEntity.ok(cartService.getCartItems(principal));
    }

    // ✅ Премахва артикул по неговото ID
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long cartItemId) {
        cartService.removeItem(cartItemId);
        return ResponseEntity.noContent().build();
    }

    // ✅ Завършване на поръчка (checkout)
    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(Principal principal) {
        return cartService.checkout(principal);
    }
}
