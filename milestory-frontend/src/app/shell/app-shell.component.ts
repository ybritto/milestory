import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  ElementRef,
  inject,
  signal,
  viewChild,
} from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { CdkTrapFocus } from '@angular/cdk/a11y';
import { FocusMonitor } from '@angular/cdk/a11y';

@Component({
  selector: 'app-shell',
  imports: [RouterLink, RouterLinkActive, RouterOutlet, CdkTrapFocus],
  templateUrl: './app-shell.component.html',
  styleUrl: './app-shell.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  host: {
    '(document:keydown)': 'handleDocumentKeydown($event)',
  },
})
export class AppShellComponent {
  private readonly focusMonitor = inject(FocusMonitor);
  private readonly destroyRef = inject(DestroyRef);
  private readonly menuTrigger = viewChild<ElementRef<HTMLButtonElement>>('menuTrigger');
  private readonly closeDrawerButton = viewChild<ElementRef<HTMLButtonElement>>('closeDrawerButton');

  protected readonly isDrawerOpen = signal(false);

  constructor() {
    this.destroyRef.onDestroy(() => {
      const trigger = this.menuTrigger()?.nativeElement;
      if (trigger) {
        this.focusMonitor.stopMonitoring(trigger);
      }
      const closeButton = this.closeDrawerButton()?.nativeElement;
      if (closeButton) {
        this.focusMonitor.stopMonitoring(closeButton);
      }
    });
  }

  protected openDrawer(): void {
    this.isDrawerOpen.set(true);
    queueMicrotask(() => {
      const closeButton = this.closeDrawerButton()?.nativeElement;
      if (closeButton) {
        this.focusMonitor.focusVia(closeButton, 'program');
      }
    });
  }

  protected closeDrawer(): void {
    if (!this.isDrawerOpen()) {
      return;
    }

    this.isDrawerOpen.set(false);
    queueMicrotask(() => {
      const trigger = this.menuTrigger()?.nativeElement;
      if (trigger) {
        this.focusMonitor.focusVia(trigger, 'program');
      }
    });
  }

  protected handleNavActivate(): void {
    this.closeDrawer();
  }

  protected handleDocumentKeydown(event: KeyboardEvent): void {
    if (event.key === 'Escape' && this.isDrawerOpen()) {
      event.preventDefault();
      this.closeDrawer();
    }
  }
}
