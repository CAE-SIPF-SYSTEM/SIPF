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
      {
        path: 'trimestres',
        loadComponent: () =>
          import('../features/admin/trimestres/admin-trimestres/admin-trimestres').then(
            m => m.AdminTrimestresComponent
          ),
      },
      {
        path: 'especialidades',
        loadComponent: () =>
          import('../features/admin/especialidades/admin-especialidades/admin-especialidades').then(
            m => m.AdminEspecialidadesComponent
          ),
      },
      {
        path: 'instructor-especialidad',
        loadComponent: () =>
          import('../features/admin/instructor-especialidad/admin-instructor-especialidad/admin-instructor-especialidad').then(
            m => m.AdminInstructorEspecialidadComponent
          ),
      },
      {
        path: 'competencia-especialidad',
        loadComponent: () =>
          import('../features/admin/competencia-especialidad/admin-competencia-especialidad/admin-competencia-especialidad.component').then(
            m => m.AdminCompetenciaEspecialidadComponent
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
      {
        path: 'disponibilidad',
        loadComponent: () =>
          import('../features/instructor/disponibilidad-instructor/disponibilidad-instructor.component').then(
            m => m.DisponibilidadInstructorComponent
          ),
      },
       {
        path: 'instructor-especialidad',
        loadComponent: () =>
          import('../features/admin/instructor-especialidad/admin-instructor-especialidad/admin-instructor-especialidad').then(
            m => m.AdminInstructorEspecialidadComponent
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

  // Perfil
  {
    path: 'perfil',
    canActivate: [roleGuard('COORDINADOR', 'INSTRUCTOR')],
    loadComponent: () =>
      import('../features/perfil/perfil/perfil').then(m => m.PerfilComponent),
  },

  // Default redirect
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' },
];
