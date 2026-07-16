export interface DisponibilidadInstructor {
  usuarioId: number;
  diasDisponibles: string[];
  horasMaximas: number;
  jornada?: string;
}
