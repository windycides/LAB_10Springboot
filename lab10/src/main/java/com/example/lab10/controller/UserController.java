package com.example.lab10.controller;

import com.example.lab10.dto.UserRegistrationDto;
import com.example.lab10.model.User;
import com.example.lab10.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // --- Existing Endpoints ---

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello! The application is working.";
    }

    // --- New Endpoint for Lab 10 Part 2 & Lab 11 ---

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @RequestHeader(value = "User-Agent", required = false) String userAgent,
            @Valid @RequestBody UserRegistrationDto userDto
    ) {
        // 1. Log the header
        System.out.println("User registering from device: " + userAgent);

        // 2. Map DTO to User Entity
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword()); // Service will encode this password!

        // 3. Save to DB using the Service
        userService.registerUser(user);

        return ResponseEntity.ok("User registered successfully!");
    }
}