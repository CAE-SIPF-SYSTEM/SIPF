package com.caeproject.cae.infraestructure.config;

import com.caeproject.cae.domain.ports.exceptions.fichas_ProgramasException.FichaFueraDeVigenciaException;
import com.caeproject.cae.domain.ports.exceptions.fichas_ProgramasException.FichaNoEncontradaException;
import com.caeproject.cae.domain.ports.exceptions.fichas_ProgramasException.ProgramaNoEncontradoException;
import com.caeproject.cae.domain.ports.exceptions.instructorException.CruceHorarioException;
import com.caeproject.cae.domain.ports.exceptions.instructorException.DocumentoYaRegistradoException;
import com.caeproject.cae.domain.ports.exceptions.instructorException.EspecialidadNoValidaException;
import com.caeproject.cae.domain.ports.exceptions.instructorException.LimiteHorasSuperadasException;
import com.caeproject.cae.domain.ports.exceptions.sessionExceptions.ContrasenaInvalidaException;
import com.caeproject.cae.domain.ports.exceptions.sessionExceptions.SesionCerradaException;
import com.caeproject.cae.domain.ports.exceptions.tokensException.TokenExpiradoException;
import com.caeproject.cae.domain.ports.exceptions.tokensException.TokenNoEncontradoException;
import com.caeproject.cae.domain.ports.exceptions.usuarioExceptions.CorreoYaRegistradoException;
import com.caeproject.cae.domain.ports.exceptions.usuarioExceptions.CredencialesIncorrectasException;
import com.caeproject.cae.domain.ports.exceptions.usuarioExceptions.UsuarioInhabilitadoException;
import com.caeproject.cae.domain.ports.exceptions.usuarioExceptions.UsuarioNoEncontradoException;
import com.caeproject.cae.infraestructure.dtos.email.ErrorResponse;
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

    @ExceptionHandler(TokenExpiradoException.class)
    public ResponseEntity<ErrorResponse> handleTokenExpirado(TokenExpiradoException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(TokenNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleTokenNoEncontrado(TokenNoEncontradoException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(SesionCerradaException.class)
    public ResponseEntity<ErrorResponse> handleSesionCerrada(SesionCerradaException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ContrasenaInvalidaException.class)
    public ResponseEntity<ErrorResponse> handleContrasenaInvalida(ContrasenaInvalidaException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    @ExceptionHandler (ProgramaNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleProgramaNoEncontrado(ProgramaNoEncontradoException ex){
        ErrorResponse error = new ErrorResponse(ex.getMessage(),HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler (FichaNoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handleFichaNoEncontrada(ProgramaNoEncontradoException ex){
        ErrorResponse error = new ErrorResponse(ex.getMessage(),HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler (ProgramaNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleProgrmaDuplicado(ProgramaNoEncontradoException ex){
        ErrorResponse error = new ErrorResponse(ex.getMessage(),HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

}
