package com.moneytracker.config;

import com.moneytracker.entity.User;
import com.moneytracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserService userService;
    
    @Override
    public void run(String... args) throws Exception {
        // Create a default test user if no users exist
        if (!userService.usernameExists("admin")) {
            userService.registerUser("admin", "admin@test.com", "admin");
            System.out.println("Created default user: admin/admin");
        }
        
        if (!userService.usernameExists("test")) {
            userService.registerUser("test", "test@test.com", "test");
            System.out.println("Created test user: test/test");
        }
    }
}