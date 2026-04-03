import { ChangeDetectionStrategy, Component, computed, inject } from '@angular/core';

import { FoundationStatusStore } from '../../core/status/foundation-status.store';

@Component({
  selector: 'app-home-page',
  templateUrl: './home.page.html',
  styleUrl: './home.page.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class HomePage {
  private readonly foundationStatusStore = inject(FoundationStatusStore);

  protected readonly viewState = this.foundationStatusStore.viewState;
  protected readonly readyPayload = computed(() => {
    const state = this.viewState();
    return state.kind === 'ready' ? state.payload : null;
  });
  protected readonly errorMessage = computed(() => {
    const state = this.viewState();
    return state.kind === 'error' ? state.message : '';
  });
  protected readonly facts = computed(() => {
    const payload = this.readyPayload();

    if (!payload) {
      return [];
    }

    return [
      `API ${payload.apiVersion} is responding.`,
      `${payload.database.name} is ${payload.database.status}.`,
      `Liquibase baseline ${payload.migration.baseline} is ${payload.migration.status}.`,
      `Snapshot captured ${formatGeneratedAt(payload.generatedAt)}.`,
    ];
  });

  protected refreshStatus(): void {
    this.foundationStatusStore.refreshStatus();
  }
}

function formatGeneratedAt(generatedAt: string): string {
  const value = new Date(generatedAt);

  return Number.isNaN(value.getTime()) ? 'recently' : value.toLocaleString();
}
