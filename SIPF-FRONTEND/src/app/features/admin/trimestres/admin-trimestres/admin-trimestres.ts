import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { SweetAlertService } from '../../../../core/use-cases/sweet-alert.service';
import { TrimestreService } from '../../../../core/use-cases/trimestre.service';
import { Trimestre } from '../../../../core/entities/trimestre.model';
import { TrimestreDialogComponent } from '../trimestre-dialog/trimestre-dialog';
import { MainLayoutComponent } from '../../../../shared/layouts/main-layout/main-layout';

@Component({
  selector: 'app-admin-trimestres',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatDialogModule,
    MatDialogModule,
    MainLayoutComponent
  ],
  templateUrl: './admin-trimestres.html',
  styleUrls: ['./admin-trimestres.css']
})
export class AdminTrimestresComponent implements OnInit {
  private readonly trimestreService = inject(TrimestreService);
  private readonly dialog = inject(MatDialog);
  private readonly sweetAlertService = inject(SweetAlertService);

  trimestres = signal<Trimestre[]>([]);
  displayedColumns: string[] = ['id', 'fichaId', 'anio', 'numeroTrimestre', 'fechaInicio', 'fechaFin', 'acciones'];

  ngOnInit(): void {
    this.loadTrimestres();
  }

  loadTrimestres(): void {
    this.trimestreService.getAll().subscribe({
      next: (data) => this.trimestres.set(data),
      error: (err) => this.sweetAlertService.error('Error al cargar los trimestres')
    });
  }

  openDialog(trimestre?: Trimestre): void {
    const dialogRef = this.dialog.open(TrimestreDialogComponent, {
      width: '500px',
      data: trimestre ? { ...trimestre } : null
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (trimestre && trimestre.id) {
          this.trimestreService.update(trimestre.id, result).subscribe({
            next: () => {
              this.sweetAlertService.success('Trimestre actualizado correctamente');
              this.loadTrimestres();
            },
            error: () => this.sweetAlertService.error('Error al actualizar el trimestre')
          });
        } else {
          this.trimestreService.create(result).subscribe({
            next: () => {
              this.sweetAlertService.success('Trimestre creado correctamente');
              this.loadTrimestres();
            },
            error: () => this.sweetAlertService.error('Error al crear el trimestre')
          });
        }
      }
    });
  }

  async deleteTrimestre(id: number): Promise<void> {
    const confirmed = await this.sweetAlertService.confirmDelete(
      '¿Eliminar Trimestre?',
      'Esta acción no se puede deshacer.'
    );

    if (confirmed) {
      this.trimestreService.delete(id).subscribe({
        next: () => {
          this.sweetAlertService.success('Trimestre eliminado correctamente');
          this.loadTrimestres();
        },
        error: () => this.sweetAlertService.error('Error al eliminar el trimestre')
      });
    }
  }
}
