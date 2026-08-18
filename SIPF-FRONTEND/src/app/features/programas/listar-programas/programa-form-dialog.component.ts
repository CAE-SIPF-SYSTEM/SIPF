import { Component, Inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, Validators, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ProgramaResponse } from '../../../core/entities/programa.model';
import { UbicacionService, MunicipioResponse } from '../../../core/use-cases/ubicacion.service';

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
            <mat-select formControlName="municipio" [compareWith]="compareMunicipios">
              <mat-option *ngFor="let mun of municipiosList()" [value]="mun">
                {{ mun.nombre }}
              </mat-option>
            </mat-select>
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
export class ProgramaFormDialogComponent implements OnInit {
  form: FormGroup;
  isSaving = signal(false);
  municipiosList = signal<MunicipioResponse[]>([]);

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<ProgramaFormDialogComponent>,
    private ubicacionService: UbicacionService,
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

  ngOnInit() {
    this.ubicacionService.obtenerMunicipios().subscribe({
      next: (data) => {
        if (data && data.length > 0) {
          this.municipiosList.set(data);
        } else {
          this.usarMunicipiosDefault();
        }
      },
      error: () => this.usarMunicipiosDefault()
    });
  }

  private usarMunicipiosDefault() {
    this.municipiosList.set([
      { id: 1, nombre: 'Bogotá D.C.' },
      { id: 2, nombre: 'Fusagasugá' },
      { id: 3, nombre: 'Girardot' },
      { id: 4, nombre: 'Soacha' },
      { id: 5, nombre: 'Chía' },
      { id: 6, nombre: 'Zipaquirá' },
      { id: 7, nombre: 'Facatativá' },
      { id: 8, nombre: 'Mosquera' },
      { id: 9, nombre: 'Madrid' },
      { id: 10, nombre: 'Funza' },
      { id: 11, nombre: 'Medellín' },
      { id: 12, nombre: 'Cali' },
      { id: 13, nombre: 'Barranquilla' },
      { id: 14, nombre: 'Cartagena' },
      { id: 15, nombre: 'Bucaramanga' },
      { id: 16, nombre: 'Pereira' },
      { id: 17, nombre: 'Manizales' },
      { id: 18, nombre: 'Ibagué' },
      { id: 19, nombre: 'Villavicencio' },
      { id: 20, nombre: 'Neiva' }
    ]);
  }

  compareMunicipios(m1: any, m2: any): boolean {
    if (!m1 || !m2) return m1 === m2;
    return typeof m1 === 'object' && typeof m2 === 'object' ? (m1.id === m2.id || m1.nombre === m2.nombre) : m1 === m2;
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
