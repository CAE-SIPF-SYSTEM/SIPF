import { Component, inject, ChangeDetectorRef, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatSelectModule } from '@angular/material/select';
import { FormsModule } from '@angular/forms';
import { ExcelService } from '../../../core/use-cases/excel.service';
import { ProgramaService } from '../../../core/use-cases/programa.service';
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
  private sweetAlertService = inject(SweetAlertService);
  private cdr = inject(ChangeDetectorRef);

  isDragging = false;
  selectedFile: File | null = null;
  isUploading = false;
  
  programas: ProgramaResponse[] = [];
  selectedProgramaId: number | null = null;

  ngOnInit() {
    this.cargarProgramas();
  }

  cargarProgramas() {
    this.programaService.getAll().subscribe({
      next: (data) => {
        this.programas = data || [];
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.warn('No se pudieron cargar los programas dinámicos del backend:', err);
        this.programas = [];
        this.cdr.detectChanges();
      }
    });
  }

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
