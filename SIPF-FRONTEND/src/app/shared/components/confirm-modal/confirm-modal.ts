import { Component, input, output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-confirm-modal',
  imports: [CommonModule],
  template: `
    <div *ngIf="isOpen()" class="fixed inset-0 z-50 flex items-center justify-center p-4">
      <!-- Backdrop -->
      <div class="absolute inset-0 bg-slate-900/80 backdrop-blur-sm animate-fade-in" (click)="cancelled.emit()"></div>
      
      <!-- Modal Panel -->
      <div class="relative bg-slate-800 border border-slate-700 rounded-xl shadow-2xl p-6 w-full max-w-md animate-fade-in-up">
        
        <!-- Icon & Title -->
        <div class="flex items-start gap-4 mb-4">
          <div class="shrink-0 w-10 h-10 rounded-full flex items-center justify-center"
               [ngClass]="type() === 'danger' ? 'bg-rose-500/20 text-rose-500' : 'bg-amber-500/20 text-amber-500'">
            <svg *ngIf="type() === 'danger'" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 6h18"/><path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6"/><path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2"/><line x1="10" x2="10" y1="11" y2="17"/><line x1="14" x2="14" y1="11" y2="17"/></svg>
            <svg *ngIf="type() === 'warning'" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m21.73 18-8-14a2 2 0 0 0-3.48 0l-8 14A2 2 0 0 0 4 21h16a2 2 0 0 0 1.73-3Z"/><line x1="12" y1="9" x2="12" y2="13"/><line x1="12" y1="17" x2="12.01" y2="17"/></svg>
          </div>
          <div>
            <h3 class="text-lg font-semibold text-white">{{ title() }}</h3>
            <p class="mt-2 text-sm text-slate-300">{{ message() }}</p>
          </div>
        </div>
        
        <!-- Actions -->
        <div class="mt-6 flex justify-end gap-3">
          <button (click)="cancelled.emit()" class="btn-secondary">Cancelar</button>
          <button (click)="confirmed.emit()" [class]="type() === 'danger' ? 'btn-danger' : 'btn-primary'">
            {{ confirmText() }}
          </button>
        </div>
      </div>
    </div>
  `
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
