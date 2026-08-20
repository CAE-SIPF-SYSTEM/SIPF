import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { MainLayoutComponent } from '../../../shared/layouts/main-layout/main-layout';
import { AuthService } from '../../../core/use-cases/auth.service';
import { ProgramacionAcademicaService } from '../../../core/use-cases/programacion-academica.service';
import { FichaService } from '../../../core/use-cases/ficha.service';
import { TrimestreService } from '../../../core/use-cases/trimestre.service';

export interface AlertaRapNoProgramado {
  fichaId: number;
  codigoFicha: string;
  programaNombre: string;
  rapId: number;
  rapDescripcion: string;
  competenciaNombre: string;
  trimestreRequerido: number;
}

@Component({
  selector: 'app-coordinador-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, MainLayoutComponent, RouterLink],
  templateUrl: './coordinador-dashboard.component.html',
  styleUrls: ['./coordinador-dashboard.component.css']
})
export class CoordinadorDashboardComponent implements OnInit {
  authService = inject(AuthService);
  private programacionService = inject(ProgramacionAcademicaService);
  private fichaService = inject(FichaService);
  private trimestreService = inject(TrimestreService);

  email = this.authService.getUserEmail();
  
  isLoadingAlertas = signal<boolean>(true);
  alertasNoProgramados = signal<AlertaRapNoProgramado[]>([]);
  todasLasAlertas: AlertaRapNoProgramado[] = [];

  fichas = signal<any[]>([]);
  selectedFichaId = signal<number | null>(null);

  trimestres = signal<any[]>([]);
  selectedTrimestreId = signal<number>(1);

  ngOnInit(): void {
    this.cargarFichas();
    this.cargarTrimestres();
  }

  cargarFichas(): void {
    this.fichaService.getAll().subscribe({
      next: (data) => {
        if (data && data.length > 0) {
          this.fichas.set(data);
          this.selectedFichaId.set(data[0].id);
        } else {
          this.fichas.set([
            { id: 1, codigoFicha: '2996315 - ADSO' },
            { id: 2, codigoFicha: '2847124 - Redes' }
          ]);
          this.selectedFichaId.set(1);
        }
        this.cargarAlertasNoProgramados();
      },
      error: () => {
        this.fichas.set([
          { id: 1, codigoFicha: '2996315 - ADSO' },
          { id: 2, codigoFicha: '2847124 - Redes' }
        ]);
        this.selectedFichaId.set(1);
        this.cargarAlertasNoProgramados();
      }
    });
  }

