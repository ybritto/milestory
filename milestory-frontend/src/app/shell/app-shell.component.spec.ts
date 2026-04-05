import { Component } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { provideRouter, RouterOutlet } from '@angular/router';
import { describe, expect, it } from 'vitest';

import { AppShellComponent } from './app-shell.component';

@Component({
  template: '',
})
class StubPageComponent {}

describe('AppShellComponent', () => {
  async function createComponent() {
    await TestBed.configureTestingModule({
      imports: [AppShellComponent],
      providers: [
        provideRouter([
          {
            path: '',
            component: AppShellComponent,
            children: [
              { path: 'dashboard', component: StubPageComponent },
              { path: 'goals/active', component: StubPageComponent },
              { path: 'goals/archive', component: StubPageComponent },
              { path: 'goals/new', component: StubPageComponent },
            ],
          },
        ]),
      ],
    }).compileComponents();

    const fixture = TestBed.createComponent(AppShellComponent);
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();

    return fixture;
  }

  it('renders the exact shell navigation labels', async () => {
    const fixture = await createComponent();
    const text = fixture.nativeElement.textContent ?? '';

    expect(text).toContain('Dashboard');
    expect(text).toContain('Active goals');
    expect(text).toContain('Archive');
    expect(text).toContain('New goal');
  });

  it('opens and closes the mobile drawer with keyboard support and returns focus to the trigger', async () => {
    const fixture = await createComponent();
    const menuTrigger = fixture.nativeElement.querySelector(
      '[data-testid="shell-menu-trigger"]',
    ) as HTMLButtonElement;

    menuTrigger.focus();
    menuTrigger.click();
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();

    const dialog = fixture.nativeElement.querySelector('[data-testid="shell-mobile-drawer"]') as HTMLElement;
    const closeButton = fixture.nativeElement.querySelector(
      '[data-testid="shell-close-drawer"]',
    ) as HTMLButtonElement;

    expect(dialog).not.toBeNull();
    expect(document.activeElement).toBe(closeButton);

    closeButton.dispatchEvent(new KeyboardEvent('keydown', { key: 'Enter' }));
    closeButton.click();
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();

    expect(fixture.nativeElement.querySelector('[data-testid="shell-mobile-drawer"]')).toBeNull();
    expect(document.activeElement).toBe(menuTrigger);
  });

  it('renders a child router outlet inside the shell layout', async () => {
    const fixture = await createComponent();

    expect(fixture.debugElement.query(By.directive(RouterOutlet))).not.toBeNull();
  });
});
