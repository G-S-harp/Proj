package com.moneytracker.repository;

import com.moneytracker.entity.Person;
import com.moneytracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    
    // Find all people for a specific user
    List<Person> findByUserOrderByNameAsc(User user);
    
    // Find person by name and user
    Optional<Person> findByNameAndUser(String name, User user);
    
    // Check if person exists for user
    boolean existsByNameAndUser(String name, User user);
    
    // Delete person by name and user
    void deleteByNameAndUser(String name, User user);
}