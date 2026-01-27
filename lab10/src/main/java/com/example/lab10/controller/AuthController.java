package com.example.lab10.controller;

import com.example.lab10.model.RefreshToken;
import com.example.lab10.model.User;
import com.example.lab10.security.JwtUtil;
import com.example.lab10.service.RefreshTokenService;
import com.example.lab10.service.UserService;
import com.example.lab10.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth") // Base URL is /auth
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil, RefreshTokenService refreshTokenService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
        this.userRepository = userRepository;
    }
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            logger.info("Login successful for user: {}", username);

        } catch (Exception e) {
            logger.warn("Login failed for user: {}", username);
            throw e;
        }

        UserDetails userDetails = userService.loadUserByUsername(username);
        String accessToken = jwtUtil.generateToken(userDetails.getUsername());
        User user = userRepository.findByUsername(username).orElseThrow();

        refreshTokenService.deleteByUserId(user.getId());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken.getToken()
        );
    }

    @PostMapping("/refresh")
    public Map<String, String> refreshToken(@RequestBody Map<String, String> request) {
        String requestRefreshToken = request.get("refreshToken");

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    refreshTokenService.deleteByUserId(user.getId());

                    String newAccessToken = jwtUtil.generateToken(user.getUsername());
                    RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getUsername());

                    return Map.of(
                            "accessToken", newAccessToken,
                            "refreshToken", newRefreshToken.getToken()
                    );
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Refresh token is missing"));
        }

        if (refreshTokenService.findByToken(refreshToken).isEmpty()) {
            return ResponseEntity.status(400).body(Map.of("error", "Session already expired or invalid token"));
        }

        refreshTokenService.findByToken(refreshToken)
                .ifPresent(token -> refreshTokenService.deleteByUserId(token.getUser().getId()));

        logger.info("User logged out successfully");
        return ResponseEntity.ok(Map.of("message", "Log out successful"));
    }
}