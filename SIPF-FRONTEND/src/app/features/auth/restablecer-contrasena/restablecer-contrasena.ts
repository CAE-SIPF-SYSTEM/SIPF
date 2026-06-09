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
  templateUrl: './restablecer-contrasena.component.html',
  styleUrl: './restablecer-contrasena.component.css'
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
