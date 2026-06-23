import { Component, inject, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { FormsModule } from '@angular/forms';
import { RapService } from '../../../core/use-cases/rap.service';
import { CrearRapRequest } from '../../../core/entities/rap.model';
import { AlertService } from '../../../core/use-cases/alert.service';
import { MatIconModule } from '@angular/material/icon';
import { CompetenciaResponse } from '../../../core/entities/competencia.model';

@Component({
  selector: 'app-crear-rap',
  standalone: true,
  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatIconModule,
    FormsModule
  ],
  templateUrl: './crear-rap.html',
  styleUrls: ['./crear-rap.css']
})
export class CrearRapComponent {
  private dialogRef = inject(MatDialogRef<CrearRapComponent>);
  private rapService = inject(RapService);
  private alertService = inject(AlertService);

  competencias: CompetenciaResponse[] = [];

  nuevoRap: CrearRapRequest = {
    competenciaId: 0,
    descripcion: '',
    horasPresenciales: 0
  };

  isSubmitting = false;

  constructor(@Inject(MAT_DIALOG_DATA) public data: { competencias: CompetenciaResponse[] }) {
    this.competencias = data.competencias || [];
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  onSubmit(): void {
    if (!this.nuevoRap.competenciaId || !this.nuevoRap.descripcion.trim() || this.nuevoRap.horasPresenciales <= 0) {
      this.alertService.warning('Por favor, completa todos los campos correctamente.');
      return;
    }

    this.isSubmitting = true;
    this.rapService.create(this.nuevoRap).subscribe({
      next: () => {
        this.alertService.success('Resultado de Aprendizaje (RAP) creado exitosamente');
        this.dialogRef.close(true);
      },
      error: (err) => {
        this.alertService.error('Error al crear el RAP');
        this.isSubmitting = false;
      }
    });
  }
}
