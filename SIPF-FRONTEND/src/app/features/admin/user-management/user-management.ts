import { Component, OnInit, signal, computed, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MainLayoutComponent } from '../../../shared/layouts/main-layout/main-layout';
import { ConfirmModalComponent } from '../../../shared/components/confirm-modal/confirm-modal';
import { UserService } from '../../../core/use-cases/user.service';
import { AlertService } from '../../../core/use-cases/alert.service';
import { UsuarioResponse, CrearUsuarioRequest, EditarUsuarioRequest } from '../../../core/entities/user.model';

@Component({
  selector: 'app-user-management',
  imports: [CommonModule, FormsModule, ReactiveFormsModule, MainLayoutComponent, ConfirmModalComponent],
  template: `
    <app-main-layout pageTitle="Gestión de Usuarios">
      
      <!-- Header Actions -->
      <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 mb-6 animate-fade-in">
        <!-- Search & Filter -->
        <div class="flex flex-col sm:flex-row gap-4 w-full sm:w-auto">
          <div class="relative w-full sm:w-64">
            <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none text-slate-500">
              <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="11" cy="11" r="8"/><path d="m21 21-4.3-4.3"/></svg>
            </div>
            <input type="text" [ngModel]="searchTerm()" (ngModelChange)="searchTerm.set($event)" class="input-field pl-10 py-2" placeholder="Buscar por correo...">
          </div>
          
          <div class="flex bg-slate-800 p-1 rounded-lg border border-slate-700">
            <button *ngFor="let role of ['TODOS', 'ADMINISTRADOR', 'INSTRUCTOR', 'COORDINADOR']"
                    (click)="selectedRole.set(role)"
                    class="px-3 py-1.5 text-xs font-medium rounded-md transition-colors"
                    [ngClass]="selectedRole() === role ? 'bg-indigo-500 text-white' : 'text-slate-400 hover:text-white'">
              {{ role === 'TODOS' ? 'Todos' : role | titlecase }}
            </button>
          </div>
        </div>

        <button (click)="openCreateModal()" class="btn-primary shrink-0 w-full sm:w-auto">
          <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M5 12h14"/><path d="M12 5v14"/></svg>
          Nuevo Usuario
        </button>
      </div>

      <!-- Data Table -->
      <div class="card p-0 overflow-hidden animate-fade-in-up">
        
        <div *ngIf="isLoading()" class="flex justify-center py-12">
          <div class="spinner"></div>
        </div>

        <div *ngIf="!isLoading() && filteredUsers().length === 0" class="text-center py-12 text-slate-400">
          <svg class="w-12 h-12 mx-auto text-slate-600 mb-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"/></svg>
          <p>No se encontraron usuarios</p>
        </div>

        <div *ngIf="!isLoading() && filteredUsers().length > 0" class="overflow-x-auto">
          <table class="data-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Correo</th>
                <th>Rol</th>
                <th>Estado</th>
                <th class="text-right">Acciones</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let user of filteredUsers()">
                <td>{{ user.id }}</td>
                <td class="font-medium text-white">{{ user.correo }}</td>
                <td>
                  <span class="badge" [ngClass]="{
                    'badge-admin': user.rol === 'ADMINISTRADOR',
                    'badge-instructor': user.rol === 'INSTRUCTOR',
                    'badge-coordinador': user.rol === 'COORDINADOR'
                  }">{{ user.rol }}</span>
                </td>
                <td>
                  <span class="badge" [ngClass]="user.estado ? 'badge-active' : 'badge-inactive'">
                    {{ user.estado ? 'Activo' : 'Inactivo' }}
                  </span>
                </td>
                <td class="text-right">
                  <div class="flex items-center justify-end gap-2">
                    <!-- Toggle Status -->
                    <button (click)="toggleStatus(user)" class="p-1.5 text-slate-400 hover:text-white rounded-md transition-colors" [title]="user.estado ? 'Inhabilitar' : 'Habilitar'">
                      <svg *ngIf="user.estado" xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-amber-500 hover:text-amber-400"><rect x="2" y="3" width="20" height="14" rx="2" ry="2"/><line x1="8" y1="21" x2="16" y2="21"/><line x1="12" y1="17" x2="12" y2="21"/><line x1="1" y1="1" x2="23" y2="23"/></svg>
                      <svg *ngIf="!user.estado" xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-emerald-500 hover:text-emerald-400"><rect width="20" height="14" x="2" y="3" rx="2"/><line x1="8" y1="21" x2="16" y2="21"/><line x1="12" y1="17" x2="12" y2="21"/></svg>
                    </button>
                    <!-- Edit -->
                    <button (click)="openEditModal(user)" class="p-1.5 text-slate-400 hover:text-indigo-400 rounded-md transition-colors" title="Editar">
                      <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17 3a2.85 2.83 0 1 1 4 4L7.5 20.5 2 22l1.5-5.5Z"/><path d="m15 5 4 4"/></svg>
                    </button>
                    <!-- Delete -->
                    <button (click)="openDeleteModal(user)" class="p-1.5 text-slate-400 hover:text-rose-500 rounded-md transition-colors" title="Eliminar">
                      <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 6h18"/><path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6"/><path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2"/><line x1="10" x2="10" y1="11" y2="17"/><line x1="14" x2="14" y1="11" y2="17"/></svg>
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- CREATE MODAL -->
      <div *ngIf="showCreateModal()" class="fixed inset-0 z-50 flex items-center justify-center p-4">
        <div class="absolute inset-0 bg-slate-900/80 backdrop-blur-sm" (click)="closeModals()"></div>
        <div class="relative bg-slate-800 border border-slate-700 rounded-xl shadow-2xl p-6 w-full max-w-2xl max-h-[90vh] overflow-y-auto animate-fade-in-up">
          <h3 class="text-xl font-bold text-white mb-6">Crear Nuevo Usuario</h3>
          
          <form [formGroup]="createForm" (ngSubmit)="onCreateSubmit()" class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div class="col-span-2 md:col-span-1">
              <label class="block text-sm font-medium text-slate-300 mb-1">Nombre</label>
              <input type="text" formControlName="nombre" class="input-field py-2">
            </div>
            <div class="col-span-2 md:col-span-1">
              <label class="block text-sm font-medium text-slate-300 mb-1">Apellido</label>
              <input type="text" formControlName="apellido" class="input-field py-2">
            </div>
            
            <div class="col-span-2 md:col-span-1">
              <label class="block text-sm font-medium text-slate-300 mb-1">Documento Identidad</label>
              <input type="number" formControlName="documentoIdentidad" class="input-field py-2">
            </div>
            <div class="col-span-2 md:col-span-1">
              <label class="block text-sm font-medium text-slate-300 mb-1">Teléfono</label>
              <input type="number" formControlName="telefono" class="input-field py-2">
            </div>

            <div class="col-span-2 md:col-span-1">
              <label class="block text-sm font-medium text-slate-300 mb-1">Correo Electrónico</label>
              <input type="email" formControlName="correo" class="input-field py-2">
            </div>
            <div class="col-span-2 md:col-span-1">
              <label class="block text-sm font-medium text-slate-300 mb-1">Contraseña</label>
              <input type="password" formControlName="contrasena" class="input-field py-2">
            </div>

            <div class="col-span-2 md:col-span-1">
              <label class="block text-sm font-medium text-slate-300 mb-1">Rol</label>
              <select formControlName="rol" class="input-field py-2 bg-slate-800">
                <option value="ADMINISTRADOR">Administrador</option>
                <option value="INSTRUCTOR">Instructor</option>
                <option value="COORDINADOR">Coordinador</option>
              </select>
            </div>
            <div class="col-span-2 md:col-span-1">
              <label class="block text-sm font-medium text-slate-300 mb-1">Tipo Contrato</label>
              <select formControlName="tipoContrato" class="input-field py-2 bg-slate-800">
                <option value="PLANTA">Planta</option>
                <option value="CONTRATISTA">Contratista</option>
              </select>
            </div>

            <div class="col-span-2 mt-6 flex justify-end gap-3 border-t border-slate-700 pt-6">
              <button type="button" (click)="closeModals()" class="btn-secondary">Cancelar</button>
              <button type="submit" [disabled]="createForm.invalid || isSaving()" class="btn-primary">
                <span *ngIf="!isSaving()">Crear Usuario</span>
                <div *ngIf="isSaving()" class="spinner w-4 h-4"></div>
              </button>
            </div>
          </form>
        </div>
      </div>

      <!-- EDIT MODAL -->
      <div *ngIf="showEditModal()" class="fixed inset-0 z-50 flex items-center justify-center p-4">
        <div class="absolute inset-0 bg-slate-900/80 backdrop-blur-sm" (click)="closeModals()"></div>
        <div class="relative bg-slate-800 border border-slate-700 rounded-xl shadow-2xl p-6 w-full max-w-md animate-fade-in-up">
          <h3 class="text-xl font-bold text-white mb-6">Editar Usuario</h3>
          
          <form [formGroup]="editForm" (ngSubmit)="onEditSubmit()" class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-slate-300 mb-1">Correo Electrónico</label>
              <input type="email" formControlName="correo" class="input-field py-2">
            </div>
            
            <div>
              <label class="block text-sm font-medium text-slate-300 mb-1">Nueva Contraseña (Opcional)</label>
              <input type="password" formControlName="contrasena" class="input-field py-2" placeholder="Dejar en blanco para no cambiar">
            </div>

            <div>
              <label class="block text-sm font-medium text-slate-300 mb-1">Rol</label>
              <select formControlName="rol" class="input-field py-2 bg-slate-800">
                <option value="ADMINISTRADOR">Administrador</option>
                <option value="INSTRUCTOR">Instructor</option>
                <option value="COORDINADOR">Coordinador</option>
              </select>
            </div>
            
            <div class="flex items-center mt-4">
              <input type="checkbox" id="estado" formControlName="estado" class="w-4 h-4 text-indigo-600 bg-slate-700 border-slate-600 rounded focus:ring-indigo-500 focus:ring-2">
              <label for="estado" class="ml-2 text-sm font-medium text-slate-300">Usuario Activo</label>
            </div>

            <div class="mt-6 flex justify-end gap-3 border-t border-slate-700 pt-6">
              <button type="button" (click)="closeModals()" class="btn-secondary">Cancelar</button>
              <button type="submit" [disabled]="editForm.invalid || isSaving()" class="btn-primary">
                <span *ngIf="!isSaving()">Guardar Cambios</span>
                <div *ngIf="isSaving()" class="spinner w-4 h-4"></div>
              </button>
            </div>
          </form>
        </div>
      </div>

      <!-- DELETE MODAL -->
      <app-confirm-modal
        [isOpen]="showDeleteModal()"
        title="Eliminar Usuario"
        [message]="'¿Estás seguro de que deseas eliminar al usuario ' + (selectedUser()?.correo || '') + '? Esta acción no se puede deshacer.'"
        confirmText="Eliminar"
        type="danger"
        (confirmed)="onDeleteConfirm()"
        (cancelled)="closeModals()">
      </app-confirm-modal>

    </app-main-layout>
  `
})
export class UserManagementComponent implements OnInit {
  private userService = inject(UserService);
  private alertService = inject(AlertService);
  private fb = inject(FormBuilder);

