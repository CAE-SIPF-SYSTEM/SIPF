import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../use-cases/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const authService = inject(AuthService);
  const token = authService.getToken();

  const isPublicAuthRequest = req.url.includes('/auth/');

  let authReq = req;
  if (token && !isPublicAuthRequest) {
    authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  return next(authReq).pipe(
    catchError((error) => {
      if ((error.status === 401 || error.status === 403) && !isPublicAuthRequest) {
        authService.logout();
      }
      return throwError(() => error);
    })
  );
};
