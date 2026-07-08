import { Component, Inject, OnInit, OnDestroy, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { FichaService } from '../../../../core/use-cases/ficha.service';
import { FichaResponse } from '../../../../core/entities/ficha.model';
import { Trimestre } from '../../../../core/entities/trimestre.model';

@Component({
  selector: 'app-trimestre-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatDatepickerModule,
    MatNativeDateModule
  ],
  templateUrl: './trimestre-dialog.html',
  styleUrls: ['./trimestre-dialog.css']
})
export class TrimestreDialogComponent implements OnInit, OnDestroy {
  private readonly fb = inject(FormBuilder);
  private readonly fichaService = inject(FichaService);

  form!: FormGroup;
  fichas = signal<FichaResponse[]>([]);
  isEditMode = false;
  numerosTrimestre = [1, 2, 3, 4, 5, 6, 7, 8, 9];

  private sub?: Subscription;

  constructor(
    public dialogRef: MatDialogRef<TrimestreDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Trimestre | null
  ) {
    this.isEditMode = !!data;
    this.initForm();
  }

  ngOnInit(): void {
    this.loadFichas();
    this.setupDateCalculation();
  }

  ngOnDestroy(): void {
    if (this.sub) {
      this.sub.unsubscribe();
    }
  }

  private setupDateCalculation(): void {
    this.sub = this.form.get('fechaInicio')?.valueChanges.subscribe(fecha => {
      if (fecha) {
        const date = new Date(fecha);
        // 11 semanas = 77 días
        date.setDate(date.getDate() + 77);
        this.form.patchValue({ fechaFin: date });
      }
    });
  }

  private initForm(): void {
    this.form = this.fb.group({
      fichaId: [{ value: this.data?.fichaId || '', disabled: this.isEditMode }, Validators.required],
      anio: [this.data?.anio || new Date().getFullYear(), [Validators.required, Validators.min(2000)]],
      numeroTrimestre: [this.data?.numeroTrimestre || '', Validators.required],
      fechaInicio: [this.data?.fechaInicio || '', Validators.required],
      fechaFin: [this.data?.fechaFin || '', Validators.required]
    });
  }

  private loadFichas(): void {
    this.fichaService.getAll().subscribe({
      next: (fichas) => this.fichas.set(fichas),
      error: (err) => console.error('Error cargando fichas', err)
    });
  }

  onSubmit(): void {
    if (this.form.valid) {
      const formValue = this.form.getRawValue();
      
      // Validar que la fecha de fin no sea menor a la fecha de inicio
      const inicio = new Date(formValue.fechaInicio);
      const fin = new Date(formValue.fechaFin);
      
      if (fin < inicio) {
        this.form.get('fechaFin')?.setErrors({ invalidDateRange: true });
        return;
      }

      this.dialogRef.close(formValue);
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
