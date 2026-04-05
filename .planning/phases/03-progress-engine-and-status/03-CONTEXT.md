# Phase 3: Progress Engine And Status - Context

**Gathered:** 2026-04-04
**Status:** Ready for planning

<domain>
## Phase Boundary

Phase 3 adds the tracking core for saved goals. The user can record dated progress updates, see cumulative progress against the target, understand whether a goal is behind, on pace, or ahead, and understand how that status relates to the checkpoint plan. It does not add dashboard-level summary experiences, authentication, multi-user behavior, or new category-specific workflows.

</domain>

<decisions>
## Implementation Decisions

### Progress update shape
- **D-01:** A progress update should include a date, a progress value, and an optional note.
- **D-02:** Each progress update represents the new cumulative total for the goal rather than an incremental delta.
- **D-03:** Phase 3 should keep a visible list of past progress updates on the goal detail experience, but editing and deleting those entries are out of scope for now.
- **D-04:** If the user enters a lower cumulative value than a previous update, Milestory should allow it but explicitly treat it as a correction rather than a silent normal update.

### Status language and tone
- **D-05:** The primary user-facing status labels should be pace-based: `Behind`, `On pace`, and `Ahead`.
- **D-06:** The backend status model should allow a small tolerance band so values close to the expected checkpoint path still count as on pace.
- **D-07:** When a goal is ahead of plan, the experience should feel strongly motivational and high-energy rather than merely factual.
- **D-08:** When a goal is behind plan, the experience should stay supportive but honest rather than blunt or evasive.

### Goal detail explanation
- **D-09:** The goal detail page should emphasize the current status first, followed by a short explanation.
- **D-10:** Plan-versus-actual explanation should use a compact summary sentence plus a small comparison block rather than a dense metrics panel.
- **D-11:** The most useful primary comparison is `actual so far` versus `expected by today`.
- **D-12:** The checkpoint section should remain mostly read-only in Phase 3, but it should be annotated or framed with progress context so the user can understand how the current state relates to the plan.

### Progress entry workflow
- **D-13:** The primary place to add progress should be directly from the goal detail page.
- **D-14:** Opening progress entry should feel like a modal or drawer layered over the detail page rather than a full navigation away from it.
- **D-15:** After saving a progress update, the user should return to the refreshed detail view with an updated status and a clear success signal.
- **D-16:** Progress history should remain visibly present on the goal detail page rather than being hidden behind tabs or secondary navigation.

### the agent's Discretion
- The exact threshold used for the on-pace tolerance band, as long as it remains understandable and does not make status feel arbitrary
- The precise wording of explanatory copy for correction entries, ahead-of-plan encouragement, and behind-plan support
- Whether the progress entry UI is implemented as a centered modal, side drawer, or responsive variant of the same overlay pattern
- The exact visual treatment used to annotate checkpoints with current progress context, as long as the checkpoint list stays readable and mostly informational

</decisions>

<specifics>
## Specific Ideas

- Progress tracking should feel lightweight enough that adding an update does not feel like filling out a journal entry, even though optional notes are available.
- The user should quickly understand "where I am versus where I expected to be by now" without reading a wall of analytics.
- Visible progress history matters in Phase 3, but this is still a tracking core phase rather than a full progress-log management phase.
- Status tone should motivate without becoming vague: ahead can celebrate, but behind should still communicate reality clearly.

</specifics>

<canonical_refs>
## Canonical References

**Downstream agents MUST read these before planning or implementing.**

### Phase definition
- `.planning/ROADMAP.md` — Phase 3 goal, requirements, and success criteria
- `.planning/REQUIREMENTS.md` — PROG-01 through PROG-04 and dashboard scope boundaries
- `.planning/PROJECT.md` — product direction, backend-owned logic constraint, and UX expectations
- `.planning/STATE.md` — current project status after Phase 2 completion

### Prior phase decisions
- `.planning/phases/01-foundation-and-personal-mode/01-CONTEXT.md` — auth-free, backend-owned, real-product tone decisions that remain in force
- `.planning/phases/02-goal-planning-and-checkpoints/02-CONTEXT.md` — goal model, checkpoint planning style, and goal detail/archive decisions that Phase 3 extends

### Repository instructions
- `AGENTS.md` — repository-wide guidance and template-leftover awareness
- `milestory-backend/AGENTS.md` — backend DDD/hexagonal and TDD expectations
- `milestory-frontend/AGENTS.md` — Angular, accessibility, and frontend architecture requirements
- `milestory-api/AGENTS.md` — OpenAPI and REST contract conventions

</canonical_refs>

<code_context>
## Existing Code Insights

### Reusable Assets
- `milestory-frontend/src/app/features/goals/detail/goal-detail.page.ts` and `milestory-frontend/src/app/features/goals/detail/goal-detail.page.html` — the existing goal detail page is already the product-owned read surface and should become the primary place for status and progress entry
- `milestory-frontend/src/app/features/goals/shared/goal-planning.store.ts` — a signal-based backend-driven store already exists for loading saved goal detail and can likely be extended for progress-entry and refreshed status behavior
- `milestory-api/rest/schemas/goal-planning.yaml` — the current contract already defines saved goal detail, checkpoint responses, and planned-path summary, which gives Phase 3 a strong shape to extend rather than replace
- `milestory-backend/src/main/java/com/ybritto/milestory/goal/domain/Goal.java` — the goal aggregate already owns planning-year, target, lifecycle status, and checkpoint invariants, making it the natural seam for progress and status additions

### Established Patterns
- Contract-first is still the governing integration pattern, so progress-entry and status APIs should be designed in OpenAPI first and then implemented through generated boundaries
- Backend-owned business logic is an explicit product constraint, so cumulative progress calculation and pace status rules should live in backend domain/application code rather than frontend helpers
- Goal detail is already the established feature surface for reading a saved goal, so Phase 3 should deepen that page instead of creating a disconnected tracking area
- Phase 2 kept checkpoint plans coherent across categories, so Phase 3 should avoid category-specific progress workflows unless they are strictly necessary

### Integration Points
- `milestory-api/rest/paths/goal-by-id.yaml` and related goal schemas — current goal detail endpoints are the most obvious place to enrich saved-goal responses with progress and status fields
- `milestory-api/rest/paths/goals.yaml` and `milestory-api/rest/schemas/goal-planning.yaml` — list/detail contracts may need new fields so active goals can expose progress-aware state consistently
- `milestory-backend/src/main/resources/db/changelog/changes/002-goal-planning-foundation.yaml` — the persistence baseline for goals and checkpoints already exists and will need a follow-on migration for progress entries and derived status data if persisted
- `milestory-backend/src/main/java/com/ybritto/milestory/goal/` — the existing goal slice should absorb progress and status behavior using the same feature-first hexagonal seam
- `milestory-frontend/src/app/features/goals/detail/` — this is the likely home for the progress overlay, status block, and visible update history

</code_context>

<deferred>
## Deferred Ideas

- Dashboard-level rollups, attention ordering, and cross-goal progress summaries belong to Phase 4 rather than this phase
- Rich edit/delete management for progress history is deferred beyond the initial tracking core
- Manual checkpoint completion toggles or dynamic checkpoint reordering are out of scope for Phase 3
- Category-specific progress modes or mixed cumulative/incremental tracking models would be separate follow-on work if needed

</deferred>

---

*Phase: 03-progress-engine-and-status*
*Context gathered: 2026-04-04*
