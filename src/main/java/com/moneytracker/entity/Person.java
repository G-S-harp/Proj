package com.moneytracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "people")
public class Person {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
    
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Transaction> transactions = new ArrayList<>();
    
    // Default constructor
    public Person() {}
    
    // Constructor with required fields
    public Person(String name, User user) {
        this.name = name;
        this.user = user;
        this.balance = BigDecimal.ZERO;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public BigDecimal getBalance() {
        return balance;
    }
    
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public List<Transaction> getTransactions() {
        return transactions;
    }
    
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
    
    // Business methods (OOP principles)
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setPerson(this);
        updateBalance(transaction);
    }
    
    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
        transaction.setPerson(null);
        reverseBalance(transaction);
    }
    
    private void updateBalance(Transaction transaction) {
        if (transaction.getType() == TransactionType.SEND) {
            // When user sends money to this person, person's balance increases (they owe us)
            this.balance = this.balance.add(transaction.getAmount());
        } else if (transaction.getType() == TransactionType.RECEIVE) {
            // When user receives money from this person, person's balance decreases (they owe us less)
            this.balance = this.balance.subtract(transaction.getAmount());
        }
    }
    
    private void reverseBalance(Transaction transaction) {
        if (transaction.getType() == TransactionType.SEND) {
            this.balance = this.balance.subtract(transaction.getAmount());
        } else if (transaction.getType() == TransactionType.RECEIVE) {
            this.balance = this.balance.add(transaction.getAmount());
        }
    }
    
    public void recalculateBalance() {
        this.balance = BigDecimal.ZERO;
        for (Transaction transaction : transactions) {
            updateBalance(transaction);
        }
    }
    
    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                '}';
    }
}