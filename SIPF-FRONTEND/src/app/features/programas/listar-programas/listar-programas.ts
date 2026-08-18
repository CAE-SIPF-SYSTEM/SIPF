import { Component, OnInit, signal, effect, inject, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MainLayoutComponent } from '../../../shared/layouts/main-layout/main-layout';
import { ProgramaService } from '../../../core/use-cases/programa.service';
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
import { ProgramaFormDialogComponent } from './programa-form-dialog.component';
import { ConfirmDialogComponent } from '../../../shared/components/confirm-modal/confirm-modal';

@Component({
  selector: 'app-listar-programas',
  standalone: true,
  imports: [
    CommonModule, FormsModule, ReactiveFormsModule, MainLayoutComponent,
    MatTableModule, MatPaginatorModule, MatSortModule,
    MatFormFieldModule, MatInputModule, MatSelectModule,
    MatButtonModule, MatIconModule, MatDialogModule
  ],
  templateUrl: './listar-programas.component.html',
  styleUrl: './listar-programas.component.css'
})
export class ListarProgramasComponent implements OnInit {
  private programaService = inject(ProgramaService);
  private dialog = inject(MatDialog);
  private sweetAlertService = inject(SweetAlertService);

  programas = signal<ProgramaResponse[]>([]);
  searchTerm = signal('');
  selectedNivel = signal('TODOS');
  selectedJornada = signal('TODOS');
  isLoading = signal(true);
  
  displayedColumns: string[] = ['id', 'nombre', 'municipio', 'nivelFormacion', 'jornada', 'duracionpracticas'];
  dataSource = new MatTableDataSource<ProgramaResponse>([]);

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor() {
    effect(() => {
      let result = this.programas();
      
      if (this.selectedNivel() !== 'TODOS') {
        result = result.filter(p => p.nivelFormacion === this.selectedNivel());
      }

      if (this.selectedJornada() !== 'TODOS') {
        result = result.filter(p => p.jornada === this.selectedJornada());
      }
      
      const rawTerm = this.searchTerm();
      const term = rawTerm ? rawTerm.toString().toLowerCase() : '';
      if (term) {
        result = result.filter(p => {
          const munStr = (typeof p.municipio === 'object' ? p.municipio?.nombre : p.municipio) || '';
          return (p.nombre?.toLowerCase().includes(term)) || 
                 (munStr.toLowerCase().includes(term)) ||
                 (p.id?.toString().includes(term));
        });
      }
      
      this.dataSource.data = result;
      if (this.dataSource.paginator) {
        this.dataSource.paginator.firstPage();
      }
    });
  }

  ngOnInit() {
    this.loadProgramas();
  }

  loadProgramas() {
    this.isLoading.set(true);
    this.programaService.getAll().subscribe({
      next: (data) => {
        const sorted = [...data].sort((a, b) => b.id - a.id);
        this.programas.set(sorted);
        setTimeout(() => {
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;
        });
        this.isLoading.set(false);
      },
      error: () => {
        this.isLoading.set(false);
        this.sweetAlertService.error('Error', 'Error al cargar la lista de programas');
      }
    });
  }

  openCreateDialog() {
    const dialogRef = this.dialog.open(ProgramaFormDialogComponent, {
      width: '600px',
      data: { mode: 'create' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && result.action === 'save') {
        this.onCreateSubmit(result.value);
      }
    });
  }

  openEditDialog(programa: ProgramaResponse) {
    const dialogRef = this.dialog.open(ProgramaFormDialogComponent, {
      width: '600px',
      data: { mode: 'edit', programa }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (result.action === 'save') {
          this.onEditSubmit(programa, result.value);
        } else if (result.action === 'delete') {
          this.onDeleteConfirm(programa);
        }
      }
    });
  }

  onCreateSubmit(formValue: any) {
    this.programaService.create(formValue).subscribe({
      next: () => {
        this.sweetAlertService.success('¡Éxito!', 'Programa creado exitosamente');
        this.loadProgramas();
      },
      error: (err) => {
        this.sweetAlertService.error('Error', err.error?.mensaje || 'Error al crear programa');
      }
    });
  }

  onEditSubmit(programa: ProgramaResponse, formValue: any) {
    this.programaService.update(programa.id, formValue).subscribe({
      next: () => {
        this.sweetAlertService.success('¡Actualizado!', 'Programa actualizado exitosamente');
        this.loadProgramas();
      },
      error: (err) => {
        this.sweetAlertService.error('Error', err.error?.mensaje || 'Error al actualizar programa');
      }
    });
  }

  async onDeleteConfirm(programa: ProgramaResponse) {
    const confirmed = await this.sweetAlertService.confirmDelete(
      'Eliminar Programa',
      `¿Estás seguro de que deseas eliminar el programa "${programa.nombre}"? Esta acción no se puede deshacer.`
    );

    if (confirmed) {
      this.programaService.delete(programa.id).subscribe({
        next: () => {
          this.sweetAlertService.success('¡Eliminado!', 'Programa eliminado exitosamente');
          this.loadProgramas();
        },
        error: (err) => {
          this.sweetAlertService.error('Error', err.error?.mensaje || 'Error al eliminar programa');
        }
      });
    }
  }
}
