package com.example.kadai_002.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kadai_002.entity.PaymentMethod;
import com.example.kadai_002.entity.User;

public interface PaymentMethodRepository
        extends JpaRepository<PaymentMethod, Long> {

    Optional<PaymentMethod> findByUser(User user);
}