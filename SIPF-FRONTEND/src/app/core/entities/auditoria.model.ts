export interface AuditoriaPerfil {
  id: number;
  usuarioId: number;
  campoModificado: string;
  valorAnterior: string;
  valorNuevo: string;
  fechaModificacion: string;
  rolUsuario?: string; // Mapeado opcionalmente si se cruza con usuarios
}
