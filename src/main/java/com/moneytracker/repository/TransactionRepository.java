package com.moneytracker.repository;

import com.moneytracker.entity.Person;
import com.moneytracker.entity.Transaction;
import com.moneytracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    // Find all transactions for a specific user, ordered by date descending
    List<Transaction> findByUserOrderByDateDesc(User user);
    
    // Find transactions for a specific person
    List<Transaction> findByPersonOrderByDateDesc(Person person);
    
    // Find transactions for a specific user and person
    List<Transaction> findByUserAndPersonOrderByDateDesc(User user, Person person);
}