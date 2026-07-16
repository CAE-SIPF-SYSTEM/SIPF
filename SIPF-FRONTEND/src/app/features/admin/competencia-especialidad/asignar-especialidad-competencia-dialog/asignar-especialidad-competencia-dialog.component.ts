import { Component, Inject, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  MAT_DIALOG_DATA,
  MatDialogRef,
  MatDialogModule,
} from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { FormsModule } from '@angular/forms';
import { EspecialidadService } from '../../../../core/use-cases/especialidad.service';
import { EspecialidadResponse } from '../../../../core/entities/especialidad.model';
import { CompetenciaEspecialidadService } from '../services/competencia-especialidad.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-asignar-especialidad-competencia-dialog',
  standalone: true,
  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule,
    MatSelectModule,
    MatFormFieldModule,
    MatIconModule,
    MatProgressSpinnerModule,
    FormsModule,
  ],
  templateUrl: './asignar-especialidad-competencia-dialog.component.html',
})
export class AsignarEspecialidadCompetenciaDialogComponent {
  private dialogRef = inject(MatDialogRef<AsignarEspecialidadCompetenciaDialogComponent>);
  private especialidadService = inject(EspecialidadService);
  private asignacionService = inject(CompetenciaEspecialidadService);

  especialidades = signal<EspecialidadResponse[]>([]);
  isLoading = signal(true);
  isSaving = signal(false);

  selectedEspecialidadId: number | null = null;
  competenciaId: number;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { competenciaId: number; currentEspecialidadId: number | null; isEdit: boolean; nombreCompetencia: string }
  ) {
    this.competenciaId = data.competenciaId;
    this.selectedEspecialidadId = data.currentEspecialidadId;
    this.loadEspecialidades();
  }

  loadEspecialidades() {
    this.especialidadService.getAll().subscribe({
      next: (data: EspecialidadResponse[]) => {
        this.especialidades.set(data);
        this.isLoading.set(false);
      },
      error: () => {
        this.isLoading.set(false);
        Swal.fire('Error', 'No se pudieron cargar las especialidades', 'error');
      },
    });
  }

  onCancel(): void {
    this.dialogRef.close(false);
  }

  onSave(): void {
    if (!this.selectedEspecialidadId) {
      Swal.fire('Atención', 'Debes seleccionar una especialidad', 'warning');
      return;
    }

    this.isSaving.set(true);

    const request = {
      competenciaId: this.competenciaId,
      especialidadId: this.selectedEspecialidadId,
    };

    const action$ = this.data.isEdit
      ? this.asignacionService.editarEspecialidad(this.competenciaId, request)
      : this.asignacionService.asignarEspecialidad(request);

    action$.subscribe({
      next: () => {
        this.isSaving.set(false);
        Swal.fire(
          '¡Éxito!',
          this.data.isEdit ? 'Especialidad actualizada.' : 'Especialidad asignada correctamente.',
          'success'
        );
        this.dialogRef.close(true);
      },
      error: (err) => {
        this.isSaving.set(false);
        const errorMsg = err.error?.message || 'Hubo un error al guardar la asignación';
        Swal.fire('Error', errorMsg, 'error');
      },
    });
  }
}
