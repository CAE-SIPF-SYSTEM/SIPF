package com.caeproject.cae.domain.ports.exceptions.instructorexception;

public class DocumentoYaRegistradoException extends RuntimeException {
    public DocumentoYaRegistradoException(Long documentoIdentidad) {
        super("El documento de identidad " + documentoIdentidad + " ya se encuentra registrado en el sistema.");
    }
}
