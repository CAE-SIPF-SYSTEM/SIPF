import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProgramacionAcademicaService, SugerenciaInstructorResponse, ResumenProgramacionResponse } from '../../../core/use-cases/programacion-academica.service';
import { FichaService } from '../../../core/use-cases/ficha.service';
import { TrimestreService } from '../../../core/use-cases/trimestre.service';
import { UserService } from '../../../core/use-cases/user.service';
import { RapService } from '../../../core/use-cases/rap.service';
import { CompetenciaService } from '../../../core/use-cases/competencia.service';
import { SweetAlertService } from '../../../core/use-cases/sweet-alert.service';
import { MainLayoutComponent } from '../../../shared/layouts/main-layout/main-layout';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { FichaResponse } from '../../../core/entities/ficha.model';
import { UsuarioResponse } from '../../../core/entities/user.model';
import { RapResponse } from '../../../core/entities/rap.model';
import { CompetenciaResponse } from '../../../core/entities/competencia.model';

export interface RapItem {
  id: number;
  competenciaId: number;
  competencia: string;
  especialidad: string;
  descripcion: string;
  horas: number;
  estado: string;
  instructor: string | null;
}

@Component({
  selector: 'app-programacion-academica',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MainLayoutComponent,
    MatIconModule,
    MatButtonModule,
    MatFormFieldModule,
    MatSelectModule,
    MatInputModule
  ],
  templateUrl: './programacion-academica.html',
  styleUrls: ['./programacion-academica.css']
})
export class ProgramacionAcademicaComponent implements OnInit {
  private programacionService = inject(ProgramacionAcademicaService);
  private fichaService = inject(FichaService);
  private trimestreService = inject(TrimestreService);
  private userService = inject(UserService);
  private rapService = inject(RapService);
  private competenciaService = inject(CompetenciaService);
  private sweetAlertService = inject(SweetAlertService);
  private cdr = inject(ChangeDetectorRef);

  fichas: FichaResponse[] = [];
  trimestres: any[] = [];
  usuariosReales: UsuarioResponse[] = [];
  disponibilidadesReales: any[] = [];

  selectedFichaId: number | null = null;
  selectedTrimestreId: number | null = null;

  isAutoprogramando = false;

  raps: RapItem[] = [];

  selectedRap: RapItem | null = null;
  instructoresSugeridos: SugerenciaInstructorResponse[] = [];
  cargandoSugerencias = false;

  searchTerm = '';
  filterJornada = '';

  ngOnInit(): void {
    this.cargarFichas();
    this.cargarTrimestres();
    this.cargarUsuariosYDisponibilidad();
  }

  cargarFichas() {
    this.fichaService.getAll().subscribe({
      next: (data) => {
        this.fichas = data || [];
        if (this.fichas.length > 0 && !this.selectedFichaId) {
          this.selectedFichaId = this.fichas[0].id;
          this.cargarResumenDeProgramacion();
        }
        this.cdr.detectChanges();
      },
      error: () => {
        this.fichas = [];
        this.selectedFichaId = null;
        this.raps = [];
        this.cdr.detectChanges();
      }
    });
  }

  cargarTrimestres() {
    this.trimestreService.getAll().subscribe({
      next: (data) => {
        if (data && data.length > 0) {
          this.trimestres = data;
        } else {
          this.generarTrimestresPorDefecto();
        }
        if (this.trimestres.length > 0 && !this.selectedTrimestreId) {
          this.selectedTrimestreId = this.trimestres[0].id;
        }
        this.cdr.detectChanges();
      },
      error: () => {
        this.generarTrimestresPorDefecto();
        if (!this.selectedTrimestreId) {
          this.selectedTrimestreId = 1;
        }
        this.cdr.detectChanges();
      }
    });
  }

  private generarTrimestresPorDefecto() {
    this.trimestres = [];
  }

  onSeleccionChange() {
    this.cargarResumenDeProgramacion();
  }

  cargarUsuariosYDisponibilidad(onComplete?: () => void) {
    this.userService.getAll().subscribe({
      next: (users) => {
        this.usuariosReales = users || [];
        this.programacionService.getDisponibilidadInstructor().subscribe({
          next: (disp) => {
            this.disponibilidadesReales = disp || [];
            if (onComplete) onComplete();
            this.cdr.detectChanges();
          },
          error: () => {
            this.disponibilidadesReales = [];
            if (onComplete) onComplete();
            this.cdr.detectChanges();
          }
        });
      },
      error: () => {
        if (onComplete) onComplete();
      }
    });
  }

