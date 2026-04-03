# Architecture

## Current Pattern

The repository is organized as a contract-first monorepo rather than a fully realized DDD or hexagonal system today. Architecture intent is visible in instructions, but implementation is still mostly scaffold.

## Main Components

- `milestory-api/` owns the OpenAPI contract
- `milestory-backend/` is the Spring Boot runtime intended to implement that contract
- `milestory-frontend/` is the Angular application and generated API client consumer

## Data And Build Flow

The current build-time flow is:

1. API YAML lives in `milestory-api/rest/api-v1.yaml`
2. Backend unpacks YAML from the API artifact in `milestory-backend/pom.xml`
3. Backend generates Spring interfaces using `openapi-processor-maven-plugin`
4. Frontend generates TypeScript Angular API code from the same YAML using `npm run client-gen`

This is the clearest architectural through-line in the codebase.

## Runtime Entry Points

- Backend entry point: `milestory-backend/src/main/java/com/ybritto/milestory/MilestoryBackendApplication.java`
- Frontend entry point: `milestory-frontend/src/main.ts`
- Angular root config: `milestory-frontend/src/app/app.config.ts`
- OpenAPI root path registration: `milestory-api/rest/api-v1.yaml`

## Present Backend Shape

The backend currently has only the application bootstrap class and configuration files. There are no visible domain, application, infrastructure, controller, repository, or security packages under `milestory-backend/src/main/java/` beyond the root application class.

Because of that, there is no implemented hexagonal layering yet despite explicit architectural guidance in `milestory-backend/AGENTS.md`.

## Present Frontend Shape

The frontend is a minimal Angular shell:

- Root component in `milestory-frontend/src/app/app.ts`
- Placeholder template in `milestory-frontend/src/app/app.html`
- Empty route table in `milestory-frontend/src/app/app.routes.ts`
- Generated API client under `milestory-frontend/src/api/`

There are no domain features, route segments, state stores, or reusable component libraries yet.

## Coupling Notes

- API contract is the strongest shared boundary across modules
- Backend and frontend are both tightly tied to generated code flows
- The frontend currently depends on a globally secured API spec even though no auth flow exists yet

## Architectural Maturity

What exists:

- Clean module separation
- Clear contract-first intent
- Configured persistence and schema migration tooling

What is still missing:

- Domain model
- Use case/application services
- Ports and adapters
- Persistence model implementation
- Auth and email architecture
- Goal tracking bounded contexts
- Dashboard/reporting architecture
