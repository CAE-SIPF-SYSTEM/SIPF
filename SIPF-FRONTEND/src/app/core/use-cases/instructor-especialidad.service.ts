import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { InstructorEspecialidadRequest, InstructorEspecialidadResponse } from '../entities/instructor-especialidad.model';

@Injectable({ providedIn: 'root' })
export class InstructorEspecialidadService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/instructor-especialidad`;

  getAll(): Observable<InstructorEspecialidadResponse[]> {
    return this.http.get<InstructorEspecialidadResponse[]>(this.apiUrl);
  }

  assign(data: InstructorEspecialidadRequest): Observable<InstructorEspecialidadResponse> {
    return this.http.post<InstructorEspecialidadResponse>(this.apiUrl, data);
  }

  update(usuarioId: number, data: InstructorEspecialidadRequest): Observable<InstructorEspecialidadResponse> {
    return this.http.put<InstructorEspecialidadResponse>(`${this.apiUrl}/${usuarioId}`, data);
  }

  unassign(usuarioId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${usuarioId}`);
  }
}
