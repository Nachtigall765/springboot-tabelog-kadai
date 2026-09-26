package com.example.kadai_002.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.kadai_002.entity.Favorite;
import com.example.kadai_002.entity.Shop;
import com.example.kadai_002.entity.User;
import com.example.kadai_002.repository.FavoriteRepository;
import com.example.kadai_002.repository.ShopRepository;
import com.example.kadai_002.repository.UserRepository;

@Controller
public class FavoriteController {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;

    public FavoriteController(
            FavoriteRepository favoriteRepository,
            UserRepository userRepository,
            ShopRepository shopRepository) {

        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.shopRepository = shopRepository;
    }

    @GetMapping("/favorites")
    public String index(Principal principal, Model model) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow();

        List<Favorite> favorites = favoriteRepository.findByUser(user);

        model.addAttribute("favorites", favorites);

        return "favorites/index";
    }

    @PostMapping("/favorites/{shopId}")
    public String store(
            @PathVariable Long shopId,
            Principal principal) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow();

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow();

        // 二重登録防止
        if (favoriteRepository.findByUserAndShop(user, shop).isEmpty()) {
            Favorite favorite = new Favorite();
            favorite.setUser(user);
            favorite.setShop(shop);
            favorite.setCreatedAt(LocalDateTime.now());
            favorite.setUpdatedAt(LocalDateTime.now());
           
            favoriteRepository.save(favorite);
        }

        return "redirect:/shops/" + shopId;
    }

    @PostMapping("/favorites/{shopId}/delete")
    public String delete(
            @PathVariable Long shopId,
            Principal principal) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow();

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow();

        favoriteRepository.findByUserAndShop(user, shop)
                .ifPresent(favoriteRepository::delete);

        return "redirect:/favorites";
    }
}


