import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { FormsModule } from '@angular/forms';
import { CompetenciaService } from '../../../core/use-cases/competencia.service';
import { CrearCompetenciaRequest } from '../../../core/entities/competencia.model';
import { AlertService } from '../../../core/use-cases/alert.service';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-crear-competencia',
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
  templateUrl: './crear-competencia.html',
  styleUrls: ['./crear-competencia.css']
})
export class CrearCompetenciaComponent {
  private dialogRef = inject(MatDialogRef<CrearCompetenciaComponent>);
  private competenciaService = inject(CompetenciaService);
  private alertService = inject(AlertService);

  nuevaCompetencia: CrearCompetenciaRequest = {
    nombre: '',
    tipoCompetencia: 'TECNICA'
  };

  isSubmitting = false;

  onCancel(): void {
    this.dialogRef.close();
  }

  onSubmit(): void {
    if (!this.nuevaCompetencia.nombre.trim()) {
      this.alertService.warning('El nombre de la competencia es requerido');
      return;
    }

    this.isSubmitting = true;
    this.competenciaService.create(this.nuevaCompetencia).subscribe({
      next: () => {
        this.alertService.success('Competencia creada exitosamente');
        this.dialogRef.close(true);
      },
      error: (err) => {
        this.alertService.error('Error al crear la competencia');
        this.isSubmitting = false;
      }
    });
  }
}
