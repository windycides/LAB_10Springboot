package com.example.lab10.controller;

import com.example.lab10.dto.UserRegistrationDto; // Import your DTO
import com.example.lab10.model.User;
import com.example.lab10.service.UserService;
import jakarta.validation.Valid; // Import for validation
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

    // --- New Endpoint for Lab 10 Part 2 ---

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            // Manually reading a header as requested in the lab [cite: 117, 130]
            @RequestHeader(value = "User-Agent", required = false) String userAgent,

            // validating the input body using the DTO [cite: 134, 119]
            @Valid @RequestBody UserRegistrationDto userDto
    ) {
        // Log the header to verify we read it
        System.out.println("User registering from device: " + userAgent);

        // In a real app, you would save the user here:
        // userService.saveNewUser(userDto);

        // Return a successful 200 OK response [cite: 123, 138]
        return ResponseEntity.ok("User registered successfully!");
    }
}