PROJECT TODOLIST: Ticketing Service (Spring Modulith)
1. PROJECT INITIALIZATION

    [X] 1.1 Create the base project (Java 21, Gradle/Maven)

    [ ] 1.2 Initialize Git repository and .gitignore

    [ ] 1.3 Add core dependencies (Web, JPA, Postgres, Flyway, Modulith, Validation, Actuator)

    [ ] 1.4 Add Testcontainers for PostgreSQL

    [ ] 1.5 Verify application starts successfully

2. MODULE STRUCTURE

    [ ] 2.1 Create base package: pl.example.ticketing

    [ ] 2.2 Create module subpackages: app, artist, concert, reservation, gateway, event

    [ ] 2.3 Set up Modulith verification test to enforce architectural rules

    [ ] 2.4 Verify no forbidden cross-module access (Repositories/Entities)

3. APP MODULE (COMPOSITION ROOT)

    [ ] 3.1 Create SpringBootApplication class

    [ ] 3.2 Configure Datasource and JPA properties

    [ ] 3.3 Configure Flyway and Actuator

    [ ] 3.4 Enable @EnableModulith support

    [ ] 3.5 Ensure no business logic resides in this module

4. ARTIST MODULE

    [ ] 4.1 Define Artist entity (id, name, bio, createdAt)

    [ ] 4.2 Create ArtistRepository (internal to module)

    [ ] 4.3 Create ArtistDTO and MapStruct mappers

    [ ] 4.4 Implement ArtistInternalAPI (for other modules)

    [ ] 4.5 Implement ArtistExternalAPI (for Gateway/CRUD)

    [ ] 4.6 Implement ArtistManagement service with transactional boundaries

5. CONCERT MODULE

    [ ] 5.1 Define Concert entity (id, artistId, venue, capacity, version for locking)

    [ ] 5.2 Create ConcertRepository with Optimistic Locking

    [ ] 5.3 Create ConcertDTO and MapStruct mappers

    [ ] 5.4 Implement ConcertInternalAPI (availability and seat reservation)

    [ ] 5.5 Implement ConcertExternalAPI (listing/CRUD)

    [ ] 5.6 Implement ConcertManagement (validate artist via ArtistInternalAPI)

6. RESERVATION MODULE (ORCHESTRATION)

    [ ] 6.1 Define Reservation entity (id, concertId, seats, status)

    [ ] 6.2 Create ReservationRepository

    [ ] 6.3 Create Reservation Request/Response DTOs

    [ ] 6.4 Implement ReservationExternalAPI (create, get, cancel)

    [ ] 6.5 Implement ReservationManagement orchestration logic

    [ ] 6.6 Handle concurrency and seat verification via ConcertInternalAPI

7. DOMAIN EVENTS

    [ ] 7.1 Define immutable events: ConcertCreated, ReservationCreated, etc.

    [ ] 7.2 Implement event publication in services

    [ ] 7.3 Set up @ApplicationModuleListener for event consumption

    [ ] 7.4 Verify event-driven decoupling between modules

8. GATEWAY MODULE (REST API)

    [ ] 8.1 Create ArtistController mapping to ArtistExternalAPI

    [ ] 8.2 Create ConcertController mapping to ConcertExternalAPI

    [ ] 8.3 Create ReservationController mapping to ReservationExternalAPI

    [ ] 8.4 Implement /api/v1 versioned paths

    [ ] 8.5 Implement Global Exception Handling (400, 404, 409, 500)

9. DATABASE AND MIGRATIONS

    [ ] 9.1 Create Flyway migration for artists table

    [ ] 9.2 Create Flyway migration for concerts table

    [ ] 9.3 Create Flyway migration for reservations table

    [ ] 9.4 Add necessary database indexes and constraints

10. TESTING

    [ ] 10.1 Write Unit Tests for business rules and mappers

    [ ] 10.2 Write Integration Tests using Testcontainers

    [ ] 10.3 Implement Concurrency Tests for the reservation flow

    [ ] 10.4 Run ApplicationModules.verify() to ensure modular integrity

11. OBSERVABILITY & DOCUMENTATION

    [ ] 11.1 Enable Actuator /health and /modulith endpoints

    [ ] 11.2 (Optional) Configure Zipkin/Micrometer tracing

    [ ] 11.3 Write README.md with architecture overview

    [ ] 11.4 Generate and include Modulith component diagrams
