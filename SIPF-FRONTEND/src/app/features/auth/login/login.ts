import { Component, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/use-cases/auth.service';
import { AlertService } from '../../../core/use-cases/alert.service';

@Component({
  selector: 'app-login',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private alertService = inject(AlertService);
  private router = inject(Router);

  showPassword = signal(false);
  isLoading = signal(false);

  loginForm = this.fb.group({
    correo: ['', [Validators.required, Validators.email]],
    contrasena: ['', [Validators.required]]
  });

  togglePassword() {
    this.showPassword.update(v => !v);
  }

  onSubmit() {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.isLoading.set(true);
    
    this.authService.login(this.loginForm.value as any).subscribe({
      next: (res: any) => {
        this.isLoading.set(false);
        this.alertService.success(`Bienvenido ${res.correo}`);
        // Navigate based on role
        switch(res.rol) {
          case 'ADMINISTRADOR': this.router.navigate(['/admin']); break;
          case 'INSTRUCTOR': this.router.navigate(['/instructor']); break;
          case 'COORDINADOR': this.router.navigate(['/coordinador']); break;
          default: this.router.navigate(['/']);
        }
      },
      error: (err: any) => {
        this.isLoading.set(false);
        const msg = err.error?.mensaje || 'Error al iniciar sesión. Verifica tus credenciales.';
        this.alertService.error(msg);
      }
    });
  }
}
