import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { 
    path: '', 
    redirectTo: 'home', 
    pathMatch: 'full' 
  },
  /*{
    path: 'home',
    loadChildren: () => import('./components/home/home.module').then(m => m.HomeModule)
  },*/
  { 
    path: 'auth',
    loadComponent: () => import('./components/auths/layout/auth.layout.component').then(m => m.AuthLayout),
    children: [
      {
        path: 'login',
        loadComponent: () => import('./components/auths/pages/login/login.page').then(m => m.LoginPage)
      },
    ]
  },
  {
    path: 'admin',
    loadComponent: () => import('./components/admins/layout/admin.layout.component').then(m => m.AdminLayout),
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
