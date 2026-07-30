# BookCorner Nepal — Development Guidelines

## Class-Level Annotations

### Service classes
```java
@Service
@RequiredArgsConstructor
public class FooServiceImpl implements FooService { ... }
```
- Always `@Service` + `@RequiredArgsConstructor` (constructor injection via Lombok)
- Implement a named interface (`FooService`) placed in the same package; impl goes in `serviceimpl/` sub-package
- Exception: simple single-class services (e.g. `AddressService`, `JwtService`) skip the interface

### Entities
```java
@Getter @Setter @NoArgsConstructor
@Entity @Table(name = "foo")
public class Foo extends BaseEntity { ... }
```
- Always extend `BaseEntity` (provides `id`, `createdAt`, `updatedAt` with JPA auditing)
- `@Getter` + `@Setter` + `@NoArgsConstructor` from Lombok — never `@Data` on entities
- `@Builder` added when builder pattern is needed (e.g. Payment entity)

### Controllers
```java
@RestController
@RequestMapping("/api/v1/foo")
@RequiredArgsConstructor
public class FooController { ... }
```

### MapStruct Mappers
```java
@Mapper(componentModel = "spring")
public interface FooMapper {
    FooResponse toFooResponse(Foo foo);
    Foo toFoo(FooRequest request);
}
```

## Dependency Injection
- Constructor injection exclusively via `@RequiredArgsConstructor` — never `@Autowired` field injection
- All fields declared `private final`

## Transaction Management
- `@Transactional` on every write method (create, update, delete)
- Read-only methods left without `@Transactional` unless they span multiple reads
- Applied at method level, not class level

## Authentication Pattern
Every service that needs the current user calls:
```java
var user = authenticationService.getAuthenticatedUser();
```
`AuthenticationService` is injected as a dependency — never access `SecurityContextHolder` directly in service layer.

## Repository Patterns

### Derived query methods
```java
Optional<Address> findByIdAndUser(Long id, User user);
List<Address> findByUserOrderByCreatedAtDesc(User user);
Optional<Address> findByUserAndDefaultAddressTrue(User user);
Page<Books> findByStatus(BookStatus status, Pageable pageable);
```

### Native SQL queries with projections
Used for complex joins returning flat read models:
```java
@Query(value = """
    SELECT b.id, b.title, b.price, b.cover_image_url AS coverImageUrl,
           a.author_name AS authorName, c.category_name AS categoryName
    FROM books b
    INNER JOIN authors a ON b.author_id = a.id
    INNER JOIN categories c ON b.category_id = c.id
    WHERE b.status = 'ACTIVE'
    """, nativeQuery = true)
Page<BookProjection> findAllActiveBooks(Pageable pageable);
```
- Projections are interfaces in a `projection/` package
- Column aliases in SQL must match projection getter names (camelCase via AS)
- Always provide a separate `countQuery` when using `nativeQuery = true` with `Page`

### Named parameters
```java
@Param("keyword") String keyword
```
Always use `@Param` with named parameters in `@Query`.

## Exception Handling

### Domain exceptions
Each module defines its own exceptions extending `RuntimeException`:
```java
public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String message) { super(message); }
}
```

### GlobalExceptionHandler
Single `@RestControllerAdvice` in `bookcorner-auth` handles all exceptions app-wide.
Each handler follows this exact pattern:
```java
@ExceptionHandler(FooException.class)
public ResponseEntity<ErrorResponse> handleFooException(FooException ex, WebRequest request) {
    ErrorResponse errorResponse = new ErrorResponse(
        LocalDateTime.now(),
        HttpStatus.XXX.value(),
        HttpStatus.XXX.getReasonPhrase(),
        ex.getMessage(),
        request.getDescription(false).replace("uri=", "")
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.XXX);
}
```
- `ErrorResponse` is from `bookcorner-shared`
- Map each exception to the semantically correct HTTP status

