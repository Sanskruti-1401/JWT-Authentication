# JWT-Authentication
# Spring Boot Validation Project with JWT Authentication

A Spring Boot application demonstrating JWT (JSON Web Token) based authentication and authorization with customer management functionality.

## Project Overview

This project implements a secure REST API with the following features:
- **JWT-based Authentication**: Token-based authentication mechanism
- **Spring Security**: Integrated security framework
- **Customer Management**: CRUD operations for customer data
- **Input Validation**: Request validation with Spring validation
- **Global Exception Handling**: Centralized error handling
- **Authorization**: Filter-based request validation

## Tech Stack

- **Java**: 21
- **Spring Boot**: 4.0.6
- **Spring Security**: Latest version
- **JWT (JJWT)**: 0.11.5
- **JPA/Hibernate**: Data persistence
- **Maven**: Build tool

## Project Structure

```
src/
├── main/
│   ├── java/com/example/demo/
│   │   ├── DemoApplication.java              # Main Spring Boot application
│   │   ├── controller/
│   │   │   ├── AuthController.java          # Authentication endpoints
│   │   │   └── CustomerController.java      # Customer management endpoints
│   │   ├── model/
│   │   │   └── Customer.java                # Customer entity
│   │   ├── repository/
│   │   │   └── CustomerRepository.java      # JPA repository
│   │   ├── security/
│   │   │   ├── JwtUtil.java                 # JWT token generation and validation
│   │   │   ├── JwtFilter.java               # Request filter for JWT validation
│   │   │   ├── SecurityConfig.java          # Spring Security configuration
│   │   │   └── AuthRequest.java             # Login request DTO
│   │   └── exeption/
│   │       ├── ErrorDetails.java            # Error response structure
│   │       ├── GlobalExceptionHandler.java  # Global exception handler
│   │       └── ResourceNotFoundException.java # Custom exception
│   └── resources/
│       └── application.properties            # Application configuration
└── test/
    └── java/com/example/demo/
        └── DemoApplicationTests.java         # Unit tests
```

## JWT Implementation Details

### 1. **Token Generation** (`JwtUtil.java`)
The `JwtUtil` component handles JWT token creation and validation:

```java
// Token generation
String token = jwtUtil.generateToken(username);
```

**Features:**
- **Secret Key**: HMAC SHA256 algorithm with a secret key
- **Token Expiration**: 1 hour (3600 seconds) from generation
- **Payload**: Contains the username as the subject

**Key Method:**
```java
public String generateToken(String username)
```
- Builds a JWT with username as subject
- Sets issue date and expiration (1 hour)
- Signs with HS256 algorithm
- Returns compact token string

### 2. **Token Validation** (`JwtFilter.java`)
The `JwtFilter` is a custom Spring Security filter that intercepts every HTTP request:

**Filter Flow:**
1. Extracts the `Authorization` header from the request
2. Validates the "Bearer" token format (e.g., `Bearer <token>`)
3. Extracts the username from the token
4. Validates token authenticity and expiration
5. Sets the authenticated user in Spring Security context if valid
6. Continues the request chain with proper authorization

**Headers Format:**
```
Authorization: Bearer <token>
```

### 3. **Security Configuration** (`SecurityConfig.java`)
Configures Spring Security with custom JWT filter:
- Disables CSRF protection (suitable for stateless JWT-based APIs)
- Registers the JWT filter before Spring's standard authentication filter
- Configures public endpoints (login)
- Secures all other endpoints

### 4. **Authentication** (`AuthController.java`)
Provides the login endpoint:

**Endpoint:** `POST /api/auth/login`

**Request Body:**
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Response:** JWT Token
```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY4MTIzNDU2MCwiZXhwIjoxNjgxMjM4MTYwfQ...
```

**Default Credentials:**
- Username: `admin`
- Password: `admin123`

## Authentication Flow

```
1. Client sends login request with credentials
   POST /api/auth/login
   {
     "username": "admin",
     "password": "admin123"
   }

2. AuthController validates credentials
   ↓
3. JwtUtil generates JWT token
   ↓
4. Token returned to client
   ↓
5. Client includes token in subsequent requests
   Authorization: Bearer <token>
   ↓
6. JwtFilter intercepts request
   ↓
7. Token is validated and user is authenticated
   ↓
8. Request proceeds to controller
```

## API Endpoints

### Authentication
- **POST** `/api/auth/login` - Login and get JWT token

### Customers (Protected - Requires JWT)
- **GET** `/api/customers` - Get all customers
- **GET** `/api/customers/{id}` - Get customer by ID
- **POST** `/api/customers` - Create new customer
- **PUT** `/api/customers/{id}` - Update customer
- **DELETE** `/api/customers/{id}` - Delete customer

## Request Examples

### 1. Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

### 2. Access Protected Resource
```bash
curl -X GET http://localhost:8080/api/customers \
  -H "Authorization: Bearer <token_from_login>"
```

## Exception Handling

The application includes global exception handling via `GlobalExceptionHandler`:

- **ResourceNotFoundException**: Thrown when a customer is not found
- **ValidationException**: Thrown for invalid input data
- **Custom Error Response**: Returns structured error details with:
  - Status code
  - Error message
  - Timestamp
  - Details

## Security Features

1. **JWT Token Security**:
   - HMAC SHA256 signature verification
   - Token expiration validation
   - Subject extraction and validation

2. **Request Filtering**:
   - Every request is intercepted by JwtFilter
   - Invalid tokens are rejected
   - Expired tokens are rejected

3. **Spring Security Integration**:
   - UsernamePasswordAuthenticationToken for authenticated user
   - SecurityContext stores authentication details
   - Stateless authentication (no session storage)

4. **CSRF Protection**: Disabled for stateless API (recommended for JWT-based APIs)

## Building and Running

### Prerequisites
- Java 21 or higher
- Maven 3.6+

### Build
```bash
mvn clean build
```

### Run
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Test
```bash
mvn test
```

## Configuration

Edit `src/main/resources/application.properties` to customize:
- Server port
- Database connection
- Logging level

## Key Components Explained

### JwtUtil Component
- **Role**: JWT token lifecycle management
- **Methods**:
  - `generateToken(username)`: Creates new JWT
  - `extractUsername(token)`: Extracts username from token
  - `validateToken(token)`: Checks token validity and expiration

### JwtFilter Component
- **Role**: Request-level authentication
- **Extends**: `OncePerRequestFilter` (executed once per request)
- **Functionality**: Validates JWT in Authorization header and sets authentication context

### SecurityConfig Component
- **Role**: Spring Security configuration
- **Responsibilities**: 
  - Security filter chain setup
  - CSRF handling
  - Custom filter registration
  - Public/protected endpoint configuration

## Notes for Developers

1. **Change Secret Key**: Update the `SECRET` in `JwtUtil.java` for production
2. **Token Expiration**: Modify the `setExpiration()` call to change token lifetime
3. **Custom Claims**: Extend `generateToken()` to add custom claims to JWT
4. **Database**: Configure database credentials in `application.properties`
5. **Error Handling**: Extend `GlobalExceptionHandler` for additional exception types

## Future Enhancements

- [ ] Refresh token implementation
- [ ] Role-based access control (RBAC)
- [ ] OAuth2 integration
- [ ] Two-factor authentication
- [ ] Token blacklisting/revocation
- [ ] Rate limiting
- [ ] API documentation (Swagger/OpenAPI)

## License

MIT License - See LICENSE file for details

## Author

Created for JWT authentication learning and demonstration purposes.

---

For questions or improvements, please create an issue or submit a pull request.
