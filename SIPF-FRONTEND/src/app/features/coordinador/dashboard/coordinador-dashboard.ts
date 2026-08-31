import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MainLayoutComponent } from '../../../shared/layouts/main-layout/main-layout';
import { AuthService } from '../../../core/use-cases/auth.service';
import { FichaService, FichaAvance } from '../../../core/use-cases/ficha.service';
import { ProgramaService } from '../../../core/use-cases/programa.service';
import { ProgramaResponse } from '../../../core/entities/programa.model';

@Component({
  selector: 'app-coordinador-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, MainLayoutComponent],
  templateUrl: './coordinador-dashboard.component.html',
  styleUrls: ['./coordinador-dashboard.component.css']
})
export class CoordinadorDashboardComponent implements OnInit {
  authService = inject(AuthService);
  private fichaService = inject(FichaService);
  private programaService = inject(ProgramaService);
  private router = inject(Router);

  email = this.authService.getUserEmail();

  fichasAvance: FichaAvance[] = [];
  programas: ProgramaResponse[] = [];

  selectedProgramaNombre = '';
  selectedJornada = '';
  searchTerm = '';
  cargando = true;

  ngOnInit(): void {
    this.cargarDatos();
  }

  cargarDatos(): void {
    this.cargando = true;

    this.programaService.getAll().subscribe({
      next: (progs) => {
        this.programas = progs || [];
      },
      error: () => {
        this.programas = [];
      }
    });

    this.fichaService.getAvanceFichas().subscribe({
      next: (data) => {
        this.fichasAvance = data || [];
        this.cargando = false;
      },
      error: () => {
        this.fichasAvance = [];
        this.cargando = false;
      }
    });
  }

  get fichasFiltradas(): FichaAvance[] {
    return this.fichasAvance.filter(f => {
      const matchPrograma = this.selectedProgramaNombre 
        ? f.programaNombre === this.selectedProgramaNombre 
        : true;

      const matchJornada = this.selectedJornada 
        ? f.jornada.toUpperCase() === this.selectedJornada.toUpperCase() 
        : true;

      const matchSearch = this.searchTerm 
        ? (f.codigoFicha || '').toLowerCase().includes(this.searchTerm.toLowerCase()) ||
          (f.programaNombre || '').toLowerCase().includes(this.searchTerm.toLowerCase())
        : true;

      return matchPrograma && matchJornada && matchSearch;
    });
  }

  get totalFichas(): number {
    return this.fichasAvance.length;
  }

  get promedioCumplimiento(): number {
    if (this.fichasAvance.length === 0) return 0;
    const suma = this.fichasAvance.reduce((acc, curr) => acc + curr.porcentajeAvance, 0);
    return Math.round(suma / this.fichasAvance.length);
  }

  get fichasCriticasCount(): number {
    return this.fichasAvance.filter(f => f.porcentajeAvance < 40).length;
  }

  getBarColorClass(porcentaje: number): string {
    if (porcentaje >= 80) return 'bg-emerald-500';
    if (porcentaje >= 40) return 'bg-amber-500';
    return 'bg-rose-500';
  }

  getBadgeClass(jornada: string): string {
    const j = (jornada || '').toUpperCase();
    if (j === 'MAÑANA') return 'bg-sky-100 text-sky-700 border-sky-200';
    if (j === 'TARDE') return 'bg-amber-100 text-amber-700 border-amber-200';
    if (j === 'NOCHE') return 'bg-indigo-100 text-indigo-700 border-indigo-200';
    return 'bg-slate-100 text-slate-700 border-slate-200';
  }

  irAProgramacion(fichaId: number): void {
    this.router.navigate(['/coordinador/programacion-academica'], { queryParams: { fichaId } });
  }

  limpiarFiltros(): void {
    this.selectedProgramaNombre = '';
    this.selectedJornada = '';
    this.searchTerm = '';
  }
}
