import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { describe, expect, it, beforeEach } from 'vitest';

import { App } from './app';
import { routes } from './app.routes';
import { GOAL_ROUTES } from './features/goals/goal.routes';

describe('App', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [App],
      providers: [provideRouter([])],
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(App);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should render the routed shell', async () => {
    const fixture = TestBed.createComponent(App);
    await fixture.whenStable();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('router-outlet')).not.toBeNull();
  });

  it('redirects the shell root to dashboard instead of shipping the home page as the default route', () => {
    const shellRoute = routes.find((route) => route.path === '');

    expect(shellRoute?.loadComponent).toBeDefined();
    expect(shellRoute?.children?.[0]).toMatchObject({
      path: '',
      pathMatch: 'full',
      redirectTo: 'dashboard',
    });
    expect(routes.some((route) => route.path === 'dashboard')).toBe(false);
  });

  it('reserves goals/active ahead of the generic goal detail route', () => {
    const activeIndex = GOAL_ROUTES.findIndex((route) => route.path === 'active');
    const goalDetailIndex = GOAL_ROUTES.findIndex((route) => route.path === ':goalId');

    expect(activeIndex).toBeGreaterThan(-1);
    expect(goalDetailIndex).toBeGreaterThan(activeIndex);
  });
});
