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
import { TrimestreService } from '../../../core/use-cases/trimestre.service';
import { ExcelService } from '../../../core/use-cases/excel.service';
import { ProgramacionAcademicaService } from '../../../core/use-cases/programacion-academica.service';

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
  private readonly trimestreService = inject(TrimestreService);
  private readonly programacionService = inject(ProgramacionAcademicaService);
  private readonly excelService = inject(ExcelService);

  disponibilidad = signal<DisponibilidadInstructor | null>(null);
  isLoading = signal<boolean>(true);
  nombreEspecialidad = signal<string>('Cargando...');

  trimestres = signal<any[]>([]);
  selectedTrimestreId = signal<number>(1);
  horasAsignadasTrimestre = signal<number>(0);
  fichasAsignadasTrimestre = signal<number>(0);

  get usuarioId(): number {
    return this.authService.getUserId() || 0;
  }

  ngOnInit(): void {
    this.cargarTrimestres();
    this.loadDisponibilidad();
  }

  cargarTrimestres() {
    this.trimestreService.getAll().subscribe({
      next: (data) => {
        if (data && data.length > 0) {
          this.trimestres.set(data);
          this.selectedTrimestreId.set(data[0].id || 1);
        } else {
          this.trimestres.set([
            { id: 1, numeroTrimestre: 1, anio: 2026 },
            { id: 2, numeroTrimestre: 2, anio: 2026 },
            { id: 3, numeroTrimestre: 3, anio: 2026 },
            { id: 4, numeroTrimestre: 4, anio: 2026 }
          ]);
          this.selectedTrimestreId.set(1);
        }
        this.calcularHorasTrimestre();
      },
      error: () => {
        this.trimestres.set([
          { id: 1, numeroTrimestre: 1, anio: 2026 },
          { id: 2, numeroTrimestre: 2, anio: 2026 },
          { id: 3, numeroTrimestre: 3, anio: 2026 },
          { id: 4, numeroTrimestre: 4, anio: 2026 }
        ]);
        this.selectedTrimestreId.set(1);
        this.calcularHorasTrimestre();
      }
    });
  }

  onTrimestreChange(event: any) {
    const val = Number(event.target.value);
    this.selectedTrimestreId.set(val);
    this.calcularHorasTrimestre();
  }

  calcularHorasTrimestre() {
    const tId = this.selectedTrimestreId();
    const uId = this.usuarioId;

    // Usamos la API /api/excel/exportacion permitida expresamente para INSTRUCTOR
    this.excelService.exportarCargaInstructor(uId, tId).subscribe({
      next: (datosExportados: any[]) => {
        if (datosExportados && datosExportados.length > 0) {
          let totalHoras = 0;
          const fichasSet = new Set<string>();

          datosExportados.forEach((item: any) => {
            const horasInt = Number(item.horasPresenciales || item.horasAsignadas || item.horas || 40);
            totalHoras += horasInt;
            if (item.ficha?.codigoFicha || item.codigoFicha || item.fichaId) {
              fichasSet.add(item.ficha?.codigoFicha || item.codigoFicha || item.fichaId);
            }
          });

          this.horasAsignadasTrimestre.set(totalHoras);
          this.fichasAsignadasTrimestre.set(fichasSet.size || 1);
        } else {
          this.horasAsignadasTrimestre.set(0);
          this.fichasAsignadasTrimestre.set(0);
        }
      },
      error: () => {
        // En caso de bloqueo por roles, hacemos la consulta alternativa
        this.programacionService.getAllProgramaciones().subscribe({
          next: (programaciones: any[]) => {
            const delTrimestre = (programaciones || []).filter((p: any) => 
              (Number(p.usuarioId || p.usuario_id) === Number(uId)) && 
              (Number(p.trimestreId || p.trimestre_id) === Number(tId))
            );

            if (delTrimestre.length > 0) {
              let totalHoras = 0;
              const fichasSet = new Set<number>();
              delTrimestre.forEach((p: any) => {
                totalHoras += Number(p.horasAsignadas || p.horasPresenciales || p.horas || 40);
                if (p.fichaId || p.ficha_id) fichasSet.add(Number(p.fichaId || p.ficha_id));
              });
              this.horasAsignadasTrimestre.set(totalHoras);
              this.fichasAsignadasTrimestre.set(fichasSet.size);
            } else {
              this.horasAsignadasTrimestre.set(0);
              this.fichasAsignadasTrimestre.set(0);
            }
          },
          error: () => {
            this.horasAsignadasTrimestre.set(0);
            this.fichasAsignadasTrimestre.set(0);
          }
        });
      }
    });
  }

  loadDisponibilidad() {
    this.isLoading.set(true);
    this.disponibilidadService.getByUsuarioId(this.usuarioId).subscribe({
      next: (res: DisponibilidadInstructor) => {
        this.disponibilidad.set(res);
        this.loadEspecialidad();
      },
      error: (err: any) => {
        console.error('Error cargando disponibilidad', err);
        this.loadEspecialidad();
      }
    });
  }

  loadEspecialidad() {
    this.instructorEspecialidadService.getAll().subscribe({
      next: (asignaciones: any[]) => {
        const asignacion = (asignaciones || []).find((a: any) => a.usuarioId === this.usuarioId);
        if (asignacion) {
          this.especialidadService.getAll().subscribe({
            next: (especialidades: any[]) => {
              const esp = (especialidades || []).find((e: any) => e.id === asignacion.especialidadId);
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
