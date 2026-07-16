import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-programacion-academica',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './programacion-academica.html',
  styleUrls: ['./programacion-academica.css']
})
export class ProgramacionAcademicaComponent implements OnInit {
  
  // Mocks para selectores
  fichas = [
    { id: 1, nombre: '2847123 - ADSO' },
    { id: 2, nombre: '2847124 - Producción Multimedia' }
  ];
  
  trimestres = [
    { id: 1, nombre: 'Trimestre 1' },
    { id: 2, nombre: 'Trimestre 2' },
    { id: 3, nombre: 'Trimestre 3' }
  ];

  selectedFicha: number | null = null;
  selectedTrimestre: number | null = null;

  // RAPs del trimestre
  raps = [
    { id: 1, competencia: 'Programación Orientada a Objetos', descripcion: 'Desarrollar componentes backend', horas: 80, estado: 'Pendiente', especialidad: 'Programación' },
    { id: 2, competencia: 'Bilingüismo', descripcion: 'Comprender textos en inglés', horas: 48, estado: 'Pendiente', especialidad: 'Bilingüismo' },
    { id: 3, competencia: 'Bases de Datos', descripcion: 'Diseñar el modelo relacional', horas: 60, estado: 'Asignado', instructor: 'Juan Pérez', especialidad: 'Bases de Datos' }
  ];

  selectedRap: any = null;

  // Instructores para el RAP seleccionado (Mock)
  instructoresSugeridos = [
    { id: 101, nombre: 'Ana Gómez', especialidad: 'Programación', jornada: 'MAÑANA', horasMaximas: 160, horasAsignadas: 100 },
    { id: 102, nombre: 'Carlos Ruiz', especialidad: 'Programación', jornada: 'TARDE', horasMaximas: 144, horasAsignadas: 140 }, // Casi lleno
    { id: 103, nombre: 'María Silva', especialidad: 'Programación', jornada: 'MAÑANA', horasMaximas: 160, horasAsignadas: 160 }, // Lleno
  ];

  searchTerm = '';
  filterJornada = '';
  
  ngOnInit(): void {
  }

  seleccionarRap(rap: any) {
    this.selectedRap = rap;
    // Reset filters
    this.searchTerm = '';
    this.filterJornada = '';
  }

  get filteredInstructores() {
    return this.instructoresSugeridos.filter(i => {
      const matchName = i.nombre.toLowerCase().includes(this.searchTerm.toLowerCase());
      const matchJornada = this.filterJornada ? i.jornada === this.filterJornada : true;
      return matchName && matchJornada;
    });
  }

  calcularPorcentajeHoras(asignadas: number, maximas: number): number {
    return (asignadas / maximas) * 100;
  }

  puedeAsignar(instructor: any): boolean {
    if (!this.selectedRap) return false;
    return (instructor.horasMaximas - instructor.horasAsignadas) >= this.selectedRap.horas;
  }

  asignarInstructor(instructor: any) {
    if (this.puedeAsignar(instructor)) {
      alert(`Instructor ${instructor.nombre} asignado correctamente al RAP.`);
      this.selectedRap.estado = 'Asignado';
      this.selectedRap.instructor = instructor.nombre;
      instructor.horasAsignadas += this.selectedRap.horas;
      this.selectedRap = null;
    } else {
      alert('El instructor no tiene suficientes horas disponibles.');
    }
  }

}
