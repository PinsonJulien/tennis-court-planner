import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './guards/auth.guard';

const routes: Routes = [
  { 
    path: '', 
    redirectTo: 'home', 
    pathMatch: 'full' 
  },
  {
    path: 'home',
    redirectTo: 'courts',
  },
  {
    path: '',
    canActivate: [
      AuthGuard
    ],
    loadComponent: () => import('./components/home/layout/home.layout').then(m => m.HomeLayout),
    children: [
      {
        path: 'courts',
        loadComponent: () => import('./components/home/pages/courts/court.page').then(m => m.CourtPage)
      }
    ],
  },
  { 
    path: 'auth',
    loadComponent: () => import('./components/auths/layout/auth.layout').then(m => m.AuthLayout),
    children: [
      {
        path: 'login',
        loadComponent: () => import('./components/auths/pages/login/login.page').then(m => m.LoginPage)
      },
    ]
  },
  {
    path: 'admin',
    canActivate: [
      AuthGuard
    ],
    loadComponent: () => import('./components/admins/layout/admin.layout').then(m => m.AdminLayout),
    children: [
      {
        path: '',
        redirectTo: 'users',
        pathMatch: 'full',
      },
      {
        path: 'users',
        loadComponent: () => import('./components/admins/pages/users/user-admin.page').then(m => m.UserAdminPage)
      },
      {
        path: 'courts',
        loadComponent: () => import('./components/admins/pages/courts/court-admin.page').then(m => m.CourtAdminPage)
      }
    ]
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
