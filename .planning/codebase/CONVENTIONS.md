# Conventions

## Repository-Level Conventions

- Use the API module as the authoritative HTTP contract source
- Keep backend logic on the server side
- Prefer DDD, hexagonal architecture, and TDD according to `milestory-backend/AGENTS.md`
- Prefer Angular standalone patterns, signals, and accessible UI according to `milestory-frontend/AGENTS.md`

## Java And Spring Conventions

Observed and instructed conventions include:

- Java package root `com.ybritto.milestory`
- Spring Boot application bootstrap in `milestory-backend/src/main/java/com/ybritto/milestory/MilestoryBackendApplication.java`
- Configuration through YAML files rather than properties
- JPA with `ddl-auto: validate` in `milestory-backend/src/main/resources/application.yaml`
- Liquibase for schema evolution

Architectural rules from `milestory-backend/AGENTS.md`:

- Domain layer should be framework-independent
- Application layer should contain use cases
- Infrastructure layer should implement ports
- Entities should favor behavior over setters
- Value objects should hold validation

These are conventions by policy today, not by implemented example.

## OpenAPI Conventions

- Paths are expected under `milestory-api/rest/paths/`
- Schemas are expected under `milestory-api/rest/schemas/`
- Path entries should be tagged according to `milestory-api/AGENTS.md`
- OpenAPI version is pinned to `3.1.2` in `milestory-api/rest/api-v1.yaml` because of generator compatibility concerns

Current contract gaps against the documented conventions:

- The sample `/api/v1/hello` path in `milestory-api/rest/paths/hello.yaml` has no `tags`
- Error handling and validation conventions are not yet embodied beyond simple schemas

## Angular Conventions

Configured and instructed patterns:

- Standalone Angular app structure
- Signals for state
- Reactive forms preferred
- `ChangeDetectionStrategy.OnPush` expected for components
- Native control flow preferred over legacy structural directives
- Avoid `ngClass`, `ngStyle`, `@HostBinding`, and `@HostListener`

Current implementation does not yet demonstrate most of these because only the starter root component exists.

## Styling And UX Conventions

- SCSS is the chosen styling language in `milestory-frontend/angular.json`
- UX quality is explicitly a project driver in the repository instructions
- The current root template still contains Angular starter placeholder markup in `milestory-frontend/src/app/app.html`

## Generated Code Handling

- `milestory-frontend/src/api/` is generated and should generally be treated as derived code
- Backend generated Spring interfaces are expected in `milestory-backend/target/generated-sources/openapi`
- Contract changes should trigger regeneration on both backend and frontend sides
