import { Routes } from '@angular/router';

export const LINK_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./pages/link-home/link-home.component').then(c => c.LinkHomeComponent)
  }
];