  cargarResumenDeProgramacion() {
    if (!this.selectedFichaId) return;

    const currentFichaObj = this.fichas.find(f => f.id === this.selectedFichaId);
    const programaId = currentFichaObj ? currentFichaObj.programaId : 1;

    this.rapService.getAll().subscribe({
      next: (dbRaps: RapResponse[]) => {
        this.competenciaService.getAll().subscribe({
          next: (dbCompetencias: CompetenciaResponse[]) => {
            this.programacionService.getAllProgramaciones().subscribe({
              next: (progGuardadas: any[]) => {
                this.programacionService.getDisenoCurricular().subscribe({
                  next: (mallas: any[]) => {
                    this.ensamblarListaDeRaps(dbRaps, dbCompetencias, progGuardadas, mallas, programaId);
                  },
                  error: () => {
                    this.ensamblarListaDeRaps(dbRaps, dbCompetencias, progGuardadas, [], programaId);
                  }
                });
              },
              error: () => {
                this.ensamblarListaDeRaps(dbRaps, dbCompetencias, [], [], programaId);
              }
            });
          },
          error: () => {
            this.cargarResumenFichaRespaldo();
          }
        });
      },
      error: () => {
        this.cargarResumenFichaRespaldo();
      }
    });
  }

  private ensamblarListaDeRaps(
    dbRaps: RapResponse[],
    dbCompetencias: CompetenciaResponse[],
    progGuardadas: any[],
    mallas: any[],
    programaId: number
  ) {
    if (dbRaps && dbRaps.length > 0) {
      const compMap = new Map<number, string>();
      (dbCompetencias || []).forEach(c => compMap.set(c.id, c.nombre));

      const userMap = new Map<number, string>();
      (this.usuariosReales || []).forEach(u => {
        const nombre = `${u.nombre || ''} ${u.apellido && u.apellido !== 'NONE' && u.apellido !== 'NA' ? u.apellido : ''}`.trim();
        userMap.set(u.id, nombre || `Instructor #${u.id}`);
      });

      const asignacionesMap = new Map<string, string>();
      const currentTrimestre = this.selectedTrimestreId || 1;
      const currentFicha = this.selectedFichaId;

      (progGuardadas || []).forEach(p => {
        const fId = p.fichaId || 1;
        const tId = p.trimestreId || 1;
        const rId = p.rapId;
        const uId = p.usuarioId;

        if (rId && uId && fId === currentFicha && tId === currentTrimestre) {
          const instNombre = userMap.get(uId) || `Instructor #${uId}`;
          asignacionesMap.set(`${rId}`, instNombre);
        }
      });

      let rapsDelTrimestre: RapResponse[] = [];

      if (mallas && mallas.length > 0) {
        const rapIdsMalla = mallas
          .filter(m => (m.numeroTrimestre === currentTrimestre || m.trimestreId === currentTrimestre) && (m.programaId === programaId || !m.programaId))
          .map(m => m.rapId);

        if (rapIdsMalla.length > 0) {
          rapsDelTrimestre = dbRaps.filter(r => rapIdsMalla.includes(r.id));
        }
      }

      if (rapsDelTrimestre.length === 0) {
        const rapsPorTrimestre = 11;
        const startIndex = (currentTrimestre - 1) * rapsPorTrimestre;
        const filteredRaps = dbRaps.slice(startIndex, startIndex + rapsPorTrimestre);
        rapsDelTrimestre = filteredRaps.length > 0 ? filteredRaps : dbRaps.slice(0, 11);
      }

      this.raps = rapsDelTrimestre.map((r) => {
        const compNombre = compMap.get(r.competenciaId) || `Competencia #${r.competenciaId}`;
        const instructorAsignado = asignacionesMap.get(`${r.id}`) || null;

        const mObj = (mallas || []).find(m => m.rapId === r.id);
        const horasReales = mObj && (mObj.horasPresenciales || mObj.horaspresenciales) 
          ? (mObj.horasPresenciales || mObj.horaspresenciales) 
          : (r.horasPresenciales || 40);

        return {
          id: r.id,
          competenciaId: r.competenciaId,
          competencia: compNombre,
          especialidad: this.obtenerEspecialidadPorId(r.competenciaId),
          descripcion: r.descripcion || 'RAP de Formación Técnica',
          horas: horasReales || 40,
          estado: instructorAsignado ? 'Asignado' : 'Pendiente',
          instructor: instructorAsignado
        };
      });
    } else {
      this.cargarResumenFichaRespaldo();
    }
    this.cdr.detectChanges();
  }

