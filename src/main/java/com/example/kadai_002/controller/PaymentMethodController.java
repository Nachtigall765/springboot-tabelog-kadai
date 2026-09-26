package com.example.kadai_002.controller;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.kadai_002.entity.PaymentMethod;
import com.example.kadai_002.entity.User;
import com.example.kadai_002.repository.PaymentMethodRepository;
import com.example.kadai_002.repository.UserRepository;

@Controller
public class PaymentMethodController {

    private final PaymentMethodRepository paymentMethodRepository;
    private final UserRepository userRepository;

    public PaymentMethodController(
            PaymentMethodRepository paymentMethodRepository,
            UserRepository userRepository) {

        this.paymentMethodRepository = paymentMethodRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/payment-method")
    public String edit(Principal principal, Model model) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow();

        PaymentMethod paymentMethod =
                paymentMethodRepository.findByUser(user)
                        .orElse(null);

        model.addAttribute("paymentMethod", paymentMethod);

        return "payment-method/edit";
    }

    @PostMapping("/payment-method")
    public String update(
            @RequestParam("cardToken") String cardToken,
            Principal principal) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow();

        PaymentMethod paymentMethod =
                paymentMethodRepository.findByUser(user)
                        .orElseGet(PaymentMethod::new);

        LocalDateTime now = LocalDateTime.now();

        paymentMethod.setUser(user);
        paymentMethod.setCardToken(cardToken);

        if (paymentMethod.getCreatedAt() == null) {
            paymentMethod.setCreatedAt(now);
        }

        paymentMethod.setUpdatedAt(now);

        paymentMethodRepository.save(paymentMethod);

        return "redirect:/mypage";
    }
}
