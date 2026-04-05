import { ChangeDetectionStrategy, Component, computed, input } from '@angular/core';

@Component({
  selector: 'app-accomplishment-band',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: '',
})
export class AccomplishmentBandComponent {
  readonly progressPercent = input.required<number>();
  readonly milestoneLabel = computed(() => '');
  readonly variant = computed(() => 'none');
}
