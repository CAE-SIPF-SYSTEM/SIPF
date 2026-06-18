package com.caeproject.cae.infraestructure.adapter.in;

import com.caeproject.cae.application.usecases.rap.commands.CrearRapCommand;
import com.caeproject.cae.application.usecases.rap.commands.EditarRapCommand;
import com.caeproject.cae.domain.ports.in.Rap.*;
import com.caeproject.cae.domain.ports.model.rap.Rap;
import com.caeproject.cae.infraestructure.dtos.rap.CrearRapRequest;
import com.caeproject.cae.infraestructure.dtos.rap.EditarRapRequest;
import com.caeproject.cae.infraestructure.dtos.rap.RapResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/raps")
public class RapController {

    private final CrearRapInputPort crearRapInputPort;
    private final EditarRapInputPort editarRapInputPort;
    private final EliminarRapInputPort eliminarRapInputPort;
    private final ListarRapsInputPort listarRapsInputPort;
    private final ObtenerRapInputPort obtenerRapInputPort;

    public RapController(CrearRapInputPort crearRapInputPort,
                         EditarRapInputPort editarRapInputPort,
                         EliminarRapInputPort eliminarRapInputPort,
                         ListarRapsInputPort listarRapsInputPort,
                         ObtenerRapInputPort obtenerRapInputPort) {
        this.crearRapInputPort = crearRapInputPort;
        this.editarRapInputPort = editarRapInputPort;
        this.eliminarRapInputPort = eliminarRapInputPort;
        this.listarRapsInputPort = listarRapsInputPort;
        this.obtenerRapInputPort = obtenerRapInputPort;
    }

    @PostMapping
    public ResponseEntity<RapResponse> crearRap(@Valid @RequestBody CrearRapRequest request) {
        CrearRapCommand command = new CrearRapCommand();
        command.setCompetenciaId(request.getCompetenciaId());
        command.setDescripcion(request.getDescripcion());
        command.setHorasPresenciales(request.getHorasPresenciales());

        Rap creado = crearRapInputPort.createRap(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(creado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RapResponse> editarRap(@PathVariable Long id, @Valid @RequestBody EditarRapRequest request) {
        EditarRapCommand command = new EditarRapCommand();
        command.setCompetenciaId(request.getCompetenciaId());
        command.setDescripcion(request.getDescripcion());
        command.setHorasPresenciales(request.getHorasPresenciales());

        Rap editado = editarRapInputPort.editarRap(command, id);
        return ResponseEntity.ok(toResponse(editado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRap(@PathVariable Long id) {
        eliminarRapInputPort.eliminarRap(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<RapResponse>> listarRaps() {
        List<Rap> raps = listarRapsInputPort.listarRaps();
        return ResponseEntity.ok(raps.stream().map(this::toResponse).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RapResponse> obtenerRap(@PathVariable Long id) {
        Rap rap = obtenerRapInputPort.obtenerRap(id);
        return ResponseEntity.ok(toResponse(rap));
    }

    @GetMapping("/competencia/{competenciaId}")
    public ResponseEntity<List<RapResponse>> obtenerRapsPorCompetencia(@PathVariable Long competenciaId) {
        List<Rap> raps = obtenerRapInputPort.obtenerRapCompetencia(competenciaId);
        return ResponseEntity.ok(raps.stream().map(this::toResponse).collect(Collectors.toList()));
    }

    private RapResponse toResponse(Rap rap) {
        RapResponse response = new RapResponse();
        response.setId(rap.getId());
        response.setCompetenciaId(rap.getCompetenciaId());
        response.setDescripcion(rap.getDescripcion());
        response.setHorasPresenciales(rap.getHorasPresenciales());
        return response;
    }
}
