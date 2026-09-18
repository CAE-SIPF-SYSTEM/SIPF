import { Component, OnInit, signal, effect, inject, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MainLayoutComponent } from '../../../shared/layouts/main-layout/main-layout';
import { FichaService } from '../../../core/use-cases/ficha.service';
import { ProgramaService } from '../../../core/use-cases/programa.service';
import { FichaResponse } from '../../../core/entities/ficha.model';
import { ProgramaResponse } from '../../../core/entities/programa.model';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { SweetAlertService } from '../../../core/use-cases/sweet-alert.service';
import { FichaFormDialogComponent } from './ficha-form-dialog.component';
import { ConfirmDialogComponent } from '../../../shared/components/confirm-modal/confirm-modal';

@Component({
  selector: 'app-listar-fichas',
  standalone: true,
  imports: [
    CommonModule, FormsModule, ReactiveFormsModule, MainLayoutComponent,
    MatTableModule, MatPaginatorModule, MatSortModule,
    MatFormFieldModule, MatInputModule, MatSelectModule,
    MatButtonModule, MatIconModule, MatDialogModule
  ],
  templateUrl: './listar-fichas.component.html',
  styleUrl: './listar-fichas.component.css'
})
export class ListarFichasComponent implements OnInit {
  private fichaService = inject(FichaService);
  private programaService = inject(ProgramaService);
  private dialog = inject(MatDialog);
  private sweetAlertService = inject(SweetAlertService);

  fichas = signal<FichaResponse[]>([]);
  programas = signal<ProgramaResponse[]>([]);
  searchTerm = signal('');
  selectedProgramaId = signal<number | 'TODOS'>('TODOS');
  isLoading = signal(true);
  
  displayedColumns: string[] = ['codigoFicha', 'programa', 'fechaInicio', 'fechaFin'];
  dataSource = new MatTableDataSource<FichaResponse>([]);

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor() {
    effect(() => {
      let result = this.fichas();
      
      if (this.selectedProgramaId() !== 'TODOS') {
        result = result.filter(f => f.programaId === this.selectedProgramaId());
      }
      
      const rawTerm = this.searchTerm();
      const term = rawTerm ? rawTerm.toString().toLowerCase() : '';
      if (term) {
        result = result.filter(f => 
          (f.codigoFicha?.toLowerCase().includes(term))
        );
      }
      
      this.dataSource.data = result;
      if (this.dataSource.paginator) {
        this.dataSource.paginator.firstPage();
      }
    });
  }

  ngOnInit() {
    this.loadProgramas();
    this.loadFichas();
  }

  loadProgramas() {
    this.programaService.getAll().subscribe({
      next: (data) => this.programas.set(data),
      error: () => this.sweetAlertService.error('Error', 'Error al cargar programas')
    });
  }

  loadFichas() {
    this.isLoading.set(true);
    this.fichaService.getAll().subscribe({
      next: (data) => {
        const sorted = [...data].sort((a, b) => b.id - a.id);
        this.fichas.set(sorted);
        setTimeout(() => {
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;
        });
        this.isLoading.set(false);
      },
      error: () => {
        this.isLoading.set(false);
        this.sweetAlertService.error('Error', 'Error al cargar la lista de fichas');
      }
    });
  }

  getProgramaNombre(id: number): string {
    const prog = this.programas().find(p => p.id === id);
    return prog ? prog.nombre : 'Desconocido';
  }

  openCreateDialog() {
    const dialogRef = this.dialog.open(FichaFormDialogComponent, {
      width: '600px',
      data: { mode: 'create' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && result.action === 'save') {
        this.onCreateSubmit(result.value);
      }
    });
  }

  openEditDialog(ficha: FichaResponse) {
    const dialogRef = this.dialog.open(FichaFormDialogComponent, {
      width: '600px',
      data: { mode: 'edit', ficha }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (result.action === 'save') {
          this.onEditSubmit(ficha, result.value);
        } else if (result.action === 'delete') {
          this.onDeleteConfirm(ficha);
        }
      }
    });
  }

  onCreateSubmit(formValue: any) {
    this.fichaService.create(formValue).subscribe({
      next: () => {
        this.sweetAlertService.success('¡Éxito!', 'Ficha creada exitosamente');
        this.loadFichas();
      },
      error: (err) => {
        this.sweetAlertService.error('Error', err.error?.mensaje || 'Error al crear ficha');
      }
    });
  }

  onEditSubmit(ficha: FichaResponse, formValue: any) {
    this.fichaService.update(ficha.id, formValue).subscribe({
      next: () => {
        this.sweetAlertService.success('¡Actualizado!', 'Ficha actualizada exitosamente');
        this.loadFichas();
      },
      error: (err) => {
        this.sweetAlertService.error('Error', err.error?.mensaje || 'Error al actualizar ficha');
      }
    });
  }

  async onDeleteConfirm(ficha: FichaResponse) {
    const confirmed = await this.sweetAlertService.confirmDelete(
      'Eliminar Ficha',
      `¿Estás seguro de que deseas eliminar la ficha "${ficha.codigoFicha}"? Esta acción no se puede deshacer.`
    );

    if (confirmed) {
      this.fichaService.delete(ficha.id).subscribe({
        next: () => {
          this.sweetAlertService.success('¡Eliminado!', 'Ficha eliminada exitosamente');
          this.loadFichas();
        },
        error: (err) => {
          this.sweetAlertService.error('Error', err.error?.mensaje || 'Error al eliminar ficha');
        }
      });
    }
  }
}
