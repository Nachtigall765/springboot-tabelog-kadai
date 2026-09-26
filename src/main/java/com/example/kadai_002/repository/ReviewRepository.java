package com.example.kadai_002.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kadai_002.entity.Review;
import com.example.kadai_002.entity.Shop;
import com.example.kadai_002.entity.User;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByShop(Shop shop);
    
    List<Review> findByShopAndVisibleTrue(Shop shop);

    List<Review> findByUser(User user);
}