import { ChangeDetectionStrategy, Component, computed, input } from '@angular/core';

@Component({
  selector: 'app-accomplishment-band',
  changeDetection: ChangeDetectionStrategy.OnPush,
  host: {
    '[class.accomplishment-band]': 'milestoneLabel() !== null',
    '[class.accomplishment-band--milestone]': 'variant() === "milestone"',
    '[class.accomplishment-band--target]': 'variant() === "target"',
    '[class.accomplishment-band--stretch]': 'variant() === "stretch"',
  },
  template: `
    @if (milestoneLabel(); as label) {
      <span class="accomplishment-band__eyebrow">Accomplishment band</span>
      <strong class="accomplishment-band__label">{{ label }}</strong>
    }
  `,
  styles: `
    :host {
      display: inline-flex;
      flex-direction: column;
      gap: 0.15rem;
      padding: 0.65rem 0.9rem;
      border-radius: 999px;
      background: linear-gradient(135deg, rgb(31 107 92 / 0.12), rgb(246 190 65 / 0.18));
      color: var(--color-ink);
      border: 1px solid rgb(31 107 92 / 0.18);
    }

    :host(:not(.accomplishment-band)) {
      display: none;
    }

    :host(.accomplishment-band--target) {
      background: linear-gradient(135deg, rgb(31 107 92 / 0.16), rgb(255 230 174 / 0.26));
    }

    :host(.accomplishment-band--stretch) {
      background: linear-gradient(135deg, rgb(31 107 92 / 0.24), rgb(255 196 110 / 0.32));
      border-color: rgb(31 107 92 / 0.3);
      box-shadow: 0 16px 32px rgb(31 107 92 / 0.16);
    }

    .accomplishment-band__eyebrow {
      font-size: 0.68rem;
      font-weight: 700;
      letter-spacing: 0.12em;
      text-transform: uppercase;
    }

    .accomplishment-band__label {
      font-size: 0.95rem;
    }
  `,
})
export class AccomplishmentBandComponent {
  readonly progressPercent = input.required<number>();
  readonly milestoneLabel = computed(() => {
    const progressPercent = this.progressPercent();

    if (progressPercent >= 120) {
      return '120% stretch';
    }

    if (progressPercent >= 100) {
      return 'Target reached';
    }

    if (progressPercent >= 80) {
      return '80% milestone';
    }

    return null;
  });
  readonly variant = computed(() => {
    const progressPercent = this.progressPercent();

    if (progressPercent >= 120) {
      return 'stretch';
    }

    if (progressPercent >= 100) {
      return 'target';
    }

    if (progressPercent >= 80) {
      return 'milestone';
    }

    return 'none';
  });
}
