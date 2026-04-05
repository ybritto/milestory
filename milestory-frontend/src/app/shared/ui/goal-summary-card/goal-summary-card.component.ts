import { ChangeDetectionStrategy, Component, input } from '@angular/core';

import { DashboardGoalCardViewModel } from '../../../features/dashboard/shared/dashboard-view.models';

@Component({
  selector: 'app-goal-summary-card',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './goal-summary-card.component.html',
  styleUrl: './goal-summary-card.component.scss',
})
export class GoalSummaryCardComponent {
  readonly goal = input.required<DashboardGoalCardViewModel>();
}
