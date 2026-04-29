package com.compost.monitor.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.compost.monitor.model.User;
import com.compost.monitor.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepo;

    public User register(String email, String password, String role) {
        if (userRepo.findByEmail(email) != null) {
            return null;
        }
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(hashPassword(password));
        user.setRole(role == null ? "USER" : role);
        return userRepo.save(user);
    }

    public String login(String email, String password) {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            return null;
        }
        if (!user.getPasswordHash().equals(hashPassword(password))) {
            return null;
        }
        return createToken(user);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Unable to hash password", e);
        }
    }

    private String createToken(User user) {
        long expiresAt = LocalDateTime.now().plusHours(6).toEpochSecond(ZoneOffset.UTC);
        String payload = String.format("%s|%s|%d", user.getEmail(), user.getRole(), expiresAt);
        return Base64.getEncoder().encodeToString(payload.getBytes(StandardCharsets.UTF_8));
    }
}
