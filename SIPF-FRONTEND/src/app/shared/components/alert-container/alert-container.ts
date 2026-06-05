import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AlertService, Alert } from '../../../core/use-cases/alert.service';

@Component({
  selector: 'app-alert-container',
  imports: [CommonModule],
  template: `
    <div class="fixed top-4 right-4 z-50 flex flex-col gap-2 pointer-events-none w-full max-w-sm">
      <div *ngFor="let alert of alertService.alerts$ | async" 
           class="pointer-events-auto overflow-hidden bg-slate-800 border shadow-lg rounded-lg flex items-start p-4 animate-fade-in-up"
           [ngClass]="{
             'border-emerald-500': alert.type === 'success',
             'border-rose-500': alert.type === 'error',
             'border-amber-500': alert.type === 'warning',
             'border-blue-500': alert.type === 'info'
           }">
        
        <!-- Icons -->
        <div class="shrink-0 mr-3">
          <!-- Success -->
          <svg *ngIf="alert.type === 'success'" class="w-5 h-5 text-emerald-500" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"/></svg>
          <!-- Error -->
          <svg *ngIf="alert.type === 'error'" class="w-5 h-5 text-rose-500" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/></svg>
          <!-- Warning -->
          <svg *ngIf="alert.type === 'warning'" class="w-5 h-5 text-amber-500" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/></svg>
          <!-- Info -->
          <svg *ngIf="alert.type === 'info'" class="w-5 h-5 text-blue-500" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>
        </div>
        
        <div class="flex-1 text-sm text-slate-200">
          {{ alert.message }}
        </div>
        
        <button (click)="alertService.remove(alert.id)" class="ml-4 shrink-0 text-slate-400 hover:text-white transition-colors">
          <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/></svg>
        </button>
      </div>
    </div>
  `
})
export class AlertContainerComponent {
  alertService = inject(AlertService);
}
