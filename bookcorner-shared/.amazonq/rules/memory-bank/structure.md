# BookCorner Nepal — Project Structure

## Repository Layout
Multi-module Maven project. All modules share a parent POM at the root.

```
bookcorner/
├── pom.xml                    # Parent POM — dependency management, plugin config
├── docker-compose.yml         # Local infra (MySQL, Redis)
├── DATABASE_DESIGN.md
├── bookcorner-api/            # Spring Boot entry point (main class, application.yml)
├── bookcorner-shared/         # Shared library (BaseEntity, DTOs, exceptions)
├── bookcorner-auth/           # Auth, JWT, Spring Security, User entity
├── bookcorner-category/       # Nested category tree
├── bookcorner-author/         # Author management
├── bookcorner-publisher/      # Publisher management
├── bookcorner-book/           # Book catalog (NEW/SECOND_HAND/RENTAL)
├── bookcorner-cart/           # Shopping cart
├── bookcorner-wishlist/       # Wishlist
├── bookcorner-order/          # Order lifecycle
├── bookcorner-address/        # User addresses
├── bookcorner-payment/        # Payment processing (eSewa/Khalti/COD)
└── bookcorner-review/         # Book reviews and ratings
```

## Standard Module Layout
Every domain module follows the same internal package structure:

```
com.bookcorner.<domain>/
├── controller/     # REST controllers (@RestController)
├── dto/            # Request/Response DTOs (records or Lombok classes)
├── entity/         # JPA entities extending BaseEntity
├── enums/          # Domain enums
├── exception/      # Domain-specific exceptions
├── mapper/         # MapStruct mappers (interface)
├── repository/     # Spring Data JPA repositories
└── service/
    ├── <Domain>Service.java          # Interface
    └── serviceimpl/
        └── <Domain>ServiceImpl.java  # Implementation
```

## bookcorner-shared Contents
```
com.bookcorner.shared/
├── entity/BaseEntity.java          # id, createdAt, updatedAt with JPA auditing
├── dto/
│   ├── ErrorResponse.java          # Standard error envelope
│   ├── PageResponse.java           # Generic paginated response wrapper
│   └── PaginationRequest.java      # Page/size/sort request params
└── exception/
    └── ResourceNotFoundException.java
```

## bookcorner-auth Contents
```
com.bookcorner.auth/
├── config/         # JwtProperties, OtpProperties, RedisConfig, SecurityConfig
├── controller/     # AuthController, TestController
├── dto/            # AuthResponse, LoginRequest, RegisterRequest, OTP DTOs
├── entity/         # User, RefreshToken
├── enums/          # Role, UserStatus, OtpPurpose
├── exception/      # GlobalExceptionHandler + domain exceptions
├── repository/     # UserRepository, RefreshTokenRepository
├── security/       # AuthenticationService, JwtAuthenticationFilter, CustomUserDetailsService
└── service/
    ├── JwtService.java
    ├── AuthService.java
    ├── SmsService.java (interface)
    └── serviceimpl/
        ├── OtpService.java
        ├── RefreshTokenService.java
        └── MockSmsServiceImpl.java
```

## Module Dependency Graph
```
bookcorner-api
  └── depends on all modules

bookcorner-payment
  └── bookcorner-auth
  └── bookcorner-order

bookcorner-order
  └── bookcorner-auth
  └── bookcorner-book
  └── bookcorner-cart

bookcorner-book
  └── bookcorner-auth
  └── bookcorner-category
  └── bookcorner-author
  └── bookcorner-publisher

bookcorner-cart / wishlist / review / address
  └── bookcorner-auth

bookcorner-auth
  └── bookcorner-shared

bookcorner-shared  (no internal deps)
```

## Architectural Patterns
- Monolithic Spring Boot app assembled in bookcorner-api; modules are Maven JARs, not microservices
- Layered architecture: Controller → Service Interface → ServiceImpl → Repository
- Cross-cutting auth resolved via AuthenticationService.getAuthenticatedUser() injected into any service
- GlobalExceptionHandler in bookcorner-auth handles all domain exceptions across the app
- MapStruct for all entity↔DTO mapping (no manual mapping code)
- Spring Data JPA with custom JPQL/derived queries in repositories
