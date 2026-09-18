import { Injectable } from '@angular/core';
import Swal from 'sweetalert2';

@Injectable({
  providedIn: 'root'
})
export class SweetAlertService {
  
  success(title: string, text?: string): void {
    Swal.fire({
      icon: 'success',
      title,
      text,
      confirmButtonColor: '#136E61', // Verde de la marca
      timer: 3000,
      timerProgressBar: true
    });
  }

  error(title: string, text?: string): void {
    Swal.fire({
      icon: 'error',
      title,
      text,
      confirmButtonColor: '#ef4444' // Rojo
    });
  }

  info(title: string, text?: string): void {
    Swal.fire({
      icon: 'info',
      title,
      text,
      confirmButtonColor: '#3b82f6' // Azul
    });
  }

  warning(title: string, text?: string): void {
    Swal.fire({
      icon: 'warning',
      title,
      text,
      confirmButtonColor: '#eab308' // Amarillo
    });
  }

  /**
   * Muestra un diálogo de confirmación para eliminar elementos.
   * @param title Título principal
   * @param text Texto descriptivo
   * @returns Promesa que se resuelve a `true` si el usuario confirma.
   */
  async confirmDelete(title: string = '¿Estás seguro?', text: string = 'Esta acción no se puede deshacer.'): Promise<boolean> {
    const result = await Swal.fire({
      title,
      text,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#ef4444',
      cancelButtonColor: '#94a3b8',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    });
    
    return result.isConfirmed;
  }
}