  // State
  users = signal<UsuarioResponse[]>([]);
  searchTerm = signal('');
  selectedRole = signal('TODOS');
  isLoading = signal(true);
  isSaving = signal(false);
  
  // Modal states
  showCreateModal = signal(false);
  showEditModal = signal(false);
  showDeleteModal = signal(false);
  selectedUser = signal<UsuarioResponse | null>(null);

  // Computed state for table
  filteredUsers = computed(() => {
    let result = this.users();
    
    // Role filter
    if (this.selectedRole() !== 'TODOS') {
      result = result.filter(u => u.rol === this.selectedRole());
    }
    
    // Search filter
    const term = this.searchTerm().toLowerCase();
    if (term) {
      result = result.filter(u => u.correo.toLowerCase().includes(term));
    }
    
    return result;
  });

  // Forms
  createForm = this.fb.group({
    correo: ['', [Validators.required, Validators.email]],
    contrasena: ['', [Validators.required, Validators.minLength(6)]],
    rol: ['INSTRUCTOR', [Validators.required]],
    nombre: ['', [Validators.required]],
    apellido: ['', [Validators.required]],
    documentoIdentidad: [null, [Validators.required, Validators.min(1)]],
    telefono: [null, [Validators.required, Validators.min(1)]],
    tipoContrato: ['PLANTA', [Validators.required]]
  });

