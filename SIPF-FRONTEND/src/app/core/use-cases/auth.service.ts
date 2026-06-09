import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  LoginRequest,
  LoginResponse,
  MensajeResponse,
  RecuperarContrasenaRequest,
  RestablecerContrasenaRequest
} from '../entities/user.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly apiUrl = environment.apiUrl;

  private readonly TOKEN_KEY = 'sipf_token';
  private readonly USER_KEY = 'sipf_user';

  login(request: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/auth/login`, request).pipe(
      tap((response) => {
        if (typeof window !== 'undefined') {
          localStorage.setItem(this.TOKEN_KEY, response.token);
          localStorage.setItem(this.USER_KEY, JSON.stringify(response));
        }
      })
    );
  }

  logout(): void {
    if (typeof window !== 'undefined') {
      localStorage.removeItem(this.TOKEN_KEY);
      localStorage.removeItem(this.USER_KEY);
    }
    this.router.navigate(['/login']);
  }

  recuperarContrasena(correo: string): Observable<MensajeResponse> {
    const request: RecuperarContrasenaRequest = { correo };
    return this.http.post<MensajeResponse>(`${this.apiUrl}/auth/recuperar-contrasena`, request);
  }

  restablecerContrasena(token: string, nuevaContrasena: string): Observable<MensajeResponse> {
    const request: RestablecerContrasenaRequest = { token, nuevaContrasena };
    return this.http.post<MensajeResponse>(`${this.apiUrl}/auth/restablecer-contrasena`, request);
  }

  isLoggedIn(): boolean {
    if (typeof window === 'undefined') return false;
    return !!localStorage.getItem(this.TOKEN_KEY);
  }

  getEstado(): boolean | null{
    const user = this.getUser();
    return user ? user.estado : null
  }

  getToken(): string | null {
    if (typeof window === 'undefined') return null;
    return localStorage.getItem(this.TOKEN_KEY);
  }

  getRole(): string | null {
    const user = this.getUser();
    return user ? user.rol : null;
  }

  getUserId(): number | null {
    const user = this.getUser();
    return user ? user.id : null;
  }

  getUserEmail(): string | null {
    const user = this.getUser();
    return user ? user.correo : null;
  }

  getUser(): LoginResponse | null {
    if (typeof window === 'undefined') return null;
    const data = localStorage.getItem(this.USER_KEY);
    if (!data) return null;
    try {
      return JSON.parse(data) as LoginResponse;
    } catch {
      return null;
    }
  }
}
