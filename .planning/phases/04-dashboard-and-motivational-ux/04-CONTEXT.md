# Phase 4: Dashboard And Motivational UX - Context

## Overview

- Phase: `04-dashboard-and-motivational-ux`
- Gathered: `2026-04-06`
- Status: `Ready for planning`

## Domain

Phase 4 turns the working tracking core into a polished Milestory experience centered on a strong dashboard and a real application shell. The dashboard becomes the primary product entry point for active use, summarizing goal health, surfacing what needs attention, celebrating meaningful accomplishment bands, and routing the user into goal detail for the next concrete action.

This phase should build on the backend-driven progress and pace model from Phase 3 rather than introducing frontend-owned status logic. It should stay focused on navigation, dashboard experience, prioritization, and motivational presentation, without expanding into authentication, multi-user concerns, or broad editing workflows that belong in later phases.

## Decisions

### Navigation Shell

- The app should adopt a persistent application shell with sidebar navigation across dashboard, active goals, archive, and goal detail.
- The sidebar should include `Dashboard`, `Active goals`, `Archive`, and a strong `New goal` action.
- The current home page should stop being the primary shipped destination once the dashboard shell exists.
- The sidebar should stay visible on desktop and collapse into a drawer on mobile and tablet.

### Dashboard Information Architecture

- The dashboard should lead with a balanced overview rather than purely risk-first or celebration-first framing.
- The top section should act as a practical command center showing counts, the strongest risk, the strongest win, and a suggested next goal to open.
- Goal summaries should then be grouped into sections such as `Behind`, `On pace`, and `Ahead`.
- Within each group, the most urgent goals should appear first.
- Strongest risk and strongest win callouts should route the user into goal detail for the next concrete action.

### Motivational Presentation

- Accomplishment bands should feel bold and highly celebratory rather than subtle.
- Celebration should stay tied to meaningful progress milestones like the roadmap-defined accomplishment thresholds, not shallow gamification.

### Card Density

- Dashboard cards should use a medium-density summary model.
- Each card should include concise status context plus `actual vs expected`, `next checkpoint`, and the strongest accomplishment or risk signal.
- The dashboard should remain a decision surface, not a stacked set of mini detail pages.

### Agent Discretion

The implementation can still choose the exact visual treatment, motion, copy tone, iconography, empty states, loading states, and responsive layout details as long as those choices preserve the decisions above and the product direction already established in prior phases.

## Specifics

- Phase 3 UAT exposed navigation friction because validating flows required direct route entry and too much manual page access.
- The dashboard should feel like the real product surface rather than a placeholder landing screen.
- Goal detail remains the primary action surface for doing work on a single goal.
- The dashboard should help users understand where to go next quickly, without taking on dense edit-in-place interactions.

## Canonical References

- `/Users/ybritto/dev/Personal/Milestory/.planning/ROADMAP.md`
- `/Users/ybritto/dev/Personal/Milestory/.planning/REQUIREMENTS.md`
- `/Users/ybritto/dev/Personal/Milestory/.planning/PROJECT.md`
- `/Users/ybritto/dev/Personal/Milestory/.planning/STATE.md`
- `/Users/ybritto/dev/Personal/Milestory/.planning/phases/01-foundation-and-personal-mode/01-CONTEXT.md`
- `/Users/ybritto/dev/Personal/Milestory/.planning/phases/02-goal-planning-and-checkpoints/02-CONTEXT.md`
- `/Users/ybritto/dev/Personal/Milestory/.planning/phases/03-progress-engine-and-status/03-CONTEXT.md`
- `/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/app.routes.ts`
- `/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/app.html`
- `/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/home/home.page.ts`
- `/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/home/home.page.scss`
- `/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/goals/detail/goal-detail.page.ts`
- `/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/goals/detail/goal-detail.page.scss`
- `/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/goals/archive/goal-archive.page.ts`
- `/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/goals/archive/goal-archive.page.html`

## Code Context

### Reusable Assets

- Phase 3 already established backend-owned pace and progress fields for goal list and detail experiences.
- Existing goal detail and archive pages provide the main downstream surfaces that the new shell must connect.
- The current Angular routing structure is ready to absorb a shared application shell and dashboard route.

### Established Patterns

- Product logic should remain backend-driven where possible, especially for status and progress interpretation.
- Frontend state patterns already lean on signal-based stores and route-driven page composition.
- The visual language established so far uses warm surfaces, layered cards, and soft-glass styling cues.
- Goal detail is already the natural drill-down surface for concrete follow-up action.

### Likely Integration Points

- Application routing and shell composition in the frontend app root.
- Existing active goal, archive, and detail routes.
- Backend list/detail contracts that already expose pace status, progress values, and checkpoint context needed for dashboard summaries.

## Deferred

- Direct progress logging from the dashboard is deferred; dashboard callouts should open goal detail instead.
- Direct checkpoint editing from the dashboard is deferred.
- Broader product areas outside the dashboard shell and motivational goal overview remain out of scope for this phase.

*Phase: 04-dashboard-and-motivational-ux*
*Context gathered: 2026-04-06*
