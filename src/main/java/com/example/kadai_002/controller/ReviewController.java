package com.example.kadai_002.controller;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.kadai_002.entity.Review;
import com.example.kadai_002.entity.Shop;
import com.example.kadai_002.entity.User;
import com.example.kadai_002.repository.ReviewRepository;
import com.example.kadai_002.repository.ShopRepository;
import com.example.kadai_002.repository.UserRepository;


@Controller
public class ReviewController {
	private final ReviewRepository reviewRepository;
	private final ShopRepository shopRepository;
	private final UserRepository userRepository;

	public ReviewController(
	        ReviewRepository reviewRepository,
	        ShopRepository shopRepository,
	        UserRepository userRepository) {
	    this.reviewRepository = reviewRepository;
	    this.shopRepository = shopRepository;
	    this.userRepository = userRepository;
	}
	@GetMapping("/reviews/create")
	public String create(@RequestParam("shopId") Long shopId, Model model) {

	    Shop shop = shopRepository.findById(shopId)
	            .orElseThrow();

	    model.addAttribute("shop", shop);
	    model.addAttribute("shopId", shopId);

	    return "reviews/create";
	}

	@PostMapping("/reviews")
	public String store(
	        @RequestParam("shopId") Long shopId,
	        @RequestParam("rating") Integer rating,
	        @RequestParam("body") String body,
	        Principal principal) {

	    Shop shop = shopRepository.findById(shopId).orElseThrow();

	    User user = userRepository.findByEmail(principal.getName())
	            .orElseThrow();

	    Review review = new Review();
	    review.setShop(shop);
	    review.setUser(user);
	    review.setRating(rating);
	    review.setBody(body);

	    review.setCreatedAt(LocalDateTime.now());
	    review.setUpdatedAt(LocalDateTime.now());

	    reviewRepository.save(review);
	    return "redirect:/shops/" + shopId;
	}
	@GetMapping("/reviews/{id}/edit")
	public String edit(
	        @PathVariable Long id,
	        Principal principal,
	        Model model) {

	    Review review = reviewRepository.findById(id)
	            .orElseThrow();

	    User user = userRepository.findByEmail(principal.getName())
	            .orElseThrow();

	    // 他人のレビューは編集不可
	    if (!review.getUser().getId().equals(user.getId())) {
	        throw new IllegalArgumentException("このレビューは編集できません");
	    }

	    model.addAttribute("review", review);

	    return "reviews/edit";
	}

	@PostMapping("/reviews/{id}/edit")
	public String update(
	        @PathVariable Long id,
	        @RequestParam("rating") Integer rating,
	        @RequestParam("body") String body,
	        Principal principal) {

	    Review review = reviewRepository.findById(id)
	            .orElseThrow();

	    User user = userRepository.findByEmail(principal.getName())
	            .orElseThrow();

	    if (!review.getUser().getId().equals(user.getId())) {
	        throw new IllegalArgumentException("このレビューは編集できません");
	    }

	    review.setRating(rating);
	    review.setBody(body);

	    reviewRepository.save(review);

	    return "redirect:/shops/" + review.getShop().getId();
	}

	@PostMapping("/reviews/{id}/delete")
	public String delete(
	        @PathVariable Long id,
	        Principal principal) {

	    Review review = reviewRepository.findById(id)
	            .orElseThrow();

	    User user = userRepository.findByEmail(principal.getName())
	            .orElseThrow();

	    if (!review.getUser().getId().equals(user.getId())) {
	        throw new IllegalArgumentException("このレビューは削除できません");
	    }

	    Long shopId = review.getShop().getId();

	    reviewRepository.delete(review);

	    return "redirect:/shops/" + shopId;
	}


}
