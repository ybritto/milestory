import { ListGoals200ResponseInner } from '../../../../api/model/listGoals200ResponseInner';

export type DashboardGroupKey = 'behind' | 'onPace' | 'ahead';
export type DashboardCalloutTone = 'risk' | 'win' | 'next';

export interface DashboardCallout {
  readonly tone: DashboardCalloutTone;
  readonly label: string;
  readonly goalId: string;
  readonly title: string;
  readonly detail: string;
}

export interface DashboardGoalCardViewModel {
  readonly goalId: string;
  readonly title: string;
  readonly unit: string;
  readonly paceStatus: ListGoals200ResponseInner['paceStatus'];
  readonly paceSummary: string;
  readonly paceDetail: string;
  readonly currentProgressValue: number;
  readonly expectedProgressValueToday: number;
  readonly progressPercentOfTarget: number;
  readonly nextCheckpointDate: string | null;
  readonly nextCheckpointLabel: string | null;
  readonly strongestSignal: string;
}

export interface DashboardGoalGroup {
  readonly key: DashboardGroupKey;
  readonly heading: string;
  readonly goals: DashboardGoalCardViewModel[];
}

export interface DashboardViewModel {
  readonly activeGoalCount: number;
  readonly strongestRiskGoalId: string | null;
  readonly strongestWinGoalId: string | null;
  readonly suggestedNextGoalId: string | null;
  readonly callouts: DashboardCallout[];
  readonly behind: DashboardGoalCardViewModel[];
  readonly onPace: DashboardGoalCardViewModel[];
  readonly ahead: DashboardGoalCardViewModel[];
  readonly groups: DashboardGoalGroup[];
}
