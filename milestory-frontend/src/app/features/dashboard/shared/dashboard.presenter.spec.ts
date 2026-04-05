import { describe, expect, it } from 'vitest';

import { ListGoals200ResponseInner } from '../../../../api/model/listGoals200ResponseInner';
import { ListGoals200ResponseInnerProgressEntriesInner } from '../../../../api/model/listGoals200ResponseInnerProgressEntriesInner';
import { buildDashboardViewModel } from './dashboard.presenter';

describe('buildDashboardViewModel', () => {
  it('groups goals by backend pace status and orders each group by urgency', () => {
    const viewModel = buildDashboardViewModel([
      createGoal({
        goalId: 'behind-severe',
        title: 'Run 500 km',
        paceStatus: 'BEHIND',
        currentProgressValue: 110,
        expectedProgressValueToday: 220,
        progressPercentOfTarget: 22,
        paceSummary: 'The goal is significantly behind.',
      }),
      createGoal({
        goalId: 'ahead-high',
        title: 'Read 36 books',
        paceStatus: 'AHEAD',
        currentProgressValue: 30,
        expectedProgressValueToday: 18,
        progressPercentOfTarget: 125,
        paceSummary: 'The goal is outperforming the plan.',
      }),
      createGoal({
        goalId: 'on-pace-late',
        title: 'Save 10000 EUR',
        paceStatus: 'ON_PACE',
        currentProgressValue: 4200,
        expectedProgressValueToday: 4200,
        progressPercentOfTarget: 42,
        checkpoints: [createCheckpoint({ checkpointDate: '2026-05-15', targetValue: 4500 })],
      }),
      createGoal({
        goalId: 'behind-moderate',
        title: 'Meditate 200 sessions',
        paceStatus: 'BEHIND',
        currentProgressValue: 70,
        expectedProgressValueToday: 100,
        progressPercentOfTarget: 35,
        paceSummary: 'The goal is behind its plan.',
      }),
      createGoal({
        goalId: 'on-pace-soon',
        title: 'Practice guitar 150 hours',
        paceStatus: 'ON_PACE',
        currentProgressValue: 60,
        expectedProgressValueToday: 60,
        progressPercentOfTarget: 40,
        checkpoints: [createCheckpoint({ checkpointDate: '2026-04-10', targetValue: 65 })],
      }),
      createGoal({
        goalId: 'ahead-low',
        title: 'Write 52 journal entries',
        paceStatus: 'AHEAD',
        currentProgressValue: 20,
        expectedProgressValueToday: 14,
        progressPercentOfTarget: 95,
        paceSummary: 'The goal is modestly ahead.',
      }),
    ]);

    expect(viewModel.behind.map((goal) => goal.goalId)).toEqual(['behind-severe', 'behind-moderate']);
    expect(viewModel.onPace.map((goal) => goal.goalId)).toEqual(['on-pace-soon', 'on-pace-late']);
    expect(viewModel.ahead.map((goal) => goal.goalId)).toEqual(['ahead-high', 'ahead-low']);
  });

  it('derives command-center callout goal ids and active goal count from the same active-goal input', () => {
    const viewModel = buildDashboardViewModel([
      createGoal({
        goalId: 'behind-severe',
        title: 'Run 500 km',
        paceStatus: 'BEHIND',
        currentProgressValue: 110,
        expectedProgressValueToday: 220,
        progressPercentOfTarget: 22,
      }),
      createGoal({
        goalId: 'on-pace-soon',
        title: 'Practice guitar 150 hours',
        paceStatus: 'ON_PACE',
        currentProgressValue: 60,
        expectedProgressValueToday: 60,
        progressPercentOfTarget: 40,
        checkpoints: [createCheckpoint({ checkpointDate: '2026-04-10', targetValue: 65 })],
      }),
      createGoal({
        goalId: 'ahead-high',
        title: 'Read 36 books',
        paceStatus: 'AHEAD',
        currentProgressValue: 30,
        expectedProgressValueToday: 18,
        progressPercentOfTarget: 125,
      }),
    ]);

    expect(viewModel.activeGoalCount).toBe(3);
    expect(viewModel.strongestRiskGoalId).toBe('behind-severe');
    expect(viewModel.strongestWinGoalId).toBe('ahead-high');
    expect(viewModel.suggestedNextGoalId).toBe('behind-severe');
  });
});

function createGoal(overrides: Partial<ListGoals200ResponseInner> = {}): ListGoals200ResponseInner {
  return {
    goalId: 'goal-id',
    planningYear: 2026,
    title: 'Default goal',
    categoryId: 'category-id',
    targetValue: 100,
    unit: 'units',
    motivation: 'Stay consistent.',
    notes: 'Keep going.',
    status: 'ACTIVE',
    suggestionBasis: 'CATEGORY_AWARE',
    customizedFromSuggestion: true,
    plannedPathSummary: 'Steady progress across the year.',
    currentProgressValue: 40,
    progressPercentOfTarget: 40,
    expectedProgressValueToday: 50,
    paceStatus: 'BEHIND',
    paceSummary: 'The goal is behind.',
    paceDetail: 'Progress is below the expected pace today.',
    archivedAt: undefined,
    progressEntries: [createProgressEntry()],
    checkpoints: [
      createCheckpoint({
        checkpointDate: '2026-06-30',
        targetValue: 50,
        progressContextLabel: 'Upcoming checkpoint',
      }),
    ],
    ...overrides,
  };
}

function createCheckpoint(
  overrides: Partial<ListGoals200ResponseInner['checkpoints'][number]> = {},
): ListGoals200ResponseInner['checkpoints'][number] {
  return {
    checkpointId: 'checkpoint-id',
    sequenceNumber: 1,
    checkpointDate: '2026-06-30',
    targetValue: 50,
    note: 'Keep the pace steady.',
    origin: 'SUGGESTED',
    originalCheckpointDate: undefined,
    originalTargetValue: undefined,
    progressContextLabel: 'Upcoming checkpoint',
    progressContextDetail: 'The next target is on the calendar.',
    ...overrides,
  };
}

function createProgressEntry(
  overrides: Partial<ListGoals200ResponseInnerProgressEntriesInner> = {},
): ListGoals200ResponseInnerProgressEntriesInner {
  return {
    progressEntryId: 'progress-entry-id',
    entryDate: '2026-04-04',
    progressValue: 40,
    note: 'Logged progress.',
    entryType: 'NORMAL',
    recordedAt: '2026-04-04T10:00:00Z',
    ...overrides,
  };
}
