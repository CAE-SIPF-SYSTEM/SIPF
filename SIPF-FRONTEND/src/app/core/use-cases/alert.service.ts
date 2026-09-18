import { Injectable, inject } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({ providedIn: 'root' })
export class AlertService {
  private snackBar = inject(MatSnackBar);

  success(message: string): void {
    this.snackBar.open(message, 'Cerrar', { 
      duration: 4000,
      panelClass: ['bg-green-600', 'text-white']
    });
  }

  error(message: string): void {
    this.snackBar.open(message, 'Cerrar', { 
      duration: 5000,
      panelClass: ['bg-red-600', 'text-white']
    });
  }

  warning(message: string): void {
    this.snackBar.open(message, 'Cerrar', { 
      duration: 4000,
      panelClass: ['bg-yellow-500', 'text-white']
    });
  }

  info(message: string): void {
    this.snackBar.open(message, 'Cerrar', { 
      duration: 4000,
      panelClass: ['bg-blue-500', 'text-white']
    });
  }
}
