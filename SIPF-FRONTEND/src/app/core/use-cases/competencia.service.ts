import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CrearCompetenciaRequest, EditarCompetenciaRequest, CompetenciaResponse } from '../entities/competencia.model';

@Injectable({ providedIn: 'root' })
export class CompetenciaService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = environment.apiUrl;

  getAll(): Observable<CompetenciaResponse[]> {
    return this.http.get<CompetenciaResponse[]>(`${this.apiUrl}/competencias`);
  }

  create(data: CrearCompetenciaRequest): Observable<CompetenciaResponse> {
    return this.http.post<CompetenciaResponse>(`${this.apiUrl}/competencias`, data);
  }

  update(id: number, data: EditarCompetenciaRequest): Observable<CompetenciaResponse> {
    return this.http.put<CompetenciaResponse>(`${this.apiUrl}/competencias/${id}`, data);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/competencias/${id}`);
  }
}
