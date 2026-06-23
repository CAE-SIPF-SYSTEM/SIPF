export interface CrearRapRequest {
  competenciaId: number;
  descripcion: string;
  horasPresenciales: number;
}

export interface EditarRapRequest {
  competenciaId: number;
  descripcion: string;
  horasPresenciales: number;
}

export interface RapResponse {
  id: number;
  competenciaId: number;
  descripcion: string;
  horasPresenciales: number;
}
