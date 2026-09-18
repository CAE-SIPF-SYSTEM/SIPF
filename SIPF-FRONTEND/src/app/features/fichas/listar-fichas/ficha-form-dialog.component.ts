import { Component, Inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, Validators, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { FichaResponse } from '../../../core/entities/ficha.model';
import { ProgramaResponse } from '../../../core/entities/programa.model';
import { ProgramaService } from '../../../core/use-cases/programa.service';

export interface FichaFormDialogData {
  mode: 'create' | 'edit';
  ficha?: FichaResponse;
}

@Component({
  selector: 'app-ficha-form-dialog',
  standalone: true,
  imports: [
    CommonModule, FormsModule, ReactiveFormsModule,
    MatDialogModule, MatFormFieldModule, MatInputModule, 
    MatSelectModule, MatButtonModule, MatIconModule
  ],
  template: `
    <h2 mat-dialog-title class="flex items-center gap-2">
      <mat-icon class="text-[#136E61]">school</mat-icon>
      {{ data.mode === 'create' ? 'Crear Ficha' : 'Editar Ficha' }}
    </h2>
    <mat-dialog-content class="mat-typography pb-4">
      <form [formGroup]="form" class="flex flex-col gap-4 mt-2 min-w-[350px] md:min-w-[500px]">
        
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <mat-form-field appearance="outline">
            <mat-label>Código de Ficha</mat-label>
            <input matInput formControlName="codigoFicha" placeholder="Ej: 2834451">
            <mat-error *ngIf="form.get('codigoFicha')?.hasError('required')">Obligatorio</mat-error>
          </mat-form-field>

          <mat-form-field appearance="outline">
            <mat-label>Programa de Formación</mat-label>
            <mat-select formControlName="programaId">
              <mat-option *ngFor="let prog of programas()" [value]="prog.id">
                {{ prog.nombre }} ({{ prog.jornada }})
              </mat-option>
            </mat-select>
            <mat-error *ngIf="form.get('programaId')?.hasError('required')">Obligatorio</mat-error>
          </mat-form-field>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <mat-form-field appearance="outline">
            <mat-label>Fecha de Inicio</mat-label>
            <input matInput type="date" formControlName="fechaInicio">
            <mat-error *ngIf="form.get('fechaInicio')?.hasError('required')">Obligatorio</mat-error>
          </mat-form-field>

          <mat-form-field appearance="outline">
            <mat-label>Fecha de Fin</mat-label>
            <input matInput type="date" formControlName="fechaFin">
            <mat-error *ngIf="form.get('fechaFin')?.hasError('required')">Obligatorio</mat-error>
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
export class FichaFormDialogComponent implements OnInit {
  form: FormGroup;
  isSaving = signal(false);
  programas = signal<ProgramaResponse[]>([]);

  constructor(
    private fb: FormBuilder,
    private programaService: ProgramaService,
    public dialogRef: MatDialogRef<FichaFormDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: FichaFormDialogData
  ) {
    if (data.mode === 'create') {
      this.form = this.fb.group({
        codigoFicha: ['', [Validators.required]],
        programaId: [null, [Validators.required]],
        fechaInicio: ['', [Validators.required]],
        fechaFin: ['', [Validators.required]]
      });
    } else {
      // Parse ISO dates for HTML date input (YYYY-MM-DD)
      const fInicio = data.ficha?.fechaInicio ? new Date(data.ficha.fechaInicio).toISOString().split('T')[0] : '';
      const fFin = data.ficha?.fechaFin ? new Date(data.ficha.fechaFin).toISOString().split('T')[0] : '';
      
      this.form = this.fb.group({
        codigoFicha: [data.ficha?.codigoFicha || '', [Validators.required]],
        programaId: [data.ficha?.programaId || null, [Validators.required]],
        fechaInicio: [fInicio, [Validators.required]],
        fechaFin: [fFin, [Validators.required]]
      });
    }
  }

  ngOnInit() {
    this.programaService.getAll().subscribe({
      next: (data: ProgramaResponse[]) => this.programas.set(data),
      error: () => console.error('Error loading programs')
    });
  }

  onSubmit() {
    if (this.form.valid) {
      const val = this.form.value;
      if (new Date(val.fechaInicio) > new Date(val.fechaFin)) {
        alert('La fecha de fin debe ser posterior a la de inicio');
        return;
      }
      
      const payload = {
        ...val,
        fechaInicio: new Date(val.fechaInicio + 'T00:00:00').toISOString(),
        fechaFin: new Date(val.fechaFin + 'T00:00:00').toISOString()
      };
      this.dialogRef.close({ action: 'save', value: payload });
    }
  }

  onDelete() {
    this.dialogRef.close({ action: 'delete' });
  }
}
