# Phase 01: Foundation And Personal Mode - Research

**Researched:** 2026-04-03
**Domain:** Spring Boot foundation, auth-free personal mode, contract-first API, Angular status shell
**Confidence:** MEDIUM

## User Constraints

### Locked Decisions
- **D-01:** Personal mode should feel invisible to the user. The app should simply feel like "my app" rather than exposing a visible profile or account concept in Phase 1.
- **D-02:** Phase 1 should not introduce an owner model or user identity abstraction unless it is technically unavoidable. Prefer storing app data directly for v1 simplicity.
- **D-03:** Data migration into a future authenticated model is intentionally not locked in this phase. Planning should optimize for Phase 1 simplicity rather than designing migration mechanics now.
- **D-04:** The UI and copy should not explicitly frame the experience as "personal mode" unless that becomes necessary to explain behavior.
- **D-05:** The frontend should become a small but polished Milestory-branded home/dashboard placeholder rather than remaining Angular starter content.
- **D-06:** The main job of the Phase 1 UI is to establish product tone and structure, even though real goal flows arrive in later phases.
- **D-07:** User-facing Angular starter content should be removed. A small amount of scaffolding can remain internally if it speeds implementation, but the shipped output should look like Milestory.
- **D-08:** The Phase 1 UI should let the user view a small Milestory-branded home screen with system or status information, not feature-complete goal management.
- **D-09:** Phase 1 endpoints should be fully auth-free in v1 personal mode. The runtime behavior should not require sign-in, tokens, or security workarounds.
- **D-10:** The current global bearer authentication requirement should be removed from the OpenAPI contract during Phase 1 so the spec matches actual product behavior.
- **D-11:** The frontend should not include auth UI or token handling in Phase 1. It should call the backend directly with no authentication layer in the user experience.
- **D-12:** Phase 1 API surface should stay narrow and foundation-oriented. Prefer only the endpoints needed to prove the stack works and support the small branded status experience.
- **D-13:** Phase 1 should perform a thorough cleanup of template leftovers across product-facing artifacts, metadata, docs, and configuration where they create confusion.
- **D-14:** Within that cleanup, documentation and repository hygiene are the highest priority cleanup target.
- **D-15:** README and related contributor-facing docs should be updated during Phase 1 so the repository clearly reads as Milestory rather than a generic template.
- **D-16:** Java package naming is not fully locked by this discussion. `com.ybritto.milestory` should be revisited during planning as part of broader cleanup, rather than being treated as permanently correct or permanently out of scope.

### Claude's Discretion
- Whether a minimal backend-side app ownership abstraction is needed purely for code organization, as long as it stays invisible to the user and does not create auth-first complexity
- The exact layout, wording, and visual treatment of the small Phase 1 Milestory home screen
- The precise set of foundation/status endpoints, as long as they remain narrow and support the Phase 1 boundary
- The depth and sequencing of package/config cleanup work needed to reduce confusion without turning the phase into rename-only work

### Deferred Ideas (OUT OF SCOPE)
- Authentication, token handling, and account migration behavior belong to later phases and should not be designed deeply in Phase 1
- Real goal creation, checkpoint planning, progress tracking, and dashboard drill-downs remain in Phases 2 through 4
- Any broader product capabilities beyond a small branded home/status experience are out of scope for this phase

## Phase Requirements

| ID | Description | Research Support |
|----|-------------|------------------|
| PLAT-01 | User can use the application in personal single-user mode without authentication | Remove global OpenAPI bearer auth, regenerate backend/frontend clients, keep Spring runtime auth-free, expose only narrow status endpoints, and remove frontend token handling entirely |
| PLAT-02 | User can rely on backend-owned domain logic for planning, progress, and dashboard status calculations | Establish DDD/hexagonal backend boundaries now, keep OpenAPI DTOs out of domain code, add backend application services and ports, and keep frontend limited to rendering status projections from backend |

## Summary

Phase 1 should be planned as a foundation hardening phase, not a feature phase. The repo already has the right top-level shape: multi-module Maven, contract-first API generation, Spring Boot backend, Angular frontend, PostgreSQL, and Liquibase wiring. The real work is turning placeholder scaffolding into a coherent Milestory skeleton that can support later goal, progress, and dashboard phases without needing a second architectural rewrite.

