# Milestory

Milestory is a backend-driven web app for defining annual goals, tracking progress, and understanding whether you are behind, on track, or ahead of plan. Phase 1 establishes the real application foundation: a narrow auth-free status contract, a Spring Boot backend, an Angular frontend, and local PostgreSQL for development.

## Phase 1 scope

Phase 1 is intentionally auth-free and focused on personal use. It proves the contract-first stack, the database baseline, and the Milestory-branded foundation experience before goal planning, progress calculations, and dashboard depth arrive in later phases.

What this phase includes:

- `milestory-api`: the OpenAPI contract that defines the Phase 1 status surface
- `milestory-backend`: the Spring Boot application that implements the contract and owns backend logic
- `milestory-frontend`: the Angular app that renders the Milestory shell and generated API client output
- `compose.yaml`: a local PostgreSQL service for development

What this phase does not include yet:

- sign-in, bearer tokens, or account flows
- goal creation and checkpoint planning
- progress tracking and motivational dashboards

## Repository structure

```text
.
├── compose.yaml
├── pom.xml
├── milestory-api/
│   └── rest/
├── milestory-backend/
│   └── src/
└── milestory-frontend/
    └── src/
```

## Local development

### Start PostgreSQL

```bash
docker-compose up -d
```

The local database service in `compose.yaml` is the expected Phase 1 development path.

### Backend

Run the backend from the repository root:

```bash
./mvnw -q -pl milestory-backend spring-boot:run
```

Run backend tests as needed:

```bash
./mvnw -q -pl milestory-backend test
```

### Frontend

Install dependencies and start the Angular app from `milestory-frontend`:

```bash
npm install
npm start
```

Regenerate the frontend API client after contract changes:

```bash
npm run client-gen
```

## Contract-first workflow

Milestory uses the OpenAPI contract as the source of truth.

1. Update `milestory-api/rest/api-v1.yaml` and related path/schema files.
2. Regenerate client code from `milestory-frontend`.
3. Implement or update backend adapters and frontend rendering against the generated types.

For Phase 1, the contract is intentionally narrow: `/api/v1/status` exists to support the Milestory foundation screen without implying authentication or broader product behavior that does not exist yet.

## Naming note

The repository still contains some template-era leftovers, and those should remain visible during implementation work until they are cleaned deliberately. One intentional exception for Phase 1 is the Java package root `com.ybritto.milestory`: Milestory is already the chosen application identity, so that package root is retained and no broader namespace rename is required for current scope.

## Current direction

Milestory is being built as a backend-driven goal and resolution tracking product with:

- backend-owned planning and progress logic
- a high-quality web UX
- a contract-first API module shared by backend and frontend
- an auth-free first release that proves personal usefulness before identity work

As later phases land, this README should evolve from foundation setup toward real product workflows.
