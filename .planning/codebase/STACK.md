# Stack

## Overview

Milestory is a multi-module Maven workspace with a contract-first API module, a Spring Boot backend, and an Angular frontend. The repository is still at scaffold stage: core runtime tooling is present, but product features are largely unimplemented.

## Languages And Runtimes

- Java 25 configured at the parent build in `pom.xml`
- TypeScript 5.9 in `milestory-frontend/package.json`
- Node/npm with `packageManager: npm@10.9.0` in `milestory-frontend/package.json`
- YAML for OpenAPI specs and Spring configuration in `milestory-api/rest/` and `milestory-backend/src/main/resources/`

## Build And Workspace Structure

- Parent aggregator: `pom.xml`
- API module: `milestory-api/pom.xml`
- Backend module: `milestory-backend/pom.xml`
- Frontend module: `milestory-frontend/pom.xml`

The parent POM uses `org.springframework.boot:spring-boot-starter-parent:4.0.4` and aggregates the three modules.

## Backend Frameworks And Libraries

- Spring Boot 4.0.4 via parent `pom.xml`
- Spring Web MVC in `milestory-backend/pom.xml`
- Spring Data JPA in `milestory-backend/pom.xml`
- Liquibase in `milestory-backend/pom.xml`
- PostgreSQL JDBC driver in `milestory-backend/pom.xml`
- Lombok annotation processing in `milestory-backend/pom.xml`
- Spring Boot Test in `milestory-backend/pom.xml`

## API Tooling

- OpenAPI 3.1.2 root contract in `milestory-api/rest/api-v1.yaml`
- `io.openapiprocessor:openapi-processor-maven-plugin` in `milestory-backend/pom.xml`
- Generated backend sources under `milestory-backend/target/generated-sources/openapi`
- Angular client generation with `@openapitools/openapi-generator-cli` in `milestory-frontend/package.json`

## Frontend Frameworks And Libraries

- Angular 21 packages in `milestory-frontend/package.json`
- RxJS 7.8 in `milestory-frontend/package.json`
- Angular build system in `milestory-frontend/angular.json`
- Vitest and Angular unit test builder in `milestory-frontend/package.json` and `milestory-frontend/angular.json`

No UI component library is installed yet. Angular Material and PrimeNG are both absent from `milestory-frontend/package.json`.

## Configuration Sources

- Backend default profile and datasource config in `milestory-backend/src/main/resources/application.yaml`
- Local overrides in `milestory-backend/src/main/resources/application-local.yaml`
- Test overrides in `milestory-backend/src/test/resources/application-test.yaml`
- Docker Compose postgres service in `compose.yaml`
- OpenAPI processor mapping in `milestory-backend/src/main/resources/openapi-processor-mapping.yaml`

## Current State Notes

- The backend references Liquibase changelog `classpath:/db/changelog/db.changelog-master.yaml`, but no changelog files are present in `milestory-backend/src/main/resources/`.
- The frontend contains generated API client code in `milestory-frontend/src/api/` but no feature modules or domain-specific UI.
- The repository still includes template-oriented docs and bootstrap assets in `README.md` and `init-template.sh`.
