package com.caeproject.cae.infraestructure.adapter.in;

import com.caeproject.cae.application.usecases.ProgramacionAcademica.CrearProgramacionAcademicaUseCase;
import com.caeproject.cae.application.usecases.ProgramacionAcademica.commands.CrearProgramacionCommand;
import com.caeproject.cae.domain.ports.in.ProgramacionAcademica.*;
import com.caeproject.cae.domain.ports.model.ProgramacionAcademica;
import com.caeproject.cae.infraestructure.dtos.programacionacademica.CrearProgramacionRequest;
import com.caeproject.cae.infraestructure.dtos.programacionacademica.ProgramacionAcademicaResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/programacionacademica")
public class ProgramacionAcademicaController {
    private final CrearProgramacionInputPort crearProgramacionInputPort;
    private final ListarProgramacionInputPort listarProgramacionInputPort;
    private final EliminarProgramacionInputPort eliminarProgramacionInputPort;

    private final ObtenerProgramacionInputPort obtenerProgramacionInputPort;


    public ProgramacionAcademicaController(ObtenerProgramacionInputPort obtenerProgramacionInputPort,CrearProgramacionInputPort crearProgramacionInputPort, ListarProgramacionInputPort listarProgramacionInputPort, EliminarProgramacionInputPort eliminarProgramacionInputPort) {
        this.crearProgramacionInputPort = crearProgramacionInputPort;
        this.listarProgramacionInputPort = listarProgramacionInputPort;
        this.eliminarProgramacionInputPort = eliminarProgramacionInputPort;

        this.obtenerProgramacionInputPort = obtenerProgramacionInputPort;
    }

    @PostMapping
    public ResponseEntity<ProgramacionAcademicaResponse> crearProgramacion (@Valid @RequestBody CrearProgramacionRequest request){
        System.out.println(">>>> ¡Sí logró llegar al Controlador!");
        CrearProgramacionCommand command = new CrearProgramacionCommand();
        command.setRapId(request.getRapId());
        command.setTrimestreId(request.getTrimestreId());
        command.setUsuarioId(request.getUsuarioId());
        command.setProgramaId(request.getProgramaId());

        ProgramacionAcademica creado = crearProgramacionInputPort.programacionAcademica(command);
        return  ResponseEntity.status(HttpStatus.CREATED).body(toResponse(creado));
    }

    private ProgramacionAcademicaResponse toResponse(ProgramacionAcademica programacionAcademica){
        ProgramacionAcademicaResponse response = new ProgramacionAcademicaResponse();
        response.setRapId(programacionAcademica.getRapId());
        response.setTrimestreId(programacionAcademica.getTrimestreId());
        response.setUsuarioId(programacionAcademica.getUsuarioId());
        response.setProgramaId(programacionAcademica.getProgramaId());
        return response;
    }
}
