package com.example.onlinemall.controller;

import com.example.onlinemall.dto.ProductRequest;
import com.example.onlinemall.dto.ProductResponse;
import com.example.onlinemall.model.Product;
import com.example.onlinemall.model.Store;
import com.example.onlinemall.model.User;
import com.example.onlinemall.service.ProductService;
import com.example.onlinemall.service.StoreService;
import com.example.onlinemall.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final StoreService storeService;
    private final UserService userService;

    @GetMapping("/store/{storeId}")
    public ResponseEntity<?> getProductsByStore(
            @PathVariable Long storeId,
            Principal principal
    ) {
        User user = userService.getByUsername(principal.getName());
        Store store = storeService.getById(storeId);

        if (!store.getOwner().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Нямате достъп до този магазин.");
        }

        List<ProductResponse> products = productService.getByStore(store).stream()
                .map(productService::toResponse)
                .toList();

        return ResponseEntity.ok(products);
    }

    @GetMapping("/public/store/{storeId}")
    public ResponseEntity<?> getPublicProductsByStore(@PathVariable Long storeId) {
        Store store = storeService.getById(storeId);
        List<ProductResponse> products = productService.getByStore(store).stream()
                .map(productService::toResponse)
                .toList();

        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String keyword) {
        List<ProductResponse> results = productService.searchByName(keyword).stream()
                .map(productService::toResponse)
                .toList();

        return ResponseEntity.ok(results);
    }

    @PostMapping("/add/{storeId}")
    public ResponseEntity<?> addProductToStore(
            @PathVariable Long storeId,
            @Valid @RequestBody ProductRequest request,
            Principal principal
    ) {
        User user = userService.getByUsername(principal.getName());
        Store store = storeService.getById(storeId);

        if (!store.getOwner().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Нямате права за този магазин.");
        }

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .imageUrl(request.getImageUrl())
                .store(store)
                .build();

        Product saved = productService.save(product);
        return ResponseEntity.ok(productService.toResponse(saved));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductRequest request,
            Principal principal
    ) {
        User user = userService.getByUsername(principal.getName());
        Product product = productService.getById(productId);

        if (!product.getStore().getOwner().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Нямате права да редактирате този продукт.");
        }

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setImageUrl(request.getImageUrl());

        Product updated = productService.save(product);
        return ResponseEntity.ok(productService.toResponse(updated));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId, Principal principal) {
        User user = userService.getByUsername(principal.getName());
        Product product = productService.getById(productId);

        if (!product.getStore().getOwner().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Нямате права да изтриете този продукт.");
        }

        productService.delete(product);
        return ResponseEntity.ok("Продуктът е изтрит успешно.");
    }
}
