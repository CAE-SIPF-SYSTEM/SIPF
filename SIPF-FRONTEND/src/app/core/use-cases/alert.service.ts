import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

export interface Alert {
  id: string;
  type: 'success' | 'error' | 'warning' | 'info';
  message: string;
}

@Injectable({ providedIn: 'root' })
export class AlertService {
  private readonly alertsSubject = new BehaviorSubject<Alert[]>([]);
  readonly alerts$: Observable<Alert[]> = this.alertsSubject.asObservable();

  success(message: string): void {
    this.addAlert('success', message, 4000);
  }

  error(message: string): void {
    this.addAlert('error', message, 5000);
  }

  warning(message: string): void {
    this.addAlert('warning', message, 4000);
  }

  info(message: string): void {
    this.addAlert('info', message, 4000);
  }

  remove(id: string): void {
    const current = this.alertsSubject.getValue();
    this.alertsSubject.next(current.filter((alert) => alert.id !== id));
  }

  private addAlert(type: Alert['type'], message: string, duration: number): void {
    const id = crypto.randomUUID();
    const alert: Alert = { id, type, message };
    const current = this.alertsSubject.getValue();
    this.alertsSubject.next([...current, alert]);

    setTimeout(() => this.remove(id), duration);
  }
}
