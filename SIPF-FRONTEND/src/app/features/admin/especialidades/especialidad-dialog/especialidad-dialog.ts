import { Component, Inject, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { EspecialidadResponse } from '../../../../core/entities/especialidad.model';

@Component({
  selector: 'app-especialidad-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ],
  templateUrl: './especialidad-dialog.html',
  styleUrls: ['./especialidad-dialog.css']
})
export class EspecialidadDialogComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  form!: FormGroup;
  isEditMode = false;

  constructor(
    public dialogRef: MatDialogRef<EspecialidadDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: EspecialidadResponse | null
  ) {
    this.isEditMode = !!data;
  }

  ngOnInit(): void {
    this.form = this.fb.group({
      nombreEspecialidad: [this.data?.nombreEspecialidad || '', [Validators.required, Validators.minLength(3)]]
    });
  }

  onSubmit(event: Event): void {
    event.preventDefault();
    if (this.form.valid) {
      this.dialogRef.close(this.form.value);
    } else {
      this.form.markAllAsTouched();
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
