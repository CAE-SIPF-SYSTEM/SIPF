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
import { RapService } from '../../../core/use-cases/rap.service';
import { CompetenciaService } from '../../../core/use-cases/competencia.service';
import { AlertService } from '../../../core/use-cases/alert.service';
import { RapResponse } from '../../../core/entities/rap.model';
import { CompetenciaResponse } from '../../../core/entities/competencia.model';
import { CrearRapComponent } from '../crear-rap/crear-rap';

@Component({
  selector: 'app-listar-raps',
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
  templateUrl: './listar-raps.html',
  styleUrls: ['./listar-raps.css']
})
export class ListarRapsComponent implements OnInit, AfterViewInit {
  private rapService = inject(RapService);
  private competenciaService = inject(CompetenciaService);
  private alertService = inject(AlertService);
  private dialog = inject(MatDialog);

  dataSource = new MatTableDataSource<RapResponse>([]);
  displayedColumns = ['id', 'competenciaId', 'descripcion', 'acciones'];
  
  competencias: CompetenciaResponse[] = [];

  isLoading = signal(true);
  searchTerm = signal('');
  selectedCompetenciaId = signal<number | 'TODOS'>('TODOS');

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor() {
    effect(() => {
      this.dataSource.filter = JSON.stringify({
        term: this.searchTerm().toLowerCase(),
        comp: this.selectedCompetenciaId()
      });
    });
  }

  ngOnInit() {
    this.cargarDatos();
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.filterPredicate = (data: RapResponse, filter: string) => {
      const searchData = JSON.parse(filter);
      let match = true;
      if (searchData.term) {
        match = match && data.descripcion.toLowerCase().includes(searchData.term);
      }
      if (searchData.comp !== 'TODOS') {
        match = match && data.competenciaId === searchData.comp;
      }
      return match;
    };
  }

  cargarDatos() {
    this.isLoading.set(true);
    // Cargar competencias para los selects y descripciones
    this.competenciaService.getAll().subscribe({
      next: (comps) => {
        this.competencias = comps;
        // Luego cargar RAPs
        this.rapService.getAll().subscribe({
          next: (raps) => {
            this.dataSource.data = raps;
            this.isLoading.set(false);
          },
          error: () => {
            this.alertService.error('Error al cargar resultados de aprendizaje');
            this.isLoading.set(false);
          }
        });
      },
      error: () => {
        this.alertService.error('Error al cargar dependencias (Competencias)');
        this.isLoading.set(false);
      }
    });
  }

  openCreateDialog() {
    const dialogRef = this.dialog.open(CrearRapComponent, {
      width: '550px',
      disableClose: true,
      data: { competencias: this.competencias } // Pasamos competencias al dialog
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.cargarDatos();
      }
    });
  }

  eliminarRap(id: number) {
    if (confirm('¿Estás seguro de eliminar este resultado de aprendizaje?')) {
      this.rapService.delete(id).subscribe({
        next: () => {
          this.alertService.success('RAP eliminado exitosamente');
          this.cargarDatos();
        },
        error: () => this.alertService.error('No se pudo eliminar el RAP')
      });
    }
  }

  getNombreCompetencia(id: number): string {
    const comp = this.competencias.find(c => c.id === id);
    return comp ? comp.nombre : `ID: ${id}`;
  }
}
