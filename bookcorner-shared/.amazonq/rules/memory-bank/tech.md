# BookCorner Nepal — Technology Stack

## Core Stack
| Layer | Technology | Version |
|---|---|---|
| Language | Java | 21 |
| Framework | Spring Boot | 3.3.2 |
| Build | Maven (multi-module) | - |
| Database (local) | MySQL | 8.x |
| Database (prod) | PostgreSQL | - |
| Cache / OTP store | Redis | 7 |
| ORM | Spring Data JPA / Hibernate | - |
| Security | Spring Security + JWT (jjwt) | 0.12.5 |
| Mapping | MapStruct | 1.5.5.Final |
| Boilerplate reduction | Lombok | 1.18.32 |
| API docs | springdoc-openapi (Swagger UI) | 2.5.0 |
| Testing | JUnit 5 + Mockito | - |

## Key Dependencies by Module

### bookcorner-auth
- spring-boot-starter-security
- spring-boot-starter-data-redis
- jjwt-api / jjwt-impl / jjwt-jackson
- spring-boot-starter-validation

### bookcorner-book / domain modules
- spring-boot-starter-data-jpa
- spring-boot-starter-web
- spring-boot-starter-validation
- postgresql (runtime)
- mapstruct

### bookcorner-api (entry point)
- All internal modules
- spring-boot-starter-web
- springdoc-openapi-starter-webmvc-ui
- mysql-connector-j (runtime)
- spring-boot-starter-test

## Development Commands

```bash
# Start required infrastructure
docker run -d -p 6379:6379 redis:7
# MySQL must be running on localhost:3306 with db 'bookcorner'

# Run with local profile (MySQL + Redis)
mvn spring-boot:run -pl bookcorner-api

# Run with postgres profile
mvn spring-boot:run -pl bookcorner-api -Dspring-boot.run.profiles=postgres

# Build all modules
mvn clean install

# Run tests
mvn test

# Build without tests
mvn clean install -DskipTests
```

## Environment Variables
| Variable | Default | Description |
|---|---|---|
| DB_URL | jdbc:mysql://localhost:3306/bookcorner | Database URL |
| DB_USERNAME | bookcorner | DB username |
| DB_PASSWORD | bookcorner | DB password |
| REDIS_HOST | localhost | Redis host |
| REDIS_PORT | 6379 | Redis port |
| BOOKCORNER_JWT_SECRET | (dev default in JwtProperties) | JWT signing secret — override in production |

## URLs (local)
- API base: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs

## Profiles
- `local` — MySQL, ddl-auto: create-drop, show-sql: true
- `postgres` — PostgreSQL, reads env vars for connection

## Annotation Processor Setup
Lombok and MapStruct are wired together via `lombok-mapstruct-binding` (0.2.0) in the parent POM compiler plugin — Lombok runs before MapStruct to ensure generated getters/setters are visible to MapStruct.
