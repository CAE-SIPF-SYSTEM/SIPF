import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Especialidad, EspecialidadResponse } from '../entities/especialidad.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EspecialidadService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/especialidades`;

  getAll(): Observable<EspecialidadResponse[]> {
    return this.http.get<EspecialidadResponse[]>(this.apiUrl);
  }

  getById(id: number): Observable<EspecialidadResponse> {
    return this.http.get<EspecialidadResponse>(`${this.apiUrl}/${id}`);
  }

  create(especialidad: Especialidad): Observable<EspecialidadResponse> {
    return this.http.post<EspecialidadResponse>(this.apiUrl, especialidad);
  }

  update(id: number, especialidad: Especialidad): Observable<EspecialidadResponse> {
    return this.http.put<EspecialidadResponse>(`${this.apiUrl}/${id}`, especialidad);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