For PLAT-01, the critical move is to make the contract and runtime honestly auth-free. Today the OpenAPI root still declares global `bearerAuth`, and the generated Angular client still injects bearer credentials into requests. That must be removed at the contract root first, then regenerated so the frontend and backend stop pretending authentication exists. The UI should stay narrow: one Milestory-branded home/status surface backed by one narrow backend status endpoint or a similarly small app-summary surface.

For PLAT-02, the key planning decision is to establish backend boundaries before real business rules arrive. The backend currently has only the bootstrap app class, so this is the cheapest moment to create `domain`, `application`, and `infrastructure` separation, keep generated OpenAPI code at the edge, and reserve the backend for future planning/progress/dashboard calculations. Liquibase should provide the real database baseline from day one rather than relying on ad hoc schema creation or placeholder startup behavior.

**Primary recommendation:** Plan Phase 1 around four linked outcomes: auth-free contract cleanup, real Liquibase baseline, DDD/hexagonal backend skeleton, and a tiny Milestory status UI/API that proves the full stack without introducing fake auth or premature domain complexity.

## Standard Stack

### Core

| Library | Version | Purpose | Why Standard |
|---------|---------|---------|--------------|
| Java | 25 | Backend language/runtime | Already pinned in the repo root and backend build; aligns with current scaffold |
| Spring Boot | 4.0.4 | Backend runtime, DI, config, test support | Already the project parent; current stable repo baseline |
| Spring Data JPA | Managed by Spring Boot 4.0.4 BOM | Persistence adapter layer | Standard Spring persistence integration for repository adapters |
| Liquibase | Managed by Spring Boot 4.0.4 BOM | Database schema migration | Standard migration tool already wired in `application.yaml`; do not hand-roll schema bootstrap |
| PostgreSQL | 18 (`compose.yaml`) | Local relational datastore | Matches existing local dev path and later analytical/query needs |
| OpenAPI | 3.1.2 | Contract-first API definition | Already the authoritative cross-module contract |
| openapi-processor-maven-plugin | 2026.1 | Generate Spring interfaces from OpenAPI | Keeps HTTP contract and backend adapter boundary aligned |
| openapi-processor-spring | 2026.2 | Spring adapter generation for backend | Current repo-pinned plugin dependency |
| Angular | 21.2.x | Frontend application shell | Existing frontend major version and standalone-first architecture |
| `@openapitools/openapi-generator-cli` | 2.31.0 | Generate Angular API client from OpenAPI | Existing client generation workflow already in use |

### Supporting

| Library | Version | Purpose | When to Use |
|---------|---------|---------|-------------|
| RxJS | 7.8.x | Async stream support in Angular client/services | For API interaction and async composition only; keep business rules out of streams |
| TypeScript | 5.9.x in repo, 6.0.2 current registry | Angular compile target | Keep repo-pinned version unless a phase task explicitly upgrades tooling |
| Vitest | 4.0.8 in repo, 4.1.2 current registry | Frontend unit testing | Use for component/store/facade tests in Phase 1 |
| Spring Boot Test | Managed by Spring Boot 4.0.4 BOM | Backend integration and slice testing | Use for adapter/integration tests; keep domain tests Spring-free |
| Lombok | Managed by Spring Boot 4.0.4 BOM | Boilerplate reduction | Use selectively in infrastructure and DTO-adjacent code, not as a substitute for domain design |

### Alternatives Considered

| Instead of | Could Use | Tradeoff |
|------------|-----------|----------|
| Auth-free OpenAPI root | Keep bearer auth and bypass it in code | Bad fit: generated clients and docs stay wrong, and Phase 1 keeps fake auth complexity |
| Liquibase baseline | `schema.sql` or Hibernate auto-create | Faster short-term, but creates migration debt immediately |
| DDD/hexagonal boundaries now | Start with controller-service-entity CRUD | Faster for one endpoint, slower for every later planning/progress phase |
| Narrow status surface | Expose goal/dashboard placeholder resources now | Looks fuller, but violates the locked phase boundary and invites fake behavior |

**Installation:**
```bash
mvn -q -pl milestory-backend test
cd milestory-frontend && npm install && npm run test:ci
cd milestory-frontend && npm run client-gen
```

