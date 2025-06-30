package com.example.onlinemall.config;

import com.example.onlinemall.model.*;
import com.example.onlinemall.repository.ProductRepository;
import com.example.onlinemall.repository.StoreRepository;
import com.example.onlinemall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) return;

        User admin = userRepository.save(User.builder()
                .username("admin")
                .email("admin@example.com")
                .password(passwordEncoder.encode("admin123"))
                .role(Role.ADMIN)
                .balance(100)
                .build());

        User seller = userRepository.save(User.builder()
                .username("seller")
                .email("seller@example.com")
                .password(passwordEncoder.encode("seller123"))
                .role(Role.SELLER)
                .balance(100)
                .build());

        User user = userRepository.save(User.builder()
                .username("user")
                .email("user@example.com")
                .password(passwordEncoder.encode("user123"))
                .role(Role.USER)
                .balance(100)
                .build());

        createStoreWithProducts("Fashion House", "Модерни дрехи", "https://picsum.photos/seed/fashion/300/200", 1, StoreCategory.CLOTHING, seller);
        createStoreWithProducts("Shoe Center", "Обувки за всеки", "https://picsum.photos/seed/shoes/300/200", 1, StoreCategory.CLOTHING, seller);

        createStoreWithProducts("Tech World", "Най-нова техника", "https://picsum.photos/seed/tech/300/200", 2, StoreCategory.ELECTRONICS, seller);
        createStoreWithProducts("GadgetZone", "Гаджети и аксесоари", "https://picsum.photos/seed/gadgets/300/200", 2, StoreCategory.ELECTRONICS, seller);

        createStoreWithProducts("Fresh Market", "Плодове и зеленчуци", "https://picsum.photos/seed/market/300/200", 3, StoreCategory.FOOD, seller);
        createStoreWithProducts("Snack Bar", "Бързи закуски", "https://picsum.photos/seed/snacks/300/200", 3, StoreCategory.FOOD, seller);

        System.out.println("✅ Заредени са тестови данни.");
    }

    private void createStoreWithProducts(String name, String desc, String img, int floor, StoreCategory cat, User owner) {
        Store store = storeRepository.save(Store.builder()
                .name(name)
                .description(desc)
                .imageUrl(img)
                .floor(floor)
                .category(cat)
                .owner(owner)
                .build());

        productRepository.saveAll(List.of(
                Product.builder()
                        .name(name + " Продукт 1")
                        .description("Описание на продукт 1")
                        .price(29.99)
                        .imageUrl("https://picsum.photos/seed/" + name.replace(" ", "_") + "_1/200/200")
                        .store(store)
                        .build(),
                Product.builder()
                        .name(name + " Продукт 2")
                        .description("Описание на продукт 2")
                        .price(49.99)
                        .imageUrl("https://picsum.photos/seed/" + name.replace(" ", "_") + "_2/200/200")
                        .store(store)
                        .build()
        ));
    }
}