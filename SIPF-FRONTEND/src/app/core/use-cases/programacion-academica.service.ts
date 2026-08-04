import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface ProgramacionManualRequest {
  fichaId: number;
  trimestreId: number;
  competenciaId: number;
  rapId: number;
  instructorId: number;
  ambienteId?: number;
  horasAsignadas: number;
}

export interface SugerenciaInstructorResponse {
  instructorId: number;
  nombreInstructor: string;
  especialidad: string;
  jornada: string;
  horasMaximas: number;
  horasAsignadas: number;
  horasDisponibles: number;
  cumpleRequisitos: boolean;
}

export interface ResumenProgramacionResponse {
  id: number;
  fichaCodigo: string;
  programaNombre: string;
  trimestreNumero: number;
  competenciaNombre: string;
  rapDescripcion: string;
  instructorNombre: string;
  horasAsignadas: number;
  estado: string;
}

export interface UbicacionResponse {
  id: number;
  nombre: string;
  sede: string;
  capacidad: number;
}

@Injectable({ providedIn: 'root' })
export class ProgramacionAcademicaService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = environment.apiUrl;

  /**
   * Obtiene la malla de diseño curricular.
   * GET /api/diseñocurricular
   */
  getDisenoCurricular(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/diseñocurricular`);
  }

  /**
   * Obtiene todas las asignaciones guardadas en la tabla ProgramacionAcademica.
   * GET /api/programacionacademica
   */
  getAllProgramaciones(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/programacionacademica`);
  }

  /**
   * Ejecuta la autoprogramación inteligente de la ficha y trimestre.
   * POST /api/programacionacademica/autoprogramar?fichaId=X&trimestreId=Y
   */
  autoprogramar(fichaId: number, trimestreId: number): Observable<any> {
    return this.http.post<any>(
      `${this.apiUrl}/programacionacademica/autoprogramar?fichaId=${fichaId}&trimestreId=${trimestreId}`,
      {}
    );
  }

  /**
   * Registra una programación manual enviando los parámetros seleccionados por el coordinador.
   * POST /api/programacionacademica
   */
  programarManual(data: ProgramacionManualRequest): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/programacionacademica`, data);
  }

  /**
   * Obtiene la lista de instructores sugeridos con sus horas disponibles según competencia, programa e intensidad horaria.
   * GET /api/programacionacademica/sugerencias?competenciaId=X&programaId=Y&horasRequeridas=Z
   */
  getSugerencias(competenciaId: number, programaId: number, horasRequeridas: number): Observable<SugerenciaInstructorResponse[]> {
    return this.http.get<SugerenciaInstructorResponse[]>(
      `${this.apiUrl}/programacionacademica/sugerencias?competenciaId=${competenciaId}&programaId=${programaId}&horasRequeridas=${horasRequeridas}`
    );
  }

  /**
   * Obtiene el resumen de programación por Programa.
   * GET /api/programacionacademica/resumen-programa/{programaId}
   */
  getResumenPrograma(programaId: number): Observable<ResumenProgramacionResponse[]> {
    return this.http.get<ResumenProgramacionResponse[]>(`${this.apiUrl}/programacionacademica/resumen-programa/${programaId}`);
  }

  /**
   * Obtiene el resumen de programación por Ficha.
   * GET /api/programacionacademica/resumen-ficha/{fichaId}
   */
  getResumenFicha(fichaId: number): Observable<ResumenProgramacionResponse[]> {
    return this.http.get<ResumenProgramacionResponse[]>(`${this.apiUrl}/programacionacademica/resumen-ficha/${fichaId}`);
  }

  /**
   * Obtiene la lista de ambientes/ubicaciones disponibles para asignación.
   * GET /api/ubicaciones
   */
  getUbicaciones(): Observable<UbicacionResponse[]> {
    return this.http.get<UbicacionResponse[]>(`${this.apiUrl}/ubicaciones`);
  }

  /**
   * Obtiene la disponibilidad de instructores.
   * GET /api/disponibilidadinstructor
   */
  getDisponibilidadInstructor(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/disponibilidadinstructor`);
  }
}
