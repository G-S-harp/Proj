package com.moneytracker.controller;

import com.moneytracker.entity.User;
import com.moneytracker.service.UserService;
import com.moneytracker.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    // User login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            String username = credentials.get("username");
            String password = credentials.get("password");
            
            if (username == null || password == null) {
                return ResponseEntity.badRequest().body("Username and password are required");
            }
            
            // Authenticate user
            User user = userService.authenticateUser(username, password);
            
            // Generate JWT token
            String token = jwtUtil.generateToken(username);
            
            // Return response with token
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("username", username);
            response.put("message", "Login successful");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Login failed: " + e.getMessage());
        }
    }
    
    // User registration
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> userDetails) {
        try {
            String username = userDetails.get("username");
            String email = userDetails.get("email");
            String password = userDetails.get("password");
            
            if (username == null || password == null) {
                return ResponseEntity.badRequest().body("Username and password are required");
            }
            
            // Register new user
            User user = userService.registerUser(username, email, password);
            
            // Generate JWT token for immediate login
            String token = jwtUtil.generateToken(username);
            
            // Return response with token
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("username", username);
            response.put("message", "Registration successful");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }
    
    // Check if username exists
    @GetMapping("/check-username")
    public ResponseEntity<?> checkUsername(@RequestParam String username) {
        try {
            boolean exists = userService.usernameExists(username);
            Map<String, Object> response = new HashMap<>();
            response.put("exists", exists);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error checking username: " + e.getMessage());
        }
    }
}