**Version verification:**
- Verified via local registry queries on 2026-04-03:
  - `npm view @angular/core version` → `21.2.7`
  - `npm view @angular/cli version` → `21.2.6`
  - `npm view rxjs version` → `7.8.2`
  - `npm view @openapitools/openapi-generator-cli version` → `2.31.0`
  - `npm view vitest version` → `4.1.2`
- Repo remains intentionally pinned below some latest patch versions. Phase 1 should consume the existing pinned stack unless an upgrade is needed to fix a concrete blocker.

## Architecture Patterns

### Recommended Project Structure

```text
milestory-api/rest/
├── api-v1.yaml                 # Contract root; no global auth in Phase 1
├── paths/
│   └── app-status.yaml         # Narrow Phase 1 status endpoint(s)
└── schemas/
    └── app-status.yaml         # DTOs only, not domain model

milestory-backend/src/main/java/com/ybritto/milestory/
├── domain/
│   └── appstatus/              # Pure domain concepts/value objects
├── application/
│   └── appstatus/              # Use cases and ports
├── infrastructure/
│   ├── persistence/            # JPA/repository adapters
│   ├── web/                    # Controller/adapters that implement generated interfaces
│   └── config/                 # Spring config and wiring
└── MilestoryBackendApplication.java

milestory-backend/src/main/resources/
└── db/changelog/
    ├── db.changelog-master.yaml
    └── changes/
        └── 001-foundation-baseline.yaml

milestory-frontend/src/app/
├── core/
│   ├── api/                    # Generated API wrappers/facades only
│   └── shell/                  # App-wide providers/config
├── features/home/
│   ├── home.page.ts
│   ├── home.store.ts
│   └── home.spec.ts
└── app.config.ts
```

### Pattern 1: Contract-First, Regenerate Before Implementing
**What:** Treat `milestory-api/rest/api-v1.yaml` as the only source of truth for Phase 1 HTTP behavior. Update the contract first, then regenerate backend interfaces and frontend client, then implement adapters.
**When to use:** Every time the Phase 1 status endpoint or schema changes.
**Why it matters here:** The current spec still declares global bearer auth, and the generated Angular client currently adds bearer credentials. Contract cleanup must happen before backend or frontend implementation.

### Pattern 2: Keep Domain Logic Behind Application Services
**What:** Domain objects and policies stay in `domain`, orchestration stays in `application`, and Spring/JPA/OpenAPI code stays in `infrastructure`.
**When to use:** For any logic that will later evolve into planning, progress, or dashboard calculations.
**Why it matters here:** PLAT-02 is a platform requirement. Phase 1 should create the seam where later business rules will live, even if the first use case is only app/status assembly.

### Pattern 3: Use a Narrow App Status Projection, Not a Fake Dashboard
**What:** Expose one small backend-owned projection that can drive a branded home screen, for example app name, environment, database readiness, migration state, and a short milestone message.
**When to use:** For the Phase 1 home screen only.
**Why it matters here:** It proves the full stack honestly without leaking future goal/dashboard semantics into Phase 1.

### Pattern 4: Baseline the Database with Liquibase Immediately
**What:** Add a real `db.changelog-master.yaml` and at least one baseline change set directory/file in Phase 1.
**When to use:** Before adding persistent domain entities.
**Why it matters here:** The backend config points to `classpath:/db/changelog/db.changelog-master.yaml`, but no such files exist in the repo today.

### Pattern 5: Frontend as Shell and Renderer, Not Rule Engine
**What:** The Angular app should fetch a backend projection, hold local UI state with signals, and avoid calculating business meaning itself.
**When to use:** For home/status presentation and all later Milestory features.
**Why it matters here:** This keeps PLAT-02 enforceable from the start.

### Anti-Patterns to Avoid
- **OpenAPI DTOs as domain objects:** Generated records/interfaces are transport contracts, not domain models.
- **Fake auth workaround:** Do not keep `bearerAuth` and just pass a dummy token. Remove auth from the spec and runtime entirely.
- **Entity-first package design:** Do not start with a flat `entity/repository/controller` structure and promise to clean it later.
- **Status logic in Angular templates or services:** No frontend-owned status interpretation beyond display formatting.
- **Migrationless persistence:** Do not rely on Hibernate DDL or manual SQL scripts for the baseline.
- **Broad cleanup spree:** Phase 1 should clean confusing leftovers that affect product identity and scaffolding, not turn into a repository-wide rename marathon.