  editForm = this.fb.group({
    correo: ['', [Validators.required, Validators.email]],
    contrasena: [''], // Optional
    rol: ['', [Validators.required]],
    estado: [true]
  });

  ngOnInit() {
    this.loadUsers();
  }

  loadUsers() {
    this.isLoading.set(true);
    this.userService.getAll().subscribe({
      next: (data: any) => {
        // Sort by ID descending
        const sorted = [...data].sort((a, b) => b.id - a.id);
        this.users.set(sorted);
        this.isLoading.set(false);
      },
      error: (err: any) => {
        this.isLoading.set(false);
        this.alertService.error('Error al cargar la lista de usuarios');
      }
    });
  }

  // Modals
  closeModals() {
    this.showCreateModal.set(false);
    this.showEditModal.set(false);
    this.showDeleteModal.set(false);
    this.selectedUser.set(null);
    this.createForm.reset({ rol: 'INSTRUCTOR', tipoContrato: 'PLANTA' });
    this.editForm.reset();
  }

  openCreateModal() {
    this.createForm.reset({ rol: 'INSTRUCTOR', tipoContrato: 'PLANTA' });
    this.showCreateModal.set(true);
  }

  openEditModal(user: UsuarioResponse) {
    this.selectedUser.set(user);
    this.editForm.patchValue({
      correo: user.correo,
      contrasena: '',
      rol: user.rol,
      estado: user.estado
    });
    this.showEditModal.set(true);
  }

