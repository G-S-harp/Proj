package com.moneytracker.service;

import com.moneytracker.entity.Person;
import com.moneytracker.entity.User;
import com.moneytracker.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PersonService {
    
    @Autowired
    private PersonRepository personRepository;
    
    // Add new person for user
    public Person addPerson(String name, User user) {
        // Check if person already exists for this user
        if (personRepository.existsByNameAndUser(name, user)) {
            throw new RuntimeException("Person with this name already exists");
        }
        
        Person person = new Person(name, user);
        user.addPerson(person); // Using OOP method from User class
        
        return personRepository.save(person);
    }
    
    // Get all people for user
    public List<Person> getAllPeopleForUser(User user) {
        return personRepository.findByUserOrderByNameAsc(user);
    }
    
    // Find person by name and user
    public Optional<Person> findByNameAndUser(String name, User user) {
        return personRepository.findByNameAndUser(name, user);
    }
    
    // Delete person by name and user
    public void deletePerson(String name, User user) {
        Optional<Person> personOpt = personRepository.findByNameAndUser(name, user);
        
        if (personOpt.isPresent()) {
            Person person = personOpt.get();
            user.removePerson(person); // Using OOP method from User class
            personRepository.delete(person);
        } else {
            throw new RuntimeException("Person not found");
        }
    }
    
    // Check if person exists for user
    public boolean personExists(String name, User user) {
        return personRepository.existsByNameAndUser(name, user);
    }
}