package com.moneytracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    @Column(length = 500)
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;
    
    @Column(nullable = false)
    private LocalDateTime date;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;
    
    // Default constructor
    public Transaction() {
        this.date = LocalDateTime.now();
    }
    
    // Constructor with required fields
    public Transaction(BigDecimal amount, TransactionType type, User user, Person person) {
        this.amount = amount;
        this.type = type;
        this.user = user;
        this.person = person;
        this.date = LocalDateTime.now();
    }
    
    // Constructor with all fields
    public Transaction(BigDecimal amount, String description, TransactionType type, User user, Person person) {
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.user = user;
        this.person = person;
        this.date = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public TransactionType getType() {
        return type;
    }
    
    public void setType(TransactionType type) {
        this.type = type;
    }
    
    public LocalDateTime getDate() {
        return date;
    }
    
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Person getPerson() {
        return person;
    }
    
    public void setPerson(Person person) {
        this.person = person;
    }
    
    // Business method to get formatted date for frontend
    public String getFormattedDate() {
        return date.toString();
    }
    
    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", date=" + date +
                ", person=" + (person != null ? person.getName() : null) +
                '}';
    }
}