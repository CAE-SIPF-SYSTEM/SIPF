export interface RegistrarFichaRequest {
  codigoFicha: string;
  programaId: number;
  fechaInicio: string; // ISO format
  fechaFin: string; // ISO format
}

export interface FichaResponse {
  id: number;
  codigoFicha: string;
  programaId: number;
  fechaInicio: string;
  fechaFin: string;
}