### orElseThrow pattern
```java
Order order = orderRepository
    .findByOrderNumberAndUser(user, request.getOrderNumber())
    .orElseThrow(() -> new OrderNotFoundException("Order not found: " + request.getOrderNumber()));
```
Always use `orElseThrow` with a domain-specific exception — never return null.

### ifPresent for conflict checks
```java
paymentRepository.findByOrder(order)
    .ifPresent(existing -> {
        throw new PaymentAlreadyExistsException("Payment already exists for order: " + order.getOrderNumber());
    });
```

## Service Method Structure (write operations)
Standard flow for transactional write methods:
1. Get authenticated user via `authenticationService.getAuthenticatedUser()`
2. Fetch required entities with `orElseThrow`
3. Check preconditions / conflicts with `ifPresent` throws
4. Build/mutate entity
5. Save via repository
6. Return mapped DTO via mapper

```java
@Override
@Transactional
public PaymentResponse processPayment(PaymentRequest request) {
    var user = authenticationService.getAuthenticatedUser();
    OrderEntity order = orderRepository
        .findByOrderNumberAndUser(user, request.getOrderNumber())
        .orElseThrow(() -> new OrderNotFoundException("Order not found: " + request.getOrderNumber()));
    paymentRepository.findByOrder(order)
        .ifPresent(existing -> { throw new PaymentAlreadyExistsException(...); });
    Payment payment = Payment.builder()
        .order(order)
        .paymentMethod(request.getPaymentMethod())
        .paymentStatus(PaymentStatus.PENDING)
        .amount(order.getTotalAmount())
        .build();
    return paymentMapper.toPaymentResponse(paymentRepository.save(payment));
}
```

## Entity Builder Pattern
Use `@Builder` on entities when constructing in service layer:
```java
Payment payment = Payment.builder()
    .order(order)
    .paymentMethod(request.getPaymentMethod())
    .paymentStatus(PaymentStatus.PENDING)
    .amount(order.getTotalAmount())
    .build();
```

## Pagination
Use `PageResponse<T>` from `bookcorner-shared` as the return type for all paginated endpoints.
Accept `Pageable` or `PaginationRequest` from `bookcorner-shared` as input.

## Naming Conventions
| Element | Convention | Example |
|---|---|---|
| Entity | PascalCase, singular | `OrderEntity`, `Books`, `Payment` |
| Repository | `<Entity>Repository` | `PaymentRepository` |
| Service interface | `<Domain>Service` | `PaymentService` |
| Service impl | `<Domain>ServiceImpl` in `serviceimpl/` | `PaymentServiceImpl` |
| Mapper | `<Domain>Mapper` | `PaymentMapper` |
| DTO (request) | `<Action>Request` | `PaymentRequest`, `AddressRequest` |
| DTO (response) | `<Domain>Response` | `PaymentResponse`, `AddressResponse` |
| Enum | PascalCase | `PaymentStatus`, `OrderStatus` |
| Package | lowercase, domain-named | `com.bookcorner.payment` |

## JWT / Security
- JWT filter extends `OncePerRequestFilter`
- Token extracted from `Authorization: Bearer <token>` header
- Each JWT exception type (`ExpiredJwtException`, `MalformedJwtException`, `SignatureException`, etc.) caught individually and returns a JSON `{"error": "..."}` with 401
- `JwtProperties` (`@ConfigurationProperties`) holds secret and expiry — never hardcode
- Override `BOOKCORNER_JWT_SECRET` env var in production

## What to Avoid
- Do not use `@Data` on JPA entities (causes issues with bidirectional relationships and equals/hashCode)
- Do not access `SecurityContextHolder` directly in service classes — use `AuthenticationService`
- Do not throw generic `RuntimeException` in new code — always use a named domain exception
- Do not write manual entity-to-DTO mapping — always use MapStruct
- Do not use `@Autowired` field injection — use constructor injection
