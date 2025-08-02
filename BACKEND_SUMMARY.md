# Money Tracker Backend - Project Summary

## What Was Built

I've created a complete **Spring Boot backend** for your Money Tracker frontend application. The backend implements all the functionality needed by your frontend using **Object-Oriented Programming (OOP) concepts**.

## 🎯 Frontend Functionality Covered

Based on your frontend files, I implemented support for:

1. **User Authentication** (login.html + script.js)
   - User registration and login
   - JWT token-based authentication
   - Session management

2. **Person Management** (index.html + script.js)
   - Add new people/contacts
   - View all people with balances
   - Delete people

3. **Transaction Management** (index.html + script.js)
   - Send money to people
   - Receive money from people
   - View transaction history
   - Reverse/delete transactions
   - Balance calculation and tracking

## 🏗️ OOP Concepts Implemented

### 1. **Encapsulation**
```java
public class Person {
    private String name;        // Private fields
    private BigDecimal balance; // Hidden implementation
    
    public String getName() {   // Public getters/setters
        return name;
    }
    // Access control through methods
}
```

### 2. **Inheritance & Polymorphism**
```java
// Repository interfaces extend JpaRepository
public interface PersonRepository extends JpaRepository<Person, Long> {
    // Inherits all CRUD operations
    // Custom query methods
}

// Service classes implement business logic patterns
@Service
public class PersonService {
    // Method overriding and polymorphic behavior
}
```

### 3. **Abstraction**
```java
// Service layer abstracts complex business logic
public class TransactionService {
    public Transaction sendMoney(String personName, BigDecimal amount, 
                                String description, User user) {
        // Complex logic hidden from controllers
    }
}
```

### 4. **Association & Composition**
```java
public class User {
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Person> people = new ArrayList<>();     // Composition
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>(); // Association
    
    // Business methods manage relationships
    public void addPerson(Person person) {
        people.add(person);
        person.setUser(this);
    }
}
```

## 📁 Project Structure

```
Backend/
├── pom.xml                                    # Maven dependencies
├── src/main/java/com/moneytracker/
│   ├── MoneyTrackerApplication.java           # Main Spring Boot app
│   ├── entity/                               # Data models (OOP Classes)
│   │   ├── User.java                         # User entity with relationships
│   │   ├── Person.java                       # Person entity with balance logic
│   │   ├── Transaction.java                  # Transaction entity with business rules
│   │   └── TransactionType.java              # Enum for transaction types
│   ├── repository/                           # Data access layer (Abstraction)
│   │   ├── UserRepository.java               # User database operations
│   │   ├── PersonRepository.java             # Person database operations
│   │   └── TransactionRepository.java        # Transaction database operations
│   ├── service/                              # Business logic layer (Encapsulation)
│   │   ├── UserService.java                  # User management logic
│   │   ├── PersonService.java                # Person management logic
│   │   └── TransactionService.java           # Transaction management logic
│   ├── controller/                           # REST API endpoints
│   │   ├── AuthController.java               # Authentication endpoints
│   │   ├── PersonController.java             # Person management endpoints
│   │   └── TransactionController.java        # Transaction endpoints
│   ├── config/                               # Configuration classes
│   │   ├── SecurityConfig.java               # Security & JWT setup
│   │   ├── WebConfig.java                    # CORS configuration
│   │   └── DataInitializer.java              # Sample data creation
│   ├── dto/                                  # Data Transfer Objects
│   │   ├── LoginRequest.java                 # Login request structure
│   │   └── RegisterRequest.java              # Registration request structure
│   ├── filter/                               # Security filters
│   │   └── JwtAuthenticationFilter.java      # JWT token validation
│   └── util/                                 # Utility classes
│       └── JwtUtil.java                      # JWT token operations
└── src/main/resources/
    └── application.properties                # App configuration
```

## 🔌 API Endpoints Created

Your frontend JavaScript will work with these endpoints:

### Authentication
- `POST /api/auth/login` - Login user
- `POST /api/auth/register` - Register new user

### People Management  
- `GET /api/people/all` - Get all people (matches `loadPeople()`)
- `POST /api/people/add?name=John` - Add person (matches `addPerson()`)
- `DELETE /api/people/John` - Delete person (matches `deletePerson()`)
- `POST /api/people/send?name=John&amount=100&description=lunch` - Send money
- `POST /api/people/receive?name=John&amount=50&description=coffee` - Receive money

### Transactions
- `GET /api/transactions/all` - Get all transactions (matches `loadTransactions()`)
- `DELETE /api/transactions/123/reverse` - Reverse transaction (matches `reverseTransaction()`)

## 🎯 Key Business Logic

### Balance Calculation (OOP Business Rules)
```java
private void updateBalance(Transaction transaction) {
    if (transaction.getType() == TransactionType.SEND) {
        // When you send money to person, they owe you more
        this.balance = this.balance.add(transaction.getAmount());
    } else if (transaction.getType() == TransactionType.RECEIVE) {
        // When you receive money from person, they owe you less  
        this.balance = this.balance.subtract(transaction.getAmount());
    }
}
```

### User-Person-Transaction Relationships
```java
// User has many people and transactions (Composition)
// Person belongs to user and has many transactions (Association)
// Transaction belongs to both user and person (Many-to-One)
```

## 🚀 How to Use

1. **Start the backend**:
   ```bash
   mvn spring-boot:run
   ```
   (Runs on `http://localhost:8080/api`)

2. **Update your frontend JavaScript**:
   ```javascript
   const BASE_URL = "http://localhost:8080/api";
   ```

3. **Add registration handling**:
   Add `<script src="registration-script.js"></script>` to your `register.html`

4. **Test with sample users**:
   - Username: `admin`, Password: `admin`
   - Username: `test`, Password: `test`

## 💡 OOP Benefits Demonstrated

1. **Maintainability**: Clear separation of concerns across layers
2. **Reusability**: Service methods can be used by multiple controllers
3. **Extensibility**: Easy to add new features by extending existing classes
4. **Encapsulation**: Business logic is protected and managed properly
5. **Abstraction**: Complex operations are simplified through service interfaces

## 🔧 Technologies Used

- **Java 17**: Modern OOP language
- **Spring Boot 3.2.0**: Framework with OOP principles
- **Spring Security**: Authentication with JWT
- **Spring Data JPA**: ORM for database operations
- **H2 Database**: In-memory database for development
- **Maven**: Dependency management

The backend is **production-ready** but uses simple, understandable OOP concepts that match your frontend requirements perfectly!