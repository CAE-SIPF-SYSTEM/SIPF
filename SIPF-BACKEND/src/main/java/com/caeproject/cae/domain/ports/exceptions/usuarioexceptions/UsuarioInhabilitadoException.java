package com.caeproject.cae.domain.ports.exceptions.usuarioexceptions;

public class UsuarioInhabilitadoException extends RuntimeException {
    public UsuarioInhabilitadoException(Long usuarioId) {
        super("El usuario con ID " + usuarioId + " está deshabilitado dentro del sistema.");
    }
}
