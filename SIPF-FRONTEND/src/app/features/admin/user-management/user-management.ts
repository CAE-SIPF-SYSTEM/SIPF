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
  templateUrl: './user-management.component.html',
  styleUrl: './user-management.component.css'
})
export class UserManagementComponent implements OnInit {
  private userService = inject(UserService);
  private alertService = inject(AlertService);
  private fb = inject(FormBuilder);

  users = signal<UsuarioResponse[]>([]);
  searchTerm = signal('');
  selectedRole = signal('TODOS');
  selectedEstado = signal('TODOS');
  isLoading = signal(true);
  isSaving = signal(false);
  
  showCreateModal = signal(false);
  showEditModal = signal(false);
  showDeleteModal = signal(false);
  selectedUser = signal<UsuarioResponse | null>(null);

  filteredUsers = computed(() => {
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
        (u.correo?.toLowerCase().includes(term))
      );
    }
    
    return result;
  });

  createForm = this.fb.group({
    nombreCompleto: ['', [Validators.required]],
    correo: ['', [Validators.required, Validators.email]],
    contrasena: ['', [Validators.required, Validators.minLength(6)]],
    rol: ['', [Validators.required]],
    documentoIdentidad: [null, [Validators.required, Validators.min(1)]],
    telefono: [null, [Validators.required, Validators.min(1)]],
    estado: [true]
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

  closeModals() {
    this.showCreateModal.set(false);
    this.showEditModal.set(false);
    this.showDeleteModal.set(false);
    this.selectedUser.set(null);
    this.createForm.reset({ estado: true });
    this.editForm.reset();
  }

  openCreateModal() {
    this.createForm.reset({ estado: true });
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
    this.showEditModal.set(false);
    this.selectedUser.set(user);
    this.showDeleteModal.set(true);
  }

  onCreateSubmit() {
    if (this.createForm.invalid) {
      this.createForm.markAllAsTouched();
      return;
    }
    
    this.isSaving.set(true);
    
    const formValue = this.createForm.value;
    
    // Separa nombre completo en nombre y apellido
    let nombre = formValue.nombreCompleto || '';
    let apellido = ' ';
    
    if (nombre.includes(' ')) {
      const parts = nombre.split(' ');
      nombre = parts[0];
      apellido = parts.slice(1).join(' ');
    }

    const payload: CrearUsuarioRequest = {
      correo: formValue.correo!,
      contrasena: formValue.contrasena!,
      rol: formValue.rol!,
      nombre: nombre,
      apellido: apellido,
      documentoIdentidad: formValue.documentoIdentidad!,
      telefono: formValue.telefono!,
      tipoContrato: 'PLANTA'
    };

    this.userService.create(payload as any).subscribe({
      next: (newUser: any) => {
        if (formValue.estado === false && newUser && newUser.id) {
           this.userService.disable(newUser.id).subscribe(() => this.finalizeCreation());
        } else {
           this.finalizeCreation();
        }
      },
      error: (err: any) => {
        this.isSaving.set(false);
        this.alertService.error(err.error?.mensaje || 'Error al crear usuario');
      }
    });
  }
  
  private finalizeCreation() {
    this.alertService.success('Usuario creado exitosamente');
    this.closeModals();
    this.loadUsers();
    this.isSaving.set(false);
  }

  onEditSubmit() {
    if (this.editForm.invalid || !this.selectedUser()) {
      this.editForm.markAllAsTouched();
      return;
    }
    
    this.isSaving.set(true);
    
    const payload: any = { ...this.editForm.value };
    if (!payload.contrasena) {
      delete payload.contrasena;
    }

    const wasActive = this.selectedUser()!.estado;
    const isNowActive = payload.estado;
    delete payload.estado;

    this.userService.update(this.selectedUser()!.id, payload).subscribe({
      next: () => {
        if (wasActive && !isNowActive) {
          this.userService.disable(this.selectedUser()!.id).subscribe(() => this.finalizeEdit());
        } else if (!wasActive && isNowActive) {
          this.userService.enable(this.selectedUser()!.id).subscribe(() => this.finalizeEdit());
        } else {
          this.finalizeEdit();
        }
      },
      error: (err: any) => {
        this.isSaving.set(false);
        this.alertService.error(err.error?.mensaje || 'Error al actualizar usuario');
      }
    });
  }
  
  private finalizeEdit() {
    this.alertService.success('Usuario actualizado exitosamente');
    this.closeModals();
    this.loadUsers();
    this.isSaving.set(false);
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
}
