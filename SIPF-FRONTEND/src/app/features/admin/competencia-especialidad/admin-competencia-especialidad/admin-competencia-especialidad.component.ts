import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { AsignarEspecialidadCompetenciaDialogComponent } from '../asignar-especialidad-competencia-dialog/asignar-especialidad-competencia-dialog.component';
import { CompetenciaEspecialidadService } from '../services/competencia-especialidad.service';
import { CompetenciaEspecialidadResponseDTO } from '../models/competencia-especialidad.model';
import { CompetenciaService } from '../../../../core/use-cases/competencia.service';
import { CompetenciaResponse } from '../../../../core/entities/competencia.model';
import { EspecialidadService } from '../../../../core/use-cases/especialidad.service';
import { EspecialidadResponse } from '../../../../core/entities/especialidad.model';
import { MainLayoutComponent } from '../../../../shared/layouts/main-layout/main-layout';
import Swal from 'sweetalert2';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-admin-competencia-especialidad',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatProgressSpinnerModule,
    MatPaginatorModule,
    MainLayoutComponent
  ],
  templateUrl: './admin-competencia-especialidad.component.html',
})
export class AdminCompetenciaEspecialidadComponent implements OnInit {
  private dialog = inject(MatDialog);
  private asignacionService = inject(CompetenciaEspecialidadService);
  private competenciaService = inject(CompetenciaService);
  private especialidadService = inject(EspecialidadService);

  competencias = signal<CompetenciaResponse[]>([]);
  especialidades = signal<EspecialidadResponse[]>([]);
  asignaciones = signal<CompetenciaEspecialidadResponseDTO[]>([]);
  isLoading = signal(true);

  // Pagination
  paginatedCompetencias = signal<CompetenciaResponse[]>([]);
  pageSize = 10;
  pageIndex = 0;

  displayedColumns: string[] = ['codigo', 'nombre', 'tipo', 'especialidad', 'acciones'];

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.isLoading.set(true);
    forkJoin({
      competencias: this.competenciaService.getAll(),
      especialidades: this.especialidadService.getAll(),
      asignaciones: this.asignacionService.listarAsignaciones()
    }).subscribe({
      next: (res) => {
        this.competencias.set(res.competencias);
        this.especialidades.set(res.especialidades);
        this.asignaciones.set(res.asignaciones);
        this.updatePaginatedData();
        this.isLoading.set(false);
      },
      error: () => {
        this.isLoading.set(false);
        Swal.fire('Error', 'No se pudieron cargar los datos', 'error');
      }
    });
  }

  updatePaginatedData(): void {
    const startIndex = this.pageIndex * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    this.paginatedCompetencias.set(this.competencias().slice(startIndex, endIndex));
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.updatePaginatedData();
  }

  getEspecialidad(competenciaId: number): EspecialidadResponse | null {
    const asignacion = this.asignaciones().find(a => a.competenciaId === competenciaId);
    if (!asignacion) return null;
    const esp = this.especialidades().find(e => e.id === asignacion.especialidadId);
    return esp || null;
  }

  getEspecialidadNombre(competenciaId: number): string | null {
    const esp = this.getEspecialidad(competenciaId);
    return esp ? esp.nombreEspecialidad : null;
  }

  openAsignarDialog(competencia: CompetenciaResponse): void {
    const currentEsp = this.getEspecialidad(competencia.id);
    
    const dialogRef = this.dialog.open(AsignarEspecialidadCompetenciaDialogComponent, {
      width: '500px',
      data: {
        competenciaId: competencia.id,
        currentEspecialidadId: currentEsp ? currentEsp.id : null,
        isEdit: !!currentEsp,
        nombreCompetencia: competencia.nombre
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData(); // Recargar datos
      }
    });
  }

  removeAsignacion(competencia: CompetenciaResponse): void {
    Swal.fire({
      title: '¿Estás seguro?',
      text: `Se removerá la especialidad de la competencia ${competencia.nombre}.`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Sí, remover',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.asignacionService.desasignarEspecialidad(competencia.id).subscribe({
          next: () => {
            Swal.fire('¡Removido!', 'La especialidad ha sido removida.', 'success');
            this.loadData();
          },
          error: (err) => {
            Swal.fire('Error', err.error?.message || 'No se pudo remover', 'error');
          }
        });
      }
    });
  }
}
