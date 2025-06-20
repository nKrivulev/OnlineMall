package com.example.onlinemall.service;

import com.example.onlinemall.dto.StoreResponse;
import com.example.onlinemall.model.Store;
import com.example.onlinemall.model.StoreCategory;
import com.example.onlinemall.model.User;
import com.example.onlinemall.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    public Store saveStore(Store store) {
        return storeRepository.save(store);
    }

    public List<Store> getStoresByFloor(int floor) {
        return storeRepository.findByFloor(floor);
    }

    public List<Store> getStoresByOwner(User owner) {
        return storeRepository.findByOwner(owner);
    }

    public Optional<Store> getStoreById(Long id) {
        return storeRepository.findById(id);
    }

    public Store getById(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Магазинът не съществува"));
    }

    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    public void deleteStore(Store store) {
        storeRepository.delete(store);
    }

    public List<Store> getStoresByCategory(StoreCategory category) {
        return storeRepository.findByCategory(category);
    }

    public StoreResponse toResponse(Store store) {
        StoreResponse response = new StoreResponse();
        response.setId(store.getId());
        response.setName(store.getName());
        response.setDescription(store.getDescription());
        response.setImageUrl(store.getImageUrl());
        response.setFloor(store.getFloor());
        response.setCategory(store.getCategory().name());
        response.setOwnerUsername(store.getOwner().getUsername());
        return response;
    }
}
