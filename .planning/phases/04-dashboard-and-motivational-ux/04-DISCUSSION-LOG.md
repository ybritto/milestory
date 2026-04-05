# Phase 4 Discussion Log

- Phase: `04-dashboard-and-motivational-ux`
- Date: `2026-04-06`

## Discussion Record

### 1. Dashboard Layout And Navigation Shell

- Options discussed:
  - Persistent app shell with sidebar navigation across dashboard, active goals, archive, and goal detail
  - Lightweight top navigation only, with each page owning more of its own layout
  - Dashboard-first shell only, while goal flows keep their current local layout
- Selected: persistent app shell with sidebar navigation across dashboard, active goals, archive, and goal detail

### 2. Goal Prioritization On The Dashboard

- Options discussed:
  - Attention-first: goals needing action rise to the top
  - Balanced overview: short summary first, then grouped sections like `Behind`, `On pace`, and `Ahead`
  - Momentum-first: wins and celebrations lead before risks
- Selected: balanced overview first, then grouped sections like `Behind`, `On pace`, and `Ahead`

### 3. Motivational Bands And Celebration Style

- Options discussed:
  - Subtle and warm
  - Energized but tasteful
  - Bold and highly celebratory
- Selected: bold and highly celebratory

### 4. Dashboard Card Density

- Options discussed:
  - Compact
  - Medium
  - Rich
- Selected: medium density with concise summary plus `actual vs expected`, `next checkpoint`, and strongest accomplishment or risk signal

### 5. Sidebar Menu Scope

- Options discussed:
  - `Dashboard`, `Active goals`, `Archive`, and a strong `New goal` action
  - Keep `Home` alongside `Dashboard`, `Active goals`, `Archive`, and `New goal`
  - Minimal shell with `Dashboard` and `Goals`
- Selected: `Dashboard`, `Active goals`, `Archive`, and a strong `New goal` action

### 6. Dashboard Hero / Overview Style

- Options discussed:
  - Concise dashboard hero with counts and encouragement
  - Emotional overview with celebration first
  - Practical command center with counts, strongest risk, strongest win, and a suggested next goal
- Selected: practical command center with counts, strongest risk, strongest win, and a suggested next goal

### 7. Goal Ordering Within Groups

- Options discussed:
  - Most urgent first
  - Recently active first
  - Closest to milestone first
- Selected: most urgent first within each group

### 8. Responsive Navigation Behavior

- Options discussed:
  - Collapsible drawer on mobile and tablet, always-visible sidebar on desktop
  - Sidebar visible on tablet, drawer only on phones
  - Compact icon rail on smaller screens
- Selected: collapsible drawer on mobile and tablet, always-visible sidebar on desktop

### 9. Strongest Risk / Win Action Pattern

- Options discussed:
  - Open the goal detail page directly for the next concrete action
  - Log a progress update immediately from the dashboard
  - Review or adjust the checkpoint plan from the dashboard
- Selected: open the goal detail page directly for the next concrete action

## Result

Phase 4 context is now constrained enough for planning. The dashboard should become the primary product surface inside a shared shell, with balanced information architecture, bold milestone celebration, medium-density summary cards, and drill-down actions that continue into goal detail rather than duplicating editing workflows on the dashboard itself.

*Recorded: 2026-04-06*
