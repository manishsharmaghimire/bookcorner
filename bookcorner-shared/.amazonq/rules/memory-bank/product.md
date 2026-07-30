# BookCorner Nepal — Product Overview

## Purpose
BookCorner is a Spring Boot monolithic backend for an online book marketplace in Nepal. It supports buying, selling, and renting books, with Nepali payment gateways (eSewa, Khalti) and mobile-number + OTP authentication.

## Target Users
- **Buyers**: Browse, search, purchase, and rent books
- **Sellers**: List and sell second-hand books (requires Seller profile upgrade)
- **Rental Users**: List books for rent (requires Rental User profile upgrade)
- **Book Shops**: Sell new books and list rentals (requires Book Shop profile upgrade)
- **Admins**: Manage platform — approve books, manage users, view dashboard

## Key Features

### Authentication & Users
- Mobile number + OTP registration (Redis-cached, expiring OTPs)
- Password login with JWT access + refresh tokens (rotation on refresh)
- Forgot-password via OTP
- Progressive profile upgrade: User → Buyer/Seller → Rental User → Book Shop
- Role/profile-based authorization

### Catalog
- Books in three types: NEW, SECOND_HAND, RENTAL
- Unlimited nested categories
- Search, filter, sort, paginate
- Seller-type enforcement (only Book Shops sell NEW; only Rental User/Book Shop list RENTAL)

### Commerce
- Cart: add/update/remove items, mixed purchase + rental lines
- Wishlist: save/remove/list books
- Orders: checkout (purchase lines), history, tracking, cancellation, return requests
- Rentals: full lifecycle (request → approve/reject → pickup → ongoing → return → returned), late-penalty calculation, deposit refund

### Payments
- eSewa, Khalti, COD via common gateway abstraction
- Payment history and refunds
- Tied to either an Order or Rental via PaymentContextType

### Social & Admin
- Reviews: rate + review books (one per user per book), like/report, incremental average rating
- In-app notifications for order/rental/payment/delivery/admin events
- Admin dashboard: users, books, revenue, rentals; book approval queue; coupon management

## Known Limitations
- Coupons not yet wired into checkout math
- Rental refunds assume single refund call (no partial refund per gateway)
- Reviews don't check verified purchase
- Rejected books reuse ARCHIVED status (no rejectionReason column)
- SMS sending is stubbed (LogSmsSender logs OTP only)
