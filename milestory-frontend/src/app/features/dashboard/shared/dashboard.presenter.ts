import { ListGoals200ResponseInner } from '../../../../api/model/listGoals200ResponseInner';
import {
  DashboardCallout,
  DashboardGoalCardViewModel,
  DashboardGoalGroup,
  DashboardViewModel,
} from './dashboard-view.models';

export function buildDashboardViewModel(goals: ListGoals200ResponseInner[]): DashboardViewModel {
  const behind = goals
    .filter((goal) => goal.paceStatus === 'BEHIND')
    .sort((left, right) => compareByShortfall(right, left))
    .map(mapGoalCard);
  const onPace = goals
    .filter((goal) => goal.paceStatus === 'ON_PACE')
    .sort(compareOnPaceUrgency)
    .map(mapGoalCard);
  const ahead = goals
    .filter((goal) => goal.paceStatus === 'AHEAD')
    .sort((left, right) => right.progressPercentOfTarget - left.progressPercentOfTarget)
    .map(mapGoalCard);

  const strongestRiskGoalId = behind[0]?.goalId ?? null;
  const strongestWinGoalId = ahead[0]?.goalId ?? null;
  const suggestedNextGoalId = strongestRiskGoalId ?? onPace[0]?.goalId ?? strongestWinGoalId;
  const goalCards = [...behind, ...onPace, ...ahead];
  const callouts = buildCallouts(goalCards, strongestRiskGoalId, strongestWinGoalId, suggestedNextGoalId);
  const groups = buildGroups(behind, onPace, ahead);

  return {
    activeGoalCount: goals.length,
    strongestRiskGoalId,
    strongestWinGoalId,
    suggestedNextGoalId,
    callouts,
    behind,
    onPace,
    ahead,
    groups,
  };
}

function compareByShortfall(left: ListGoals200ResponseInner, right: ListGoals200ResponseInner): number {
  return getShortfall(left) - getShortfall(right);
}

function compareOnPaceUrgency(left: ListGoals200ResponseInner, right: ListGoals200ResponseInner): number {
  const leftCheckpointDate = getNextCheckpointDate(left);
  const rightCheckpointDate = getNextCheckpointDate(right);

  if (leftCheckpointDate && rightCheckpointDate && leftCheckpointDate !== rightCheckpointDate) {
    return leftCheckpointDate.localeCompare(rightCheckpointDate);
  }

  if (leftCheckpointDate && !rightCheckpointDate) {
    return -1;
  }

  if (!leftCheckpointDate && rightCheckpointDate) {
    return 1;
  }

  return left.progressPercentOfTarget - right.progressPercentOfTarget;
}

function getShortfall(goal: ListGoals200ResponseInner): number {
  return goal.expectedProgressValueToday - goal.currentProgressValue;
}

function mapGoalCard(goal: ListGoals200ResponseInner): DashboardGoalCardViewModel {
  const nextCheckpoint = getNextCheckpoint(goal);

  return {
    goalId: goal.goalId,
    title: goal.title,
    unit: goal.unit,
    paceStatus: goal.paceStatus,
    paceSummary: goal.paceSummary,
    paceDetail: goal.paceDetail,
    currentProgressValue: goal.currentProgressValue,
    expectedProgressValueToday: goal.expectedProgressValueToday,
    progressPercentOfTarget: goal.progressPercentOfTarget,
    nextCheckpointDate: nextCheckpoint?.checkpointDate ?? null,
    nextCheckpointLabel: nextCheckpoint?.progressContextLabel ?? null,
    strongestSignal: goal.paceStatus === 'AHEAD' ? goal.paceSummary : goal.paceDetail,
  };
}

function getNextCheckpoint(goal: ListGoals200ResponseInner) {
  return [...goal.checkpoints]
    .sort((left, right) => left.checkpointDate.localeCompare(right.checkpointDate))
    .find((checkpoint) => checkpoint.targetValue > goal.currentProgressValue) ?? goal.checkpoints[0] ?? null;
}

function getNextCheckpointDate(goal: ListGoals200ResponseInner): string | null {
  return getNextCheckpoint(goal)?.checkpointDate ?? null;
}

function buildCallouts(
  goals: DashboardGoalCardViewModel[],
  strongestRiskGoalId: string | null,
  strongestWinGoalId: string | null,
  suggestedNextGoalId: string | null,
): DashboardCallout[] {
  const callouts: Array<DashboardCallout | null> = [
    createCallout(goals, strongestRiskGoalId, 'risk', 'Strongest risk'),
    createCallout(goals, strongestWinGoalId, 'win', 'Strongest win'),
    createCallout(goals, suggestedNextGoalId, 'next', 'Open next'),
  ];

  return callouts.filter((callout): callout is DashboardCallout => callout !== null);
}

function createCallout(
  goals: DashboardGoalCardViewModel[],
  goalId: string | null,
  tone: DashboardCallout['tone'],
  label: string,
): DashboardCallout | null {
  const goal = goalId ? goals.find((entry) => entry.goalId === goalId) : null;

  if (!goal) {
    return null;
  }

  return {
    tone,
    label,
    goalId: goal.goalId,
    title: goal.title,
    detail: goal.strongestSignal,
  };
}

function buildGroups(
  behind: DashboardGoalCardViewModel[],
  onPace: DashboardGoalCardViewModel[],
  ahead: DashboardGoalCardViewModel[],
): DashboardGoalGroup[] {
  return [
    { key: 'behind', heading: 'Behind', goals: behind },
    { key: 'onPace', heading: 'On pace', goals: onPace },
    { key: 'ahead', heading: 'Ahead', goals: ahead },
  ];
}
