import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatCardModule } from '@angular/material/card';
import { MatTooltipModule } from '@angular/material/tooltip';
import { SweetAlertService } from '../../../../core/use-cases/sweet-alert.service';
import { EspecialidadService } from '../../../../core/use-cases/especialidad.service';
import { EspecialidadResponse } from '../../../../core/entities/especialidad.model';
import { EspecialidadDialogComponent } from '../especialidad-dialog/especialidad-dialog';
import { MainLayoutComponent } from '../../../../shared/layouts/main-layout/main-layout';

@Component({
  selector: 'app-admin-especialidades',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatDialogModule,
    MatProgressSpinnerModule,
    MatCardModule,
    MatTooltipModule,
    MainLayoutComponent
  ],
  templateUrl: './admin-especialidades.html',
  styleUrls: ['./admin-especialidades.css']
})
export class AdminEspecialidadesComponent implements OnInit {
  private readonly dialog = inject(MatDialog);
  private readonly sweetAlertService = inject(SweetAlertService);
  private readonly especialidadService = inject(EspecialidadService);

  especialidades = signal<EspecialidadResponse[]>([]);
  isLoading = signal<boolean>(true);
  displayedColumns: string[] = ['id', 'nombreEspecialidad', 'acciones'];

  ngOnInit(): void {
    this.loadEspecialidades();
  }

  loadEspecialidades(): void {
    this.isLoading.set(true);
    this.especialidadService.getAll().subscribe({
      next: (data) => {
        this.especialidades.set(data);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error cargando especialidades', err);
        this.isLoading.set(false);
        this.sweetAlertService.error('Error', 'No se pudieron cargar las especialidades');
      }
    });
  }

  openDialog(especialidad?: EspecialidadResponse): void {
    const dialogRef = this.dialog.open(EspecialidadDialogComponent, {
      width: '400px',
      data: especialidad || null,
      disableClose: true,
      panelClass: 'custom-dialog-container'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (especialidad) {
          this.especialidadService.update(especialidad.id, result).subscribe({
            next: () => {
              this.sweetAlertService.success('¡Actualizado!', 'La especialidad se ha actualizado correctamente.');
              this.loadEspecialidades();
            },
            error: (err) => {
              console.error(err);
              this.sweetAlertService.error('Error', 'No se pudo actualizar la especialidad. ' + (err.error?.message || ''));
            }
          });
        } else {
          this.especialidadService.create(result).subscribe({
            next: () => {
              this.sweetAlertService.success('¡Creado!', 'La especialidad se ha creado correctamente.');
              this.loadEspecialidades();
            },
            error: (err) => {
              console.error(err);
              this.sweetAlertService.error('Error', 'No se pudo crear la especialidad. ' + (err.error?.message || ''));
            }
          });
        }
      }
    });
  }

  async deleteEspecialidad(id: number, event: Event): Promise<void> {
    event.stopPropagation();
    
    const confirmed = await this.sweetAlertService.confirmDelete(
      '¿Eliminar Especialidad?', 
      'Esta acción no se puede deshacer.'
    );

    if (confirmed) {
      this.especialidadService.delete(id).subscribe({
        next: () => {
          this.sweetAlertService.success('¡Eliminado!', 'La especialidad ha sido eliminada.');
          this.loadEspecialidades();
        },
        error: (err) => {
          console.error(err);
          this.sweetAlertService.error('Error', 'No se pudo eliminar la especialidad. Puede que esté en uso.');
        }
      });
    }
  }
}
