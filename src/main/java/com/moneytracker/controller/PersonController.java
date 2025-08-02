package com.moneytracker.controller;

import com.moneytracker.entity.Person;
import com.moneytracker.entity.User;
import com.moneytracker.service.PersonService;
import com.moneytracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/people")
@CrossOrigin(origins = "*")
public class PersonController {
    
    @Autowired
    private PersonService personService;
    
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
    
    // Get all people for current user
    @GetMapping("/all")
    public ResponseEntity<?> getAllPeople(Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            List<Person> people = personService.getAllPeopleForUser(user);
            return ResponseEntity.ok(people);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting people: " + e.getMessage());
        }
    }
    
    // Add new person
    @PostMapping("/add")
    public ResponseEntity<?> addPerson(@RequestParam String name, Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            Person person = personService.addPerson(name.trim(), user);
            return ResponseEntity.ok(person);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding person: " + e.getMessage());
        }
    }
    
    // Delete person by name
    @DeleteMapping("/{name}")
    public ResponseEntity<?> deletePerson(@PathVariable String name, Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            personService.deletePerson(name, user);
            return ResponseEntity.ok("Person deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting person: " + e.getMessage());
        }
    }
    
    @Autowired
    private com.moneytracker.service.TransactionService transactionService;
    
    // Send money to person
    @PostMapping("/send")
    public ResponseEntity<?> sendMoney(@RequestParam String name, 
                                       @RequestParam String amount,
                                       @RequestParam(required = false) String description,
                                       Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            java.math.BigDecimal amountValue = new java.math.BigDecimal(amount);
            com.moneytracker.entity.Transaction transaction = transactionService.sendMoney(name, amountValue, description, user);
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
            java.math.BigDecimal amountValue = new java.math.BigDecimal(amount);
            com.moneytracker.entity.Transaction transaction = transactionService.receiveMoney(name, amountValue, description, user);
            return ResponseEntity.ok(transaction);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error receiving money: " + e.getMessage());
        }
    }
}