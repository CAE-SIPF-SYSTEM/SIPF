package com.caeproject.cae.domain.ports.exceptions;

public class CorreoYaRegistradoException extends RuntimeException {
    public CorreoYaRegistradoException(String correo) {
        super("El correo '" + correo + "' ya se encuentra registrado en el sistema.");
    }
}
