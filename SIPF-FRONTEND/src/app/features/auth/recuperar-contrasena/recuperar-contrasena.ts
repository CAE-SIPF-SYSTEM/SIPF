import { Component, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../core/use-cases/auth.service';
import { AlertService } from '../../../core/use-cases/alert.service';

@Component({
  selector: 'app-recuperar-contrasena',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  template: `
    <div class="min-h-screen flex flex-col bg-[#f4f7f6] relative overflow-hidden">
      <!-- Top Brand Header (Mockup style) -->
      <div class="w-full bg-brand p-6 flex flex-col items-center justify-center shadow-md relative z-10">
        <div class="flex items-center gap-3 text-white">
          <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"><path d="M2 22 12 2l10 20"/><path d="M12 22V12"/><path d="M6 14h12"/></svg>
          <span class="text-3xl font-bold tracking-wider">SIPF</span>
        </div>
      </div>

      <!-- Background decorative elements -->
      <div class="absolute top-[20%] left-[10%] w-[40%] h-[40%] rounded-full bg-brand opacity-5 blur-[100px] pointer-events-none"></div>
      <div class="absolute bottom-[10%] right-[10%] w-[30%] h-[30%] rounded-full bg-[#37CD2D] opacity-10 blur-[100px] pointer-events-none"></div>
      
      <div class="flex-1 flex items-center justify-center p-4">
        <div class="card w-full max-w-md relative z-10 animate-fade-in-up">
          <div class="text-center mb-8">
            <h2 class="text-3xl font-bold text-[#334155] mb-2">Recuperar Contraseña</h2>
            <p class="text-[#64748b] text-sm">Ingresa tu correo y te enviaremos instrucciones</p>
          </div>

          <div *ngIf="isSuccess()" class="bg-[#f0fdf4] border border-[#bbf7d0] rounded-lg p-4 text-center mb-6 animate-fade-in">
            <svg class="w-8 h-8 text-[#22c55e] mx-auto mb-2" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"/></svg>
            <p class="text-[#166534] text-sm font-medium">Instrucciones enviadas. Por favor revisa tu bandeja de entrada.</p>
          </div>

          <form *ngIf="!isSuccess()" [formGroup]="form" (ngSubmit)="onSubmit()" class="space-y-6">
            <div>
              <label class="block text-sm font-semibold text-[#334155] mb-2" for="email">Correo electrónico</label>
              <input id="email" type="email" formControlName="correo" class="input-field bg-[#f8fafc]" placeholder="usuario@sena.edu.co">
              <div *ngIf="form.get('correo')?.invalid && form.get('correo')?.touched" class="text-rose-500 text-xs mt-1 font-medium">
                Ingresa un correo válido
              </div>
            </div>

            <button type="submit" [disabled]="form.invalid || isLoading()" class="btn-primary w-full py-3 shadow-md">
              <span *ngIf="!isLoading()">Enviar instrucciones</span>
              <div *ngIf="isLoading()" class="spinner"></div>
            </button>
          </form>

          <div class="mt-8 text-center">
            <a routerLink="/login" class="text-sm font-medium text-[#64748b] hover:text-brand transition-colors flex items-center justify-center gap-2">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m15 18-6-6 6-6"/></svg>
              Volver al inicio de sesión
            </a>
          </div>
        </div>
      </div>
    </div>
  `
})
export class RecuperarContrasenaComponent {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private alertService = inject(AlertService);

  isLoading = signal(false);
  isSuccess = signal(false);

  form = this.fb.group({
    correo: ['', [Validators.required, Validators.email]]
  });

  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.isLoading.set(true);
    
    this.authService.recuperarContrasena(this.form.value.correo!).subscribe({
      next: () => {
        this.isLoading.set(false);
        this.isSuccess.set(true);
      },
      error: (err: any) => {
        this.isLoading.set(false);
        const msg = err.error?.mensaje || 'Error al enviar instrucciones.';
        this.alertService.error(msg);
      }
    });
  }
}
