# BookCorner Shared Module

This module contains shared utilities, DTOs, and common code used across the BookCorner project.

## Purpose

- **BaseEntity**: Common entity fields (id, createdAt, updatedAt) with JPA auditing
- **PageResponse**: Generic paginated response wrapper for consistent API responses
- **Exception classes**: Common exceptions like ResourceNotFoundException
- **DTOs**: Shared data transfer objects
- **Utilities**: Common utility methods and helpers

## Usage

Add as a dependency in other modules:

```xml
<dependency>
    <groupId>com.bookcorner</groupId>
    <artifactId>bookcorner-shared</artifactId>
</dependency>
```

## Structure

```
bookcorner-shared/
├── src/main/java/com/bookcorner/shared/
│   ├── BaseEntity.java
│   ├── PageResponse.java
│   └── exception/
│       └── ResourceNotFoundException.java
└── pom.xml
```