## Don't Hand-Roll

| Problem | Don't Build | Use Instead | Why |
|---------|-------------|-------------|-----|
| Database bootstrap | Manual startup SQL or Hibernate schema generation | Liquibase changelog baseline | Schema history, repeatability, and later migrations matter immediately |
| Backend HTTP contract implementation | Hand-written controllers detached from spec | `openapi-processor` generated interfaces plus thin adapters | Prevents contract drift and clarifies the edge boundary |
| Frontend API client | Custom `fetch` wrappers for Phase 1 endpoints | Generated Angular client plus a small facade/store | Keeps contract changes cheap and removes duplicated HTTP typing |
| Auth-free mode | Fake JWTs, hardcoded bearer tokens, or disabled checks around auth middleware | No auth in the spec and no auth layer in runtime | Phase 1 behavior must be honestly auth-free |
| Backend-owned status calculations | UI-side derived business meaning | Application service assembling a status projection | PLAT-02 explicitly requires backend-owned logic |

**Key insight:** Phase 1 is mostly about using the existing scaffold correctly. The expensive mistakes here are custom shortcuts that bypass the tools already present in the repo.

## Common Pitfalls

### Pitfall 1: Removing auth only in Spring, not in the contract
**What goes wrong:** The backend accepts anonymous requests, but the OpenAPI root still declares `bearerAuth`, so generated clients and docs still require auth.
**Why it happens:** Teams patch runtime behavior first and forget that the contract is the real source of truth.
**How to avoid:** Remove global `security` and `bearerAuth` from `api-v1.yaml` before regenerating code.
**Warning signs:** Generated Angular client still contains `addCredentialToHeaders('bearerAuth', ...)`.

### Pitfall 2: Liquibase configuration points to a file tree that does not exist
**What goes wrong:** Startup behavior becomes ambiguous, baseline schema is not explicitly managed, and later migrations begin from an unclear state.
**Why it happens:** Liquibase is configured in `application.yaml`, but `db/changelog` files are missing from the repo.
**How to avoid:** Create the master changelog and a `changes/` baseline in Phase 1, then verify startup against a clean database.
**Warning signs:** No `db/changelog` files in source control, no baseline migration plan in tasks.

### Pitfall 3: The backend `local` profile overrides the database URL with an empty DB name default
**What goes wrong:** Local boot depends on env state or on database defaults rather than explicit configuration.
**Why it happens:** `application-local.yaml` currently uses `${MILESTORY_DB_NAME:}` instead of a non-empty fallback.
**How to avoid:** Normalize datasource defaults during Phase 1 and verify local/test profiles explicitly.
**Warning signs:** Startup connects to `jdbc:postgresql://localhost:5432/` without an explicit database name.

### Pitfall 4: Starter content survives behind a branded heading
**What goes wrong:** The app technically “looks less template-like” but still ships Angular demo structure, styles, and copy.
**Why it happens:** Teams edit the title string and leave the generated shell mostly intact.
**How to avoid:** Replace the starter template wholesale and create a real home/status feature area.
**Warning signs:** `app.html` still contains Angular starter comments, demo links, or inline starter styles.

### Pitfall 5: DDD layering gets postponed until “real features”
**What goes wrong:** The first real goal/progress work lands in controllers or JPA entities and Phase 2 becomes a refactor.
**Why it happens:** Phase 1 seems too small to justify boundaries.
**How to avoid:** Create the package boundaries and one real application service in Phase 1.
**Warning signs:** New backend code lands only under a flat package or only inside controller classes.

### Pitfall 6: Template cleanup turns into a rename-only phase
**What goes wrong:** Time is spent sweeping every placeholder while core platform gaps remain.
**Why it happens:** Template leftovers are visible and easy to chase.
**How to avoid:** Prioritize product-facing docs, metadata, config, generated package names, and obviously confusing leftovers tied to Phase 1 deliverables.
**Warning signs:** Cleanup tasks outnumber contract, migration, and boundary-establishing tasks.

## Code Examples

Verified patterns from official sources:

### Auth-Free Angular HTTP Setup
```typescript
import { ApplicationConfig } from '@angular/core';
import { provideHttpClient } from '@angular/common/http';
import { provideRouter } from '@angular/router';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(),
  ],
};
```
Source: https://angular.dev/guide/http/setup

