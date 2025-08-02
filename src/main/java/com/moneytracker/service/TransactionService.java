package com.moneytracker.service;

import com.moneytracker.entity.Person;
import com.moneytracker.entity.Transaction;
import com.moneytracker.entity.TransactionType;
import com.moneytracker.entity.User;
import com.moneytracker.repository.PersonRepository;
import com.moneytracker.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TransactionService {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private PersonRepository personRepository;
    
    // Send money to person
    public Transaction sendMoney(String personName, BigDecimal amount, String description, User user) {
        Optional<Person> personOpt = personRepository.findByNameAndUser(personName, user);
        
        if (personOpt.isEmpty()) {
            throw new RuntimeException("Person not found");
        }
        
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be positive");
        }
        
        Person person = personOpt.get();
        Transaction transaction = new Transaction(amount, description, TransactionType.SEND, user, person);
        
        // Using OOP methods to maintain relationships and update balances
        user.addTransaction(transaction);
        person.addTransaction(transaction);
        
        // Save person first to update balance, then transaction
        personRepository.save(person);
        return transactionRepository.save(transaction);
    }
    
    // Receive money from person
    public Transaction receiveMoney(String personName, BigDecimal amount, String description, User user) {
        Optional<Person> personOpt = personRepository.findByNameAndUser(personName, user);
        
        if (personOpt.isEmpty()) {
            throw new RuntimeException("Person not found");
        }
        
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be positive");
        }
        
        Person person = personOpt.get();
        Transaction transaction = new Transaction(amount, description, TransactionType.RECEIVE, user, person);
        
        // Using OOP methods to maintain relationships and update balances
        user.addTransaction(transaction);
        person.addTransaction(transaction);
        
        // Save person first to update balance, then transaction
        personRepository.save(person);
        return transactionRepository.save(transaction);
    }
    
    // Get all transactions for user
    public List<Transaction> getAllTransactionsForUser(User user) {
        return transactionRepository.findByUserOrderByDateDesc(user);
    }
    
    // Get transactions for specific person
    public List<Transaction> getTransactionsForPerson(Person person) {
        return transactionRepository.findByPersonOrderByDateDesc(person);
    }
    
    // Reverse/Delete transaction
    public void reverseTransaction(Long transactionId, User user) {
        Optional<Transaction> transactionOpt = transactionRepository.findById(transactionId);
        
        if (transactionOpt.isEmpty()) {
            throw new RuntimeException("Transaction not found");
        }
        
        Transaction transaction = transactionOpt.get();
        
        // Check if transaction belongs to this user
        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to reverse this transaction");
        }
        
        Person person = transaction.getPerson();
        
        // Using OOP methods to maintain relationships and update balances
        person.removeTransaction(transaction);
        
        // Save person to update balance, then delete transaction
        personRepository.save(person);
        transactionRepository.delete(transaction);
    }
}