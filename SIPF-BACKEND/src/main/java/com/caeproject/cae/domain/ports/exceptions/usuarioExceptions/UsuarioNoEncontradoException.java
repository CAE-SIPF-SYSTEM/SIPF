package com.caeproject.cae.domain.ports.exceptions.usuarioExceptions;

public class UsuarioNoEncontradoException extends RuntimeException {
    public UsuarioNoEncontradoException(Long usuarioId) {
        super("El usuario con ID " + usuarioId + " no fue encontrado en el sistema.");
    }
}