### Liquibase Master Changelog
```yaml
databaseChangeLog:
  - include:
      file: db/changelog/changes/001-foundation-baseline.yaml
```
Source: https://docs.liquibase.com/reference-guide/changelog-attributes/include

### Spring Boot Full-Context Test Baseline
```java
@SpringBootTest
class MilestoryBackendApplicationTests {

    @Test
    void contextLoads() {
    }
}
```
Source: https://docs.spring.io/spring-boot/reference/testing/spring-boot-applications.html

### openapi-processor Mapping Baseline
```yaml
openapi-processor-mapping: v17

options:
  package-name: com.ybritto.milestory.generated
  model-type: record
```
Source: https://openapiprocessor.io/spring/2025.3/processor/configuration.html

## State of the Art

| Old Approach | Current Approach | When Changed | Impact |
|--------------|------------------|--------------|--------|
| Angular NgModules as the default app model | Standalone application config with provider functions | Angular 15+, now standard in Angular 21 docs | Phase 1 should add `provideHttpClient()` in `app.config.ts`, not reintroduce NgModules |
| Controller-first HTTP contracts | Contract-first OpenAPI with generated backend/frontend code | Already established in this repo | Plan tasks around spec update → generation → implementation |
| Schema bootstrap via Hibernate or raw SQL | Liquibase-managed baseline and migrations | Longstanding standard practice; already wired in repo | Phase 1 should create the real changelog tree immediately |
| Auth scaffold everywhere by default | Explicitly auth-free v1 personal mode | Locked by current product direction on 2026-04-03 | Remove contract/runtime auth assumptions now, not later |

**Deprecated/outdated:**
- Angular starter shell as shipped app UI: outdated for this repo; replace entirely with Milestory-specific shell.
- Global OpenAPI bearer auth for Phase 1: outdated relative to the locked auth-free product boundary.
- Flat bootstrap-only backend package: insufficient for PLAT-02 once domain logic arrives.

## Open Questions

1. **Should Phase 1 keep `com.ybritto.milestory` or rename package roots now?**
   - What we know: The context explicitly says package naming is not fully locked, and template leftovers still matter.
   - What's unclear: Whether package renaming is worth Phase 1 scope versus focusing on docs/config/product-facing cleanup first.
   - Recommendation: Treat package-root cleanup as a scoped planning decision, not an automatic sweep. If changed, update `openapi-processor-mapping.yaml` and generated package expectations in the same wave.

2. **What is the exact narrow status endpoint surface?**
   - What we know: Phase 1 should stay narrow and support a branded home/status experience only.
   - What's unclear: Whether one `GET /api/v1/app/status` endpoint is enough or whether a second small health/info endpoint is justified.
   - Recommendation: Default to one endpoint returning a single backend-owned status projection unless a second endpoint removes real coupling.

3. **How much template cleanup belongs in Phase 1?**
   - What we know: Docs/repo hygiene are the highest-priority cleanup targets.
   - What's unclear: How far to go into package/module/config naming cleanup without turning the phase into rename-only work.
   - Recommendation: Plan cleanup tasks only where confusion directly affects current development, generated code, visible UX, docs, or configuration behavior.

## Validation Architecture

### Test Framework

| Property | Value |
|----------|-------|
| Framework | JUnit 5 + Spring Boot Test; Vitest 4.x via Angular builder |
| Config file | none explicit for backend; Angular test builder in [angular.json](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/angular.json) |
| Quick run command | `mvn -q -pl milestory-backend test` |
| Full suite command | `mvn -q test` and `cd milestory-frontend && npm run test:ci` |

### Phase Requirements → Test Map

| Req ID | Behavior | Test Type | Automated Command | File Exists? |
|--------|----------|-----------|-------------------|-------------|
| PLAT-01 | Anonymous request to the Phase 1 status endpoint succeeds without auth headers | backend integration | `mvn -q -pl milestory-backend -Dtest=PersonalModeStatusIntegrationTest test` | ❌ Wave 0 |
| PLAT-01 | Generated frontend client calls backend without bearer auth plumbing | frontend unit | `cd milestory-frontend && npm run test:ci` | ❌ Wave 0 |
| PLAT-01 | Branded home/status page renders real backend-backed status and no auth UI | frontend component/integration | `cd milestory-frontend && npm run test:ci` | ❌ Wave 0 |
| PLAT-02 | Status projection assembly stays in backend application/domain code | backend unit | `mvn -q -pl milestory-backend -Dtest=AppStatusServiceTest test` | ❌ Wave 0 |
| PLAT-02 | Domain logic remains independent from Spring/JPA/OpenAPI transport types | backend unit/architecture | `mvn -q -pl milestory-backend -Dtest=DomainIsolationTest test` | ❌ Wave 0 |

