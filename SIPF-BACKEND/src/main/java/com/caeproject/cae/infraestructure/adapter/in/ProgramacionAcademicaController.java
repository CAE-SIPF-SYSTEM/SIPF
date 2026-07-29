package com.caeproject.cae.infraestructure.adapter.in;

import com.caeproject.cae.application.usecases.ProgramacionAcademica.commands.CrearProgramacionCommand;
import com.caeproject.cae.domain.ports.in.ProgramacionAcademica.*;
import com.caeproject.cae.domain.ports.in.asignarinstructor.ObtenerResumenProgramaInputPort;
import com.caeproject.cae.domain.ports.in.asignarinstructor.SugerirInstructorInputPort;
import com.caeproject.cae.domain.ports.model.DisponibilidadInstructor;
import com.caeproject.cae.domain.ports.model.ProgramacionAcademica;
import com.caeproject.cae.infraestructure.dtos.asignacioninstructor.InstructorSugeridoResponse;
import com.caeproject.cae.infraestructure.dtos.asignacioninstructor.ResumenProgramaCompetenciasResponse;
import com.caeproject.cae.infraestructure.dtos.programacionacademica.CrearProgramacionRequest;
import com.caeproject.cae.infraestructure.dtos.programacionacademica.ProgramacionAcademicaResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/programacionacademica")
public class ProgramacionAcademicaController {
    private final CrearProgramacionInputPort crearProgramacionInputPort;
    private final ListarProgramacionInputPort listarProgramacionInputPort;
    private final EliminarProgramacionInputPort eliminarProgramacionInputPort;
    private final ObtenerProgramacionInputPort obtenerProgramacionInputPort;
    private final SugerirInstructorInputPort sugerirInstructorInputPort;
    private final ObtenerResumenProgramaInputPort obtenerResumenProgramaInputPort;
    private final AutoProgramacionInputPort autoProgramacionInputPort;

    public ProgramacionAcademicaController(
            ObtenerProgramacionInputPort obtenerProgramacionInputPort,
            CrearProgramacionInputPort crearProgramacionInputPort,
            ListarProgramacionInputPort listarProgramacionInputPort,
            EliminarProgramacionInputPort eliminarProgramacionInputPort,
            SugerirInstructorInputPort sugerirInstructorInputPort,
            ObtenerResumenProgramaInputPort obtenerResumenProgramaInputPort,
            AutoProgramacionInputPort autoProgramacionInputPort) {
        this.crearProgramacionInputPort = crearProgramacionInputPort;
        this.listarProgramacionInputPort = listarProgramacionInputPort;
        this.eliminarProgramacionInputPort = eliminarProgramacionInputPort;
        this.obtenerProgramacionInputPort = obtenerProgramacionInputPort;
        this.sugerirInstructorInputPort = sugerirInstructorInputPort;
        this.obtenerResumenProgramaInputPort = obtenerResumenProgramaInputPort;
        this.autoProgramacionInputPort = autoProgramacionInputPort;
    }

    @PostMapping
    public ResponseEntity<ProgramacionAcademicaResponse> crearProgramacion (@Valid @RequestBody CrearProgramacionRequest request){
        CrearProgramacionCommand command = new CrearProgramacionCommand();
        command.setRapId(request.getRapId());
        command.setTrimestreId(request.getTrimestreId());
        command.setUsuarioId(request.getUsuarioId());
        command.setProgramaId(request.getProgramaId());

        ProgramacionAcademica creado = crearProgramacionInputPort.programacionAcademica(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(creado));
    }

    @PostMapping("/autoprogramar")
    public ResponseEntity<List<ProgramacionAcademicaResponse>> autoprogramar(
            @RequestParam Long programaId,
            @RequestParam Long trimestreId
    ) {
        List<ProgramacionAcademica> resultado = autoProgramacionInputPort.autoprogramar(programaId, trimestreId);
        List<ProgramacionAcademicaResponse> response = resultado.stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/sugerencias")
    public ResponseEntity<List<InstructorSugeridoResponse>> obtenerInstructoresSugeridos(
            @RequestParam Long competenciaId,
            @RequestParam Long programaId,
            @RequestParam Long horasRequeridas
    ){
        List<DisponibilidadInstructor> instructoresSugeridos = sugerirInstructorInputPort
                .sugerirInstructores(competenciaId, programaId, horasRequeridas);

        List<InstructorSugeridoResponse> response = instructoresSugeridos.stream()
                .map(this::toInstructorSugeridoResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/resumen-programa/{programaId}")
    public ResponseEntity<ResumenProgramaCompetenciasResponse> obtenerResumenPrograma(
            @PathVariable Long programaId
    ){
        ResumenProgramaCompetenciasResponse response = obtenerResumenProgramaInputPort.obtenerResumenPrograma(programaId);
        return ResponseEntity.ok(response);
    }

    private ProgramacionAcademicaResponse toResponse(ProgramacionAcademica programacionAcademica){
        ProgramacionAcademicaResponse response = new ProgramacionAcademicaResponse();
        response.setRapId(programacionAcademica.getRapId());
        response.setTrimestreId(programacionAcademica.getTrimestreId());
        response.setUsuarioId(programacionAcademica.getUsuarioId());
        response.setProgramaId(programacionAcademica.getProgramaId());
        return response;
    }

    private InstructorSugeridoResponse toInstructorSugeridoResponse(DisponibilidadInstructor disponibilidad) {
        InstructorSugeridoResponse response = new InstructorSugeridoResponse();
        response.setUsuarioId(disponibilidad.getUsuarioId());
        response.setDiasDisponibles(disponibilidad.getDiasDisponibles());
        response.setHorasMaximas(disponibilidad.getHorasMaximas());
        response.setHorasAsignadas(disponibilidad.getHorasAsignadas());
        response.setHorasDisponibles(disponibilidad.getHorasDisponibles());
        response.setJornada(disponibilidad.getJornada());
        return response;
    }
}
