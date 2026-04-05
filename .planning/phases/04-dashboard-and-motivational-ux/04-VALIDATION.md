---
phase: 04
slug: dashboard-and-motivational-ux
status: draft
nyquist_compliant: false
wave_0_complete: false
created: 2026-04-06
---

# Phase 04 — Validation Strategy

> Per-phase validation contract for feedback sampling during execution.

---

## Test Infrastructure

| Property | Value |
|----------|-------|
| **Framework** | Angular unit-test builder + Vitest (`ng test`, repo currently `^4.0.8`) |
| **Config file** | `milestory-frontend/angular.json` and `milestory-frontend/tsconfig.spec.json`; no standalone `vitest.config.ts` |
| **Quick run command** | `npm --prefix milestory-frontend test -- --watch=false --include src/app/features/dashboard/**/*.spec.ts` |
| **Full suite command** | `npm --prefix milestory-frontend test -- --watch=false` |
| **Estimated runtime** | ~30 seconds |

---

## Sampling Rate

- **After every task commit:** Run `npm --prefix milestory-frontend test -- --watch=false --include <touched-specs>`
- **After every plan wave:** Run `npm --prefix milestory-frontend test -- --watch=false`
- **Before `$gsd-verify-work`:** Full suite must be green
- **Max feedback latency:** 30 seconds

---

## Per-Task Verification Map

| Task ID | Plan | Wave | Requirement | Test Type | Automated Command | File Exists | Status |
|---------|------|------|-------------|-----------|-------------------|-------------|--------|
| 04-01-01 | 01 | 1 | DASH-01 | presenter/unit | `npm --prefix milestory-frontend test -- --watch=false --include src/app/features/dashboard/shared/dashboard.presenter.spec.ts` | ❌ W0 | ⬜ pending |
| 04-01-02 | 01 | 1 | DASH-02 | presenter/unit | `npm --prefix milestory-frontend test -- --watch=false --include src/app/features/dashboard/shared/dashboard.presenter.spec.ts` | ❌ W0 | ⬜ pending |
| 04-01-03 | 01 | 1 | DASH-03 | component/unit | `npm --prefix milestory-frontend test -- --watch=false --include src/app/shared/ui/accomplishment-band/accomplishment-band.component.spec.ts` | ❌ W0 | ⬜ pending |
| 04-01-04 | 01 | 1 | DASH-04 | component/routing | `npm --prefix milestory-frontend test -- --watch=false --include src/app/features/dashboard/dashboard.page.spec.ts --filter "drill|navigate"` | ❌ W0 | ⬜ pending |
| 04-02-01 | 02 | 2 | PLAT-03 | component/routing | `npm --prefix milestory-frontend test -- --watch=false --include src/app/shell/app-shell.component.spec.ts --include src/app/app.spec.ts --include src/app/features/goals/active/active-goals.page.spec.ts` | ❌ W0 | ⬜ pending |
| 04-03-01 | 03 | 3 | DASH-03 | component/unit | `npm --prefix milestory-frontend test -- --watch=false --include src/app/features/goals/detail/goal-detail.page.spec.ts --include src/app/shared/ui/accomplishment-band/accomplishment-band.component.spec.ts` | ❌ W0 | ⬜ pending |
| 04-03-02 | 03 | 3 | DASH-04 | component/routing | `npm --prefix milestory-frontend test -- --watch=false --include src/app/features/goals/active/active-goals.page.spec.ts --include src/app/features/goals/detail/goal-detail.page.spec.ts` | ❌ W0 | ⬜ pending |

*Status: ⬜ pending · ✅ green · ❌ red · ⚠️ flaky*

---

## Wave 0 Requirements

- [ ] `milestory-frontend/src/app/shell/app-shell.component.spec.ts` — shell nav, active-link semantics, mobile drawer behavior
- [ ] `milestory-frontend/src/app/features/dashboard/shared/dashboard.presenter.spec.ts` — grouping, urgency ordering, strongest risk/win, suggested-next-goal
- [ ] `milestory-frontend/src/app/features/dashboard/dashboard.page.spec.ts` — command-center rendering and drill-down links
- [ ] `milestory-frontend/src/app/shared/ui/accomplishment-band/accomplishment-band.component.spec.ts` — threshold rendering for 80/100/120
- [ ] `milestory-frontend/src/app/features/goals/active/active-goals.page.spec.ts` — active-goals route rendering and drill-down behavior

---

## Manual-Only Verifications

| Behavior | Requirement | Why Manual | Test Instructions |
|----------|-------------|------------|-------------------|
| Mobile/tablet drawer feels polished and preserves focus return after close | PLAT-03 | Responsive motion and perceived shell quality are hard to judge from unit tests alone | Open the app at phone and tablet widths, open and close the nav drawer with keyboard and pointer, confirm focus returns to the trigger and the main content remains reachable |
| Dashboard cards feel medium-density and motivational rather than cluttered | DASH-01, DASH-02, DASH-03 | Visual hierarchy, celebration tone, and card scanning quality are product-judgment checks | Seed goals across Behind/On pace/Ahead states, confirm the command-center summary is scannable, and verify cards show status, actual vs expected, next checkpoint, and accomplishment signal without turning into detail pages |
| Drill-down from command-center callouts and grouped cards feels obvious | DASH-04 | Navigation clarity depends on copy, placement, and spatial hierarchy beyond simple link presence | Click strongest risk, strongest win, suggested next goal, and a grouped card from the dashboard; confirm each lands in the expected goal detail context without confusion |

---

## Validation Sign-Off

- [ ] All tasks have `<automated>` verify or Wave 0 dependencies
- [ ] Sampling continuity: no 3 consecutive tasks without automated verify
- [ ] Wave 0 covers all MISSING references
- [ ] No watch-mode flags
- [ ] Feedback latency < 30s
- [ ] `nyquist_compliant: true` set in frontmatter

**Approval:** pending
