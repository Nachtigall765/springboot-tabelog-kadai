package com.example.kadai_002.controller;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.kadai_002.entity.Shop;
import com.example.kadai_002.repository.CategoryRepository;
import com.example.kadai_002.repository.ReviewRepository;
import com.example.kadai_002.repository.ShopRepository;

@Controller
public class ShopController {

    private final ShopRepository shopRepository;
    private final CategoryRepository categoryRepository;
    private final ReviewRepository reviewRepository;
    
    public ShopController(
            ShopRepository shopRepository,
            CategoryRepository categoryRepository,
            ReviewRepository reviewRepository) {

        this.shopRepository = shopRepository;
        this.categoryRepository = categoryRepository;
        this.reviewRepository = reviewRepository;
    }

    @GetMapping("/shops")
    public String index(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            Model model) {

        List<Shop> shops;

        if (keyword != null && !keyword.isBlank()) {
            shops = shopRepository.findByNameContaining(keyword);
        } else if (categoryId != null) {
            shops = shopRepository.findByCategoryId(categoryId);
        } else {
            shops = shopRepository.findAll();
        }

        model.addAttribute("shops", shops);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categories", categoryRepository.findAll());

        return "shops/index";
    }


    @GetMapping("/shops/{id}")
    public String show(@PathVariable("id") Long id, Model model) {

        Shop shop = shopRepository.findById(id).orElseThrow();

        model.addAttribute("shop", shop);
        model.addAttribute("reviews", reviewRepository.findByShopAndVisibleTrue(shop));

        return "shops/show";
    }


}