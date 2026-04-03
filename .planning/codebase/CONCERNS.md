# Concerns

## Highest Priority Concerns

### 1. Backend Is Not Yet Implemented Beyond Bootstrap

The only production Java source currently visible is `milestory-backend/src/main/java/com/ybritto/milestory/MilestoryBackendApplication.java`. There are no controllers, use cases, domain objects, repositories, or adapters yet. Planning should treat the backend as effectively greenfield.

### 2. Liquibase Is Configured But Missing Changelog Files

`milestory-backend/src/main/resources/application.yaml`, `application-local.yaml`, and `application-test.yaml` all reference `classpath:/db/changelog/db.changelog-master.yaml`, but no `db/changelog/` tree exists under `milestory-backend/src/main/resources/`. Any startup or tests that require Liquibase are likely to fail until this is added.

### 3. Frontend Is Still Angular Starter Content

`milestory-frontend/src/app/app.html` is the long default Angular placeholder template, `app.routes.ts` is empty, and `app.spec.ts` still asserts starter text. This means UX work has not started and frontend tests are validating scaffold behavior rather than product intent.

### 4. Auth Is Declared In The Contract Before It Exists

`milestory-api/rest/api-v1.yaml` applies bearer auth globally, and the generated Angular client sends bearer credentials. With no backend auth stack yet, the contract may create friction for early non-auth endpoints or local smoke testing unless phased carefully.

## Medium Priority Concerns

### 5. DDD And Hexagonal Intent Are Not Reflected In Code Yet

The backend instructions call for domain/application/infrastructure boundaries, but no such package layout exists. Without deliberate early phase planning, the codebase could drift into a standard controller-service-repository shape before the intended architecture is established.

### 6. Template Leftovers Can Pollute Product Planning

`README.md`, `AGENTS.md`, and `init-template.sh` still frame the repo as a reusable template. That is useful operationally, but it can confuse planning artifacts and contributors if product-specific docs are not clearly separated from template bootstrap guidance.

### 7. Frontend Build Is Coupled To npm Install In Maven Generate-Sources

`milestory-frontend/pom.xml` runs `npm install` during Maven `generate-sources`. This may slow CI, complicate offline work, and make backend-only operations unexpectedly depend on Node tooling.

## Lower Priority But Notable

### 8. OpenAPI Conventions Are Only Partially Applied

The API instructions require path tags and reusable schemas, but the existing sample path in `milestory-api/rest/paths/hello.yaml` has no tags and only demonstrates a trivial hello endpoint.

### 9. Missing UI Library Decision

The project direction mentions Angular Material or PrimeNG, but no decision is embodied in code yet. Since UX quality is a core driver, choosing a component strategy early will shape design system, accessibility, and development speed.

## Planning Implications

- Treat Milestory as a scaffolded brownfield, not a feature-rich brownfield
- Establish architecture and database migration foundations early
- Clean or quarantine template leftovers before broad feature growth
- Sequence auth carefully against the stated desire to keep business logic on the backend
