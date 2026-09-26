package com.example.kadai_002.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kadai_002.entity.Favorite;
import com.example.kadai_002.entity.Shop;
import com.example.kadai_002.entity.User;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findByUser(User user);

    Optional<Favorite> findByUserAndShop(User user, Shop shop);
}
