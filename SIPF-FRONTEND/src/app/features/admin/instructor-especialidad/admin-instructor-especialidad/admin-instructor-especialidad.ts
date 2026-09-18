import { Component, OnInit, signal, effect, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MainLayoutComponent } from '../../../../shared/layouts/main-layout/main-layout';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { SweetAlertService } from '../../../../core/use-cases/sweet-alert.service';
import { UserService } from '../../../../core/use-cases/user.service';
import { EspecialidadService } from '../../../../core/use-cases/especialidad.service';
import { InstructorEspecialidadService } from '../../../../core/use-cases/instructor-especialidad.service';
import { UsuarioResponse } from '../../../../core/entities/user.model';
import { EspecialidadResponse } from '../../../../core/entities/especialidad.model';
import { InstructorEspecialidadResponse } from '../../../../core/entities/instructor-especialidad.model';
import { AsignarEspecialidadDialogComponent } from '../asignar-especialidad-dialog/asignar-especialidad-dialog';

@Component({
  selector: 'app-admin-instructor-especialidad',
  standalone: true,
  imports: [
    CommonModule,
    MainLayoutComponent,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatDialogModule
  ],
  templateUrl: './admin-instructor-especialidad.html',
  styleUrls: []
})
export class AdminInstructorEspecialidadComponent implements OnInit {
  private userService = inject(UserService);
  private especialidadService = inject(EspecialidadService);
  private instructorEspecialidadService = inject(InstructorEspecialidadService);
  private dialog = inject(MatDialog);
  private sweetAlertService = inject(SweetAlertService);

  instructoresDataSource = signal<UsuarioResponse[]>([]);
  especialidades = signal<EspecialidadResponse[]>([]);
  asignaciones = signal<InstructorEspecialidadResponse[]>([]);
  
  displayedColumns: string[] = ['id', 'nombre', 'correo', 'especialidad', 'acciones'];

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.userService.getByRole('INSTRUCTOR').subscribe({
      next: (data) => this.instructoresDataSource.set(data),
      error: () => this.sweetAlertService.error('Error', 'No se pudieron cargar los instructores')
    });

    this.especialidadService.getAll().subscribe({
      next: (data) => this.especialidades.set(data),
      error: () => this.sweetAlertService.error('Error', 'No se pudieron cargar las especialidades')
    });

    this.instructorEspecialidadService.getAll().subscribe({
      next: (data) => this.asignaciones.set(data),
      error: () => this.sweetAlertService.error('Error', 'No se pudieron cargar las asignaciones')
    });
  }

  getEspecialidadNombre(instructorId: number): string | null {
    const asignacion = this.asignaciones().find(a => a.usuarioId === instructorId);
    if (!asignacion) return null;
    const especialidad = this.especialidades().find(e => e.id === asignacion.especialidadId);
    return especialidad ? especialidad.nombreEspecialidad : null;
  }

  openDialog(instructor: UsuarioResponse, isEdit: boolean): void {
    const dialogRef = this.dialog.open(AsignarEspecialidadDialogComponent, {
      width: '500px',
      data: {
        instructor,
        especialidades: this.especialidades(),
        isEdit
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (isEdit) {
          this.instructorEspecialidadService.update(instructor.id, {
            usuarioId: instructor.id,
            especialidadId: result.especialidadId
          }).subscribe({
            next: () => {
              this.sweetAlertService.success('¡Actualizado!', 'Especialidad cambiada correctamente');
              this.loadData();
            },
            error: (err) => this.sweetAlertService.error('Error', err.error?.mensaje || 'Error al actualizar')
          });
        } else {
          this.instructorEspecialidadService.assign({
            usuarioId: instructor.id,
            especialidadId: result.especialidadId
          }).subscribe({
            next: () => {
              this.sweetAlertService.success('¡Asignado!', 'Especialidad asignada correctamente');
              this.loadData();
            },
            error: (err) => this.sweetAlertService.error('Error', err.error?.mensaje || 'Error al asignar')
          });
        }
      }
    });
  }

  async unassign(instructorId: number): Promise<void> {
    const instructor = this.instructoresDataSource().find(i => i.id === instructorId);
    const nombre = instructor ? `${instructor.nombre} ${instructor.apellido}` : 'este instructor';

    const confirm = await this.sweetAlertService.confirmDelete(
      'Remover Especialidad',
      `¿Estás seguro de que deseas quitarle la especialidad a ${nombre}?`
    );

    if (confirm) {
      this.instructorEspecialidadService.unassign(instructorId).subscribe({
        next: () => {
          this.sweetAlertService.success('¡Removido!', 'Se ha quitado la especialidad correctamente');
          this.loadData();
        },
        error: (err) => this.sweetAlertService.error('Error', err.error?.mensaje || 'Error al remover la especialidad')
      });
    }
  }
}
