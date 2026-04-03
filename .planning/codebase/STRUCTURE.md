# Structure

## Top Level Layout

- `pom.xml` — parent Maven aggregator and shared Java version configuration
- `compose.yaml` — local PostgreSQL service
- `README.md` — bootstrap and template guidance
- `init-template.sh` — template rename automation
- `scripts/test-init-template.sh` — smoke test for bootstrap script
- `milestory-api/` — API contract module
- `milestory-backend/` — Spring Boot backend module
- `milestory-frontend/` — Angular frontend module

## API Module Layout

- `milestory-api/pom.xml`
- `milestory-api/rest/api-v1.yaml`
- `milestory-api/rest/paths/hello.yaml`
- `milestory-api/rest/schemas/commons.yaml`

The API folder structure already follows domain-separated `paths/` and `schemas/` conventions, but only a single sample endpoint exists.

## Backend Module Layout

- `milestory-backend/pom.xml`
- `milestory-backend/src/main/java/com/ybritto/milestory/MilestoryBackendApplication.java`
- `milestory-backend/src/main/resources/application.yaml`
- `milestory-backend/src/main/resources/application-local.yaml`
- `milestory-backend/src/main/resources/openapi-processor-mapping.yaml`
- `milestory-backend/src/test/java/com/ybritto/milestory/MilestoryBackendApplicationTests.java`
- `milestory-backend/src/test/resources/application-test.yaml`

Notably absent:

- `domain/`, `application/`, `infrastructure/`, or adapter package trees
- `db/changelog/` resources
- Controllers or generated source check-ins

## Frontend Module Layout

- `milestory-frontend/package.json`
- `milestory-frontend/angular.json`
- `milestory-frontend/src/app/` — root app shell only
- `milestory-frontend/src/api/` — generated OpenAPI client
- `milestory-frontend/public/` — static assets

Key application files:

- `milestory-frontend/src/app/app.ts`
- `milestory-frontend/src/app/app.html`
- `milestory-frontend/src/app/app.scss`
- `milestory-frontend/src/app/app.routes.ts`
- `milestory-frontend/src/app/app.config.ts`

## Naming Conventions Observed

- Module names align with the Milestory product identity: `milestory-api`, `milestory-backend`, `milestory-frontend`
- Java package root is still `com.ybritto.milestory`
- Some repository docs still describe the repo as a generic template in `README.md` and `AGENTS.md`

## Generated And Build Output

- Frontend generated API client is committed in `milestory-frontend/src/api/`
- Frontend Maven metadata appears in `milestory-frontend/target/maven-archiver/pom.properties`
- Backend generated sources are expected under `milestory-backend/target/generated-sources/openapi`

## Practical Read Of The Structure

This is a clean starting skeleton with good module boundaries, but it is not yet a feature-bearing codebase. Planning should treat it as an initialized scaffold with infrastructure hooks rather than as a mature application that needs incremental extension.
