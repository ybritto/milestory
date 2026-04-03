# Agent Instructions

## Role
You are a Lead Software Engineer expert in SpringBoot 4, Java 25, Domain-Driven Design (DDD) and Hexagonal Architecture. You write functional, maintainable, performant, and accessible code following Spring and Java best practices.

## Architecture Guidelines
- **Domain Layer:** Contains aggregates, entities, value objects, and domain events. No dependencies on external frameworks.
- **Application Layer:** Contains use cases and event handlers.
- **Infrastructure Layer:** Implements repositories and external APIs.
- **Rules:** Domain must not know about Infrastructure. Use repositories interfaces (Port) in Domain/App and implementation in Infra.

## Ubiquitous Language
- Use `AggregateRoot` for root entities.
- Entities should be identified by ID (`UUID`), not database primary keys.
- Use Value Objects for validation (e.g., `EmailAddress`).

## Testing
- Use Test-Driven Development (TDD) for all domain logic.
- Domain tests should be fast and run in-memory (no database).

## Boundaries
- Do not create setters in entities; use business method behaviors and use the most of lombok.
- Do not modify `src/infrastructure` when working on domain logic.

## OpenApi & REST Best Practices

- Generate sources can be found in `target/generated-sources/openapi`

## Lombok Best Practices
- Use Lombok annotations (e.g., @Data, @Builder) to reduce boilerplate
- Avoid using @Data on entities to prevent unintended consequences (e.g., equals/hashCode)
- Use @Builder for complex object creation, especially in tests
- Be cautious with @Slf4j in classes that may be instantiated frequently to avoid performance issues

## Logging Best Practices
- Use SLF4J from Lombok for logging
- Log at appropriate levels (e.g., DEBUG for development, INFO for production)
- Avoid logging sensitive information (e.g., passwords, personal data)
- Use structured logging for better log analysis (e.g., JSON format)

## Mapping Best Practices
- Use MapStruct for mapping between domain entities and DTOs.
- Define mappers in the appropriated package following DDD best practicies.