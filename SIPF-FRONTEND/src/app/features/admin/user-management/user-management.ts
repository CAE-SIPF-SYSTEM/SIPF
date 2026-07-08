import { Component, OnInit, signal, computed, inject, ViewChild, EffectRef, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MainLayoutComponent } from '../../../shared/layouts/main-layout/main-layout';
import { UserService } from '../../../core/use-cases/user.service';
import { UsuarioResponse, CrearUsuarioRequest } from '../../../core/entities/user.model';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { SweetAlertService } from '../../../core/use-cases/sweet-alert.service';
import { MatCardModule } from '@angular/material/card';
import { UserFormDialogComponent } from './user-form-dialog.component';

@Component({
  selector: 'app-user-management',
  standalone: true,
  imports: [
    CommonModule, FormsModule, ReactiveFormsModule, MainLayoutComponent,
    MatTableModule, MatPaginatorModule, MatSortModule,
    MatFormFieldModule, MatInputModule, MatSelectModule,
    MatButtonModule, MatIconModule, MatDialogModule,
    MatCardModule
  ],
  templateUrl: './user-management.component.html',
  styleUrl: './user-management.component.css'
})
export class UserManagementComponent implements OnInit {
  private userService = inject(UserService);
  private dialog = inject(MatDialog);
  private sweetAlertService = inject(SweetAlertService);

  users = signal<UsuarioResponse[]>([]);
  searchTerm = signal('');
  selectedRole = signal('TODOS');
  selectedEstado = signal('TODOS');
  isLoading = signal(true);
  
  displayedColumns: string[] = ['documentoIdentidad', 'nombre', 'correo', 'rol', 'estado'];
  dataSource = new MatTableDataSource<UsuarioResponse>([]);

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor() {
    effect(() => {
      let result = this.users();
      
      if (this.selectedRole() !== 'TODOS') {
        result = result.filter(u => u.rol === this.selectedRole());
      }

      if (this.selectedEstado() !== 'TODOS') {
        const isActivo = this.selectedEstado() === 'ACTIVO';
        result = result.filter(u => u.estado === isActivo);
      }
      
      const rawTerm = this.searchTerm();
      const term = rawTerm ? rawTerm.toString().toLowerCase() : '';
      if (term) {
        result = result.filter(u => 
          (u.documentoIdentidad?.toString().includes(term)) || 
          (u.correo?.toLowerCase().includes(term)) ||
          (u.nombre?.toLowerCase().includes(term)) ||
          (u.apellido?.toLowerCase().includes(term))
        );
      }
      
      this.dataSource.data = result;
      if (this.dataSource.paginator) {
        this.dataSource.paginator.firstPage();
      }
    });
  }

  ngOnInit() {
    this.loadUsers();
  }

  loadUsers() {
    this.isLoading.set(true);
    this.userService.getAll().subscribe({
      next: (data: any) => {
        const sorted = [...data].sort((a, b) => b.id - a.id);
        this.users.set(sorted);
        setTimeout(() => {
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;
        });
        this.isLoading.set(false);
      },
      error: (err: any) => {
        this.isLoading.set(false);
        this.sweetAlertService.error('Error', 'Error al cargar la lista de usuarios');
      }
    });
  }

  openCreateDialog() {
    const dialogRef = this.dialog.open(UserFormDialogComponent, {
      width: '500px',
      data: { mode: 'create' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && result.action === 'save') {
        this.onCreateSubmit(result.value);
      }
    });
  }

  openEditDialog(user: UsuarioResponse) {
    const dialogRef = this.dialog.open(UserFormDialogComponent, {
      width: '500px',
      data: { mode: 'edit', user }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (result.action === 'save') {
          this.onEditSubmit(user, result.value);
        } else if (result.action === 'delete') {
          this.onDeleteConfirm(user);
        }
      }
    });
  }

  onCreateSubmit(formValue: any) {
    let nombre = formValue.nombreCompleto || '';
    let apellido = ' ';
    
    if (nombre.includes(' ')) {
      const parts = nombre.split(' ');
      nombre = parts[0];
      apellido = parts.slice(1).join(' ');
    }

    const payload: CrearUsuarioRequest = {
      correo: formValue.correo,
      contrasena: formValue.contrasena,
      rol: formValue.rol,
      nombre: nombre,
      apellido: apellido,
      documentoIdentidad: formValue.documentoIdentidad,
      telefono: formValue.telefono,
      tipoContrato: 'PLANTA'
    };

    this.userService.create(payload as any).subscribe({
      next: (newUser: any) => {
        if (formValue.estado === false && newUser && newUser.id) {
           this.userService.disable(newUser.id).subscribe(() => {
             this.sweetAlertService.success('¡Éxito!', 'Usuario creado exitosamente');
             this.loadUsers();
           });
        } else {
           this.sweetAlertService.success('¡Éxito!', 'Usuario creado exitosamente');
           this.loadUsers();
        }
      },
      error: (err: any) => {
        this.sweetAlertService.error('Error', err.error?.mensaje || 'Error al crear usuario');
      }
    });
  }

  onEditSubmit(user: UsuarioResponse, formValue: any) {
    const payload: any = { ...formValue };
    if (!payload.contrasena) {
      delete payload.contrasena;
    }

    const wasActive = user.estado;
    const isNowActive = payload.estado;
    delete payload.estado;

    this.userService.update(user.id, payload).subscribe({
      next: () => {
        if (wasActive && !isNowActive) {
          this.userService.disable(user.id).subscribe(() => {
            this.sweetAlertService.success('¡Actualizado!', 'Usuario actualizado exitosamente');
            this.loadUsers();
          });
        } else if (!wasActive && isNowActive) {
          this.userService.enable(user.id).subscribe(() => {
            this.sweetAlertService.success('¡Actualizado!', 'Usuario actualizado exitosamente');
            this.loadUsers();
          });
        } else {
          this.sweetAlertService.success('¡Actualizado!', 'Usuario actualizado exitosamente');
          this.loadUsers();
        }
      },
      error: (err: any) => {
        this.sweetAlertService.error('Error', err.error?.mensaje || 'Error al actualizar usuario');
      }
    });
  }

  async onDeleteConfirm(user: UsuarioResponse) {
    const confirmed = await this.sweetAlertService.confirmDelete(
      'Eliminar Usuario',
      `¿Estás seguro de que deseas eliminar al usuario ${user.correo}? Esta acción no se puede deshacer.`
    );

    if (confirmed) {
      this.userService.delete(user.id).subscribe({
        next: () => {
          this.sweetAlertService.success('¡Eliminado!', 'Usuario eliminado exitosamente');
          this.loadUsers();
        },
        error: (err: any) => {
          this.sweetAlertService.error('Error', err.error?.mensaje || 'Error al eliminar usuario');
        }
      });
    }
  }
}
