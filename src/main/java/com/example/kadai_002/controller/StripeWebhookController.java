package com.example.kadai_002.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kadai_002.entity.Reservation;
import com.example.kadai_002.entity.Shop;
import com.example.kadai_002.entity.User;
import com.example.kadai_002.repository.ReservationRepository;
import com.example.kadai_002.repository.ShopRepository;
import com.example.kadai_002.repository.UserRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;

@RestController
@RequestMapping("/stripe")
public class StripeWebhookController {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    public StripeWebhookController(
            ReservationRepository reservationRepository,
            UserRepository userRepository,
            ShopRepository shopRepository) {

        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.shopRepository = shopRepository;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> webhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String signature) {
    	
    	System.out.println("=== WEBHOOK RECEIVED ===");
    	System.out.println(payload);

        Event event;

        try {
            event = Webhook.constructEvent(
                    payload,
                    signature,
                    webhookSecret
            );
        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().body("Invalid signature");
        }
        
        System.out.println("=== EVENT TYPE === "+ event.getType());

        if ("checkout.session.completed".equals(event.getType())) {

            Session session =
                    (Session) event.getDataObjectDeserializer()
                            .getObject()
                            .orElse(null);
            System.out.println("session = "+ session);
            if (session != null) {
            	
            	System.out.println("=== Stripe Webhook ===");
            	System.out.println("metadata = " + session.getMetadata());
            	
                String userId = session.getMetadata().get("userId");
                String shopId = session.getMetadata().get("shopId");
                String reservationAt =
                		session.getMetadata().get("reservationAt");
                String numberOfPeople =
                		session.getMetadata().get("numberOfPeople");

                if (userId != null
                        && shopId != null
                        && reservationAt != null
                        && numberOfPeople != null) {

                    User user = userRepository
                            .findById(Long.valueOf(userId))
                            .orElseThrow();

                    Shop shop = shopRepository
                            .findById(Long.valueOf(shopId))
                            .orElseThrow();

                    Reservation reservation = new Reservation();

                    reservation.setUser(user);
                    reservation.setShop(shop);
                    reservation.setReservationAt(
                            LocalDateTime.parse(reservationAt));
                    reservation.setNumberOfPeople(
                            Integer.valueOf(numberOfPeople));
                    reservation.setCreatedAt(LocalDateTime.now());
                    reservation.setUpdatedAt(LocalDateTime.now());

                    System.out.println("=== 予約保存直前 ===");
                    System.out.println("userId = " + userId);
                    System.out.println("shopId = " + shopId);
                    System.out.println("reservationAt = " + reservationAt);
                    System.out.println("numberOfPeople = " + numberOfPeople);

                    reservationRepository.save(reservation);

                    System.out.println("=== 予約保存完了 ===");

                }
            }
        }

        return ResponseEntity.ok("success");
    }
}