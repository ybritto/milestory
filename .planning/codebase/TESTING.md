# Testing

## Current Test Surface

Testing is present only at scaffold level.

Backend:

- `milestory-backend/src/test/java/com/ybritto/milestory/MilestoryBackendApplicationTests.java`
- `milestory-backend/src/test/resources/application-test.yaml`

Frontend:

- `milestory-frontend/src/app/app.spec.ts`
- Angular unit tests run through the `test` target in `milestory-frontend/angular.json`
- `vitest` is listed in `milestory-frontend/package.json`

## Backend Testing Stack

- `spring-boot-starter-test` in `milestory-backend/pom.xml`
- Spring Boot context test naming suggests a smoke test only
- No domain test suites, repository tests, integration tests, or contract tests are present yet

The backend instructions explicitly want TDD and fast in-memory domain tests, but the codebase has not reached that stage.

## Frontend Testing Stack

- `ng test` and `ng test --watch=false` scripts in `milestory-frontend/package.json`
- Angular TestBed usage in `milestory-frontend/src/app/app.spec.ts`
- No component harnesses, routing tests, service tests, or API integration tests are present

There is a likely mismatch to watch:

- Angular CLI unit test builder is configured
- `vitest` is installed
- The repo does not yet show a clear Vitest integration pattern beyond package dependency presence

## What Is Being Tested Today

- Backend application context startup, assuming missing Liquibase resources do not block it
- Frontend root component creation
- Frontend starter heading text rendered from placeholder template

## Risk Areas For Future Testing

- Goal progress calculation and checkpoint suggestion logic
- Authentication and email-driven entry flow
- JWT issuance, refresh, and authorization boundaries
- Contract alignment between OpenAPI, backend implementation, and generated frontend client
- Accessibility and UX regressions in dashboard-heavy flows

## Recommended Testing Direction

- Domain-first unit tests for core goal and progress rules
- Integration tests for persistence and Liquibase migrations
- API slice or contract tests around generated Spring interfaces
- Frontend unit tests for route guards, stores, and dashboard presentation logic
- End-to-end tests once auth and core goal workflows exist
