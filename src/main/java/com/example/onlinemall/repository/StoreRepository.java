package com.example.onlinemall.repository;

import com.example.onlinemall.model.Store;
import com.example.onlinemall.model.StoreCategory;
import com.example.onlinemall.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByFloor(int floor);
    List<Store> findByOwner(User owner);
    List<Store> findByCategory(StoreCategory category);
}
