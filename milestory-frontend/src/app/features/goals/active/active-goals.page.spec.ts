import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { describe, expect, it } from 'vitest';

import { ActiveGoalsPage } from './active-goals.page';

describe('ActiveGoalsPage', () => {
  it('renders the Active goals placeholder copy', async () => {
    await TestBed.configureTestingModule({
      imports: [ActiveGoalsPage],
      providers: [provideRouter([])],
    }).compileComponents();

    const fixture = TestBed.createComponent(ActiveGoalsPage);
    fixture.detectChanges();
    await fixture.whenStable();

    expect(fixture.nativeElement.textContent ?? '').toContain('Active goals');
  });
});
