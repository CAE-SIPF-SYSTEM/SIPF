package com.caeproject.cae.infraestructure.config;

import com.caeproject.cae.domain.ports.exceptions.asignacionexceptions.HorasInsuficientesException;
import com.caeproject.cae.domain.ports.exceptions.asignacionexceptions.InstructorEspecialidadIncompatibleException;
import com.caeproject.cae.domain.ports.exceptions.asignacionexceptions.JornadaIncompatibleException;
import com.caeproject.cae.domain.ports.exceptions.asignacionexceptions.RapYaAsignadoEnTrimestreException;
import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.*;
import com.caeproject.cae.domain.ports.exceptions.competenciaexception.CompetenciaNoEncontradaException;
import com.caeproject.cae.domain.ports.exceptions.competenciaexception.CompetenciaDuplicadaException;
import com.caeproject.cae.domain.ports.exceptions.rapexception.RapNoEncontradoException;
import com.caeproject.cae.domain.ports.exceptions.rapexception.RapDuplicadoException;
import com.caeproject.cae.domain.ports.exceptions.instructorexception.CruceHorarioException;
import com.caeproject.cae.domain.ports.exceptions.instructorexception.DocumentoYaRegistradoException;
import com.caeproject.cae.domain.ports.exceptions.competenciaespecialidadexception.CompetenciaEspecialidadDuplicadaException;
import com.caeproject.cae.domain.ports.exceptions.competenciaespecialidadexception.CompetenciaEspecialidadNoEncontradaException;
import com.caeproject.cae.domain.ports.exceptions.instructorexception.EspecialidadNoValidaException;
import com.caeproject.cae.domain.ports.exceptions.instructorexception.LimiteHorasSuperadasException;
import com.caeproject.cae.domain.ports.exceptions.sessionexceptions.ContrasenaInvalidaException;
import com.caeproject.cae.domain.ports.exceptions.sessionexceptions.SesionCerradaException;
import com.caeproject.cae.domain.ports.exceptions.tokensexception.TokenExpiradoException;
import com.caeproject.cae.domain.ports.exceptions.tokensexception.TokenNoEncontradoException;
import com.caeproject.cae.domain.ports.exceptions.usuarioexceptions.CorreoYaRegistradoException;
import com.caeproject.cae.domain.ports.exceptions.usuarioexceptions.CredencialesIncorrectasException;
import com.caeproject.cae.domain.ports.exceptions.usuarioexceptions.UsuarioInhabilitadoException;
import com.caeproject.cae.domain.ports.exceptions.usuarioexceptions.UsuarioNoEncontradoException;
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
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(ContrasenaInvalidaException.class)
    public ResponseEntity<ErrorResponse> handleContrasenaInvalida(ContrasenaInvalidaException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    @ExceptionHandler(ProgramaNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleProgramaNoEncontrado(ProgramaNoEncontradoException ex){
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ProgramaDuplicadoException.class)
    public ResponseEntity<ErrorResponse> handleProgramaDuplicado(ProgramaDuplicadoException ex){
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(FichaNoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handleFichaNoEncontrada(FichaNoEncontradaException ex){
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(FichaDuplicadaException.class)
    public ResponseEntity<ErrorResponse> handleFichaDuplicada(FichaDuplicadaException ex){
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(FichaInvalidaException.class)
    public ResponseEntity<ErrorResponse> handleFichaInvalida(FichaInvalidaException ex){
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(CompetenciaEspecialidadNoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handleCompetenciaEspecialidadNoEncontradaException(CompetenciaEspecialidadNoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(CompetenciaEspecialidadDuplicadaException.class)
    public ResponseEntity<ErrorResponse> handleCompetenciaEspecialidadDuplicadaException(CompetenciaEspecialidadDuplicadaException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(CompetenciaNoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handleCompetenciaNoEncontrada(CompetenciaNoEncontradaException ex){
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(CompetenciaDuplicadaException.class)
    public ResponseEntity<ErrorResponse> handleCompetenciaDuplicada(CompetenciaDuplicadaException ex){
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(RapNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleRapNoEncontrado(RapNoEncontradoException ex){
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(RapDuplicadoException.class)
    public ResponseEntity<ErrorResponse> handleRapDuplicado(RapDuplicadoException ex){
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(InstructorEspecialidadIncompatibleException.class)
    public ResponseEntity<ErrorResponse> handleInstructorEspecialidadIncompatible(InstructorEspecialidadIncompatibleException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(JornadaIncompatibleException.class)
    public ResponseEntity<ErrorResponse> handleJornadaIncompatible(JornadaIncompatibleException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(RapYaAsignadoEnTrimestreException.class)
    public ResponseEntity<ErrorResponse> handleRapYaAsignadoEnTrimestre(RapYaAsignadoEnTrimestreException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
    @ExceptionHandler(HorasInsuficientesException.class)
    public ResponseEntity<ErrorResponse>handleHorasInsuficientes(HorasInsuficientesException ex){
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}
