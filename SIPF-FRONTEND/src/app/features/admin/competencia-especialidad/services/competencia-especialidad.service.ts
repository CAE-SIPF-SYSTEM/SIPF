import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../../environments/environment';
import {
  CompetenciaEspecialidadRequestDTO,
  CompetenciaEspecialidadResponseDTO,
} from '../models/competencia-especialidad.model';

@Injectable({
  providedIn: 'root',
})
export class CompetenciaEspecialidadService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/competencia-especialidad`;

  listarAsignaciones(): Observable<CompetenciaEspecialidadResponseDTO[]> {
    return this.http.get<CompetenciaEspecialidadResponseDTO[]>(this.apiUrl);
  }

  asignarEspecialidad(request: CompetenciaEspecialidadRequestDTO): Observable<CompetenciaEspecialidadResponseDTO> {
    return this.http.post<CompetenciaEspecialidadResponseDTO>(this.apiUrl, request);
  }

  editarEspecialidad(competenciaId: number, request: CompetenciaEspecialidadRequestDTO): Observable<CompetenciaEspecialidadResponseDTO> {
    return this.http.put<CompetenciaEspecialidadResponseDTO>(`${this.apiUrl}/${competenciaId}`, request);
  }

  desasignarEspecialidad(competenciaId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${competenciaId}`);
  }
}
