# Money Tracker Backend

A simple Spring Boot backend for the Money Tracker application with Object-Oriented Programming principles.

## Features

- **User Authentication**: JWT-based authentication with login and registration
- **Person Management**: Add, view, and delete people/contacts
- **Transaction Management**: Send/receive money to/from people with balance tracking
- **Balance Calculation**: Automatic balance calculation and updates
- **RESTful APIs**: Clean REST endpoints for all functionality

## Technology Stack

- **Java 17**: Core programming language
- **Spring Boot 3.2.0**: Main framework
- **Spring Security**: Authentication and authorization
- **Spring Data JPA**: Database operations
- **H2 Database**: In-memory database for development
- **JWT**: Token-based authentication
- **Maven**: Build and dependency management

## OOP Concepts Implemented

1. **Encapsulation**: Private fields with public getters/setters in entity classes
2. **Inheritance**: Base entity patterns and service layer hierarchy
3. **Polymorphism**: Interface implementations and method overriding
4. **Abstraction**: Service layer abstracts business logic from controllers
5. **Association**: Relationships between User, Person, and Transaction entities
6. **Composition**: User contains collections of People and Transactions

## Project Structure

```
src/main/java/com/moneytracker/
├── MoneyTrackerApplication.java     # Main application class
├── config/
│   ├── SecurityConfig.java          # Security configuration
│   ├── WebConfig.java              # Web/CORS configuration
│   └── DataInitializer.java        # Sample data setup
├── controller/
│   ├── AuthController.java         # Authentication endpoints
│   ├── PersonController.java       # Person management endpoints
│   └── TransactionController.java  # Transaction endpoints
├── dto/
│   ├── LoginRequest.java           # Login request DTO
│   └── RegisterRequest.java        # Registration request DTO
├── entity/
│   ├── User.java                   # User entity
│   ├── Person.java                 # Person entity
│   ├── Transaction.java            # Transaction entity
│   └── TransactionType.java        # Transaction type enum
├── filter/
│   └── JwtAuthenticationFilter.java # JWT token filter
├── repository/
│   ├── UserRepository.java         # User data access
│   ├── PersonRepository.java       # Person data access
│   └── TransactionRepository.java  # Transaction data access
├── service/
│   ├── UserService.java            # User business logic
│   ├── PersonService.java          # Person business logic
│   └── TransactionService.java     # Transaction business logic
└── util/
    └── JwtUtil.java                # JWT utility methods
```

## Setup and Installation

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Installation Steps

1. **Clone or create the project**:
   ```bash
   mkdir money-tracker-backend
   cd money-tracker-backend
   ```

2. **Build the project**:
   ```bash
   mvn clean install
   ```

3. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

The backend will start on `http://localhost:8080/api`

## API Endpoints

### Authentication

- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `GET /api/auth/check-username?username=test` - Check username availability

### People Management

- `GET /api/people/all` - Get all people for authenticated user
- `POST /api/people/add?name=PersonName` - Add new person
- `DELETE /api/people/{name}` - Delete person by name
- `POST /api/people/send?name=PersonName&amount=100&description=desc` - Send money
- `POST /api/people/receive?name=PersonName&amount=100&description=desc` - Receive money

### Transactions

- `GET /api/transactions/all` - Get all transactions for authenticated user
- `POST /api/transactions/send?name=PersonName&amount=100&description=desc` - Send money
- `POST /api/transactions/receive?name=PersonName&amount=100&description=desc` - Receive money
- `DELETE /api/transactions/{id}/reverse` - Reverse/delete transaction

## Authentication

All endpoints except `/auth/**` require a JWT token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

## Sample Data

The application creates two test users on startup:

- **Username**: admin, **Password**: admin
- **Username**: test, **Password**: test

## Database Access

The H2 database console is available at: `http://localhost:8080/api/h2-console`

- **JDBC URL**: `jdbc:h2:mem:moneytracker`
- **Username**: `sa`
- **Password**: (leave empty)

## Configuration

Key configuration in `application.properties`:

```properties
# Server Configuration
server.port=8080
server.servlet.context-path=/api

# Database Configuration
spring.datasource.url=jdbc:h2:mem:moneytracker
spring.jpa.hibernate.ddl-auto=create-drop

# JWT Configuration
jwt.secret=mySecretKey123456789012345678901234567890
jwt.expiration=86400000  # 24 hours

# CORS Configuration
cors.allowed-origins=http://localhost:3000,http://127.0.0.1:5500,http://localhost:5500
```

## Frontend Integration

To connect your frontend:

1. Update the `BASE_URL` in your frontend JavaScript:
   ```javascript
   const BASE_URL = "http://localhost:8080/api";
   ```

2. For registration functionality, add this to your `register.html`:
   ```html
   <script src="registration-script.js"></script>
   ```

## Error Handling

The API returns appropriate HTTP status codes and error messages:

- `200 OK`: Success
- `400 Bad Request`: Invalid request data
- `401 Unauthorized`: Invalid or missing token
- `403 Forbidden`: Access denied
- `500 Internal Server Error`: Server error

## Business Logic

### Balance Calculation

- When you **send money** to a person: Their balance increases (they owe you more)
- When you **receive money** from a person: Their balance decreases (they owe you less)
- Positive balance = Person owes you money
- Negative balance = You owe the person money

### Transaction Types

- `SEND`: You sent money to someone
- `RECEIVE`: You received money from someone

## Development

### Adding New Features

1. Create entity classes in `entity/` package
2. Add repository interfaces in `repository/` package
3. Implement business logic in `service/` package
4. Create REST controllers in `controller/` package
5. Add DTOs in `dto/` package if needed

### Testing

Use tools like Postman or curl to test the API endpoints:

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"test"}'

# Add person (requires token)
curl -X POST "http://localhost:8080/api/people/add?name=John" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

## Troubleshooting

1. **Port already in use**: Change the port in `application.properties`
2. **Database errors**: Check H2 console for data issues
3. **JWT errors**: Verify token format and expiration
4. **CORS errors**: Update allowed origins in configuration

## Contributing

1. Follow Java naming conventions
2. Add proper error handling
3. Write meaningful commit messages
4. Test all endpoints before submitting

## License

This project is created for educational purposes.