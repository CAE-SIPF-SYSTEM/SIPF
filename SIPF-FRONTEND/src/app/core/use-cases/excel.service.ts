import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ExcelService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/excel`;

  subirAlimentacion(file: File, programaId: number): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('programaId', programaId.toString());
    
    return this.http.post(`${this.apiUrl}/alimentacion`, formData, {
      responseType: 'text' // El backend devuelve un String, no un JSON estructurado
    });
  }

  exportarCargaInstructor(usuarioId: number, trimestreId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/exportacion?usuarioId=${usuarioId}&trimestreId=${trimestreId}`);
  }

  descargarReporteInstructor(usuarioId: number, trimestreId: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/reportes/instructor/descargar?usuarioId=${usuarioId}&trimestreId=${trimestreId}`, {
      responseType: 'blob'
    });
  }

  descargarReporteFicha(fichaId: number, trimestreId: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/reportes/ficha/descargar?fichaId=${fichaId}&trimestreId=${trimestreId}`, {
      responseType: 'blob'
    });
  }
}