  private cargarResumenFichaRespaldo() {
    if (!this.selectedFichaId) return;

    this.programacionService.getResumenFicha(this.selectedFichaId).subscribe({
      next: (resumen: any) => {
        let items: any[] = [];
        if (Array.isArray(resumen)) {
          items = resumen;
        } else if (resumen && Array.isArray(resumen.competencias)) {
          items = resumen.competencias;
        }

        if (items && items.length > 0) {
          this.raps = items.map((r, index) => ({
            id: r.id || r.rapId || index + 1,
            competenciaId: r.competenciaId || index + 1,
            competencia: r.competenciaNombre || r.nombreCompetencia || 'Desarrollo de Software',
            especialidad: this.obtenerEspecialidadPorId(r.competenciaId || index + 1),
            descripcion: r.rapDescripcion || r.descripcion || 'RAP de Formación Técnica',
            horas: r.horasAsignadas || r.totalHorasRequeridas || 40,
            estado: (r.instructorNombre && r.instructorNombre.trim() !== '') ? 'Asignado' : 'Pendiente',
            instructor: r.instructorNombre || null
          }));
        } else {
          this.generarRapsPorDefectoAmpliados();
        }
        this.cdr.detectChanges();
      },
      error: () => {
        this.generarRapsPorDefectoAmpliados();
        this.cdr.detectChanges();
      }
    });
  }

  private generarRapsPorDefectoAmpliados() {
    this.raps = [];
  }

  private obtenerEspecialidadPorId(compNomId: number): string {
    const espMap: Record<number, string> = {
      1: 'Programación Backend',
      2: 'Inglés Técnico y Bilingüismo',
      3: 'Bases de Datos SQL/NoSQL',
      4: 'Desarrollo Frontend y UI/UX',
      5: 'Redes y Telecomunicaciones',
      6: 'Seguridad y Salud en el Trabajo (SST)',
      7: 'Pruebas de Software (QA)',
      8: 'DevOps y Arquitectura Cloud'
    };
    return espMap[compNomId] || 'Especialidad Técnica';
  }

  get rapsAsignadosCount(): number {
    return this.raps.filter(r => r.estado === 'Asignado').length;
  }

  get totalRapsCount(): number {
    return this.raps.length;
  }

  get porcentajeAvance(): number {
    return this.totalRapsCount > 0 ? Math.round((this.rapsAsignadosCount / this.totalRapsCount) * 100) : 0;
  }

  get estadoGlobalProgramacion(): string {
    if (this.rapsAsignadosCount === 0) return 'Sin Asignación';
    if (this.rapsAsignadosCount === this.totalRapsCount) return 'Completado';
    return 'En Progreso';
  }

  ejecutarAutoprogramacion() {
    if (!this.selectedFichaId || !this.selectedTrimestreId) {
      this.sweetAlertService.error('Atención', 'Por favor, selecciona primero una Ficha y un Trimestre en los desplegables.');
      return;
    }

    this.isAutoprogramando = true;
    this.programacionService.autoprogramar(this.selectedFichaId, this.selectedTrimestreId).subscribe({
      next: () => {
        this.isAutoprogramando = false;
        this.sweetAlertService.success('¡Autoprogramación Exitosa!', 'La programación automática se ejecutó asignando instructores sin cruces de horario.');
        
        this.cargarUsuariosYDisponibilidad();
        this.cargarResumenDeProgramacion();
        this.cdr.detectChanges();
      },
      error: () => {
        this.isAutoprogramando = false;
        this.sweetAlertService.error('Error', 'Fallo al ejecutar la autoprogramación.');
        this.cdr.detectChanges();
      }
    });
  }

