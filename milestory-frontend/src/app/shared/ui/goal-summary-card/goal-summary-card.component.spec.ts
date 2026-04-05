import { TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { provideRouter } from '@angular/router';
import { RouterLink } from '@angular/router';
import { describe, expect, it } from 'vitest';

import { DashboardGoalCardViewModel } from '../../../features/dashboard/shared/dashboard-view.models';
import { GoalSummaryCardComponent } from './goal-summary-card.component';

describe('GoalSummaryCardComponent', () => {
  it('shows exact summary labels and links to the goal detail route', async () => {
    await TestBed.configureTestingModule({
      imports: [GoalSummaryCardComponent],
      providers: [provideRouter([])],
    }).compileComponents();

    const fixture = TestBed.createComponent(GoalSummaryCardComponent);
    fixture.componentRef.setInput('goal', createGoalCard());
    fixture.detectChanges();
    await fixture.whenStable();

    const text = fixture.nativeElement.textContent ?? '';
    const link = fixture.debugElement.query(By.directive(RouterLink)).injector.get(RouterLink);

    expect(text).toContain('Actual so far');
    expect(text).toContain('Expected by today');
    expect(text).toContain('Next checkpoint');
    expect(link.href).toBe('/goals/goal-id');
  });
});

function createGoalCard(overrides: Partial<DashboardGoalCardViewModel> = {}): DashboardGoalCardViewModel {
  return {
    goalId: 'goal-id',
    title: 'Read 24 books',
    unit: 'books',
    paceStatus: 'ON_PACE',
    paceSummary: "You're right where this goal expected you to be today.",
    paceDetail: 'You are matching the planned pace for today.',
    currentProgressValue: 6,
    expectedProgressValueToday: 6,
    progressPercentOfTarget: 25,
    nextCheckpointDate: '2026-06-30',
    nextCheckpointLabel: 'Upcoming checkpoint',
    strongestSignal: 'Steady pace into the next checkpoint.',
    ...overrides,
  };
}
