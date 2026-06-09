import { Component, input, output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-confirm-modal',
  imports: [CommonModule],
  templateUrl: './confirm-modal.component.html',
  styleUrl: './confirm-modal.component.css'
})
export class ConfirmModalComponent {
  isOpen = input<boolean>(false);
  title = input<string>('Confirmar acción');
  message = input<string>('¿Estás seguro?');
  confirmText = input<string>('Confirmar');
  type = input<'danger' | 'warning'>('danger');
  
  confirmed = output<void>();
  cancelled = output<void>();
}
