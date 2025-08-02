package com.moneytracker.service;

import com.moneytracker.entity.User;
import com.moneytracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // Register new user
    public User registerUser(String username, String email, String password) {
        // Check if username already exists
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        
        // Check if email already exists (if provided)
        if (email != null && !email.trim().isEmpty() && userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
        
        // Create new user with encrypted password
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        
        return userRepository.save(user);
    }
    
    // Authenticate user
    public User authenticateUser(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        
        throw new RuntimeException("Invalid username or password");
    }
    
    // Find user by username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    // Find user by ID
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    // Check if username exists
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }
    
    // Check if email exists
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}