import { Routes } from '@angular/router';
import { authGuard } from '../core/ports/auth.guard';
import { roleGuard } from '../core/ports/role.guard';

export const routes: Routes = [
  // Public routes
  {
    path: 'login',
    loadComponent: () => import('../features/auth/login/login').then(m => m.LoginComponent),
  },
  {
    path: 'recuperar-contrasena',
    loadComponent: () =>
      import('../features/auth/recuperar-contrasena/recuperar-contrasena').then(
        m => m.RecuperarContrasenaComponent
      ),
  },
  {
    path: 'restablecer-contrasena',
    loadComponent: () =>
      import('../features/auth/restablecer-contrasena/restablecer-contrasena').then(
        m => m.RestablecerContrasenaComponent
      ),
  },

  // Admin routes
  {
    path: 'admin',
    canActivate: [roleGuard('ADMINISTRADOR')],
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      {
        path: 'dashboard',
        loadComponent: () =>
          import('../features/admin/dashboard/admin-dashboard').then(
            m => m.AdminDashboardComponent
          ),
      },
      {
        path: 'usuarios',
        loadComponent: () =>
          import('../features/admin/user-management/user-management').then(
            m => m.UserManagementComponent
          ),
      },
      {
        path: 'alimentacion-sistema',
        loadComponent: () =>
          import('../features/admin/alimentacion-sistema/alimentacion-sistema').then(
            m => m.AlimentacionSistemaComponent
          ),
      },
      {
        path: 'competencias',
        loadComponent: () =>
          import('../features/competencias/listar-competencias/listar-competencias').then(
            m => m.ListarCompetenciasComponent
          ),
      },
      {
        path: 'raps',
        loadComponent: () =>
          import('../features/raps/listar-raps/listar-raps').then(
            m => m.ListarRapsComponent
          ),
      },
    ],
  },

  {
    path: 'instructor',
    canActivate: [roleGuard('INSTRUCTOR')],
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      {
        path: 'dashboard',
        loadComponent: () =>
          import('../features/instructor/dashboard/instructor-dashboard').then(
            m => m.InstructorDashboardComponent
          ),
      },
    ],
  },

  {
    path: 'coordinador',
    canActivate: [roleGuard('COORDINADOR')],
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      {
        path: 'dashboard',
        loadComponent: () =>
          import('../features/coordinador/dashboard/coordinador-dashboard').then(
            m => m.CoordinadorDashboardComponent
          ),
      },
      {
        path: 'programas',
        loadComponent: () =>
          import('../features/programas/listar-programas/listar-programas').then(
            m => m.ListarProgramasComponent
          ),
      },
      {
        path: 'fichas',
        loadComponent: () =>
          import('../features/fichas/listar-fichas/listar-fichas').then(
            m => m.ListarFichasComponent
          ),
      },
      {
        path: 'competencias',
        loadComponent: () =>
          import('../features/competencias/listar-competencias/listar-competencias').then(
            m => m.ListarCompetenciasComponent
          ),
      },
      {
        path: 'raps',
        loadComponent: () =>
          import('../features/raps/listar-raps/listar-raps').then(
            m => m.ListarRapsComponent
          ),
      },
    ],
  },

  // Unauthorized
  {
    path: 'no-autorizado',
    loadComponent: () =>
      import('../features/auth/unauthorized/unauthorized').then(m => m.UnauthorizedComponent),
  },

  // Default redirect
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' },
];
