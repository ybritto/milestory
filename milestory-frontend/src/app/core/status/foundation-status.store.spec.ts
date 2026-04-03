import { beforeEach, describe, expect, it, vi } from 'vitest';
import { TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';

import { FoundationStatusService } from '../../../api/api/foundationStatus.service';
import { GetFoundationStatus200Response } from '../../../api/model/getFoundationStatus200Response';
import { FoundationStatusStore } from './foundation-status.store';

describe('FoundationStatusStore', () => {
  let service: { getFoundationStatus: ReturnType<typeof vi.fn> };

  beforeEach(() => {
    service = {
      getFoundationStatus: vi.fn(),
    };

    TestBed.configureTestingModule({
      providers: [
        FoundationStatusStore,
        {
          provide: FoundationStatusService,
          useValue: service as unknown as FoundationStatusService,
        },
      ],
    });
  });

  it('loads a ready status on initialization', () => {
    service.getFoundationStatus.mockReturnValue(of(createPayload()));

    const store = TestBed.inject(FoundationStatusStore);

    expect(service.getFoundationStatus).toHaveBeenCalledTimes(1);
    expect(store.viewState().kind).toBe('ready');
  });

  it('maps an empty payload into the empty state', () => {
    service.getFoundationStatus.mockReturnValue(
      of(createPayload({ mode: GetFoundationStatus200Response.ModeEnum.Empty })),
    );

    const store = TestBed.inject(FoundationStatusStore);

    expect(store.viewState().kind).toBe('empty');
  });

  it('retries after an error', () => {
    service.getFoundationStatus
      .mockReturnValueOnce(throwError(() => new Error('offline')))
      .mockReturnValueOnce(of(createPayload()));

    const store = TestBed.inject(FoundationStatusStore);

    expect(store.viewState().kind).toBe('error');

    store.refreshStatus();

    expect(service.getFoundationStatus).toHaveBeenCalledTimes(2);
    expect(store.viewState().kind).toBe('ready');
  });
});

function createPayload(
  overrides: Partial<GetFoundationStatus200Response> = {},
): GetFoundationStatus200Response {
  return {
    headline: 'Foundation is ready',
    summary: 'Backend status is available for the Milestory home screen.',
    mode: GetFoundationStatus200Response.ModeEnum.Ready,
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
    notes: ['Database connected', 'Liquibase baseline applied'],
    ...overrides,
  };
}
