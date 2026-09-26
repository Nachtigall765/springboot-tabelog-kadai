package com.example.kadai_002.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.kadai_002.entity.Category;
import com.example.kadai_002.entity.Review;
import com.example.kadai_002.entity.Shop;
import com.example.kadai_002.entity.User;
import com.example.kadai_002.repository.CategoryRepository;
import com.example.kadai_002.repository.ReviewRepository;
import com.example.kadai_002.repository.ShopRepository;
import com.example.kadai_002.repository.UserRepository;




@Controller
public class AdminController {
	private final ShopRepository shopRepository;
	private final CategoryRepository categoryRepository;
	private final UserRepository userRepository;
	private final ReviewRepository reviewRepository;




	public AdminController(
	        ShopRepository shopRepository,
	        CategoryRepository categoryRepository,
	        UserRepository userRepository,
	        ReviewRepository reviewRepository) {

	    this.shopRepository = shopRepository;
	    this.categoryRepository = categoryRepository;
	    this.userRepository = userRepository;
	    this.reviewRepository = reviewRepository;
	}
	@GetMapping("/admin/login")
    public String login() {
        return "admin/login";
    }
    @GetMapping("/admin")
    public String index() {
        return "admin/index";
    }
    @GetMapping("/admin/shops")
    public String shops(
            @RequestParam(required = false) String keyword,
            Model model) {

        List<Shop> shops;

        if (keyword != null && !keyword.isBlank()) {
            shops = shopRepository.findByNameContaining(keyword);
        } else {
            shops = shopRepository.findAll();
        }

        model.addAttribute("shops", shops);
        model.addAttribute("keyword", keyword);

        return "admin/shops/index";
    }

    @GetMapping("/admin/shops/create")
    public String createShop(Model model) {

        model.addAttribute("categories", categoryRepository.findAll());

        return "admin/shops/create";
    }

    @PostMapping("/admin/shops")
    public String storeShop(
            @RequestParam String name,
            @RequestParam Long categoryId,
            @RequestParam String address,
            @RequestParam String description) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow();

        Shop shop = new Shop();

        shop.setName(name);
        shop.setCategory(category);
        shop.setAddress(address);
        shop.setDescription(description);
        shop.setCreatedAt(LocalDateTime.now());
        shop.setUpdatedAt(LocalDateTime.now());

        shopRepository.save(shop);

        return "redirect:/admin/shops";
    }

    @GetMapping("/admin/shops/{id}/edit")
    public String editShop(
            @PathVariable Long id,
            Model model) {

        Shop shop = shopRepository.findById(id)
                .orElseThrow();

        model.addAttribute("shop", shop);
        model.addAttribute("categories", categoryRepository.findAll());

        return "admin/shops/edit";
    }

    @PostMapping("/admin/shops/{id}")
    public String updateShop(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam Long categoryId,
            @RequestParam String address,
            @RequestParam String description) {

        Shop shop = shopRepository.findById(id)
                .orElseThrow();

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow();

        shop.setName(name);
        shop.setCategory(category);
        shop.setAddress(address);
        shop.setDescription(description);
        shop.setUpdatedAt(LocalDateTime.now());

        shopRepository.save(shop);

        return "redirect:/admin/shops";
    }

    @PostMapping("/admin/shops/{id}/delete")
    public String deleteShop(@PathVariable Long id) {

        shopRepository.deleteById(id);

        return "redirect:/admin/shops";
    }
    @GetMapping("/admin/users")
    public String users(
            @RequestParam(required = false) String keyword,
            Model model) {

        List<User> users;

        if (keyword != null && !keyword.isBlank()) {
            users = userRepository
                    .findByNameContainingOrEmailContaining(keyword, keyword);
        } else {
            users = userRepository.findAll();
        }

        model.addAttribute("users", users);
        model.addAttribute("keyword", keyword);

        return "admin/users/index";
    }
    @GetMapping("/admin/categories")
    public String categories(
            @RequestParam(required = false) String keyword,
            Model model) {

        List<Category> categories;

        if (keyword != null && !keyword.isBlank()) {
            categories = categoryRepository.findByNameContaining(keyword);
        } else {
            categories = categoryRepository.findAll();
        }

        model.addAttribute("categories", categories);
        model.addAttribute("keyword", keyword);

        return "admin/categories/index";
    }
    @PostMapping("/admin/categories")
    public String createCategory(@RequestParam String name) {
        Category category = new Category();
        category.setName(name);
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());

        categoryRepository.save(category);

        return "redirect:/admin/categories";
    }
    @GetMapping("/admin/categories/{id}/edit")
    public String editCategory(@PathVariable Long id, Model model) {
        Category category = categoryRepository.findById(id)
                .orElseThrow();

        model.addAttribute("category", category);

        return "admin/categories/edit";
    }

    @PostMapping("/admin/categories/{id}/edit")
    public String updateCategory(
            @PathVariable Long id,
            @RequestParam String name) {

        Category category = categoryRepository.findById(id)
                .orElseThrow();

        category.setName(name);
        category.setUpdatedAt(LocalDateTime.now());

        categoryRepository.save(category);

        return "redirect:/admin/categories";
    }

    @PostMapping("/admin/categories/{id}/delete")
    public String deleteCategory(@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return "redirect:/admin/categories";
    }
    @GetMapping("/admin/reviews")
    public String reviews(Model model) {
        model.addAttribute("reviews", reviewRepository.findAll());
        return "admin/reviews/index";
    }
    @PostMapping("/admin/reviews/{id}/hide")
    public String hideReview(@PathVariable Long id) {

        Review review = reviewRepository.findById(id)
                .orElseThrow();

        review.setVisible(false);
        reviewRepository.save(review);

        return "redirect:/admin/reviews";
    }
}