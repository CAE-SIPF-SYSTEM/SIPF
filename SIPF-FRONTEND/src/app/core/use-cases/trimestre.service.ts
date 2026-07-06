import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Trimestre } from '../entities/trimestre.model';

@Injectable({ providedIn: 'root' })
export class TrimestreService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = environment.apiUrl;

  getAll(): Observable<Trimestre[]> {
    return this.http.get<Trimestre[]>(`${this.apiUrl}/trimestres`);
  }

  getById(id: number): Observable<Trimestre> {
    return this.http.get<Trimestre>(`${this.apiUrl}/trimestres/${id}`);
  }

  getByFichaId(fichaId: number): Observable<Trimestre[]> {
    return this.http.get<Trimestre[]>(`${this.apiUrl}/trimestres/ficha/${fichaId}`);
  }

  create(data: Trimestre): Observable<Trimestre> {
    return this.http.post<Trimestre>(`${this.apiUrl}/trimestres`, data);
  }

  update(id: number, data: Trimestre): Observable<Trimestre> {
    return this.http.put<Trimestre>(`${this.apiUrl}/trimestres/${id}`, data);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/trimestres/${id}`);
  }
}
