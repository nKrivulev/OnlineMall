package com.example.onlinemall.controller;

import com.example.onlinemall.dto.AddToCartRequest;
import com.example.onlinemall.model.CartItem;
import com.example.onlinemall.service.CartService;
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

    @PostMapping("/{productId}")
    public ResponseEntity<?> addToCart(
            Principal principal,
            @PathVariable Long productId,
            @RequestBody AddToCartRequest input
    ) {
        return cartService.addToCart(principal, productId, input.getQuantity());
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
