package com.example.kadai_002.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kadai_002.entity.Shop;

public interface ShopRepository extends JpaRepository<Shop, Long> {

    List<Shop> findByNameContaining(String keyword);
    List<Shop> findByCategoryId(Long categoryId);
    
}