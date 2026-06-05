import { Component, signal, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators, AbstractControl } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/use-cases/auth.service';
import { AlertService } from '../../../core/use-cases/alert.service';

function passwordsMatchValidator(control: AbstractControl): { [key: string]: boolean } | null {
  const newPassword = control.get('nuevaContrasena');
  const confirmPassword = control.get('confirmarContrasena');
  if (newPassword && confirmPassword && newPassword.value !== confirmPassword.value) {
    return { 'mismatch': true };
  }
  return null;
}

@Component({
  selector: 'app-restablecer-contrasena',
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
            <h2 class="text-3xl font-bold text-[#334155] mb-2">Cambio de Contraseña</h2>
            <p class="text-[#64748b] text-sm">Ingresa tu nueva contraseña</p>
          </div>

          <form [formGroup]="form" (ngSubmit)="onSubmit()" class="space-y-6">
            <div>
              <label class="block text-sm font-semibold text-[#334155] mb-2" for="nuevaContrasena">Contraseña</label>
              <div class="relative">
                <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none text-[#94a3b8]">
                  <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect width="18" height="11" x="3" y="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg>
                </div>
                <input id="nuevaContrasena" type="password" formControlName="nuevaContrasena" class="input-field pl-10 pr-10 bg-[#f8fafc]" placeholder="Ingresa tu nueva contraseña">
              </div>
              <div *ngIf="form.get('nuevaContrasena')?.invalid && form.get('nuevaContrasena')?.touched" class="text-rose-500 text-xs mt-1 font-medium">
                Debe tener al menos 6 caracteres
              </div>
            </div>

            <div>
              <label class="block text-sm font-semibold text-[#334155] mb-2" for="confirmarContrasena">Repetir contraseña</label>
              <div class="relative">
                <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none text-[#94a3b8]">
                  <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect width="18" height="11" x="3" y="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg>
                </div>
                <input id="confirmarContrasena" type="password" formControlName="confirmarContrasena" class="input-field pl-10 pr-10 bg-[#f8fafc]" placeholder="Repite tu nueva contraseña">
              </div>
              <div *ngIf="form.hasError('mismatch') && form.get('confirmarContrasena')?.touched" class="text-rose-500 text-xs mt-1 font-medium">
                Las contraseñas no coinciden
              </div>
            </div>

            <button type="submit" [disabled]="form.invalid || isLoading()" class="btn-primary w-full py-3 shadow-md">
              <span *ngIf="!isLoading()">Cambiar contraseña</span>
              <div *ngIf="isLoading()" class="spinner"></div>
            </button>
          </form>
          
          <div class="mt-8 text-center">
            <a routerLink="/login" class="text-sm font-medium text-[#64748b] hover:text-brand transition-colors">
              Volver al inicio de sesion
            </a>
          </div>
        </div>
      </div>
    </div>
  `
})
export class RestablecerContrasenaComponent implements OnInit {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private alertService = inject(AlertService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  isLoading = signal(false);
  token = '';

  form = this.fb.group({
    nuevaContrasena: ['', [Validators.required, Validators.minLength(6)]],
    confirmarContrasena: ['', [Validators.required]]
  }, { validators: passwordsMatchValidator });

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      if (params['token']) {
        this.token = params['token'];
      } else {
        this.alertService.error('Token no proporcionado');
        this.router.navigate(['/login']);
      }
    });
  }

  onSubmit() {
    if (this.form.invalid || !this.token) {
      this.form.markAllAsTouched();
      return;
    }

    this.isLoading.set(true);
    
    this.authService.restablecerContrasena(this.token, this.form.value.nuevaContrasena!).subscribe({
      next: () => {
        this.isLoading.set(false);
        this.alertService.success('Contraseña restablecida exitosamente');
        setTimeout(() => this.router.navigate(['/login']), 2000);
      },
      error: (err: any) => {
        this.isLoading.set(false);
        const msg = err.error?.mensaje || 'Error al restablecer la contraseña.';
        this.alertService.error(msg);
      }
    });
  }
}