### Sampling Rate
- **Per task commit:** `mvn -q -pl milestory-backend test` or `cd milestory-frontend && npm run test:ci`
- **Per wave merge:** `mvn -q test` plus frontend `npm run test:ci`
- **Phase gate:** Backend and frontend suites green, and regeneration (`npm run client-gen`) produces no unexpected drift

### Wave 0 Gaps
- [ ] `milestory-backend/src/test/java/.../PersonalModeStatusIntegrationTest.java` — proves auth-free endpoint behavior for PLAT-01
- [ ] `milestory-backend/src/test/java/.../AppStatusServiceTest.java` — proves backend-owned status assembly for PLAT-02
- [ ] `milestory-backend/src/test/java/.../DomainIsolationTest.java` or equivalent package-boundary coverage — protects DDD/hexagonal separation
- [ ] `milestory-frontend/src/app/features/home/home.spec.ts` — replaces starter title test with real Milestory home/status coverage
- [ ] `milestory-frontend/src/app/core/api/...spec.ts` or facade/store spec — verifies generated client usage without auth assumptions

## Sources

### Primary (HIGH confidence)
- Repository inspection on 2026-04-03:
  - [api-v1.yaml](/Users/ybritto/dev/Personal/Milestory/milestory-api/rest/api-v1.yaml)
  - [application.yaml](/Users/ybritto/dev/Personal/Milestory/milestory-backend/src/main/resources/application.yaml)
  - [application-local.yaml](/Users/ybritto/dev/Personal/Milestory/milestory-backend/src/main/resources/application-local.yaml)
  - [openapi-processor-mapping.yaml](/Users/ybritto/dev/Personal/Milestory/milestory-backend/src/main/resources/openapi-processor-mapping.yaml)
  - [app.ts](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/app.ts)
  - [app.html](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/app.html)
  - [package.json](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/package.json)
- Spring Boot reference: https://docs.spring.io/spring-boot/reference/testing/spring-boot-applications.html
- Spring Boot database initialization / Liquibase docs: https://docs.spring.io/spring-boot/how-to/data-initialization.html
- Spring Boot 4.0.4 release reference: https://spring.io/blog/2026/03/19/spring-boot-4-0-4-available-now
- Angular HTTP setup: https://angular.dev/guide/http/setup
- Angular routing guide: https://angular.dev/guide/routing/common-router-tasks
- Liquibase changelog docs: https://docs.liquibase.com/reference-guide/changelog-format/what-is-a-changelog
- Liquibase `include`: https://docs.liquibase.com/reference-guide/changelog-attributes/include
- Liquibase `includeAll`: https://docs.liquibase.com/reference-guide/changelog-attributes/includeall
- openapi-processor Spring configuration: https://openapiprocessor.io/spring/2025.3/processor/configuration.html
- npm registry verification via local commands on 2026-04-03:
  - `npm view @angular/core version`
  - `npm view @angular/cli version`
  - `npm view rxjs version`
  - `npm view @openapitools/openapi-generator-cli version`
  - `npm view vitest version`

### Secondary (MEDIUM confidence)
- Live backend/frontend test runs in the current repo on 2026-04-03:
  - `mvn -q -pl milestory-backend test`
  - `cd milestory-frontend && npm run test:ci`

### Tertiary (LOW confidence)
- None

## Metadata

**Confidence breakdown:**
- Standard stack: HIGH - mostly verified from the repo plus official docs/registry queries
- Architecture: MEDIUM - recommended boundaries are strongly supported by project constraints and stack conventions, but exact package naming and endpoint shape remain discretionary
- Pitfalls: HIGH - directly verified from current repo state (`bearerAuth`, missing changelog tree, starter UI, local DB URL override)

**Research date:** 2026-04-03
**Valid until:** 2026-05-03
