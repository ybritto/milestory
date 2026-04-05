import { ChangeDetectionStrategy, Component, computed, inject } from '@angular/core';
import { RouterLink } from '@angular/router';

import { GoalPlanningStore } from '../goals/shared/goal-planning.store';
import { GoalSummaryCardComponent } from '../../shared/ui/goal-summary-card/goal-summary-card.component';
import { buildDashboardViewModel } from './shared/dashboard.presenter';
import { DashboardCallout } from './shared/dashboard-view.models';

@Component({
  selector: 'app-dashboard-page',
  imports: [RouterLink, GoalSummaryCardComponent],
  templateUrl: './dashboard.page.html',
  styleUrl: './dashboard.page.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DashboardPage {
  private readonly goalPlanningStore = inject(GoalPlanningStore);

  protected readonly viewModel = computed(() => buildDashboardViewModel(this.goalPlanningStore.goals()));
  protected readonly groups = computed(() => this.viewModel().groups.filter((group) => group.goals.length > 0));
  protected readonly strongestRisk = computed(() => this.findCallout('risk'));
  protected readonly strongestWin = computed(() => this.findCallout('win'));
  protected readonly suggestedNext = computed(() => this.findCallout('next'));

  constructor() {
    this.goalPlanningStore.loadGoals('ACTIVE');
  }

  private findCallout(tone: DashboardCallout['tone']): DashboardCallout | null {
    return this.viewModel().callouts.find((callout) => callout.tone === tone) ?? null;
  }
}
