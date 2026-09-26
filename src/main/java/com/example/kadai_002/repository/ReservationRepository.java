package com.example.kadai_002.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kadai_002.entity.Reservation;
import com.example.kadai_002.entity.User;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUser(User user);
}