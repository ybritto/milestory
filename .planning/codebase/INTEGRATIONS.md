# Integrations

## Overview

External integration surface is minimal at the moment. The scaffold is prepared for database-backed backend services and JWT-protected HTTP APIs, but only PostgreSQL is concretely wired today.

## Database

- PostgreSQL 18 container defined in `compose.yaml`
- Backend datasource properties in `milestory-backend/src/main/resources/application.yaml`
- Local host/port/name overrides in `milestory-backend/src/main/resources/application-local.yaml`
- Test datasource overrides in `milestory-backend/src/test/resources/application-test.yaml`

Expected environment variables:

- `POSTGRES_DB`
- `POSTGRES_USER`
- `POSTGRES_PASSWORD`
- `POSTGRES_PORT`
- `MILESTORY_DB_URL`
- `MILESTORY_DB_HOST`
- `MILESTORY_DB_PORT`
- `MILESTORY_DB_NAME`
- `MILESTORY_DB_USERNAME`
- `MILESTORY_DB_PASSWORD`

## Schema Management

- Liquibase is enabled in `milestory-backend/src/main/resources/application.yaml`
- The configured changelog path is `classpath:/db/changelog/db.changelog-master.yaml`

This integration is incomplete because the referenced changelog tree does not exist yet.

## HTTP Contract Integration

- Backend consumes API YAML packaged by `milestory-api` through the `maven-dependency-plugin` execution in `milestory-backend/pom.xml`
- Frontend generates a TypeScript Angular client from `../milestory-api/rest/api-v1.yaml` via the `client-gen` script in `milestory-frontend/package.json`

This establishes the contract-first workflow for both server and client.

## Authentication Surface

- Global bearer auth security scheme defined in `milestory-api/rest/api-v1.yaml`
- Generated Angular client injects bearer credentials in `milestory-frontend/src/api/api/default.service.ts`
- Generated configuration supports username/password and bearer tokens in `milestory-frontend/src/api/configuration.ts`

There is no real auth provider, token issuance, email system, or backend security configuration implemented yet.

## External Services

Currently absent:

- Email delivery provider
- OAuth or third-party identity provider
- Payment provider
- Analytics provider
- Object storage
- Messaging or queue infrastructure
- Monitoring or error reporting integration

## Local Development Integration Story

The intended local setup appears to be:

1. Start PostgreSQL with `compose.yaml`
2. Build the Maven workspace so `milestory-api` artifacts are available to the backend
3. Let the frontend module install npm dependencies and regenerate the API client during Maven generate-sources

This will need refinement because frontend `npm install` as a Maven lifecycle step can be slow and brittle in CI or offline environments.
