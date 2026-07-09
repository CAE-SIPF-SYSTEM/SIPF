import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { UsuarioResponse } from '../../../../core/entities/user.model';
import { EspecialidadResponse } from '../../../../core/entities/especialidad.model';

export interface AsignarDialogData {
  instructor: UsuarioResponse;
  especialidades: EspecialidadResponse[];
  isEdit: boolean;
}

@Component({
  selector: 'app-asignar-especialidad-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatSelectModule,
    MatButtonModule
  ],
  templateUrl: './asignar-especialidad-dialog.html',
  styleUrls: []
})
export class AsignarEspecialidadDialogComponent {
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<AsignarEspecialidadDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: AsignarDialogData
  ) {
    this.form = this.fb.group({
      especialidadId: ['', Validators.required]
    });
  }

  save() {
    if (this.form.valid) {
      this.dialogRef.close(this.form.value);
    }
  }
}
