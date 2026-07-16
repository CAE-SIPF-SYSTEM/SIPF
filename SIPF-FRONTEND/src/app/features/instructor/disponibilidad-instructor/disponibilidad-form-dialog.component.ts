import { Component, Inject, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { DisponibilidadInstructorService } from '../../../core/use-cases/disponibilidad-instructor.service';
import { SweetAlertService } from '../../../core/use-cases/sweet-alert.service';
import { DisponibilidadInstructor } from '../../../core/entities/disponibilidad-instructor.model';

@Component({
  selector: 'app-disponibilidad-form-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule
  ],
  templateUrl: './disponibilidad-form-dialog.component.html',
})
export class DisponibilidadFormDialogComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly disponibilidadService = inject(DisponibilidadInstructorService);
  private readonly sweetAlertService = inject(SweetAlertService);

  form!: FormGroup;
  isEditMode = false;
  isSubmitting = signal(false);

  diasSemanas = ['LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES'];

  constructor(
    public dialogRef: MatDialogRef<DisponibilidadFormDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { disponibilidad: DisponibilidadInstructor | null, usuarioId: number }
  ) {}

  ngOnInit(): void {
    this.isEditMode = !!this.data.disponibilidad;
    
    this.form = this.fb.group({
      diasDisponibles: [this.data.disponibilidad?.diasDisponibles || [], [Validators.required]]
    });
  }

  onSubmit() {
    if (this.form.invalid) return;

    this.isSubmitting.set(true);
    const formValue = this.form.getRawValue();

    if (this.isEditMode) {
      this.disponibilidadService.update(this.data.usuarioId, { diasDisponibles: formValue.diasDisponibles }).subscribe({
        next: () => {
          this.sweetAlertService.success('Disponibilidad actualizada exitosamente');
          this.dialogRef.close(true);
        },
        error: (err) => {
          console.error(err);
          this.sweetAlertService.error('Error al actualizar disponibilidad');
          this.isSubmitting.set(false);
        }
      });
    } else {
      const createData = {
        usuarioId: this.data.usuarioId,
        diasDisponibles: formValue.diasDisponibles,
        horasMaximas: 144
      };
      this.disponibilidadService.create(createData).subscribe({
        next: () => {
          this.sweetAlertService.success('Disponibilidad creada exitosamente');
          this.dialogRef.close(true);
        },
        error: (err) => {
          console.error(err);
          this.sweetAlertService.error('Error al crear disponibilidad');
          this.isSubmitting.set(false);
        }
      });
    }
  }

  onCancel() {
    this.dialogRef.close(false);
  }
}
