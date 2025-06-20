package com.example.onlinemall.controller;

import com.example.onlinemall.dto.CreateStoreRequest;
import com.example.onlinemall.dto.StoreResponse;
import com.example.onlinemall.model.*;
import com.example.onlinemall.repository.StoreRepository;
import com.example.onlinemall.repository.UserRepository;
import com.example.onlinemall.service.ProductService;
import com.example.onlinemall.service.StoreService;
import com.example.onlinemall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/stores")
@CrossOrigin(origins = "*")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoreRepository storeRepository;

    @GetMapping("/floor/{floor}")
    public ResponseEntity<List<Store>> getStoresByFloor(@PathVariable int floor) {
        return ResponseEntity.ok(storeService.getStoresByFloor(floor));
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<Store> getStoreById(@PathVariable Long storeId) {
        return storeService.getStoreById(storeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<StoreResponse> getAllStores() {
        return storeService.getAllStores().stream()
                .map(storeService::toResponse)
                .toList();
    }

    @PostMapping
    public ResponseEntity<?> createStore(@RequestBody CreateStoreRequest request, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Потребителят не е намерен"));

        if (user.getRole() != Role.SELLER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Само продавачи могат да създават магазини.");
        }

        Store store = Store.builder()
                .name(request.getName())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .floor(request.getFloor())
                .category(request.getCategory())
                .owner(user)
                .build();

        return ResponseEntity.ok(storeRepository.save(store));
    }

    @GetMapping("/category/{category}")
    public List<Store> getStoresByCategory(@PathVariable StoreCategory category) {
        return storeService.getStoresByCategory(category);
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyStores(Principal principal) {
        User user = userService.getByUsername(principal.getName());

        if (!user.getRole().name().equals("SELLER")) {
            return ResponseEntity.status(403).body("Само продавачи имат магазини.");
        }

        return ResponseEntity.ok(storeService.getStoresByOwner(user));
    }

    @PutMapping("/{storeId}")
    public ResponseEntity<?> updateStore(
            @PathVariable Long storeId,
            @RequestBody CreateStoreRequest request,
            Principal principal
    ) {
        User user = userService.getByUsername(principal.getName());
        Store store = storeService.getById(storeId);

        if (!store.getOwner().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Нямате права да редактирате този магазин.");
        }

        store.setName(request.getName());
        store.setDescription(request.getDescription());
        store.setImageUrl(request.getImageUrl());
        store.setFloor(request.getFloor());

        storeService.saveStore(store);
        return ResponseEntity.ok("Магазинът е обновен успешно.");
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<?> deleteStore(@PathVariable Long storeId, Principal principal) {
        User user = userService.getByUsername(principal.getName());
        Store store = storeService.getById(storeId);

        if (!store.getOwner().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Нямате права да изтриете този магазин.");
        }

        List<Product> products = productService.getByStore(store);
        for (Product product : products) {
            productService.delete(product);
        }

        storeService.deleteStore(store);
        return ResponseEntity.ok("Магазинът е изтрит успешно.");
    }
}
