export interface LoginRequest {
  correo: string;
  contrasena: string;
}

export interface LoginResponse {
  id: number;
  correo: string;
  rol: 'COORDINADOR' | 'ADMINISTRADOR' | 'INSTRUCTOR';
  token: string;
  estado: boolean
}

export interface CrearUsuarioRequest {
  correo: string;
  contrasena: string;
  rol: string;
  nombre: string;
  apellido: string;
  documentoIdentidad: number;
  telefono: number;
  tipoContrato: 'PLANTA' | 'CONTRATISTA';
}

export interface EditarUsuarioRequest {
  correo?: string;
  contrasena?: string;
  rol?: string;
  estado?: boolean;
  nombre?: string;
  apellido?: string;
  documentoIdentidad?: number;
  telefono?: number;
  tipoContrato?: string;
}

export interface UsuarioResponse {
  id: number;
  correo: string;
  rol: string;
  estado: boolean;
  nombre?: string;
  apellido?: string;
  documentoIdentidad?: number;
  telefono?: number;
  tipoContrato?: string;
}

export interface MensajeResponse {
  mensaje: string;
}

export interface ErrorResponse {
  mensaje: string;
  codigo: number;
  timestamp: string;
}

export interface RecuperarContrasenaRequest {
  correo: string;
}

export interface RestablecerContrasenaRequest {
  token: string;
  nuevaContrasena: string;
}
