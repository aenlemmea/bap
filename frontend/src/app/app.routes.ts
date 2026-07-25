import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: 'listings', pathMatch: 'full' },
  {
    path: 'listings',
    loadComponent: () => import('./features/listings/listing-list/listing-list').then(m => m.ListingList)
  },
  {
    path: 'listings/:id',
    loadComponent: () => import('./features/listings/listing-detail/listing-detail').then(m => m.ListingDetailComponent)
  },
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login/login').then(m => m.LoginComponent)
  },
  {
    path: 'register',
    loadComponent: () => import('./features/auth/register/register').then(m => m.RegisterComponent)
  },
  { path: '**', redirectTo: 'listings' }
];
