import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadChildren: () =>
      import('./features/link/link.routes').then(m => m.LINK_ROUTES)
  }
];