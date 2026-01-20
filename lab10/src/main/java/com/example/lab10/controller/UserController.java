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

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello! The application is working.";
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @RequestHeader(value = "User-Agent", required = false) String userAgent,
            @Valid @RequestBody UserRegistrationDto userDto
    ) {

        System.out.println("User registering from device: " + userAgent);

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());


        userService.registerUser(user);
//return ResponseEntity.ok(Map.of("message", "User registered successfully!"));
        return ResponseEntity.ok("User registered successfully!");
    }
}