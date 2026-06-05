import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MainLayoutComponent } from '../../../shared/layouts/main-layout/main-layout';
import { AuthService } from '../../../core/use-cases/auth.service';

@Component({
  selector: 'app-coordinador-dashboard',
  imports: [CommonModule, MainLayoutComponent],
  templateUrl: './coordinador-dashboard.component.html',
  styleUrls: ['./coordinador-dashboard.component.css']
})
export class CoordinadorDashboardComponent {
  authService = inject(AuthService);
  email = this.authService.getUserEmail();
}
