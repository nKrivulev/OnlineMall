package com.example.onlinemall.config;

import com.example.onlinemall.model.*;
import com.example.onlinemall.repository.*;
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

        // Създай потребители
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

        // Създай магазин
        Store store = storeRepository.save(Store.builder()
                .name("TechShop")
                .description("Техника и джаджи")
                .imageUrl("https://via.placeholder.com/150")
                .floor(1)
                .owner(seller)
                .build());

        // Продукти
        productRepository.saveAll(List.of(
                Product.builder()
                        .name("Лаптоп Lenovo")
                        .description("Бърз и надежден лаптоп")
                        .price(1200)
                        .imageUrl("https://via.placeholder.com/150")
                        .store(store)
                        .build(),

                Product.builder()
                        .name("Слушалки Sony")
                        .description("Безжични слушалки с шумопотискане")
                        .price(250)
                        .imageUrl("https://via.placeholder.com/150")
                        .store(store)
                        .build()
        ));

        System.out.println("✅ Заредени са тестови данни.");
    }
}
