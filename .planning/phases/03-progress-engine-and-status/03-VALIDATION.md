---
phase: 03
slug: progress-engine-and-status
status: ready
nyquist_compliant: true
wave_0_complete: true
created: 2026-04-04
---

# Phase 03 — Validation Strategy

> Per-phase validation contract for feedback sampling during execution.

---

## Test Infrastructure

| Property | Value |
|----------|-------|
| **Framework** | JUnit 5 + Spring Boot Test (backend), Angular unit-test builder with Vitest (frontend) |
| **Config file** | Backend: `milestory-backend/src/test/resources/application-test.yaml`; Frontend: `milestory-frontend/angular.json` |
| **Quick run command** | `mvn -q -pl milestory-backend -Dtest='*Goal*Test,*Goal*IntegrationTest' test` |
| **Full suite command** | `mvn -q test && npm --prefix milestory-frontend test -- --watch=false` |
| **Estimated runtime** | ~120 seconds |

---

## Sampling Rate

- **After every task commit:** Run the targeted backend or frontend command for the touched requirement area.
- **After every plan wave:** Run `mvn -q -pl milestory-backend test` or `npm --prefix milestory-frontend test -- --watch=false`, and run both at cross-boundary checkpoints.
- **Before `$gsd-verify-work`:** `mvn -q test && npm --prefix milestory-frontend test -- --watch=false` must be green.
- **Max feedback latency:** 120 seconds

---

## Per-Task Verification Map

| Task ID | Plan | Wave | Requirement | Test Type | Automated Command | File Exists | Status |
|---------|------|------|-------------|-----------|-------------------|-------------|--------|
| 03-01-01 | 01 | 1 | PROG-01 | contract verification | `rg -n "progress-entries|GoalProgressEntry|paceStatus|currentProgressValue|expectedProgressValueToday" milestory-api/rest/api-v1.yaml milestory-api/rest/paths/*.yaml milestory-api/rest/schemas/goal-planning.yaml` | ✅ same task | ⬜ pending |
| 03-01-02 | 01 | 1 | PROG-01, PROG-02, PROG-03 | backend unit | `mvn -q -pl milestory-backend -Dtest='RecordProgressEntryUseCaseTest,GoalProgressStatusServiceTest,GoalTest' test` | ✅ same task | ⬜ pending |
| 03-02-01 | 02 | 2 | PROG-01, PROG-02, PROG-03 | backend controller + persistence integration | `mvn -q -pl milestory-backend -Dtest='GoalControllerIntegrationTest,RecordProgressEntryUseCaseTest,GoalProgressStatusServiceTest' test` | ✅ same task | ⬜ pending |
| 03-02-02 | 02 | 2 | PROG-02, PROG-03, PROG-04 | backend read-model verification | `mvn -q -pl milestory-backend -Dtest='GoalControllerIntegrationTest,GoalProgressStatusServiceTest' test` | ✅ same task | ⬜ pending |
| 03-03-01 | 03 | 3 | PROG-01, PROG-04 | frontend store + interaction flow | `npm --prefix milestory-frontend test -- --watch=false --include='src/app/features/goals/shared/goal-planning.store.spec.ts' --include='src/app/features/goals/detail/goal-detail.page.spec.ts'` | ✅ existing specs extend | ⬜ pending |
| 03-03-02 | 03 | 3 | PROG-02, PROG-03, PROG-04 | frontend detail rendering | `npm --prefix milestory-frontend test -- --watch=false --include='src/app/features/goals/detail/goal-detail.page.spec.ts'` | ✅ existing spec extends | ⬜ pending |

*Status: ⬜ pending · ✅ green · ❌ red · ⚠️ flaky*

---

## Wave 0 Requirements

No separate Wave 0 is required for this phase. The missing progress-entry and status tests identified by research can be created inside the first backend tasks that introduce the corresponding behavior, and the existing frontend goal detail/store specs can be extended in the same execution wave that adds the UI.

---

## Manual-Only Verifications

| Behavior | Requirement | Why Manual | Test Instructions |
|----------|-------------|------------|-------------------|
| Progress entry feels lightweight and returns the user to an updated detail view with clear feedback | PROG-01 | Interaction quality, overlay behavior, and success feedback are not fully provable with unit tests | Run the frontend with the backend live, open a saved goal detail page, add a dated cumulative progress update, confirm the overlay flow feels lightweight, and verify the detail page refreshes with a visible success signal. |
| Pace status, comparison block, and checkpoint context feel understandable and motivating rather than purely mechanical | PROG-03, PROG-04 | Human judgment is needed for tone, clarity, and visual hierarchy | Run the frontend with seeded goals that are behind, on pace, and ahead. Confirm the detail hero explains status first, shows `actual so far` vs `expected by today`, and keeps checkpoint context readable without overwhelming the page. |

---

## Frontend Entry URLs

- Saved goal detail: `/goals/:goalId`
- Archived goals: `/goals/archive`

---

## Validation Sign-Off

- [ ] All tasks have `<automated>` verify or Wave 0 dependencies
- [ ] All task-level automated commands reference artifacts available at that point in execution
- [ ] Sampling continuity: no 3 consecutive tasks without automated verify
- [ ] No MISSING verification references remain
- [ ] No watch-mode flags
- [ ] Feedback latency < 120s
- [ ] `nyquist_compliant: true` set in frontmatter

**Approval:** pending
