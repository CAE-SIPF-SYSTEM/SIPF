import { Component, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { ExcelService } from '../../../core/use-cases/excel.service';
import { AlertService } from '../../../core/use-cases/alert.service';
import { MainLayoutComponent } from '../../../shared/layouts/main-layout/main-layout';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-alimentacion-sistema',
  standalone: true,
  imports: [
    CommonModule,
    MatIconModule,
    MatButtonModule,
    MatCardModule,
    MainLayoutComponent,
    MatProgressSpinnerModule
  ],
  templateUrl: './alimentacion-sistema.html',
  styleUrls: ['./alimentacion-sistema.css']
})
export class AlimentacionSistemaComponent {
  private excelService = inject(ExcelService);
  private alertService = inject(AlertService);
  private cdr = inject(ChangeDetectorRef);

  isDragging = false;
  selectedFile: File | null = null;
  isUploading = false;

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
      this.alertService.error('Archivo no válido: Por favor, selecciona un archivo Excel (.xlsx o .xls)');
      this.selectedFile = null;
    }
  }

  removeFile() {
    this.selectedFile = null;
  }

  uploadFile() {
    if (!this.selectedFile) return;

    this.isUploading = true;
    this.cdr.detectChanges();

    this.excelService.subirAlimentacion(this.selectedFile).subscribe({
      next: (response) => {
        this.isUploading = false;
        this.selectedFile = null;
        this.alertService.success('Carga Exitosa: La alimentación del sistema se ha completado exitosamente.');
        this.cdr.detectChanges();
      },
      error: (error) => {
        this.isUploading = false;
        const errorMessage = error.error || 'Ocurrió un error inesperado al procesar el archivo.';
        this.alertService.error('Error al subir: ' + errorMessage);
        this.cdr.detectChanges();
      }
    });
  }
}
