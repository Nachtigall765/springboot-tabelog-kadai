package com.example.kadai_002.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kadai_002.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByVerificationToken(String verificationToken);

    List<User> findByNameContainingOrEmailContaining(
            String name,
            String email);
}