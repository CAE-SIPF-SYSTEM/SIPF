export interface CrearCompetenciaRequest {
  nombre: string;
  tipoCompetencia: string;
}

export interface EditarCompetenciaRequest {
  nombre: string;
  tipoCompetencia: string;
}

export interface CompetenciaResponse {
  id: number;
  nombre: string;
  tipoCompetencia: string;
}
