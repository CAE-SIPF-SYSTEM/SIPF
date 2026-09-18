import { Component, OnInit, signal, computed, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MainLayoutComponent } from '../../../shared/layouts/main-layout/main-layout';
import { UserService } from '../../../core/use-cases/user.service';
import { UsuarioResponse } from '../../../core/entities/user.model';
import { AlertService } from '../../../core/use-cases/alert.service';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, MainLayoutComponent, MatCardModule, MatIconModule],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css'
})
export class AdminDashboardComponent implements OnInit {
  private userService = inject(UserService);
  private alertService = inject(AlertService);

  currentDate = new Date();
  
  users = signal<UsuarioResponse[]>([]);
  recentUsers = signal<UsuarioResponse[]>([]);
  
  totalUsers = signal(0);
  totalAdmins = signal(0);
  totalInstructors = signal(0);
  totalCoordinators = signal(0);
  
  isLoading = signal(true);

  ngOnInit() {
    this.loadUsers();
  }

  loadUsers() {
    this.isLoading.set(true);
    this.userService.getAll().subscribe({
      next: (data: any) => {
        this.users.set(data);
        this.totalUsers.set(data.length);
        this.totalAdmins.set(data.filter((u: any) => u.rol === 'ADMINISTRADOR').length);
        this.totalInstructors.set(data.filter((u: any) => u.rol === 'INSTRUCTOR').length);
        this.totalCoordinators.set(data.filter((u: any) => u.rol === 'COORDINADOR').length);
        
        // Get last 5 users based on ID
        const sorted = [...data].sort((a, b) => b.id - a.id);
        this.recentUsers.set(sorted.slice(0, 5));
        
        this.isLoading.set(false);
      },
      error: (err: any) => {
        this.isLoading.set(false);
        this.alertService.error('Error al cargar estadísticas de usuarios');
      }
    });
  }
}
