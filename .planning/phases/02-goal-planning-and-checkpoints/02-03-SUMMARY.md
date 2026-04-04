---
phase: 02-goal-planning-and-checkpoints
plan: 03
subsystem: ui
tags: [angular, signals, reactive-forms, lazy-routes, goal-planning]
requires:
  - phase: 02-goal-planning-and-checkpoints
    provides: backend goal lifecycle API and generated frontend client surface
provides:
  - guided goal draft and checkpoint review flow
  - saved-goal detail and archive/restore experience
  - signal-driven frontend store over the generated goal-planning API client
affects: [milestory-frontend, phase-02]
tech-stack:
  added: []
  patterns: [backend-driven planning store, guided multi-step goal flow, lazy-loaded goal route tree]
key-files:
  created: [milestory-frontend/src/app/features/goals/goal.routes.ts, milestory-frontend/src/app/features/goals/shared/goal-planning.store.ts, milestory-frontend/src/app/features/goals/create/goal-create.page.ts, milestory-frontend/src/app/features/goals/plan-review/goal-plan-review.page.ts, milestory-frontend/src/app/features/goals/detail/goal-detail.page.ts, milestory-frontend/src/app/features/goals/archive/goal-archive.page.ts, milestory-frontend/src/app/features/goals/shared/goal-planning.store.spec.ts, milestory-frontend/src/app/features/goals/create/goal-create.page.spec.ts, milestory-frontend/src/app/features/goals/plan-review/goal-plan-review.page.spec.ts, milestory-frontend/src/app/features/goals/detail/goal-detail.page.spec.ts, milestory-frontend/src/app/features/goals/archive/goal-archive.page.spec.ts]
  modified: [milestory-frontend/src/app/app.routes.ts, milestory-frontend/src/styles.scss]
key-decisions:
  - "Reuse the guided create/edit flow for updates so goal editing stays consistent with the initial planning experience."
  - "Keep the frontend store backend-driven: previews, details, archive state, and restore outcomes all come from the API instead of local planning math."
  - "Preserve the warm Milestory visual tone while extending it for form-heavy routes instead of switching to admin-table styling."
patterns-established:
  - "Goal flows live under a lazy-loaded `/goals` route tree with focused standalone pages."
  - "Signal-backed stores coordinate generated API clients and page state for multi-step flows."
requirements-completed: [GOAL-01, GOAL-02, GOAL-03, GOAL-04, PLAN-01, PLAN-02, PLAN-03]
duration: 14 min
completed: 2026-04-04
---

# Phase 02 Plan 03: Guided goal planning frontend

**Milestory now ships a backend-driven goal planning experience with guided draft, checkpoint review, saved-goal detail, and archive flows.**

## Performance

- **Duration:** 14 min
- **Started:** 2026-04-04T08:49:45Z
- **Completed:** 2026-04-04T09:01:35Z
- **Tasks:** 2
- **Files modified:** 21

## Accomplishments
- Added a lazy-loaded `/goals` route tree with create, review, detail, edit, and archive entry points.
- Implemented a signal-driven `GoalPlanningStore` that uses the generated OpenAPI client for categories, previews, create, update, archive, restore, and list operations.
- Built standalone Angular pages for the goal draft, checkpoint review, saved goal detail, and archive/restore flow while keeping the backend as the source of truth.
- Extended the shared Milestory styling so the new planning routes feel product-native instead of generic CRUD UI.

## Task Commits

1. **Plan 02-03 frontend implementation** - `632234d` (feat)

## Files Created/Modified
- `milestory-frontend/src/app/app.routes.ts` and `milestory-frontend/src/app/features/goals/goal.routes.ts` - Add the lazy-loaded goals route tree and guided edit path.
- `milestory-frontend/src/app/features/goals/shared/goal-planning.store.ts` - Coordinates the generated API client and frontend planning state with signals.
- `milestory-frontend/src/app/features/goals/create/*` - Implements the yearly goal draft step with starter/custom category selection.
- `milestory-frontend/src/app/features/goals/plan-review/*` - Implements editable checkpoint cards, customization detection, and final save.
- `milestory-frontend/src/app/features/goals/detail/*` and `archive/*` - Implement the saved-goal view plus read-only archive and restore actions.
- `milestory-frontend/src/styles.scss` - Extends the existing warm design system for form-heavy Phase 2 routes.

## Decisions Made
- Reused the guided create/edit flow so updates do not fork into a separate CRUD screen.
- Kept backend responses authoritative for preview, detail, archive, and restore state instead of adding frontend planning helpers.
- Moved the archive route ahead of `:goalId` in the route tree so `/goals/archive` cannot be captured as a goal identifier.

## Deviations from Plan

### Auto-fixed Issues

**1. [Rule 1 - Routing bug] Reordered the archive route ahead of the detail route**
- **Found during:** Final frontend review
- **Issue:** `/goals/archive` would have matched the generic `:goalId` route first, making the archive page unreachable.
- **Fix:** Moved the `archive` route above `:goalId` in `goal.routes.ts`.
- **Files modified:** `milestory-frontend/src/app/features/goals/goal.routes.ts`
- **Verification:** `cd milestory-frontend && npm run test:ci`
- **Committed in:** `632234d`

---

**Total deviations:** 1 auto-fixed (1 routing bug)
**Impact on plan:** The auto-fix prevented a real archive navigation regression without changing the planned product scope.

## Issues Encountered
- A concurrent frontend test run briefly failed while `npm run client-gen` was deleting and recreating `src/api`; rerunning the suite sequentially resolved it with no code changes.

## User Setup Required
None - no external service configuration required.

## Next Phase Readiness
- Phase 2 is complete and ready for end-to-end user verification.
- Phase 3 can now build on real goal creation, editing, archive, and restore flows instead of placeholders.

## Self-Check: PASSED

---
*Phase: 02-goal-planning-and-checkpoints*
*Completed: 2026-04-04*
