import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { RegistrarFichaRequest, FichaResponse } from '../entities/ficha.model';

@Injectable({ providedIn: 'root' })
export class FichaService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = environment.apiUrl;

  getAll(): Observable<FichaResponse[]> {
    return this.http.get<FichaResponse[]>(`${this.apiUrl}/fichas`);
  }

  create(data: RegistrarFichaRequest): Observable<FichaResponse> {
    return this.http.post<FichaResponse>(`${this.apiUrl}/fichas`, data);
  }

  update(id: number, data: any): Observable<FichaResponse> {
    return this.http.put<FichaResponse>(`${this.apiUrl}/fichas/${id}`, data);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/fichas/${id}`);
  }
}