  openDeleteModal(user: UsuarioResponse) {
    this.selectedUser.set(user);
    this.showDeleteModal.set(true);
  }

  // Actions
  onCreateSubmit() {
    if (this.createForm.invalid) {
      this.createForm.markAllAsTouched();
      return;
    }
    
    this.isSaving.set(true);
    this.userService.create(this.createForm.value as any).subscribe({
      next: () => {
        this.alertService.success('Usuario creado exitosamente');
        this.closeModals();
        this.loadUsers();
        this.isSaving.set(false);
      },
      error: (err: any) => {
        this.isSaving.set(false);
        this.alertService.error(err.error?.mensaje || 'Error al crear usuario');
      }
    });
  }

  onEditSubmit() {
    if (this.editForm.invalid || !this.selectedUser()) {
      this.editForm.markAllAsTouched();
      return;
    }
    
    this.isSaving.set(true);
    
    // Only send password if it was filled
    const payload: any = { ...this.editForm.value };
    if (!payload.contrasena) {
      delete payload.contrasena;
    }
    
    this.userService.update(this.selectedUser()!.id, payload).subscribe({
      next: () => {
        this.alertService.success('Usuario actualizado exitosamente');
        this.closeModals();
        this.loadUsers();
        this.isSaving.set(false);
      },
      error: (err: any) => {
        this.isSaving.set(false);
        this.alertService.error(err.error?.mensaje || 'Error al actualizar usuario');
      }
    });
  }

  onDeleteConfirm() {
    if (!this.selectedUser()) return;
    
    this.userService.delete(this.selectedUser()!.id).subscribe({
      next: () => {
        this.alertService.success('Usuario eliminado exitosamente');
        this.closeModals();
        this.loadUsers();
      },
      error: (err: any) => {
        this.closeModals();
        this.alertService.error(err.error?.mensaje || 'Error al eliminar usuario');
      }
    });
  }

  toggleStatus(user: UsuarioResponse) {
    if (user.estado) {
      // If active, call inhabilitar
      this.userService.disable(user.id).subscribe({
        next: () => {
          this.alertService.success('Usuario inhabilitado');
          this.loadUsers();
        },
        error: (err: any) => this.alertService.error(err.error?.mensaje || 'Error al inhabilitar usuario')
      });
    } else {
      // If inactive, update state to true
      this.userService.update(user.id, { estado: true }).subscribe({
        next: () => {
          this.alertService.success('Usuario habilitado');
          this.loadUsers();
        },
        error: (err: any) => this.alertService.error(err.error?.mensaje || 'Error al habilitar usuario')
      });
    }
  }
}
