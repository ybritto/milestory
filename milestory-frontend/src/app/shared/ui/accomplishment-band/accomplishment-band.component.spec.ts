import { TestBed } from '@angular/core/testing';
import { describe, expect, it } from 'vitest';

import { AccomplishmentBandComponent } from './accomplishment-band.component';

describe('AccomplishmentBandComponent', () => {
  it('applies a celebratory variant for progress at or above 120 percent', async () => {
    await TestBed.configureTestingModule({
      imports: [AccomplishmentBandComponent],
    }).compileComponents();

    const fixture = TestBed.createComponent(AccomplishmentBandComponent);
    fixture.componentRef.setInput('progressPercent', 120);
    fixture.detectChanges();

    expect(fixture.componentInstance.variant()).toBe('stretch');
    expect(fixture.componentInstance.milestoneLabel()).toBe('120% stretch');
  });
});
