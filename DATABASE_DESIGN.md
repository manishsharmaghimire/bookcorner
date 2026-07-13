# BookCorner Database Design

## Overview
Independent entity design without JPA join columns. Relationships are managed through foreign key IDs and custom queries.

## Core Tables

### 1. users
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    mobile_number VARCHAR(20) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(150),
    email VARCHAR(150),
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    mobile_verified BOOLEAN NOT NULL DEFAULT FALSE,
    status VARCHAR(25) NOT NULL DEFAULT 'PENDING_VERIFICATION',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_mobile_number (mobile_number),
    INDEX idx_status (status)
);
```

### 2. user_profile_types
```sql
CREATE TABLE user_profile_types (
    user_id BIGINT NOT NULL,
    profile_type VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, profile_type),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

### 3. categories
```sql
CREATE TABLE categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(120) NOT NULL UNIQUE,
    description VARCHAR(500),
    parent_id BIGINT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_parent_id (parent_id),
    INDEX idx_slug (slug),
    FOREIGN KEY (parent_id) REFERENCES categories(id) ON DELETE SET NULL
);
```

### 4. books
```sql
CREATE TABLE books (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    subtitle VARCHAR(255),
    author VARCHAR(150) NOT NULL,
    isbn VARCHAR(20),
    publisher VARCHAR(150),
    language VARCHAR(50),
    edition VARCHAR(30),
    book_condition VARCHAR(20),
    description VARCHAR(2000),
    price DECIMAL(10,2) NOT NULL,
    discount_percent DECIMAL(5,2) DEFAULT 0.00,
    quantity INT NOT NULL,
    book_type VARCHAR(20) NOT NULL,
    category_id BIGINT NOT NULL,
    seller_id BIGINT NOT NULL,
    average_rating DECIMAL(3,2) DEFAULT 0.00,
    review_count INT DEFAULT 0,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING_APPROVAL',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_title (title),
    INDEX idx_isbn (isbn),
    INDEX idx_status (status),
    INDEX idx_book_type (book_type),
    INDEX idx_category_id (category_id),
    INDEX idx_seller_id (seller_id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);
```

### 5. book_images
```sql
CREATE TABLE book_images (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    book_id BIGINT NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    display_order INT NOT NULL,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);
```

### 6. rental_details
```sql
CREATE TABLE rental_details (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    book_id BIGINT NOT NULL UNIQUE,
    rent_per_day DECIMAL(10,2) NOT NULL,
    min_rental_days INT NOT NULL,
    max_rental_days INT NOT NULL,
    deposit_amount DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);
```

### 7. cart_items
```sql
CREATE TABLE cart_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    rental_days INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_book (user_id, book_id),
    INDEX idx_user_id (user_id),
    INDEX idx_book_id (book_id),
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);
```

### 8. orders
```sql
CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_number VARCHAR(30) NOT NULL UNIQUE,
    buyer_id BIGINT NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    shipping_charge DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    discount_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    total_amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(25) NOT NULL DEFAULT 'PLACED',
    payment_method VARCHAR(20) NOT NULL,
    delivery_full_name VARCHAR(150),
    delivery_phone VARCHAR(20),
    delivery_address_line1 VARCHAR(255),
    delivery_address_line2 VARCHAR(255),
    delivery_city VARCHAR(100),
    delivery_state VARCHAR(100),
    delivery_postal_code VARCHAR(20),
    delivery_country VARCHAR(100) DEFAULT 'Nepal',
    delivery_partner VARCHAR(100),
    tracking_number VARCHAR(100),
    estimated_delivery_date DATE,
    cancellation_reason VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_order_number (order_number),
    INDEX idx_buyer_id (buyer_id),
    INDEX idx_status (status)
);
```

### 9. order_items
```sql
CREATE TABLE order_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    seller_id BIGINT NOT NULL,
    book_title_snapshot VARCHAR(255) NOT NULL,
    book_type VARCHAR(20) NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_order_id (order_id),
    INDEX idx_book_id (book_id),
    INDEX idx_seller_id (seller_id),
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(id)
);
```

### 10. rental_transactions
```sql
CREATE TABLE rental_transactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    book_id BIGINT NOT NULL,
    renter_id BIGINT NOT NULL,
    owner_id BIGINT NOT NULL,
    rent_per_day DECIMAL(10,2) NOT NULL,
    deposit_amount DECIMAL(10,2) NOT NULL,
    rental_days INT NOT NULL,
    total_rent_amount DECIMAL(10,2) NOT NULL,
    pickup_date DATE,
    expected_return_date DATE,
    actual_return_date DATE,
    late_days INT DEFAULT 0,
    penalty_amount DECIMAL(10,2) DEFAULT 0.00,
    deposit_refund_amount DECIMAL(10,2),
    status VARCHAR(25) NOT NULL DEFAULT 'REQUESTED',
    rejection_reason VARCHAR(500),
    delivery_full_name VARCHAR(150),
    delivery_phone VARCHAR(20),
    delivery_address_line1 VARCHAR(255),
    delivery_address_line2 VARCHAR(255),
    delivery_city VARCHAR(100),
    delivery_state VARCHAR(100),
    delivery_postal_code VARCHAR(20),
    delivery_country VARCHAR(100) DEFAULT 'Nepal',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_book_id (book_id),
    INDEX idx_renter_id (renter_id),
    INDEX idx_owner_id (owner_id),
    INDEX idx_status (status),
    FOREIGN KEY (book_id) REFERENCES books(id)
);
```

### 11. refresh_tokens
```sql
CREATE TABLE refresh_tokens (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    token VARCHAR(255) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    revoked BOOLEAN NOTNULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_token (token),
    INDEX idx_user_id (user_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

### 12. coupons
```sql
CREATE TABLE coupons (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    discount_percent DECIMAL(5,2) NOT NULL,
    max_discount_amount DECIMAL(10,2),
    min_order_amount DECIMAL(10,2),
    usage_limit INT,
    used_count INT DEFAULT 0,
    valid_from TIMESTAMP NOT NULL,
    valid_until TIMESTAMP NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_code (code)
);
```

## Relationship Summary

- **users** → **user_profile_types**: One-to-many (profile types)
- **categories** → **categories**: Self-referencing (parent-child)
- **categories** → **books**: One-to-many (books belong to categories)
- **users** → **books**: One-to-many (users as sellers)
- **books** → **book_images**: One-to-many (book images)
- **books** → **rental_details**: One-to-one (rental-specific data)
- **users** → **cart_items**: One-to-many (user cart)
- **books** → **cart_items**: One-to-many (book in carts)
- **users** → **orders**: One-to-many (user orders)
- **orders** → **order_items**: One-to-many (order items)
- **books** → **order_items**: One-to-many (book in orders)
- **users** → **rental_transactions**: One-to-many (as renter)
- **users** → **rental_transactions**: One-to-many (as owner)
- **books** → **rental_transactions**: One-to-many (book rentals)
- **users** → **refresh_tokens**: One-to-many (user sessions)

## Query Strategy

All joins will be handled via:
1. **Custom JPQL queries** in repositories
2. **Native SQL queries** for complex reporting
3. **Service layer composition** for simple lookups

Example custom query pattern:
```java
@Query("SELECT b FROM Book b WHERE b.categoryId = :categoryId")
List<Book> findByCategoryId(@Param("categoryId") Long categoryId);
```
