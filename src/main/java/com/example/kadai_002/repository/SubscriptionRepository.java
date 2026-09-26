package com.example.kadai_002.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kadai_002.entity.Subscription;
import com.example.kadai_002.entity.User;

public interface SubscriptionRepository
        extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByUser(User user);
}