  cargarTrimestres(): void {
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
            { id: 4, numeroTrimestre: 4, anio: 2026 },
            { id: 5, numeroTrimestre: 5, anio: 2026 },
            { id: 6, numeroTrimestre: 6, anio: 2026 },
            { id: 7, numeroTrimestre: 7, anio: 2026 }
          ]);
          this.selectedTrimestreId.set(1);
        }
        this.cargarAlertasNoProgramados();
      },
      error: () => {
        this.trimestres.set([
          { id: 1, numeroTrimestre: 1, anio: 2026 },
          { id: 2, numeroTrimestre: 2, anio: 2026 },
          { id: 3, numeroTrimestre: 3, anio: 2026 },
          { id: 4, numeroTrimestre: 4, anio: 2026 },
          { id: 5, numeroTrimestre: 5, anio: 2026 },
          { id: 6, numeroTrimestre: 6, anio: 2026 },
          { id: 7, numeroTrimestre: 7, anio: 2026 }
        ]);
        this.selectedTrimestreId.set(1);
        this.cargarAlertasNoProgramados();
      }
    });
  }

  onTrimestreChange(event: any): void {
    const val = Number(event.target.value);
    this.selectedTrimestreId.set(val);
    this.filtrarAlertas();
  }

  onFichaChange(event: any): void {
    const val = Number(event.target.value);
    this.selectedFichaId.set(val);
    this.filtrarAlertas();
  }

  cargarAlertasNoProgramados(): void {
    this.isLoadingAlertas.set(true);

    this.programacionService.getDisenoCurricular().subscribe({
      next: (mallas: any[]) => {
        this.programacionService.getAllProgramaciones().subscribe({
          next: (programaciones: any[]) => {
            const programadosSet = new Set<string>();
            (programaciones || []).forEach(p => {
              const fId = p.fichaId || p.ficha_id;
              const rId = p.rapId || p.rap_id;
              const tId = p.trimestreId || p.trimestre_id;
              programadosSet.add(`${fId}_${rId}_${tId}`);
            });

            const listaAlertas: AlertaRapNoProgramado[] = [];
            const listFichas = this.fichas().length > 0 ? this.fichas() : [
              { id: 1, codigoFicha: '2996315 - ADSO', nombrePrograma: 'Análisis y Desarrollo de Software' },
              { id: 2, codigoFicha: '2847124 - Redes', nombrePrograma: 'Gestión de Redes de Datos' }
            ];

            (mallas || []).forEach(item => {
              const tReq = item.numeroTrimestre || item.trimestreId || 1;
              listFichas.forEach(f => {
                const key = `${f.id}_${item.rapId || item.rap_id}_${tReq}`;
                if (!programadosSet.has(key)) {
                  listaAlertas.push({
                    fichaId: f.id,
                    codigoFicha: f.codigoFicha || `Ficha #${f.id}`,
                    programaNombre: f.nombrePrograma || item.nombrePrograma || 'Análisis y Desarrollo de Software',
                    rapId: item.rapId || item.rap_id || 1,
                    rapDescripcion: item.rapDescripcion || item.descripcionRap || 'Resultado de Aprendizaje pendiente por asignar instructor.',
                    competenciaNombre: item.competenciaNombre || 'Competencia Técnica / Transversal',
                    trimestreRequerido: tReq
                  });
                }
              });
            });

            this.todasLasAlertas = listaAlertas;
            this.filtrarAlertas();
            this.isLoadingAlertas.set(false);
          },
          error: () => this.usarAlertasDemo()
        });
      },
      error: () => this.usarAlertasDemo()
    });
  }

  filtrarAlertas(): void {
    const tId = this.selectedTrimestreId();
    const fId = this.selectedFichaId();
    
    const filtradas = this.todasLasAlertas.filter(a => {
      const matchTrimestre = a.trimestreRequerido === tId;
      const matchFicha = fId ? Number(a.fichaId) === Number(fId) : true;
      return matchTrimestre && matchFicha;
    });

    this.alertasNoProgramados.set(filtradas);
  }

  private usarAlertasDemo(): void {
    const demoAlertas: AlertaRapNoProgramado[] = [
      {
        fichaId: 1,
        codigoFicha: '2996315 - ADSO',
        programaNombre: 'Análisis y Desarrollo de Software (ADSO)',
        rapId: 64,
        rapDescripcion: 'Construir el prototipo de la solución de software de acuerdo con el diseño establecido.',
        competenciaNombre: 'Establecer requisitos de la solución de software',
        trimestreRequerido: 1
      },
      {
        fichaId: 2,
        codigoFicha: '2847124 - Redes',
        programaNombre: 'Gestión de Redes de Datos',
        rapId: 70,
        rapDescripcion: 'Configurar dispositivos de interconexión de red según diseño técnico.',
        competenciaNombre: 'Implementar la estructura de la red de datos',
        trimestreRequerido: 1
      },
      {
        fichaId: 1,
        codigoFicha: '2996315 - ADSO',
        programaNombre: 'Análisis y Desarrollo de Software (ADSO)',
        rapId: 72,
        rapDescripcion: 'Evaluar requisitos de la solución de software de acuerdo con metodologías de análisis y estándares.',
        competenciaNombre: 'Evaluar requisitos de la solución de software',
        trimestreRequerido: 2
      },
      {
        fichaId: 1,
        codigoFicha: '2996315 - ADSO',
        programaNombre: 'Análisis y Desarrollo de Software (ADSO)',
        rapId: 73,
        rapDescripcion: 'Desarrollar la estructura de datos del sistema de acuerdo con el diseño relacional.',
        competenciaNombre: 'Bases de Datos y Persistencia SQL',
        trimestreRequerido: 2
      }
    ];
    this.todasLasAlertas = demoAlertas;
    this.filtrarAlertas();
    this.isLoadingAlertas.set(false);
  }
}
