package com.example.kadai_002.controller;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.kadai_002.entity.Subscription;
import com.example.kadai_002.entity.User;
import com.example.kadai_002.repository.SubscriptionRepository;
import com.example.kadai_002.repository.UserRepository;


@Controller
public class SubscriptionController {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    public SubscriptionController(
            SubscriptionRepository subscriptionRepository,
            UserRepository userRepository) {

        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/subscription")
    public String index(Principal principal, Model model) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow();

        Subscription subscription =
                subscriptionRepository.findByUser(user)
                        .orElse(null);

        model.addAttribute("subscription", subscription);

        return "subscription/index";
    }

    @PostMapping("/subscription")
    public String subscribe(Principal principal) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow();

        Subscription subscription =
                subscriptionRepository.findByUser(user)
                        .orElseGet(Subscription::new);

        LocalDateTime now = LocalDateTime.now();

        subscription.setUser(user);
        subscription.setStatus("ACTIVE");
        subscription.setStartedAt(now);
        subscription.setEndedAt(null);

        if (subscription.getCreatedAt() == null) {
            subscription.setCreatedAt(now);
        }

        subscription.setUpdatedAt(now);

        subscriptionRepository.save(subscription);

        return "redirect:/mypage";
    }

    @PostMapping("/subscription/cancel")
    public String cancel(Principal principal) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow();

        Subscription subscription =
                subscriptionRepository.findByUser(user)
                        .orElseThrow();

        subscription.setStatus("CANCELED");
        subscription.setEndedAt(LocalDateTime.now());
        subscription.setUpdatedAt(LocalDateTime.now());

        subscriptionRepository.save(subscription);

        return "redirect:/mypage";
    }
}