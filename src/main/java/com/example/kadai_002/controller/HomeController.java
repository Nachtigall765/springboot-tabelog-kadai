package com.example.kadai_002.controller;

import java.security.Principal;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.kadai_002.entity.User;
import com.example.kadai_002.repository.UserRepository;




@Controller
public class HomeController {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public HomeController(
	        UserRepository userRepository,
	        PasswordEncoder passwordEncoder) {
	    this.userRepository = userRepository;
	    this.passwordEncoder = passwordEncoder;
	}


    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }
    
    @GetMapping("/mypage")
    public String mypage() {
        return "mypage/index";
    }
    @GetMapping("/mypage/profile")
    public String profile(Principal principal, Model model) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow();

        model.addAttribute("user", user);

        return "mypage/profile";
    }


    @PostMapping("/mypage/profile")
    public String updateProfile(
            Principal principal,
            @RequestParam("name") String name,
            @RequestParam("email") String email) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow();

        user.setName(name);
        user.setEmail(email);

        userRepository.save(user);

        return "redirect:/mypage/profile";
    }
    @GetMapping("/password-reset")
    public String passwordReset() {
        return "password-reset";
    }

    @PostMapping("/password-reset")
    public String passwordReset(
            @RequestParam String email,
            @RequestParam String password,
            Model model) {

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            model.addAttribute("error", "登録されているメールアドレスが見つかりません。");
            return "password-reset";
        }

        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        return "redirect:/login?reset";
    }


}