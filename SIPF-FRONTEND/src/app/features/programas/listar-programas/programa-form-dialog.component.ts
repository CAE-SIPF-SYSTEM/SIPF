import { Component, Inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, Validators, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ProgramaResponse } from '../../../core/entities/programa.model';

export interface ProgramaFormDialogData {
  mode: 'create' | 'edit';
  programa?: ProgramaResponse;
}

@Component({
  selector: 'app-programa-form-dialog',
  standalone: true,
  imports: [
    CommonModule, FormsModule, ReactiveFormsModule,
    MatDialogModule, MatFormFieldModule, MatInputModule, 
    MatSelectModule, MatButtonModule, MatIconModule
  ],
  template: `
    <h2 mat-dialog-title class="flex items-center gap-2">
      <mat-icon class="text-[#136E61]">class</mat-icon>
      {{ data.mode === 'create' ? 'Crear Programa' : 'Editar Programa' }}
    </h2>
    <mat-dialog-content class="mat-typography pb-4">
      <form [formGroup]="form" class="flex flex-col gap-4 mt-2 min-w-[350px] md:min-w-[500px]">
        
        <mat-form-field appearance="outline">
          <mat-label>Nombre del Programa</mat-label>
          <input matInput formControlName="nombre" placeholder="Ej: Análisis y Desarrollo de Software">
          <mat-error *ngIf="form.get('nombre')?.hasError('required')">El nombre es obligatorio</mat-error>
        </mat-form-field>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <mat-form-field appearance="outline">
            <mat-label>Nivel de Formación</mat-label>
            <mat-select formControlName="nivelFormacion">
              <mat-option value="TECNICO">Técnico</mat-option>
              <mat-option value="TECNOLOGO">Tecnólogo</mat-option>
              <mat-option value="ESPECIALIZACION">Especialización</mat-option>
              <mat-option value="OPERARIO">Operario</mat-option>
              <mat-option value="AUXILIAR">Auxiliar</mat-option>
            </mat-select>
            <mat-error *ngIf="form.get('nivelFormacion')?.hasError('required')">Obligatorio</mat-error>
          </mat-form-field>

          <mat-form-field appearance="outline">
            <mat-label>Jornada</mat-label>
            <mat-select formControlName="jornada">
              <mat-option value="MAÑANA">Mañana</mat-option>
              <mat-option value="TARDE">Tarde</mat-option>
            </mat-select>
            <mat-error *ngIf="form.get('jornada')?.hasError('required')">Obligatorio</mat-error>
          </mat-form-field>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <mat-form-field appearance="outline">
            <mat-label>Municipio</mat-label>
            <input matInput formControlName="municipio" placeholder="Ej: Bogotá">
            <mat-error *ngIf="form.get('municipio')?.hasError('required')">Obligatorio</mat-error>
          </mat-form-field>

          <mat-form-field appearance="outline">
            <mat-label>Duración en Prácticas (meses)</mat-label>
            <input matInput type="number" formControlName="duracionpracticas" placeholder="Ej: 6">
            <mat-error *ngIf="form.get('duracionpracticas')?.hasError('required')">Obligatorio</mat-error>
            <mat-error *ngIf="form.get('duracionpracticas')?.hasError('min')">Mínimo 1</mat-error>
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
export class ProgramaFormDialogComponent {
  form: FormGroup;
  isSaving = signal(false);

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<ProgramaFormDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: ProgramaFormDialogData
  ) {
    if (data.mode === 'create') {
      this.form = this.fb.group({
        nombre: ['', [Validators.required]],
        municipio: ['', [Validators.required]],
        nivelFormacion: ['', [Validators.required]],
        jornada: ['', [Validators.required]],
        duracionpracticas: [null, [Validators.required, Validators.min(1)]]
      });
    } else {
      this.form = this.fb.group({
        nombre: [data.programa?.nombre || '', [Validators.required]],
        municipio: [data.programa?.municipio || '', [Validators.required]],
        nivelFormacion: [data.programa?.nivelFormacion || '', [Validators.required]],
        jornada: [data.programa?.jornada || '', [Validators.required]],
        duracionpracticas: [data.programa?.duracionpracticas || null, [Validators.required, Validators.min(1)]]
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
