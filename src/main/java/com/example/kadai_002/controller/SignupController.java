package com.example.kadai_002.controller;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.kadai_002.entity.User;
import com.example.kadai_002.repository.UserRepository;
import com.example.kadai_002.service.MailService;



@Controller
public class SignupController {

    private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final MailService mailService;

	@Value("${app.base-url:http://localhost:8080}")
	private String baseUrl;

	public SignupController(
	        UserRepository userRepository,
	        PasswordEncoder passwordEncoder,
	        MailService mailService) {

	    this.userRepository = userRepository;
	    this.passwordEncoder = passwordEncoder;
	    this.mailService = mailService;
	}


   
    
    
    @PostMapping("/signup")
    public String register(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password) {

    	
    	
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);


        user.setCreatedAt(LocalDateTime.now());

        user.setUpdatedAt(LocalDateTime.now());


        userRepository.save(user);
        String verificationUrl =
                baseUrl + "/verify?token=" + verificationToken;

        mailService.sendVerificationMail(email, verificationUrl);


        return "redirect:/login";
    }
        @GetMapping("/verify")
        public String verifyEmail(@RequestParam("token") String token) {

            User user = userRepository.findByVerificationToken(token)
                    .orElseThrow();

            user.setEmailVerifiedAt(LocalDateTime.now());
            user.setVerificationToken(null);
            user.setUpdatedAt(LocalDateTime.now());

            userRepository.save(user);

            return "redirect:/login?verified";
        }
    }
