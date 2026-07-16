import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MainLayoutComponent } from '../../../shared/layouts/main-layout/main-layout';
import { AuthService } from '../../../core/use-cases/auth.service';

@Component({
  selector: 'app-instructor-dashboard',
  imports: [CommonModule, MainLayoutComponent],
  templateUrl: './instructor-dashboard.component.html',
  styleUrls: ['./instructor-dashboard.component.css']
})
export class InstructorDashboardComponent {
  authService = inject(AuthService);
  email = this.authService.getUserEmail();
  nombre = this.authService.getNombre();
}
