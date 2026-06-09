import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AlertService, Alert } from '../../../core/use-cases/alert.service';

@Component({
  selector: 'app-alert-container',
  imports: [CommonModule],
  templateUrl: './alert-container.component.html',
  styleUrl: './alert-container.component.css'
})
export class AlertContainerComponent {
  alertService = inject(AlertService);
}
