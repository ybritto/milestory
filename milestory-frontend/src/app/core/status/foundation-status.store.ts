import { computed, inject, Injectable, signal } from '@angular/core';

import { FoundationStatusService } from '../../../api/api/foundationStatus.service';
import { GetFoundationStatus200Response } from '../../../api/model/getFoundationStatus200Response';

export type FoundationStatusViewState =
  | { kind: 'loading' }
  | { kind: 'ready'; payload: GetFoundationStatus200Response }
  | { kind: 'empty'; payload: GetFoundationStatus200Response | null }
  | { kind: 'error'; message: string };

@Injectable({
  providedIn: 'root',
})
export class FoundationStatusStore {
  private readonly foundationStatusService = inject(FoundationStatusService);
  private readonly state = signal<FoundationStatusViewState>({ kind: 'loading' });

  readonly viewState = computed(() => this.state());

  constructor() {
    this.loadStatus();
  }

  loadStatus(): void {
    this.state.set({ kind: 'loading' });

    this.foundationStatusService.getFoundationStatus().subscribe({
      next: (payload) => this.state.set(this.mapPayload(payload)),
      error: () =>
        this.state.set({
          kind: 'error',
          message:
            'Milestory could not load the current foundation status. Confirm the backend and database are running, then try again.',
        }),
    });
  }

  refreshStatus(): void {
    this.loadStatus();
  }

  private mapPayload(payload: GetFoundationStatus200Response): FoundationStatusViewState {
    if (payload.mode === GetFoundationStatus200Response.ModeEnum.Empty) {
      return { kind: 'empty', payload };
    }

    if (!payload.headline && !payload.summary && payload.notes.length === 0) {
      return { kind: 'empty', payload: null };
    }

    return { kind: 'ready', payload };
  }
}
