---
phase: 01-foundation-and-personal-mode
plan: 01
subsystem: api
tags: [openapi, angular, client-generation, docs, phase-1]
requires: []
provides:
  - auth-free `/api/v1/status` contract for the Milestory foundation screen
  - Milestory-specific Phase 1 repository guidance and generation metadata
  - regenerated Angular API client without bearer-auth endpoint wiring
affects: [milestory-api, milestory-backend, milestory-frontend, phase-01]
tech-stack:
  added: []
  patterns: [contract-first cleanup before implementation, auth-free phase-1 API surface, committed generated client artifacts]
key-files:
  created: [.planning/phases/01-foundation-and-personal-mode/01-01-SUMMARY.md, milestory-api/rest/paths/status.yaml, milestory-api/rest/schemas/status.yaml, milestory-frontend/src/api/api/foundationStatus.service.ts, milestory-frontend/src/api/model/getFoundationStatus200Response.ts, milestory-frontend/src/api/model/getFoundationStatus503Response.ts]
  modified: [README.md, milestory-api/rest/api-v1.yaml, milestory-backend/src/main/resources/openapi-processor-mapping.yaml, milestory-frontend/package.json, milestory-frontend/src/api/api/api.ts, milestory-frontend/src/api/model/models.ts]
key-decisions:
  - "Phase 1 exposes only `/api/v1/status` and removes global bearer auth from the OpenAPI root."
  - "The Java package root `com.ybritto.milestory` remains intentional for Phase 1 and is called out explicitly in the README."
  - "Generated frontend client artifacts were committed after regeneration so downstream work starts from the cleaned contract."
patterns-established:
  - "API-first Phase 1 changes must update the OpenAPI root, regenerate the Angular client, and commit the generated output together."
  - "Product-facing docs should describe Milestory and the auth-free personal scope directly instead of template/bootstrap framing."
requirements-completed: [PLAT-01]
duration: 12min
completed: 2026-04-03
---

# Phase 01 Plan 01: Auth-free status contract, Milestory Phase 1 docs, and regenerated frontend client

**Auth-free `/api/v1/status` contract with Milestory-branded Phase 1 docs and a regenerated Angular client aligned to the cleaned API surface**

## Performance

- **Duration:** 12 min
- **Started:** 2026-04-03T22:00:00Z
- **Completed:** 2026-04-03T22:12:26Z
- **Tasks:** 2
- **Files modified:** 13

## Accomplishments
- Replaced the scaffold hello contract with a single auth-free `/api/v1/status` endpoint and Phase 1 status schemas.
- Rewrote the root README around Milestory, personal auth-free scope, the real module names, and the retained `com.ybritto.milestory` package root.
- Regenerated the Angular API client so the committed client surface now exposes `FoundationStatusService` instead of the hello endpoint and no longer references `bearerAuth`.

## Task Commits

Each task was committed atomically:

1. **Task 1: Replace the scaffold contract with an auth-free Phase 1 status surface** - `1881b5f` (feat)
2. **Task 2: Align generation and product-facing docs with Milestory Phase 1 reality** - `38e3481` (chore)

**Additional verification-driven commit:** `d986c81` (chore)

## Files Created/Modified
- `milestory-api/rest/api-v1.yaml` - Removes bearer auth, registers `/api/v1/status`, and normalizes the API title to `Milestory API`.
- `milestory-api/rest/paths/status.yaml` - Defines the tagged foundation status operation and `200`/`503` responses.
- `milestory-api/rest/schemas/status.yaml` - Defines `FoundationStatusResponse` and `FoundationStatusUnavailableResponse`.
- `README.md` - Reframes the repository as Milestory with Phase 1 auth-free scope and current workflow guidance.
- `milestory-backend/src/main/resources/openapi-processor-mapping.yaml` - Keeps generated models under `com.ybritto.milestory.generated` and removes unused path-level response mapping.
- `milestory-frontend/package.json` - Keeps the `client-gen` path and adds Milestory-specific package metadata.
- `milestory-frontend/src/api/` - Regenerated frontend client surface for the foundation status endpoint.

## Decisions Made
- Kept the API surface intentionally narrow with a single status endpoint to match the Phase 1 boundary and avoid fake future behavior.
- Treated `com.ybritto.milestory` as the correct current identity, not a placeholder mismatch, and documented that explicitly instead of broadening cleanup scope.
- Committed regenerated client files after verification because the repository tracks `src/api`, and leaving stale generated output would undermine downstream work.

## Deviations from Plan

### Auto-fixed Issues

**1. [Rule 1 - Product-facing inconsistency] Normalized the OpenAPI title and regenerated the client**
- **Found during:** Plan verification
- **Issue:** `milestory-api/rest/api-v1.yaml` still used `MileStory API`, which propagated that inconsistent title into generated frontend files.
- **Fix:** Updated the OpenAPI `info.title` to `Milestory API` and reran `npm run client-gen`.
- **Files modified:** `milestory-api/rest/api-v1.yaml`, `milestory-frontend/src/api/*`
- **Verification:** `npm run client-gen`; `rg -n "bearerAuth|Authorization|addCredentialToHeaders\\(\\s*'bearerAuth'" milestory-frontend/src/api` returned no matches
- **Committed in:** `d986c81`

---

**Total deviations:** 1 auto-fixed (1 bug/product-facing inconsistency)
**Impact on plan:** The fix kept the generated client consistent with Milestory branding and ensured committed generated output matched the cleaned contract.

## Issues Encountered
None

## User Setup Required
None - no external service configuration required.

## Next Phase Readiness
- Backend and frontend can now implement against a narrow auth-free status contract without inheriting bearer-auth assumptions from the scaffold.
- The committed frontend client already exposes the new status surface, so follow-on backend and UI work can build directly on generated types and services.

## Self-Check: PASSED

---
*Phase: 01-foundation-and-personal-mode*
*Completed: 2026-04-03*
