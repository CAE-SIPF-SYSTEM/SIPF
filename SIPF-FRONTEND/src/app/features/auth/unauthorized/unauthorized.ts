import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-unauthorized',
  imports: [RouterLink],
  template: `
    <div class="min-h-screen flex items-center justify-center bg-slate-900 text-center p-4">
      <div class="animate-fade-in-up max-w-lg">
        <h1 class="text-9xl font-black gradient-text mb-4">403</h1>
        <h2 class="text-3xl font-bold text-white mb-4">Acceso No Autorizado</h2>
        <p class="text-slate-400 mb-8 text-lg">No tienes permisos para acceder a esta sección o tu rol no es válido.</p>
        <a routerLink="/" class="btn-primary">
          <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="mr-2"><path d="m3 9 9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9 22 9 12 15 12 15 22"/></svg>
          Volver al inicio
        </a>
      </div>
    </div>
  `
})
export class UnauthorizedComponent {}
