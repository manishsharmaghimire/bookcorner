# BookCorner Nepal — Backend

Monolithic Spring Boot backend for buying, selling, and renting books in Nepal.
Java 21 · Spring Boot 3.3 · Spring Security + JWT · Spring Data JPA · PostgreSQL
(H2 for local dev) · Redis (OTP cache) · MapStruct · Lombok · springdoc-openapi
(Swagger UI) · JUnit 5 + Mockito.

## Run it

Redis must be reachable (OTP codes are cached there). Easiest local setup:

```bash
docker run -d -p 6379:6379 redis:7
mvn spring-boot:run
```

Defaults to the `local` profile — in-memory H2, zero DB setup. Swagger UI:
http://localhost:8080/swagger-ui.html · H2 console: http://localhost:8080/h2-console

For Postgres:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
# reads DB_URL / DB_USERNAME / DB_PASSWORD, REDIS_HOST / REDIS_PORT env vars
```

Run tests: `mvn test`

## Modules

| Module | Covers |
|---|---|
| **security / auth** | Mobile-number + OTP registration (Redis-cached, expiring), password login, JWT access + refresh tokens (rotation on refresh), forgot-password via OTP, BCrypt hashing, stateless Spring Security filter chain, role/profile-based authorization |
| **user** | Profile upgrade path: bare User → Buyer/Seller → Rental User → Book Shop, stacking capabilities on one account |
| **catalog** | Books (NEW / SECOND_HAND / RENTAL), unlimited nested categories, search/filter/sort/paginate, seller-type enforcement (only Book Shops sell NEW, only Rental User/Book Shop list RENTAL) |
| **cart** | Add/update/remove items, mixed purchase + rental lines in one cart |
| **wishlist** | Save/remove/list books |
| **order** | Checkout (purchase lines only), order history, tracking, cancellation, return requests, shipment updates |
| **rental** | Full lifecycle: request → approve/reject → pickup → ongoing → return requested → returned, with late-penalty calculation (capped at deposit) and deposit refund |
| **payment** | eSewa / Khalti / COD via a common gateway abstraction, payment history, refunds, tied to either an Order or a Rental via `PaymentContextType` |
| **review** | Rate + review books (one per user per book), like/report, book's average rating kept in sync incrementally |
| **notification** | In-app notifications for order/rental/payment/delivery/admin events |
| **admin** | Dashboard (users, books, revenue, rentals), book approval/rejection queue, user suspend/activate, coupon management |

## Known simplifications (called out in code comments)
- Coupons aren't yet wired into checkout math (`Order.discountAmount` is always 0 for now) — `AdminService` manages them, `OrderService` applying one is a natural next step.
- Rental refunds assume a single `refund()` call per payment; a true *partial* refund (deposit minus late penalty) needs a per-gateway partial-refund endpoint, which isn't modeled yet.
- Reviews don't check "verified purchase" (no lookup into Order/Rental history yet).
- Rejected books reuse the `ARCHIVED` status rather than a dedicated `REJECTED` state (no `rejectionReason` column on `Book` yet).
- SMS sending is stubbed (`LogSmsSender` just logs the OTP) — swap in Sparrow SMS/Twilio for real delivery.

## Suggested next passes
1. Wire coupon codes into `OrderService.createOrder`.
2. Scheduled job to flip `ONGOING` rentals past `expectedReturnDate` to `OVERDUE` and notify both parties.
3. Real SMS gateway integration.
4. Rate limiting on OTP request endpoints (spec calls for this explicitly).
