import { MunicipioResponse } from '../use-cases/ubicacion.service';

export interface CrearProgramaRequest {
  nombre: string;
  municipio: MunicipioResponse | string;
  nivelFormacion: 'TECNICO' | 'TECNOLOGO' | 'ESPECIALIZACION' | 'OPERARIO' | 'AUXILIAR';
  jornada: 'MAÑANA' | 'TARDE';
  duracionpracticas: number;
}

export interface ProgramaResponse {
  id: number;
  nombre: string;
  municipio: MunicipioResponse | string;
  nivelFormacion: string;
  jornada: string;
  duracionpracticas: number;
}
