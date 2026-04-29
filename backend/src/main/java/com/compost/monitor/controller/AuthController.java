package com.compost.monitor.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.compost.monitor.model.User;
import com.compost.monitor.service.AuthService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> payload) {
        String email = (String) payload.get("email");
        String password = (String) payload.get("password");
        String role = (String) payload.getOrDefault("role", "USER");

        if (email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email and password are required."));
        }

        User user = authService.register(email, password, role);
        if (user == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "User already exists."));
        }
        return ResponseEntity.ok(Map.of("message", "Registration successful"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> payload) {
        String email = (String) payload.get("email");
        String password = (String) payload.get("password");

        if (email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email and password are required."));
        }

        String token = authService.login(email, password);
        if (token == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials."));
        }
        return ResponseEntity.ok(Map.of(
                "token", token,
                "user", Map.of("email", email)));
    }
}
