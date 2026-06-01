package com.caeproject.cae.infraestructure.config;

import com.caeproject.cae.domain.ports.exceptions.CorreoYaRegistradoException;
import com.caeproject.cae.domain.ports.exceptions.CredencialesIncorrectasException;
import com.caeproject.cae.domain.ports.exceptions.CruceHorarioException;
import com.caeproject.cae.domain.ports.exceptions.DocumentoYaRegistradoException;
import com.caeproject.cae.domain.ports.exceptions.EspecialidadNoValidaException;
import com.caeproject.cae.domain.ports.exceptions.FichaFueraDeVigenciaException;
import com.caeproject.cae.domain.ports.exceptions.LimiteHorasSuperadasException;
import com.caeproject.cae.domain.ports.exceptions.UsuarioInhabilitadoException;
import com.caeproject.cae.domain.ports.exceptions.UsuarioNoEncontradoException;
import com.caeproject.cae.infraestructure.dtos.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleUsuarioNoEncontrado(UsuarioNoEncontradoException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(CorreoYaRegistradoException.class)
    public ResponseEntity<ErrorResponse> handleCorreoYaRegistrado(CorreoYaRegistradoException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(DocumentoYaRegistradoException.class)
    public ResponseEntity<ErrorResponse> handleDocumentoYaRegistrado(DocumentoYaRegistradoException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(UsuarioInhabilitadoException.class)
    public ResponseEntity<ErrorResponse> handleUsuarioInhabilitado(UsuarioInhabilitadoException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN.value());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(CredencialesIncorrectasException.class)
    public ResponseEntity<ErrorResponse> handleCredencialesIncorrectas(CredencialesIncorrectasException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(CruceHorarioException.class)
    public ResponseEntity<ErrorResponse> handleCruceHorario(CruceHorarioException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(LimiteHorasSuperadasException.class)
    public ResponseEntity<ErrorResponse> handleLimiteHoras(LimiteHorasSuperadasException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(EspecialidadNoValidaException.class)
    public ResponseEntity<ErrorResponse> handleEspecialidadNoValida(EspecialidadNoValidaException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(FichaFueraDeVigenciaException.class)
    public ResponseEntity<ErrorResponse> handleFichaFueraDeVigencia(FichaFueraDeVigenciaException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
