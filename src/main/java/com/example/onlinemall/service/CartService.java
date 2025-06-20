package com.example.onlinemall.service;

import com.example.onlinemall.model.CartItem;
import com.example.onlinemall.model.Product;
import com.example.onlinemall.model.User;
import com.example.onlinemall.repository.CartItemRepository;
import com.example.onlinemall.repository.ProductRepository;
import com.example.onlinemall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderService orderService;

    public ResponseEntity<?> addToCart(Principal principal, Long productId, int quantity) {
        if (quantity <= 0) {
            return ResponseEntity.badRequest().body("Количество трябва да е положително.");
        }

        User user = getUser(principal);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Продуктът не е намерен."));

        CartItem item = cartItemRepository.findByUser(user).stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (item != null) {
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
            return ResponseEntity.ok("Актуализирано количество в количката.");
        }

        CartItem newItem = CartItem.builder()
                .user(user)
                .product(product)
                .quantity(quantity)
                .build();

        cartItemRepository.save(newItem);
        return ResponseEntity.ok("Продуктът е добавен в количката.");
    }

    public List<CartItem> getCartItems(Principal principal) {
        return cartItemRepository.findByUser(getUser(principal));
    }

    public void removeItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    public ResponseEntity<String> checkout(Principal principal) {
        User user = getUser(principal);

        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        if (cartItems.isEmpty()) {
            return ResponseEntity.badRequest().body("Количката е празна.");
        }

        double total = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        if (user.getBalance() < total) {
            return ResponseEntity.badRequest().body("Недостатъчен баланс: нужни са " + total + " лв.");
        }

        user.setBalance(user.getBalance() - total);
        userRepository.save(user);

        List<Product> products = cartItems.stream()
                .map(CartItem::getProduct)
                .toList();

        orderService.createOrder(user, products, total);
        cartItemRepository.deleteByUser(user);

        return ResponseEntity.ok("Успешно завършена поръчка. Изхарчени: " + total + " лв.");
    }

    private User getUser(Principal principal) {
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Потребителят не е намерен."));
    }
}
