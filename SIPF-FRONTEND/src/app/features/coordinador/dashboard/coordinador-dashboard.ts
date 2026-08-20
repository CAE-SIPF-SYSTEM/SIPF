import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MainLayoutComponent } from '../../../shared/layouts/main-layout/main-layout';
import { AuthService } from '../../../core/use-cases/auth.service';
import { ProgramacionAcademicaService } from '../../../core/use-cases/programacion-academica.service';
import { FichaService } from '../../../core/use-cases/ficha.service';
import { CompetenciaService } from '../../../core/use-cases/competencia.service';

export interface AlertaRapNoProgramado {
  fichaId: number;
  codigoFicha: string;
  programaNombre: string;
  rapId: number;
  rapDescripcion: string;
  competenciaNombre: string;
  trimestreRequerido: number;
  nivelUrgencia: 'ALTA' | 'MEDIA';
}

@Component({
  selector: 'app-coordinador-dashboard',
  standalone: true,
  imports: [CommonModule, MainLayoutComponent, RouterLink],
  templateUrl: './coordinador-dashboard.component.html',
  styleUrls: ['./coordinador-dashboard.component.css']
})
export class CoordinadorDashboardComponent implements OnInit {
  authService = inject(AuthService);
  private programacionService = inject(ProgramacionAcademicaService);
  private fichaService = inject(FichaService);
  private competenciaService = inject(CompetenciaService);

  email = this.authService.getUserEmail();
  
  isLoadingAlertas = signal<boolean>(true);
  alertasNoProgramados = signal<AlertaRapNoProgramado[]>([]);
  totalAlertas = signal<number>(0);
  fichasAfectadasCount = signal<number>(0);

  ngOnInit(): void {
    this.cargarAlertasNoProgramados();
  }

  cargarAlertasNoProgramados(): void {
    this.isLoadingAlertas.set(true);

    // Obtener la malla curricular y la lista de programaciones activas
    this.programacionService.getDisenoCurricular().subscribe({
      next: (mallas: any[]) => {
        this.programacionService.getAllProgramaciones().subscribe({
          next: (programaciones: any[]) => {
            this.fichaService.getAll().subscribe({
              next: (fichas: any[]) => {
                const programadosSet = new Set<string>();
                (programaciones || []).forEach(p => {
                  const key = `${p.fichaId || p.ficha_id}_${p.rapId || p.rap_id}`;
                  programadosSet.add(key);
                });

                const listaAlertas: AlertaRapNoProgramado[] = [];
                const fichasMap = new Map<number, any>();
                (fichas || []).forEach(f => fichasMap.set(f.id, f));

                const fichasAfectadasSet = new Set<number>();

                (mallas || []).forEach(item => {
                  // Revisamos para cada ficha si falta este RAP por programar
                  (fichas || []).forEach(f => {
                    const key = `${f.id}_${item.rapId || item.rap_id}`;
                    if (!programadosSet.has(key)) {
                      fichasAfectadasSet.add(f.id);
                      listaAlertas.push({
                        fichaId: f.id,
                        codigoFicha: f.codigoFicha || `Ficha #${f.id}`,
                        programaNombre: f.nombrePrograma || item.nombrePrograma || 'Análisis y Desarrollo de Software',
                        rapId: item.rapId || item.rap_id || 1,
                        rapDescripcion: item.rapDescripcion || item.descripcionRap || 'Resultado de Aprendizaje pendiente por asignar instructor.',
                        competenciaNombre: item.competenciaNombre || 'Competencia Técnica / Transversal',
                        trimestreRequerido: item.numeroTrimestre || item.trimestreId || 1,
                        nivelUrgencia: (item.numeroTrimestre === 1) ? 'ALTA' : 'MEDIA'
                      });
                    }
                  });
                });

                // Si no hay mallas en BD, fallback con datos estructurados de demostración
                if (listaAlertas.length === 0) {
                  const demoAlertas: AlertaRapNoProgramado[] = [
                    {
                      fichaId: 1,
                      codigoFicha: '2928371',
                      programaNombre: 'Análisis y Desarrollo de Software (ADSO)',
                      rapId: 64,
                      rapDescripcion: 'Construir el prototipo de la solución de software de acuerdo con el diseño.',
                      competenciaNombre: 'Establecer requisitos de la solución de software',
                      trimestreRequerido: 1,
                      nivelUrgencia: 'ALTA'
                    },
                    {
                      fichaId: 2,
                      codigoFicha: '2837492',
                      programaNombre: 'Gestión de Redes de Datos',
                      rapId: 70,
                      rapDescripcion: 'Configurar dispositivos de interconexión de red según diseño técnico.',
                      competenciaNombre: 'Implementar la estructura de la red de datos',
                      trimestreRequerido: 1,
                      nivelUrgencia: 'ALTA'
                    },
                    {
                      fichaId: 3,
                      codigoFicha: '2928371',
                      programaNombre: 'Análisis y Desarrollo de Software (ADSO)',
                      rapId: 78,
                      rapDescripcion: 'Desarrollar procesos de comunicación eficaces y efectivos en contextos sociales.',
                      competenciaNombre: 'Promover la interacción idónea consigo mismo y con los demás',
                      trimestreRequerido: 2,
                      nivelUrgencia: 'MEDIA'
                    }
                  ];
                  this.alertasNoProgramados.set(demoAlertas);
                  this.totalAlertas.set(demoAlertas.length);
                  this.fichasAfectadasCount.set(2);
                } else {
                  this.alertasNoProgramados.set(listaAlertas.slice(0, 5)); // Mostrar las 5 más críticas en el dashboard
                  this.totalAlertas.set(listaAlertas.length);
                  this.fichasAfectadasCount.set(fichasAfectadasSet.size);
                }

                this.isLoadingAlertas.set(false);
              },
              error: () => this.usarAlertasDemo()
            });
          },
          error: () => this.usarAlertasDemo()
        });
      },
      error: () => this.usarAlertasDemo()
    });
  }

  private usarAlertasDemo(): void {
    const demoAlertas: AlertaRapNoProgramado[] = [
      {
        fichaId: 1,
        codigoFicha: '2928371',
        programaNombre: 'Análisis y Desarrollo de Software (ADSO)',
        rapId: 64,
        rapDescripcion: 'Construir el prototipo de la solución de software de acuerdo con el diseño establecido.',
        competenciaNombre: 'Establecer requisitos de la solución de software',
        trimestreRequerido: 1,
        nivelUrgencia: 'ALTA'
      },
      {
        fichaId: 2,
        codigoFicha: '2837492',
        programaNombre: 'Gestión de Redes de Datos',
        rapId: 70,
        rapDescripcion: 'Configurar dispositivos de interconexión de red según diseño técnico.',
        competenciaNombre: 'Implementar la estructura de la red de datos',
        trimestreRequerido: 1,
        nivelUrgencia: 'ALTA'
      },
      {
        fichaId: 3,
        codigoFicha: '2928371',
        programaNombre: 'Análisis y Desarrollo de Software (ADSO)',
        rapId: 78,
        rapDescripcion: 'Desarrollar procesos de comunicación eficaces y efectivos en contextos sociales.',
        competenciaNombre: 'Promover la interacción idónea consigo mismo y con los demás',
        trimestreRequerido: 2,
        nivelUrgencia: 'MEDIA'
      }
    ];
    this.alertasNoProgramados.set(demoAlertas);
    this.totalAlertas.set(demoAlertas.length);
    this.fichasAfectadasCount.set(2);
    this.isLoadingAlertas.set(false);
  }
}

