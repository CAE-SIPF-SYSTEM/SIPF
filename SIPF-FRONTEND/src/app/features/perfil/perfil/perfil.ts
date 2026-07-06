import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MainLayoutComponent } from '../../../shared/layouts/main-layout/main-layout';
import { AuthService } from '../../../core/use-cases/auth.service';
import { UserService } from '../../../core/use-cases/user.service';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { EditarPerfilDialogComponent } from '../editar-perfil-dialog/editar-perfil-dialog';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [
    CommonModule, 
    MainLayoutComponent, 
    MatDialogModule, 
    MatIconModule, 
    MatButtonModule,
    MatSnackBarModule
  ],
  templateUrl: './perfil.html',
  styleUrls: ['./perfil.css']
})
export class PerfilComponent implements OnInit {
  private authService = inject(AuthService);
  private userService = inject(UserService);
  private dialog = inject(MatDialog);
  private snackBar = inject(MatSnackBar);

  userId: number | null = null;
  cargo = signal<string>('');
  centroFormacion = signal<string>('Centro AgroEcologico y Empresarial');
  idInstitucional = signal<string>('00000000000');
  especialidad = signal<string>('ADSO');

  nombreCompleto = signal<string>('XXXXX XXXXX XXXXX XXXXX');
  correoElectronico = signal<string>('');
  telefono = signal<string>('+57 XXX XXX XX XX');

  ngOnInit(): void {
    const role = this.authService.getRole();
    const email = this.authService.getUserEmail();
    this.userId = this.authService.getUserId();
    
    // Capitalize first letter of role
    if (role) {
      this.cargo.set(role.charAt(0).toUpperCase() + role.slice(1).toLowerCase());
    }

    if (email) {
      this.correoElectronico.set(email);
    }
    
    if (this.userId) {
      this.cargarDatosUsuario(this.userId);
    }
  }

  cargarDatosUsuario(id: number) {
    this.userService.getById(id).subscribe({
      next: (user) => {
        if (user.nombre || user.apellido) {
          this.nombreCompleto.set(`${user.nombre || ''} ${user.apellido || ''}`.trim());
        } else {
          this.nombreCompleto.set('No especificado');
        }
        
        if (user.telefono) {
          this.telefono.set(user.telefono.toString());
        } else {
          this.telefono.set('No especificado');
        }
        
        if (user.documentoIdentidad) {
          this.idInstitucional.set(user.documentoIdentidad.toString());
        }
      },
      error: (err) => console.error('Error al cargar datos del usuario', err)
    });
  }

  abrirModalEditar(): void {
    const dialogRef = this.dialog.open(EditarPerfilDialogComponent, {
      width: '500px',
      data: {
        nombre: this.nombreCompleto(),
        correo: this.correoElectronico(),
        telefono: this.telefono()
      },
      disableClose: true,
      panelClass: 'custom-dialog-container'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && this.userId) {
        // Optimistic update
        this.nombreCompleto.set(result.nombre);
        this.correoElectronico.set(result.correo);
        this.telefono.set(result.telefono);
        
        // Split nombre back into nombre and apellido roughly
        const nameParts = result.nombre.split(' ');
        const nombre = nameParts[0] || '';
        const apellido = nameParts.slice(1).join(' ') || '';

        // Update backend
        this.userService.update(this.userId, {
          correo: result.correo,
          nombre: nombre,
          apellido: apellido,
          telefono: parseInt(result.telefono.replace(/\D/g, '')) || undefined
        }).subscribe({
          next: () => {
            console.log('Perfil actualizado correctamente');
            this.snackBar.open('Información actualizada correctamente', 'Cerrar', {
              duration: 3000,
              horizontalPosition: 'right',
              verticalPosition: 'top',
              panelClass: ['bg-green-500', 'text-white']
            });
          },
          error: (err) => {
            console.error('Error al actualizar perfil', err);
            // Optionally revert or show alert
          }
        });
      }
    });
  }
}
