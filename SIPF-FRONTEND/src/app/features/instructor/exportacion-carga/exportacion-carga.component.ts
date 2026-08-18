import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MainLayoutComponent } from '../../../shared/layouts/main-layout/main-layout';
import { AuthService } from '../../../core/use-cases/auth.service';
import { TrimestreService } from '../../../core/use-cases/trimestre.service';
import { ExcelService } from '../../../core/use-cases/excel.service';
import { SweetAlertService } from '../../../core/use-cases/sweet-alert.service';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';

@Component({
  selector: 'app-exportacion-carga',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MainLayoutComponent,
    MatIconModule,
    MatButtonModule,
    MatFormFieldModule,
    MatSelectModule
  ],
  templateUrl: './exportacion-carga.component.html',
  styleUrls: ['./exportacion-carga.component.css']
})
export class ExportacionCargaComponent implements OnInit {
  private authService = inject(AuthService);
  private trimestreService = inject(TrimestreService);
  private excelService = inject(ExcelService);
  private sweetAlertService = inject(SweetAlertService);

  usuarioId: number = this.authService.getUserId() || 1;
  nombreInstructor: string = String(this.authService.getNombre() || 'Instructor');
  
  trimestres: any[] = [];
  selectedTrimestreId: number | null = null;
  isDownloading = false;

  ngOnInit(): void {
    this.cargarTrimestres();
  }

  cargarTrimestres(): void {
    this.trimestreService.getAll().subscribe({
      next: (data) => {
        if (data && data.length > 0) {
          this.trimestres = data;
        } else {
          this.trimestres = [
            { id: 1, numeroTrimestre: 1, anio: 2026 },
            { id: 2, numeroTrimestre: 2, anio: 2026 },
            { id: 3, numeroTrimestre: 3, anio: 2026 },
            { id: 4, numeroTrimestre: 4, anio: 2026 }
          ];
        }
        if (this.trimestres.length > 0) {
          this.selectedTrimestreId = this.trimestres[0].id;
        }
      },
      error: () => {
        this.trimestres = [
          { id: 1, numeroTrimestre: 1, anio: 2026 },
          { id: 2, numeroTrimestre: 2, anio: 2026 },
          { id: 3, numeroTrimestre: 3, anio: 2026 },
          { id: 4, numeroTrimestre: 4, anio: 2026 }
        ];
        this.selectedTrimestreId = 1;
      }
    });
  }

  descargarReporteDirecto(): void {
    if (!this.selectedTrimestreId) {
      this.sweetAlertService.error('Atención', 'Selecciona el trimestre académico para la descarga.');
      return;
    }

    this.isDownloading = true;

    this.excelService.exportarCargaInstructor(this.usuarioId, this.selectedTrimestreId).subscribe({
      next: (datosCarga) => {
        this.isDownloading = false;
        
        if (!datosCarga || datosCarga.length === 0) {
          this.sweetAlertService.warning(
            'Sin Asignación de Carga',
            `No registras programaciones de clases guardadas en el Trimestre ${this.selectedTrimestreId}.`
          );
          return;
        }

        // Generación directa del Excel / CSV descargable
        const headers = ['Trimestre', 'Código Ficha', 'Programa', 'Jornada', 'Horas Presenciales', 'Descripción RAP'];
        const rows = datosCarga.map((d: any) => [
          d.trimestreNumero || d.trimestreId || this.selectedTrimestreId,
          d.ficha?.codigoFicha || d.codigoFicha || 'N/A',
          d.programa?.nombre || d.nombrePrograma || 'N/A',
          d.programa?.jornada || d.jornada || 'MAÑANA',
          d.horasPresenciales || d.horasAsignadas || 40,
          `"${(d.rap?.descripcion || d.descripcionRap || 'RAP de Formación').replace(/"/g, '""')}"`
        ]);

        const csvContent = 'data:text/csv;charset=utf-8,\uFEFF' 
          + [headers.join(','), ...rows.map((e: any) => e.join(','))].join('\n');

        const encodedUri = encodeURI(csvContent);
        const link = document.createElement('a');
        link.setAttribute('href', encodedUri);
        link.setAttribute('download', `CARGA_ACADEMICA_TRIMESTRE_${this.selectedTrimestreId}_${this.nombreInstructor.replace(/\s+/g, '_')}.csv`);
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);

        this.sweetAlertService.success(
          '¡Descarga Finalizada!',
          `Se ha descargado el archivo Excel/CSV con el reporte oficial de tu carga académica (${datosCarga.length} RAPs).`
        );
      },
      error: () => {
        this.isDownloading = false;
        this.sweetAlertService.error('Error', 'No se pudo conectar con el servidor para exportar el archivo de carga.');
      }
    });
  }
}
