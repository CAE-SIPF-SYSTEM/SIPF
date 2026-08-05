import { Component, OnInit, signal, computed, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MainLayoutComponent } from '../../../shared/layouts/main-layout/main-layout';
import { AuditoriaService } from '../../../core/use-cases/auditoria.service';
import { UserService } from '../../../core/use-cases/user.service';
import { AuditoriaPerfil } from '../../../core/entities/auditoria.model';
import { UsuarioResponse } from '../../../core/entities/user.model';
import { AlertService } from '../../../core/use-cases/alert.service';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-auditoria',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MainLayoutComponent,
    MatCardModule,
    MatIconModule,
    MatTableModule,
    MatInputModule,
    MatSelectModule,
    MatFormFieldModule,
    MatButtonModule
  ],
  templateUrl: './auditoria.component.html',
  styleUrl: './auditoria.component.css'
})
export class AuditoriaComponent implements OnInit {
  private auditoriaService = inject(AuditoriaService);
  private userService = inject(UserService);
  private alertService = inject(AlertService);

  auditorias = signal<AuditoriaPerfil[]>([]);
  usuariosMap = signal<Map<number, UsuarioResponse>>(new Map());
  
  isLoading = signal(true);

  // Filtros
  filtroUsuarioId = signal<string>('');
  filtroRol = signal<string>('');

  // Columnas a mostrar en la tabla
  displayedColumns: string[] = ['id', 'fechaModificacion', 'usuarioId', 'rol', 'campoModificado', 'valorAnterior', 'valorNuevo'];

  // Auditorías filtradas dinámicamente
  auditoriasFiltradas = computed(() => {
    let result = this.auditorias();

    const uId = this.filtroUsuarioId().trim();
    if (uId) {
      result = result.filter(a => String(a.usuarioId).includes(uId));
    }

    const rolSelected = this.filtroRol();
    if (rolSelected) {
      result = result.filter(a => {
        const u = this.usuariosMap().get(a.usuarioId);
        return u && u.rol === rolSelected;
      });
    }

    return result;
  });

  ngOnInit() {
    this.cargarDatos();
  }

  cargarDatos() {
    this.isLoading.set(true);

    // Cargar usuarios primero para poder filtrar por Rol y mostrar información detallada
    this.userService.getAll().subscribe({
      next: (users) => {
        const map = new Map<number, UsuarioResponse>();
        users.forEach(u => map.set(u.id, u));
        this.usuariosMap.set(map);

        // Cargar historial de auditorías
        this.auditoriaService.obtenerTodas().subscribe({
          next: (data) => {
            this.auditorias.set(data);
            this.isLoading.set(false);
          },
          error: (err) => {
            this.isLoading.set(false);
            this.alertService.error('Error al cargar la tabla de auditorías');
          }
        });
      },
      error: (err) => {
        this.isLoading.set(false);
        this.alertService.error('Error al obtener información de usuarios');
      }
    });
  }

  limpiarFiltros() {
    this.filtroUsuarioId.set('');
    this.filtroRol.set('');
  }

  getRolUsuario(usuarioId: number): string {
    const user = this.usuariosMap().get(usuarioId);
    return user ? user.rol : 'DESCONOCIDO';
  }

  getNombreUsuario(usuarioId: number): string {
    const user = this.usuariosMap().get(usuarioId);
    if (!user) return `Usuario #${usuarioId}`;
    return user.nombre ? `${user.nombre} ${user.apellido || ''}` : user.correo;
  }
}