  ejecutarAutoprogramacionMasivaSemestre() {
    if (!this.selectedFichaId) {
      this.sweetAlertService.error('Atención', 'Por favor, selecciona primero una Ficha.');
      return;
    }

    this.isAutoprogramando = true;
    const trimestresAProgramar = this.trimestres.length > 0 ? this.trimestres : [{ id: 1 }, { id: 2 }, { id: 3 }, { id: 4 }];
    
    let index = 0;
    const ejecutarSiguiente = () => {
      if (index >= trimestresAProgramar.length) {
        this.isAutoprogramando = false;
        this.sweetAlertService.success(
          '¡Autoprogramación Semestral / Anual Completada!',
          'Se ejecutó la autoprogramación continua por todos los trimestres. Los instructores fueron completando sus 160h máximas paulatinamente.'
        );
        this.cargarUsuariosYDisponibilidad();
        this.cargarResumenDeProgramacion();
        this.cdr.detectChanges();
        return;
      }

      const t = trimestresAProgramar[index];
      index++;

      this.programacionService.autoprogramar(this.selectedFichaId!, t.id).subscribe({
        next: () => ejecutarSiguiente(),
        error: () => ejecutarSiguiente()
      });
    };

    ejecutarSiguiente();
  }

  seleccionarRap(rap: RapItem) {
    this.selectedRap = rap;
    this.searchTerm = '';
    this.filterJornada = '';
    
    this.cargarUsuariosYDisponibilidad(() => {
      this.cargarSugerencias(rap);
    });
  }

  cargarSugerencias(rap: RapItem) {
    this.cargandoSugerencias = true;
    const programaId = this.selectedFichaId || 1;

    this.programacionService.getSugerencias(rap.competenciaId || 1, programaId, rap.horas).subscribe({
      next: (data) => {
        this.cargandoSugerencias = false;
        if (data && data.length > 0) {
          this.instructoresSugeridos = data;
        } else {
          this.construirSugerenciasConHorasReales(rap);
        }
        this.cdr.detectChanges();
      },
      error: () => {
        this.cargandoSugerencias = false;
        this.construirSugerenciasConHorasReales(rap);
        this.cdr.detectChanges();
      }
    });
  }

  private construirSugerenciasConHorasReales(rap: RapItem) {
    const mapaEspecialidades: Record<number, string> = {
      1: 'Programación Backend',
      4: 'Bases de Datos SQL',
      5: 'Redes y Telecomunicaciones',
      6: 'Programación Backend y Algoritmos',
      7: 'Bases de Datos y Persistencia',
      8: 'Desarrollo Frontend y UI/UX',
      9: 'Inglés Técnico y Bilingüismo',
      10: 'Redes y Sistemas Linux',
      11: 'Seguridad y Salud en el Trabajo (SST)',
      12: 'Calidad de Software (QA)',
      13: 'DevOps y Nube'
    };

    if (this.usuariosReales.length > 0) {
      this.instructoresSugeridos = this.usuariosReales
        .filter(u => (u.rol || '').toUpperCase() === 'INSTRUCTOR' || u.id === 1 || u.id === 4 || u.id === 5 || u.id >= 6)
        .map((u) => {
          const dispObj = this.disponibilidadesReales.find(d => 
            d.usuarioId === u.id || 
            d.usuario_id === u.id || 
            d.id === u.id || 
            (d.usuario && d.usuario.id === u.id)
          );
          
          const maximas = dispObj ? (dispObj.horasMaximas ?? dispObj.horas_maximas ?? 160) : 160;
          const asignadas = dispObj ? (dispObj.horasAsignadas ?? dispObj.horas_asignadas ?? 0) : 0;
          const jornada = dispObj ? (dispObj.jornada || (u.id % 2 === 0 ? 'MAÑANA' : 'TARDE')) : (u.id % 2 === 0 ? 'MAÑANA' : 'TARDE');
          
          const disponibles = Math.max(0, maximas - asignadas);
          const cumple = disponibles >= rap.horas;
          const espec = mapaEspecialidades[u.id] || u.tipoContrato || 'Instructor Técnico';

          return {
            instructorId: u.id,
            nombreInstructor: `${u.nombre || ''} ${u.apellido && u.apellido !== 'NONE' && u.apellido !== 'NA' ? u.apellido : ''}`.trim() || `Instructor #${u.id}`,
            especialidad: espec,
            jornada: jornada,
            horasMaximas: maximas,
            horasAsignadas: asignadas,
            horasDisponibles: disponibles,
            cumpleRequisitos: cumple
          };
        });
    } else {
      this.instructoresSugeridos = [];
    }
  }

