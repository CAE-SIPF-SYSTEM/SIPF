import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface MunicipioResponse {
  id: number;
  nombre: string;
  departamentoId?: number;
}

@Injectable({
  providedIn: 'root'
})
export class UbicacionService {
  private apiUrl = `${environment.apiUrl}/ubicaciones`;
  municipios = signal<MunicipioResponse[]>([]);

  constructor(private http: HttpClient) {}

  obtenerMunicipios(): Observable<MunicipioResponse[]> {
    return this.http.get<MunicipioResponse[]>(`${this.apiUrl}/municipios`).pipe(
      tap(data => this.municipios.set(data))
    );
  }
}
