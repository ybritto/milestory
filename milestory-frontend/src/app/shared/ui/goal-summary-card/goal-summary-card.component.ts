import { ChangeDetectionStrategy, Component, computed, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { DashboardGoalCardViewModel } from '../../../features/dashboard/shared/dashboard-view.models';
import { AccomplishmentBandComponent } from '../accomplishment-band/accomplishment-band.component';

@Component({
  selector: 'app-goal-summary-card',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterLink, AccomplishmentBandComponent],
  templateUrl: './goal-summary-card.component.html',
  styleUrl: './goal-summary-card.component.scss',
})
export class GoalSummaryCardComponent {
  readonly goal = input.required<DashboardGoalCardViewModel>();
  readonly goalLink = computed(() => ['/goals', this.goal().goalId]);
  readonly nextCheckpointText = computed(() => this.goal().nextCheckpointDate ?? 'No checkpoint scheduled');
  readonly paceTone = computed(() => this.goal().paceStatus.toLowerCase().replace('_', '-'));
}
