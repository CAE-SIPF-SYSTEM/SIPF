package com.caeproject.cae.domain.ports.exceptions.asignacionexceptions;

public class HorasInsuficientesException extends RuntimeException {
    public HorasInsuficientesException(String message) {
        super(message);
    }
}
