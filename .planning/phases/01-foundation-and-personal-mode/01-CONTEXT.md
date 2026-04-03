# Phase 1: Foundation And Personal Mode - Context

**Gathered:** 2026-04-03
**Status:** Ready for planning

<domain>
## Phase Boundary

Phase 1 turns the current scaffold into a real Milestory foundation. It establishes backend package boundaries, working Liquibase migrations, a contract/frontend generation flow that still works after cleanup, and a personal auth-free runtime mode. It does not add goal planning, progress tracking, dashboard features, or authentication flows from later phases.

</domain>

<decisions>
## Implementation Decisions

### Personal mode identity
- **D-01:** Personal mode should feel invisible to the user. The app should simply feel like "my app" rather than exposing a visible profile or account concept in Phase 1.
- **D-02:** Phase 1 should not introduce an owner model or user identity abstraction unless it is technically unavoidable. Prefer storing app data directly for v1 simplicity.
- **D-03:** Data migration into a future authenticated model is intentionally not locked in this phase. Planning should optimize for Phase 1 simplicity rather than designing migration mechanics now.
- **D-04:** The UI and copy should not explicitly frame the experience as "personal mode" unless that becomes necessary to explain behavior.

### Phase 1 visible experience
- **D-05:** The frontend should become a small but polished Milestory-branded home/dashboard placeholder rather than remaining Angular starter content.
- **D-06:** The main job of the Phase 1 UI is to establish product tone and structure, even though real goal flows arrive in later phases.
- **D-07:** User-facing Angular starter content should be removed. A small amount of scaffolding can remain internally if it speeds implementation, but the shipped output should look like Milestory.
- **D-08:** The Phase 1 UI should let the user view a small Milestory-branded home screen with system or status information, not feature-complete goal management.

### Unauthenticated API behavior
- **D-09:** Phase 1 endpoints should be fully auth-free in v1 personal mode. The runtime behavior should not require sign-in, tokens, or security workarounds.
- **D-10:** The current global bearer authentication requirement should be removed from the OpenAPI contract during Phase 1 so the spec matches actual product behavior.
- **D-11:** The frontend should not include auth UI or token handling in Phase 1. It should call the backend directly with no authentication layer in the user experience.
- **D-12:** Phase 1 API surface should stay narrow and foundation-oriented. Prefer only the endpoints needed to prove the stack works and support the small branded status experience.

### Template-leftover cleanup scope
- **D-13:** Phase 1 should perform a thorough cleanup of template leftovers across product-facing artifacts, metadata, docs, and configuration where they create confusion.
- **D-14:** Within that cleanup, documentation and repository hygiene are the highest priority cleanup target.
- **D-15:** README and related contributor-facing docs should be updated during Phase 1 so the repository clearly reads as Milestory rather than a generic template.
- **D-16:** Java package naming is not fully locked by this discussion. `com.ybritto.milestory` should be revisited during planning as part of broader cleanup, rather than being treated as permanently correct or permanently out of scope.

### the agent's Discretion
- Whether a minimal backend-side app ownership abstraction is needed purely for code organization, as long as it stays invisible to the user and does not create auth-first complexity
- The exact layout, wording, and visual treatment of the small Phase 1 Milestory home screen
- The precise set of foundation/status endpoints, as long as they remain narrow and support the Phase 1 boundary
- The depth and sequencing of package/config cleanup work needed to reduce confusion without turning the phase into rename-only work

</decisions>

<specifics>
## Specific Ideas

- The app should feel like a real Milestory product early, not like an Angular starter or admin scaffold.
- Phase 1 should avoid pretending future auth exists when it does not; contract and UI should reflect the auth-free reality cleanly.
- Product tone matters in Phase 1, but scope should stay honest: a branded home/status experience is preferred over prematurely exposing later goal features.

</specifics>

<canonical_refs>
## Canonical References

**Downstream agents MUST read these before planning or implementing.**

### Phase definition
- `.planning/ROADMAP.md` — Phase 1 goal, requirements, and success criteria
- `.planning/REQUIREMENTS.md` — PLAT-01 and PLAT-02 plus v1/v2 scope boundaries
- `.planning/PROJECT.md` — core value, architectural constraints, and product direction
- `.planning/STATE.md` — current project status and sequencing notes

### Repository instructions
- `AGENTS.md` — repository-wide template/bootstrap guidance and known leftover concerns
- `milestory-backend/AGENTS.md` — DDD and hexagonal architecture expectations for backend work
- `milestory-frontend/AGENTS.md` — Angular, accessibility, and frontend architecture requirements
- `milestory-api/AGENTS.md` — OpenAPI and REST contract conventions

</canonical_refs>

<code_context>
## Existing Code Insights

### Reusable Assets
- `milestory-api/rest/api-v1.yaml` — existing contract root that can be simplified for auth-free Phase 1 behavior
- `milestory-frontend/src/api/` — committed generated API client, which gives Phase 1 a working frontend integration point once the contract is updated
- `compose.yaml` — local PostgreSQL service already exists for database-backed foundation work
- `init-template.sh` — repository already contains automation patterns for broad naming cleanup if planning decides to reuse or adapt them

### Established Patterns
- Contract-first flow is already the strongest cross-module pattern: API YAML feeds backend interface generation and frontend client generation
- Backend currently has almost no application code, so Phase 1 can establish domain/application/infrastructure boundaries cleanly instead of refactoring mature code
- Frontend currently has only the root shell, so Phase 1 can replace the starter output without worrying about preserving existing feature flows

### Integration Points
- `milestory-api/rest/api-v1.yaml` and `milestory-api/rest/paths/hello.yaml` — current API surface and the place to remove the premature global bearer auth requirement
- `milestory-backend/src/main/resources/application.yaml` and `milestory-backend/src/test/resources/application-test.yaml` — Liquibase is configured here but points to a missing changelog tree
- `milestory-backend/src/main/java/com/ybritto/milestory/` — current backend root where Phase 1 will establish real package boundaries
- `milestory-frontend/src/app/app.ts`, `milestory-frontend/src/app/app.html`, and `milestory-frontend/src/app/app.routes.ts` — current Angular starter shell that Phase 1 should replace with a Milestory-branded status experience
- `README.md` and other template-facing docs — high-value cleanup targets because they still describe the repository primarily as a reusable template

</code_context>

<deferred>
## Deferred Ideas

- Authentication, token handling, and account migration behavior belong to later phases and should not be designed deeply in Phase 1
- Real goal creation, checkpoint planning, progress tracking, and dashboard drill-downs remain in Phases 2 through 4
- Any broader product capabilities beyond a small branded home/status experience are out of scope for this phase

</deferred>

---

*Phase: 01-foundation-and-personal-mode*
*Context gathered: 2026-04-03*