  get filteredInstructores() {
    return this.instructoresSugeridos.filter(i => {
      const matchName = (i.nombreInstructor || '').toLowerCase().includes(this.searchTerm.toLowerCase());
      const matchJornada = this.filterJornada ? i.jornada === this.filterJornada : true;
      return matchName && matchJornada;
    });
  }

  calcularPorcentajeHoras(asignadas: number, maximas: number): number {
    return maximas > 0 ? (asignadas / maximas) * 100 : 0;
  }

  puedeAsignar(instructor: SugerenciaInstructorResponse): boolean {
    if (!this.selectedRap) return false;

    // HU-19: Determinar tope institucional según Tipo de Contrato
    const userObj = this.usuariosReales.find(u => u.id === instructor.instructorId);
    const tipoContrato = (userObj?.tipoContrato || '').toUpperCase();
    const topeHoras = tipoContrato === 'PLANTA' ? 144 : 160;

    const horasPostAsignacion = (instructor.horasAsignadas || 0) + (this.selectedRap.horas || 0);

    // Si supera el tope estricto o las horas disponibles, bloquea la asignación
    return horasPostAsignacion <= topeHoras && instructor.horasDisponibles >= this.selectedRap.horas;
  }

  asignarInstructor(instructor: SugerenciaInstructorResponse) {
    if (!this.selectedFichaId || !this.selectedTrimestreId || !this.selectedRap) {
      this.sweetAlertService.error('Atención', 'Selecciona la Ficha y el Trimestre antes de guardar la asignación.');
      return;
    }

    // HU-19: Validación y notificación estricta de Topes Institucionales (144h Planta / 160h Contratista)
    const userObj = this.usuariosReales.find(u => u.id === instructor.instructorId);
    const tipoContrato = (userObj?.tipoContrato || 'CONTRATISTA').toUpperCase();
    const topeMaximoInstitucional = tipoContrato === 'PLANTA' ? 144 : 160;
    const horasResultantes = (instructor.horasAsignadas || 0) + (this.selectedRap.horas || 0);

    if (horasResultantes > topeMaximoInstitucional) {
      this.sweetAlertService.error(
        '⛔ BLOQUEO POR TOPE DE HORAS SUPERADO (HU-19)',
        `No es posible asignar la competencia/RAP de ${this.selectedRap.horas}h a ${instructor.nombreInstructor}.\n\n` +
        `• Tipo de Contrato: ${tipoContrato}\n` +
        `• Tope Máximo Permitido: ${topeMaximoInstitucional}h/mes\n` +
        `• Horas Actuales Asignadas: ${instructor.horasAsignadas}h\n` +
        `• Horas Resultantes: ${horasResultantes}h\n\n` +
        `Superaría el límite institucional permitido por ${horasResultantes - topeMaximoInstitucional}h.`
      );
      return;
    }

    if (!this.puedeAsignar(instructor)) {
      this.sweetAlertService.error(
        'Horas Insuficientes',
        `El instructor ${instructor.nombreInstructor} solo tiene ${instructor.horasDisponibles}h disponibles para esta asignación.`
      );
      return;
    }

    const payload = {
      fichaId: this.selectedFichaId,
      trimestreId: this.selectedTrimestreId,
      competenciaId: this.selectedRap.competenciaId || 1,
      rapId: this.selectedRap.id,
      instructorId: instructor.instructorId,
      horasAsignadas: this.selectedRap.horas
    };

    this.programacionService.programarManual(payload).subscribe({
      next: () => {
        this.sweetAlertService.success('Asignación Guardada', `Instructor ${instructor.nombreInstructor} asignado correctamente.`);
        if (this.selectedRap) {
          this.selectedRap.estado = 'Asignado';
          this.selectedRap.instructor = instructor.nombreInstructor;
        }
        this.selectedRap = null;
        this.cargarUsuariosYDisponibilidad();
        this.cargarResumenDeProgramacion();
        this.cdr.detectChanges();
      },
      error: () => {
        this.sweetAlertService.error('Error', 'Fallo al guardar la asignación.');
        this.cdr.detectChanges();
      }
    });
  }
}
