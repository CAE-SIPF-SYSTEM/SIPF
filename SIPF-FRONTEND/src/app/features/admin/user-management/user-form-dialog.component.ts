import { Component, Inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, Validators, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { UsuarioResponse } from '../../../core/entities/user.model';

export interface UserFormDialogData {
  mode: 'create' | 'edit';
  user?: UsuarioResponse;
}

import { passwordValidator } from '../../../core/validators/password.validator';

@Component({
  selector: 'app-user-form-dialog',
  standalone: true,
  imports: [
    CommonModule, FormsModule, ReactiveFormsModule,
    MatDialogModule, MatFormFieldModule, MatInputModule, 
    MatSelectModule, MatButtonModule, MatIconModule
  ],
  template: `
    <h2 mat-dialog-title>{{ data.mode === 'create' ? 'Crear Usuario' : 'Editar Usuario' }}</h2>
    <mat-dialog-content class="mat-typography pb-4">
      <form [formGroup]="form" class="flex flex-col gap-4 mt-2 min-w-[350px] md:min-w-[450px]">
        <mat-form-field appearance="outline" *ngIf="data.mode === 'create'">
          <mat-label>Nombre completo</mat-label>
          <input matInput formControlName="nombreCompleto" placeholder="Ingrese el nombre completo">
        </mat-form-field>
        
        <mat-form-field appearance="outline" *ngIf="data.mode === 'create'">
          <mat-label>Documento de Identidad (CC)</mat-label>
          <input matInput type="number" formControlName="documentoIdentidad" placeholder="Ingrese el número de cédula">
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Correo electrónico</mat-label>
          <input matInput type="email" formControlName="correo" placeholder="Ingrese el correo electrónico">
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>{{ data.mode === 'create' ? 'Contraseña inicial' : 'Cambiar Contraseña (Opcional)' }}</mat-label>
          <input matInput type="password" formControlName="contrasena" [placeholder]="data.mode === 'create' ? 'Ingrese una contraseña temporal' : 'Dejar en blanco para conservar'">
          <mat-error *ngIf="form.get('contrasena')?.hasError('passwordStrength')">
            La contraseña debe tener mínimo 8 caracteres, mayúsculas, minúsculas, números y un símbolo.
          </mat-error>
        </mat-form-field>

        <mat-form-field appearance="outline" *ngIf="data.mode === 'create'">
          <mat-label>Teléfono</mat-label>
          <input matInput type="number" formControlName="telefono" placeholder="Ingrese el número de teléfono">
        </mat-form-field>

        <div class="flex gap-4">
          <mat-form-field appearance="outline" class="flex-1">
            <mat-label>Rol</mat-label>
            <mat-select formControlName="rol">
              <mat-option value="ADMINISTRADOR">Administrador</mat-option>
              <mat-option value="INSTRUCTOR">Instructor</mat-option>
              <mat-option value="COORDINADOR">Coordinador</mat-option>
            </mat-select>
          </mat-form-field>

          <mat-form-field appearance="outline" class="flex-1">
            <mat-label>Estado</mat-label>
            <mat-select formControlName="estado">
              <mat-option [value]="true">Activo</mat-option>
              <mat-option [value]="false">Inactivo</mat-option>
            </mat-select>
          </mat-form-field>
        </div>
      </form>
    </mat-dialog-content>
    <mat-dialog-actions align="end" class="px-6 pb-6">
      <button mat-button *ngIf="data.mode === 'edit'" color="warn" (click)="onDelete()" class="mr-auto">Eliminar</button>
      <button mat-button mat-dialog-close>Cancelar</button>
      <button mat-flat-button color="primary" [disabled]="form.invalid || isSaving()" (click)="onSubmit()">
        Guardar
      </button>
    </mat-dialog-actions>
  `
})
export class UserFormDialogComponent {
  form: FormGroup;
  isSaving = signal(false);

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<UserFormDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: UserFormDialogData
  ) {
    if (data.mode === 'create') {
      this.form = this.fb.group({
        nombreCompleto: ['', [Validators.required]],
        correo: ['', [Validators.required, Validators.email]],
        contrasena: ['', [Validators.required, passwordValidator()]],
        rol: ['', [Validators.required]],
        documentoIdentidad: [null, [Validators.required, Validators.min(1)]],
        telefono: [null, [Validators.required, Validators.min(1)]],
        estado: [true]
      });
    } else {
      this.form = this.fb.group({
        correo: [data.user?.correo || '', [Validators.required, Validators.email]],
        contrasena: ['', [passwordValidator()]], // Optional
        rol: [data.user?.rol || '', [Validators.required]],
        estado: [data.user?.estado !== false]
      });
    }
  }

  onSubmit() {
    if (this.form.valid) {
      this.dialogRef.close({ action: 'save', value: this.form.value });
    }
  }

  onDelete() {
    this.dialogRef.close({ action: 'delete' });
  }
}
