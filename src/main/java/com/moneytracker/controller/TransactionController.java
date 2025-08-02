package com.moneytracker.controller;

import com.moneytracker.entity.Transaction;
import com.moneytracker.entity.User;
import com.moneytracker.service.TransactionService;
import com.moneytracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {
    
    @Autowired
    private TransactionService transactionService;
    
    @Autowired
    private UserService userService;
    
    // Get current user from authentication
    private User getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return userOpt.get();
    }
    
    // Get all transactions for current user
    @GetMapping("/all")
    public ResponseEntity<?> getAllTransactions(Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            List<Transaction> transactions = transactionService.getAllTransactionsForUser(user);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting transactions: " + e.getMessage());
        }
    }
    
    // Send money to person
    @PostMapping("/send")
    public ResponseEntity<?> sendMoney(@RequestParam String name, 
                                       @RequestParam String amount,
                                       @RequestParam(required = false) String description,
                                       Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            BigDecimal amountValue = new BigDecimal(amount);
            Transaction transaction = transactionService.sendMoney(name, amountValue, description, user);
            return ResponseEntity.ok(transaction);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error sending money: " + e.getMessage());
        }
    }
    
    // Receive money from person
    @PostMapping("/receive")
    public ResponseEntity<?> receiveMoney(@RequestParam String name, 
                                          @RequestParam String amount,
                                          @RequestParam(required = false) String description,
                                          Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            BigDecimal amountValue = new BigDecimal(amount);
            Transaction transaction = transactionService.receiveMoney(name, amountValue, description, user);
            return ResponseEntity.ok(transaction);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error receiving money: " + e.getMessage());
        }
    }
    
    // Reverse/Delete transaction
    @DeleteMapping("/{id}/reverse")
    public ResponseEntity<?> reverseTransaction(@PathVariable Long id, Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            transactionService.reverseTransaction(id, user);
            return ResponseEntity.ok("Transaction reversed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error reversing transaction: " + e.getMessage());
        }
    }
}