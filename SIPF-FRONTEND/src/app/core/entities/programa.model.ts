export interface CrearProgramaRequest {
  nombre: string;
  municipio: string;
  nivelFormacion: 'TECNICO' | 'TECNOLOGO' | 'ESPECIALIZACION' | 'OPERARIO' | 'AUXILIAR';
  jornada: 'MAÑANA' | 'TARDE';
  duracionpracticas: number;
}

export interface ProgramaResponse {
  id: number;
  nombre: string;
  municipio: string;
  nivelFormacion: string;
  jornada: string;
  duracionpracticas: number;
}
