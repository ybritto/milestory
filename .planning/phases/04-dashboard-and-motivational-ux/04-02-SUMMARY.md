---
phase: 04-dashboard-and-motivational-ux
plan: 02
subsystem: ui
tags: [angular, routing, shell, accessibility, cdk]
requires:
  - phase: 04-01
    provides: dashboard page and presenter routed inside the shell
provides:
  - dashboard-first root route tree under a persistent Milestory shell
  - responsive sidebar and mobile drawer navigation with CDK-backed focus management
  - reserved /goals/active route with a lightweight placeholder page ahead of :goalId
affects: [dashboard, goals, routing, navigation]
tech-stack:
  added: [@angular/cdk]
  patterns: [shell-first child routing, CDK focus management for transient navigation surfaces]
key-files:
  created:
    - milestory-frontend/src/app/shell/app-shell.component.ts
    - milestory-frontend/src/app/features/goals/active/active-goals.page.ts
  modified:
    - milestory-frontend/src/app/app.routes.ts
    - milestory-frontend/src/app/features/goals/goal.routes.ts
    - milestory-frontend/src/app/app.spec.ts
key-decisions:
  - "Ship the frontend through AppShellComponent and redirect the shell root to dashboard instead of keeping HomePage as the default entry."
  - "Use Angular CDK focus primitives for the mobile drawer so focus capture and focus return stay accessible without custom trap logic."
  - "Reserve /goals/active ahead of :goalId with an intentional lightweight page until Plan 04-03 implements the fuller active-goals surface."
patterns-established:
  - "Shell routes own shipped entry points while feature routes stay nested under the root shell."
  - "Reserved literal goal subpaths must be declared before generic :goalId detail routes."
requirements-completed: [PLAT-03]
duration: 5 min
completed: 2026-04-05
---

# Phase 04 Plan 02: Dashboard Shell Summary

**Dashboard-first shell routing with an accessible mobile drawer and a reserved active-goals destination inside Milestory**

## Performance

- **Duration:** 5 min
- **Started:** 2026-04-05T23:10:00Z
- **Completed:** 2026-04-05T23:15:42Z
- **Tasks:** 1
- **Files modified:** 14

## Accomplishments
- Replaced the shipped root `HomePage` destination with a shell-first route tree that redirects `''` to `dashboard`.
- Added `AppShellComponent` with persistent desktop navigation plus a mobile drawer that traps focus and returns focus to the trigger on close.
- Reserved `/goals/active` ahead of `:goalId` and created a lightweight placeholder page so downstream active-goals work has a real route seam.

## Task Commits

Each task was committed atomically:

1. **Task 1: Add the shell-first route tree and responsive app shell** - `a9f3aeb` (feat)

## Files Created/Modified
- `milestory-frontend/src/app/shell/app-shell.component.ts` - standalone root shell with drawer state and focus management
- `milestory-frontend/src/app/shell/app-shell.component.html` - explicit dashboard, active-goals, archive, and new-goal navigation plus child outlet
- `milestory-frontend/src/app/shell/app-shell.component.scss` - responsive sidebar and mobile drawer styling
- `milestory-frontend/src/app/app.routes.ts` - shell-first child route tree with dashboard as the shipped default
- `milestory-frontend/src/app/features/goals/goal.routes.ts` - reserved `active` route entry before `:goalId`
- `milestory-frontend/src/app/features/goals/active/active-goals.page.ts` - lightweight placeholder page for the future active-goals surface
- `milestory-frontend/src/app/app.spec.ts` - root route and reserved-route assertions

## Decisions Made
- Switched the shipped entry route to a nested shell so future dashboard and goals flows inherit one persistent navigation frame.
- Kept the drawer accessibility implementation CDK-backed with `CdkTrapFocus` and `FocusMonitor` rather than bespoke key handling.
- Treated the active-goals page as an intentional route placeholder so Plan 04-03 can add density without revisiting route safety.

## Deviations from Plan

None - plan executed exactly as written.

## Issues Encountered

None

## User Setup Required

None - no external service configuration required.

## Known Stubs

- `milestory-frontend/src/app/features/goals/active/active-goals.page.html:5` keeps the route as an intentional placeholder until Plan 04-03 delivers the medium-density active-goals experience.

## Next Phase Readiness

- The app now boots into the Milestory shell with dashboard-first navigation, so Plan 04-03 can focus on active-goals content instead of root routing.
- `/goals/active` is safely reserved and test-covered, which prevents detail-route collisions during the next plan.

---
*Phase: 04-dashboard-and-motivational-ux*
*Completed: 2026-04-05*

## Self-Check: PASSED
