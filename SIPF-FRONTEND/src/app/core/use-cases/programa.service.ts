import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CrearProgramaRequest, ProgramaResponse } from '../entities/programa.model';

@Injectable({ providedIn: 'root' })
export class ProgramaService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = environment.apiUrl;

  getAll(): Observable<ProgramaResponse[]> {
    return this.http.get<ProgramaResponse[]>(`${this.apiUrl}/programas`);
  }

  create(data: CrearProgramaRequest): Observable<ProgramaResponse> {
    return this.http.post<ProgramaResponse>(`${this.apiUrl}/programas`, data);
  }

  update(id: number, data: any): Observable<ProgramaResponse> {
    return this.http.put<ProgramaResponse>(`${this.apiUrl}/programas/${id}`, data);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/programas/${id}`);
  }
}
