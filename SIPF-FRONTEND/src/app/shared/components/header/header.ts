import { Component, input, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/use-cases/auth.service';

@Component({
  selector: 'app-header',
  imports: [CommonModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  authService = inject(AuthService);
  
  title = input<string>('Dashboard');
  
  email = this.authService.getUserEmail() || 'Usuario';
  role = this.authService.getRole() || '';
  initial = this.email.charAt(0).toUpperCase();

  get badgeClass(): string {
    switch(this.role) {
      case 'ADMINISTRADOR': return 'badge-admin';
      case 'INSTRUCTOR': return 'badge-instructor';
      case 'COORDINADOR': return 'badge-coordinador';
      default: return '';
    }
  }
}
