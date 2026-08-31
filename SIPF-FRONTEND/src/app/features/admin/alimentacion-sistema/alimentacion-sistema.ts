import { Component, inject, ChangeDetectorRef, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatSelectModule } from '@angular/material/select';
import { FormsModule } from '@angular/forms';
import { ExcelService } from '../../../core/use-cases/excel.service';
import { ProgramaService } from '../../../core/use-cases/programa.service';
import { FichaService } from '../../../core/use-cases/ficha.service';
import { TrimestreService } from '../../../core/use-cases/trimestre.service';
import { UserService } from '../../../core/use-cases/user.service';
import { SweetAlertService } from '../../../core/use-cases/sweet-alert.service';
import { MainLayoutComponent } from '../../../shared/layouts/main-layout/main-layout';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ProgramaResponse } from '../../../core/entities/programa.model';

@Component({
  selector: 'app-alimentacion-sistema',
  standalone: true,
  imports: [
    CommonModule,
    MatIconModule,
    MatButtonModule,
    MatCardModule,
    MatSelectModule,
    FormsModule,
    MainLayoutComponent,
    MatProgressSpinnerModule
  ],
  templateUrl: './alimentacion-sistema.html',
  styleUrls: ['./alimentacion-sistema.css']
})
export class AlimentacionSistemaComponent implements OnInit {
  private excelService = inject(ExcelService);
  private programaService = inject(ProgramaService);
  private fichaService = inject(FichaService);
  private trimestreService = inject(TrimestreService);
  private userService = inject(UserService);
  private sweetAlertService = inject(SweetAlertService);
  private cdr = inject(ChangeDetectorRef);

  // Estados de Drag & Drop
  isDragging = false;
  selectedFile: File | null = null;
  isUploading = false;
  
  // Datos para Malla Curricular
  programas: ProgramaResponse[] = [];
  selectedProgramaId: number | null = null;

  // Datos para Reportes
  fichas: any[] = [];
  trimestres: any[] = [];
  instructores: any[] = [];
  
  selectedFichaId: number | null = null;
  selectedTrimestreFichaId: number | null = null;
  
  selectedInstructorId: number | null = null;
  selectedTrimestreInstructorId: number | null = null;

  ngOnInit() {
    this.cargarProgramas();
    this.cargarDatosReportes();
  }

  cargarProgramas() {
    this.programaService.getAll().subscribe({
      next: (data) => {
        this.programas = data || [];
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.warn('No se pudieron cargar los programas:', err);
      }
    });
  }

  cargarDatosReportes() {
    this.fichaService.getAll().subscribe((res: any[]) => {
      this.fichas = res || [];
      this.cdr.detectChanges();
    });
    
    this.trimestreService.getAll().subscribe((res: any[]) => {
      this.trimestres = res || [];
      this.cdr.detectChanges();
    });

    this.userService.getByRole('INSTRUCTOR').subscribe((res: any[]) => {
      this.instructores = res || [];
      this.cdr.detectChanges();
    });
  }

  // --- MÉTODOS DE DESCARGA DE REPORTES ---

  descargarReporteInstructor() {
    if (!this.selectedInstructorId || !this.selectedTrimestreInstructorId) {
      this.sweetAlertService.error('Atención', 'Selecciona el Instructor y el Trimestre.');
      return;
    }
    
    this.excelService.descargarReporteInstructor(this.selectedInstructorId, this.selectedTrimestreInstructorId)
      .subscribe({
        next: (blob) => {
          const url = window.URL.createObjectURL(blob);
          const a = document.createElement('a');
          a.href = url;
          a.download = 'reporte_instructor.xlsx';
          a.click();
          window.URL.revokeObjectURL(url);
        },
        error: (err) => {
          this.sweetAlertService.error('Error', 'No se pudo descargar el reporte del instructor.');
        }
      });
  }

  descargarReporteFicha() {
    if (!this.selectedFichaId || !this.selectedTrimestreFichaId) {
      this.sweetAlertService.error('Atención', 'Selecciona la Ficha y el Trimestre.');
      return;
    }
    
    this.excelService.descargarReporteFicha(this.selectedFichaId, this.selectedTrimestreFichaId)
      .subscribe({
        next: (blob) => {
          const url = window.URL.createObjectURL(blob);
          const a = document.createElement('a');
          a.href = url;
          a.download = 'reporte_ficha.xlsx';
          a.click();
          window.URL.revokeObjectURL(url);
        },
        error: (err) => {
          this.sweetAlertService.error('Error', 'No se pudo descargar el reporte de la ficha.');
        }
      });
  }

  // --- LÓGICA DE DRAG & DROP ORIGINAL ---

  onDragOver(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();
    if (!this.isUploading) this.isDragging = true;
  }

  onDragLeave(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();
    this.isDragging = false;
  }

  onDrop(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();
    this.isDragging = false;
    
    if (!this.isUploading && event.dataTransfer && event.dataTransfer.files.length > 0) {
      this.handleFile(event.dataTransfer.files[0]);
    }
  }

  onFileSelected(event: any) {
    if (!this.isUploading && event.target.files && event.target.files.length > 0) {
      this.handleFile(event.target.files[0]);
    }
  }

  private handleFile(file: File) {
    const validExtensions = ['.xlsx', '.xls'];
    const fileExtension = file.name.substring(file.name.lastIndexOf('.')).toLowerCase();
    
    if (validExtensions.includes(fileExtension)) {
      this.selectedFile = file;
    } else {
      this.sweetAlertService.error('Error', 'Archivo no válido: Por favor, selecciona un archivo Excel (.xlsx o .xls)');
      this.selectedFile = null;
    }
  }

  removeFile() {
    this.selectedFile = null;
  }

  uploadFile() {
    if (!this.selectedFile) return;
    if (!this.selectedProgramaId) {
      this.sweetAlertService.error('Atención', 'Debes seleccionar un Programa antes de subir el archivo.');
      return;
    }

    this.isUploading = true;
    this.cdr.detectChanges();

    this.excelService.subirAlimentacion(this.selectedFile, this.selectedProgramaId).subscribe({
      next: (response) => {
        this.isUploading = false;
        this.selectedFile = null;
        this.sweetAlertService.success('¡Éxito!', 'Carga Exitosa: La alimentación del sistema se ha completado exitosamente.');
        this.cdr.detectChanges();
      },
      error: (error) => {
        this.isUploading = false;
        const errorMessage = error.error || 'Ocurrió un error inesperado al procesar el archivo.';
        this.sweetAlertService.error('Error', 'Error al subir: ' + errorMessage);
        this.cdr.detectChanges();
      }
    });
  }
}
