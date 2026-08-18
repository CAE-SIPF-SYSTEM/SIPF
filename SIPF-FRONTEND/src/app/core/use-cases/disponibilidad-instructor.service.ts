import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { DisponibilidadInstructor } from '../entities/disponibilidad-instructor.model';

@Injectable({ providedIn: 'root' })
export class DisponibilidadInstructorService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = environment.apiUrl;

  getByUsuarioId(usuarioId: number): Observable<DisponibilidadInstructor> {
    return this.http.get<DisponibilidadInstructor>(`${this.apiUrl}/disponibilidadinstructor/${usuarioId}`);
  }

  getMiDisponibilidad(): Observable<DisponibilidadInstructor> {
    return this.http.get<DisponibilidadInstructor>(`${this.apiUrl}/disponibilidadinstructor/me`);
  }

  create(data: { usuarioId: number, diasDisponibles: string[], horasMaximas: number, jornada?: string }): Observable<DisponibilidadInstructor> {
    return this.http.post<DisponibilidadInstructor>(`${this.apiUrl}/disponibilidadinstructor`, data);
  }

  update(usuarioId: number, data: { diasDisponibles: string[], jornada?: string }): Observable<DisponibilidadInstructor> {
    return this.http.put<DisponibilidadInstructor>(`${this.apiUrl}/disponibilidadinstructor/${usuarioId}`, data);
  }
}
