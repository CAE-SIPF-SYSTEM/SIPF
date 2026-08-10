import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuditoriaPerfil } from '../entities/auditoria.model';

@Injectable({
  providedIn: 'root'
})
export class AuditoriaService {
  private apiUrl = 'http://localhost:8080/api/auditorias';

  constructor(private http: HttpClient) {}

  obtenerTodas(): Observable<AuditoriaPerfil[]> {
    return this.http.get<AuditoriaPerfil[]>(this.apiUrl);
  }

  obtenerPorUsuarioId(usuarioId: number): Observable<AuditoriaPerfil[]> {
    return this.http.get<AuditoriaPerfil[]>(`${this.apiUrl}/usuario/${usuarioId}`);
  }
}
