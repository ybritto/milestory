import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { signal } from '@angular/core';

import {
  FoundationStatusStore,
  FoundationStatusViewState,
} from '../../core/status/foundation-status.store';
import { HomePage } from './home.page';

describe('HomePage', () => {
  let fixture: ComponentFixture<HomePage>;
  let state: ReturnType<typeof signal<FoundationStatusViewState>>;
  let refreshStatus: ReturnType<typeof vi.fn>;

  beforeEach(async () => {
    state = signal<FoundationStatusViewState>({ kind: 'loading' });
    refreshStatus = vi.fn();

    await TestBed.configureTestingModule({
      imports: [HomePage],
      providers: [
        {
          provide: FoundationStatusStore,
          useValue: {
            viewState: state.asReadonly(),
            refreshStatus,
          },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(HomePage);
  });

  it('renders the loading state', () => {
    fixture.detectChanges();

    expect(textContent()).toContain('Milestory');
    expect(fixture.nativeElement.querySelector('.status-card__loading')).not.toBeNull();
  });

  it('renders the ready state facts', () => {
    state.set({
      kind: 'ready',
      payload: {
        headline: 'Foundation is ready',
        summary: 'The backend is serving the current status snapshot.',
        mode: 'ready',
        apiVersion: 'v1',
        database: {
          name: 'milestory',
          status: 'connected',
        },
        migration: {
          status: 'applied',
          baseline: '1',
        },
        generatedAt: '2026-04-03T22:29:05Z',
        notes: ['Database connected'],
      },
    });

    fixture.detectChanges();

    expect(textContent()).toContain('Foundation is ready');
    expect(textContent()).toContain('API v1 is responding.');
  });

  it('renders the error state and retry action', () => {
    state.set({
      kind: 'error',
      message:
        'Milestory could not load the current foundation status. Confirm the backend and database are running, then try again.',
    });

    fixture.detectChanges();

    const button = fixture.nativeElement.querySelector('.status-card__retry') as HTMLButtonElement;
    expect(textContent()).toContain('Status unavailable');

    button.click();

    expect(refreshStatus).toHaveBeenCalled();
  });
});

function textContent(): string {
  return document.body.textContent ?? '';
}
