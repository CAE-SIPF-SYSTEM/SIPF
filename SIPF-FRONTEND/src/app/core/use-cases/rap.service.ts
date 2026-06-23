import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CrearRapRequest, EditarRapRequest, RapResponse } from '../entities/rap.model';

@Injectable({ providedIn: 'root' })
export class RapService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = environment.apiUrl;

  getAll(): Observable<RapResponse[]> {
    return this.http.get<RapResponse[]>(`${this.apiUrl}/raps`);
  }

  getByCompetenciaId(competenciaId: number): Observable<RapResponse[]> {
    return this.http.get<RapResponse[]>(`${this.apiUrl}/raps/competencia/${competenciaId}`);
  }

  create(data: CrearRapRequest): Observable<RapResponse> {
    return this.http.post<RapResponse>(`${this.apiUrl}/raps`, data);
  }

  update(id: number, data: EditarRapRequest): Observable<RapResponse> {
    return this.http.put<RapResponse>(`${this.apiUrl}/raps/${id}`, data);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/raps/${id}`);
  }
}
