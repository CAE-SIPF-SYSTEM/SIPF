import { Component, OnInit, AfterViewInit, ViewChild, inject, signal, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { FormsModule } from '@angular/forms';
import { MainLayoutComponent } from '../../../shared/layouts/main-layout/main-layout';
import { CompetenciaService } from '../../../core/use-cases/competencia.service';
import { AlertService } from '../../../core/use-cases/alert.service';
import { CompetenciaResponse } from '../../../core/entities/competencia.model';
import { CrearCompetenciaComponent } from '../crear-competencia/crear-competencia';

@Component({
  selector: 'app-listar-competencias',
  standalone: true,
  imports: [
    CommonModule,
    MainLayoutComponent,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatPaginatorModule,
    MatDialogModule,
    FormsModule
  ],
  templateUrl: './listar-competencias.html',
  styleUrls: ['./listar-competencias.css']
})
export class ListarCompetenciasComponent implements OnInit, AfterViewInit {
  private competenciaService = inject(CompetenciaService);
  private alertService = inject(AlertService);
  private dialog = inject(MatDialog);

  dataSource = new MatTableDataSource<CompetenciaResponse>([]);
  displayedColumns = ['id', 'nombre', 'tipoCompetencia', 'acciones'];
  
  isLoading = signal(true);
  searchTerm = signal('');
  selectedTipo = signal('TODOS');

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor() {
    effect(() => {
      this.dataSource.filter = JSON.stringify({
        term: this.searchTerm().toLowerCase(),
        tipo: this.selectedTipo()
      });
    });
  }

  ngOnInit() {
    this.cargarCompetencias();
  }

  cargarCompetencias() {
    this.isLoading.set(true);
    this.competenciaService.getAll().subscribe({
      next: (data) => {
        this.dataSource.data = data;
        this.isLoading.set(false);
      },
      error: () => {
        this.alertService.error('Error al cargar competencias');
        this.isLoading.set(false);
      }
    });
  }

  openCreateDialog() {
    const dialogRef = this.dialog.open(CrearCompetenciaComponent, {
      width: '500px',
      disableClose: true
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.cargarCompetencias();
      }
    });
  }

  eliminarCompetencia(id: number) {
    if (confirm('¿Estás seguro de eliminar esta competencia?')) {
      this.competenciaService.delete(id).subscribe({
        next: () => {
          this.alertService.success('Competencia eliminada exitosamente');
          this.cargarCompetencias();
        },
        error: () => this.alertService.error('No se pudo eliminar la competencia')
      });
    }
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.filterPredicate = (data: CompetenciaResponse, filter: string) => {
      const searchData = JSON.parse(filter);
      let match = true;
      if (searchData.term) {
        match = match && data.nombre.toLowerCase().includes(searchData.term);
      }
      if (searchData.tipo !== 'TODOS') {
        match = match && data.tipoCompetencia === searchData.tipo;
      }
      return match;
    };
  }
}
