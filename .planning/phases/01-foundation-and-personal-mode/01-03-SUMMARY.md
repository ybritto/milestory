---
phase: 01-foundation-and-personal-mode
plan: 03
subsystem: ui
tags: [angular, signals, openapi, frontend, status]
requires:
  - phase: 01-01
    provides: auth-free contract cleanup and Milestory product framing
  - phase: 01-02
    provides: anonymous foundation status endpoint and generated status models
provides:
  - routed Milestory home screen at /
  - signal-based status store backed by generated API client
  - warm global theme tokens and frontend UI coverage
affects: [phase-02-goal-planning, phase-04-dashboard-ux, frontend-routing]
tech-stack:
  added: []
  patterns: [same-origin generated API provider, signal facade for backend-owned status, standalone lazy route]
key-files:
  created:
    - milestory-frontend/src/app/core/status/foundation-status.store.ts
    - milestory-frontend/src/app/features/home/home.page.ts
    - milestory-frontend/src/app/features/home/home.spec.ts
  modified:
    - milestory-frontend/src/app/app.config.ts
    - milestory-frontend/src/app/app.routes.ts
    - milestory-frontend/src/app/app.html
    - milestory-frontend/src/styles.scss
key-decisions:
  - "Use provideApi('') so the generated client stays same-origin and auth-free in local Phase 1 routing."
  - "Model the status card as explicit loading, ready, empty, and error signal states instead of leaking transport details into the template."
  - "Keep the Phase 1 shell to a single lazy route and semantic three-zone layout with no auth or future-feature scaffolding."
patterns-established:
  - "Frontend status data flows through a dedicated signal store that wraps generated OpenAPI services."
  - "App shell stays routed and minimal, with feature UI loaded through standalone lazy components."
requirements-completed: [PLAT-01, PLAT-02]
duration: 8 min
completed: 2026-04-03
---

# Phase 01 Plan 03: Foundation Home Screen Summary

**Milestory now ships a routed home screen that renders backend-owned foundation status through the generated auth-free Angular client.**

## Performance

- **Duration:** 8 min
- **Started:** 2026-04-03T22:29:05Z
- **Completed:** 2026-04-03T22:37:25Z
- **Tasks:** 2
- **Files modified:** 13

## Accomplishments

- Wired the Angular app shell to `provideHttpClient`, the generated OpenAPI client, and a lazy `/` route.
- Added a signal-based `FoundationStatusStore` with explicit `loading`, `ready`, `empty`, and `error` UI states plus retry behavior.
- Replaced the Angular starter experience with a Milestory-branded three-zone home page, global theme tokens, and passing frontend tests.

## Task Commits

Each task was committed atomically:

1. **Task 1: Wire the frontend app shell to the auth-free generated status client** - `eebba92` (feat)
2. **Task 2: Build the Milestory home page and remove all shipped starter content** - `79a9416` (feat)

## Files Created/Modified

- `milestory-frontend/src/app/app.config.ts` - Provides `HttpClient`, router, and same-origin generated API config.
- `milestory-frontend/src/app/app.routes.ts` - Defines the lazy-loaded `/` route for `HomePage`.
- `milestory-frontend/src/app/core/status/foundation-status.store.ts` - Encapsulates status loading and retry with signals.
- `milestory-frontend/src/app/features/home/home.page.ts` - Connects the home surface to the status store.
- `milestory-frontend/src/app/features/home/home.page.html` - Renders the three-zone Milestory layout and all four status states.
- `milestory-frontend/src/styles.scss` - Defines the Phase 1 typography and warm palette tokens.

## Decisions Made

- Used same-origin API configuration so the generated client calls `/api/v1/status` directly without auth plumbing.
- Kept all status interpretation inside a dedicated store so the template remains a thin renderer over backend facts.
- Preserved the Phase 1 information architecture as a single read-only home surface with restrained future-facing copy.

## Deviations from Plan

### Auto-fixed Issues

**1. [Rule 3 - Blocking] Adjusted the implementation to the real generated Angular client shape**
- **Found during:** Task 1
- **Issue:** The plan referenced `DefaultService` and `apiV1StatusGet`, but the regenerated client exposes `FoundationStatusService#getFoundationStatus`.
- **Fix:** Wired the store to `FoundationStatusService` and verified it through regenerated client output.
- **Files modified:** `milestory-frontend/src/app/core/status/foundation-status.store.ts`, `milestory-frontend/src/app/core/status/foundation-status.store.spec.ts`
- **Verification:** `cd milestory-frontend && npm run client-gen && npm run test:ci -- --include src/app/core/status/foundation-status.store.spec.ts`
- **Committed in:** `eebba92`

**2. [Rule 3 - Blocking] Updated tests to the repo's Vitest-based Angular runner and explicit template narrowing**
- **Found during:** Task 1
- **Issue:** Initial Task 1 verification failed because `jasmine` types were unavailable and Angular template type-checking would not narrow the view-state union directly.
- **Fix:** Rewrote specs to use `vitest` mocks and added computed accessors for ready/error template reads.
- **Files modified:** `milestory-frontend/src/app/core/status/foundation-status.store.spec.ts`, `milestory-frontend/src/app/features/home/home.page.ts`, `milestory-frontend/src/app/features/home/home.page.html`, `milestory-frontend/src/app/features/home/home.spec.ts`
- **Verification:** `cd milestory-frontend && npm run test:ci`
- **Committed in:** `eebba92`, `79a9416`

---

**Total deviations:** 2 auto-fixed (2 blocking)
**Impact on plan:** Both fixes were required to align the implementation with the generated client and the actual frontend test environment. No scope creep.

## Issues Encountered

- The initial `app.html` patch left part of the Angular starter template behind; it was replaced with a pure router host before final verification.

## User Setup Required

None - no external service configuration required.

## Next Phase Readiness

- Phase 2 can build goal-planning flows on top of an existing routed shell, shared theme tokens, and the established backend-owned frontend data pattern.
- The homepage is intentionally narrow and read-only, so later feature phases can expand routing without undoing any auth-first scaffolding.

## Self-Check: PASSED

- Verified summary exists at `.planning/phases/01-foundation-and-personal-mode/01-03-SUMMARY.md`.
- Verified task commits `eebba92` and `79a9416` exist in git history.

---
*Phase: 01-foundation-and-personal-mode*
*Completed: 2026-04-03*
