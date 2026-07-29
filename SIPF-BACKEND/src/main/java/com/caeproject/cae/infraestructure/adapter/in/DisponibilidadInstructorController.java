package com.caeproject.cae.infraestructure.adapter.in;

import com.caeproject.cae.application.usecases.disponibilidadInstructor.commands.CrearDisponibilidadCommand;
import com.caeproject.cae.application.usecases.disponibilidadInstructor.commands.EditarDisponibilidadCommand;
import com.caeproject.cae.domain.ports.in.DisponibilidadInstructor.*;
import com.caeproject.cae.domain.ports.model.DisponibilidadInstructor;
import com.caeproject.cae.domain.ports.model.enums.DiasDisponibles;
import com.caeproject.cae.infraestructure.dtos.disponibilidadinstrucor.CrearDisponibilidadInstructorRequest;
import com.caeproject.cae.infraestructure.dtos.disponibilidadinstrucor.DisponibilidadInstructorResponse;
import com.caeproject.cae.infraestructure.dtos.disponibilidadinstrucor.EditarDisponibilidadInstructorRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/disponibilidadinstructor")
public class DisponibilidadInstructorController {

    private final CrearDisponibilidadInstructorInputPort crearDisponibilidadInstructorInputPort;
    private final EditarDisponibilidadInstructorInputPort editarDisponibilidadInstructorInputPort;
    private final EliminarDisponibilidadInputPort eliminarDisponibilidadInputPort;
    private final ListarDisponibilidadInstructorInputPort listarDisponibilidadInstructorInputPort;
    private final ObtenerDisponibilidadInstructorInputPort obtenerDisponibilidadInstructorInputPort;

    public DisponibilidadInstructorController(CrearDisponibilidadInstructorInputPort crearDisponibilidadInstructorInputPort, EditarDisponibilidadInstructorInputPort editarDisponibilidadInstructorInputPort, EliminarDisponibilidadInputPort eliminarDisponibilidadInputPort, ListarDisponibilidadInstructorInputPort listarDisponibilidadInstructorInputPort, ObtenerDisponibilidadInstructorInputPort obtenerDisponibilidadInstructorInputPort) {
        this.crearDisponibilidadInstructorInputPort = crearDisponibilidadInstructorInputPort;
        this.editarDisponibilidadInstructorInputPort = editarDisponibilidadInstructorInputPort;
        this.eliminarDisponibilidadInputPort = eliminarDisponibilidadInputPort;
        this.listarDisponibilidadInstructorInputPort = listarDisponibilidadInstructorInputPort;
        this.obtenerDisponibilidadInstructorInputPort = obtenerDisponibilidadInstructorInputPort;
    }

    @PostMapping
    public ResponseEntity<DisponibilidadInstructorResponse> crearDisponibilidad (@Valid @RequestBody CrearDisponibilidadInstructorRequest request){
        CrearDisponibilidadCommand command = new CrearDisponibilidadCommand();
        command.setUsuarioId(request.getUsuarioId());
        command.setDiasDisponibles(request.getDiasDisponibles());
        command.setJornada(request.getJornada());
        command.setMunicipios(request.getMunicipios());
        command.setJornada(request.getJornada());

        DisponibilidadInstructor creado = crearDisponibilidadInstructorInputPort.crearDisponibilidad(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(creado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisponibilidadInstructorResponse> editarDisponibilidad(
            @PathVariable("id") Long usuarioId,
            @Valid @RequestBody EditarDisponibilidadInstructorRequest request) {

        EditarDisponibilidadCommand command = new EditarDisponibilidadCommand();

        command.setDiasDisponibles(request.getDiasDisponibles());
        command.setJornada(request.getJornada());
        command.setMunicipios(request.getMunicipios());

        DisponibilidadInstructor editado = editarDisponibilidadInstructorInputPort.editarDisponibilidad(command, usuarioId);
        return ResponseEntity.ok(toResponse(editado));
    }

    @GetMapping
    public ResponseEntity<List<DisponibilidadInstructorResponse>> listarDisponibilidades(){
        List<DisponibilidadInstructor> disponibilidadInstructor = listarDisponibilidadInstructorInputPort.listarDisponibilidad();
        return ResponseEntity.ok(disponibilidadInstructor.stream().map(this::toResponse).collect(Collectors.toList()));
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<DisponibilidadInstructorResponse> obtenerDisponibilidad(@PathVariable Long usuarioId){
        DisponibilidadInstructor disponibilidad = obtenerDisponibilidadInstructorInputPort.obtenerDisponiblidad(usuarioId);
        return ResponseEntity.ok(toResponse(disponibilidad));
    }

    @GetMapping("/dia/{diaDisponible}")
    public ResponseEntity<List<DisponibilidadInstructorResponse>> obtenerPorDiasDisponibles(@PathVariable DiasDisponibles diasDisponibles){
        List<DisponibilidadInstructor> disponibilidades = obtenerDisponibilidadInstructorInputPort.obtenerPorDiasDisponibles(diasDisponibles);
        return ResponseEntity.ok(disponibilidades.stream().map(this::toResponse).collect(Collectors.toList()));
    }

    private DisponibilidadInstructorResponse toResponse (DisponibilidadInstructor disponibilidadInstructor){
        DisponibilidadInstructorResponse response = new DisponibilidadInstructorResponse();
        response.setUsuarioId(disponibilidadInstructor.getUsuarioId());
        response.setDiasDisponibles(disponibilidadInstructor.getDiasDisponibles());
        response.setHorasMaximas(disponibilidadInstructor.getHorasMaximas());
        response.setHorasAsignadas(disponibilidadInstructor.getHorasAsignadas());
        response.setHorasDisponibles(disponibilidadInstructor.getHorasDisponibles());
        response.setJornada(disponibilidadInstructor.getJornada());
        response.setMunicipios(disponibilidadInstructor.getMunicipios());
        return response;
    }
}
