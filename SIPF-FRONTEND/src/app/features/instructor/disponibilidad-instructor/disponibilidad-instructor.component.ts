import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MainLayoutComponent } from '../../../shared/layouts/main-layout/main-layout';
import { DisponibilidadInstructorService } from '../../../core/use-cases/disponibilidad-instructor.service';
import { AuthService } from '../../../core/use-cases/auth.service';
import { SweetAlertService } from '../../../core/use-cases/sweet-alert.service';
import { DisponibilidadInstructor } from '../../../core/entities/disponibilidad-instructor.model';
import { DisponibilidadFormDialogComponent } from './disponibilidad-form-dialog.component';

import { EspecialidadService } from '../../../core/use-cases/especialidad.service';
import { InstructorEspecialidadService } from '../../../core/use-cases/instructor-especialidad.service';

@Component({
  selector: 'app-disponibilidad-instructor',
  standalone: true,
  imports: [CommonModule, MainLayoutComponent, MatDialogModule],
  templateUrl: './disponibilidad-instructor.component.html',
  styleUrls: ['./disponibilidad-instructor.component.css']
})
export class DisponibilidadInstructorComponent implements OnInit {
  private readonly dialog = inject(MatDialog);
  private readonly disponibilidadService = inject(DisponibilidadInstructorService);
  private readonly authService = inject(AuthService);
  private readonly sweetAlertService = inject(SweetAlertService);
  private readonly especialidadService = inject(EspecialidadService);
  private readonly instructorEspecialidadService = inject(InstructorEspecialidadService);

  disponibilidad = signal<DisponibilidadInstructor | null>(null);
  isLoading = signal<boolean>(true);
  nombreEspecialidad = signal<string>('Cargando...');

  get usuarioId(): number {
    return this.authService.getUserId() || 0;
  }

  ngOnInit(): void {
    this.loadDisponibilidad();
  }

  loadDisponibilidad() {
    this.isLoading.set(true);
    this.disponibilidadService.getByUsuarioId(this.usuarioId).subscribe({
      next: (res) => {
        this.disponibilidad.set(res);
        this.loadEspecialidad();
      },
      error: (err) => {
        console.error('Error cargando disponibilidad', err);
        this.loadEspecialidad(); // Cargar la especialidad incluso si no hay disponibilidad
      }
    });
  }

  loadEspecialidad() {
    this.instructorEspecialidadService.getAll().subscribe({
      next: (asignaciones) => {
        const asignacion = asignaciones.find(a => a.usuarioId === this.usuarioId);
        if (asignacion) {
          this.especialidadService.getAll().subscribe({
            next: (especialidades) => {
              const esp = especialidades.find(e => e.id === asignacion.especialidadId);
              this.nombreEspecialidad.set(esp ? esp.nombreEspecialidad : 'No Asignada');
              this.isLoading.set(false);
            },
            error: () => this.isLoading.set(false)
          });
        } else {
          this.nombreEspecialidad.set('No Asignada');
          this.isLoading.set(false);
        }
      },
      error: () => {
        this.nombreEspecialidad.set('No Asignada');
        this.isLoading.set(false);
      }
    });
  }

  openManageDialog() {
    const dialogRef = this.dialog.open(DisponibilidadFormDialogComponent, {
      width: '400px',
      data: { disponibilidad: this.disponibilidad(), usuarioId: this.usuarioId }
    });

    dialogRef.afterClosed().subscribe((result: boolean) => {
      if (result) {
        this.loadDisponibilidad();
      }
    });
  }
}
