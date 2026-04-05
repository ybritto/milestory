import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./shell/app-shell.component').then((module) => module.AppShellComponent),
    children: [
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'dashboard',
      },
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./features/dashboard/dashboard.page').then((module) => module.DashboardPage),
      },
      {
        path: 'goals',
        loadChildren: () =>
          import('./features/goals/goal.routes').then((module) => module.GOAL_ROUTES),
      },
    ],
  },
];
