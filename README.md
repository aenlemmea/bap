# BAP

**B**ook **A** **P**lace is a property rental platform. BAP is a web app built 
using Spring Boot, Spring 
Data JPA,
Spring Security, Spring Validation, JJWT for JWT handling. The project 
is built from scratch where possible. Maven is used for 
building and dependency management.

The database used is PostgreSQL run using Docker. The frontend piece is 
built 
using 
Angular.

### Goals
1. DTOs are used as the API contract.
2. Routes are versioned (as a crutch), grouped and protected.
3. Validation is done both at DTO level and Entity/Model level.
4. Maintain a concise API focused on usability.
5. Global exception handling.


### Features

1. JWT based authentication (returned as a `Set-Cookie`, for HTTP-Only 
   cookie storage) with 
   Authorization 
   Header fallback.
2. Fully functional `SecurityFilterChain` with route configuration.
3. CRUD query methods for efficient retrieval using Spring Data JPA.
4. Automatic price calculation for booked listing.
