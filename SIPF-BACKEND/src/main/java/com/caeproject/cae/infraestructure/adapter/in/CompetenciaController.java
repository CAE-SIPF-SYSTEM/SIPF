package com.caeproject.cae.infraestructure.adapter.in;

import com.caeproject.cae.application.usecases.competencia.commands.CrearCompetenciaCommand;
import com.caeproject.cae.application.usecases.competencia.commands.EditarCompetenciaCommand;
import com.caeproject.cae.domain.ports.in.competencia.*;
import com.caeproject.cae.domain.ports.model.Competencia;
import com.caeproject.cae.infraestructure.dtos.competencia.CompetenciaResponse;
import com.caeproject.cae.infraestructure.dtos.competencia.CrearCompetenciaRequest;
import com.caeproject.cae.infraestructure.dtos.competencia.EditarCompetenciaRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/competencias")
public class CompetenciaController {

    private final CrearCompetenciaInputPort crearCompetenciaInputPort;
    private final EditarCompetenciaInputPort editarCompetenciaInputPort;
    private final EliminarCompetenciaInputPort eliminarCompetenciaInputPort;
    private final ListarCompetenciasInputPort listarCompetenciasInputPort;
    private final ObtenerCompetenciaInputPort obtenerCompetenciaInputPort;

    public CompetenciaController(CrearCompetenciaInputPort crearCompetenciaInputPort,
                                 EditarCompetenciaInputPort editarCompetenciaInputPort,
                                 EliminarCompetenciaInputPort eliminarCompetenciaInputPort,
                                 ListarCompetenciasInputPort listarCompetenciasInputPort,
                                 ObtenerCompetenciaInputPort obtenerCompetenciaInputPort) {
        this.crearCompetenciaInputPort = crearCompetenciaInputPort;
        this.editarCompetenciaInputPort = editarCompetenciaInputPort;
        this.eliminarCompetenciaInputPort = eliminarCompetenciaInputPort;
        this.listarCompetenciasInputPort = listarCompetenciasInputPort;
        this.obtenerCompetenciaInputPort = obtenerCompetenciaInputPort;
    }

    @PostMapping
    public ResponseEntity<CompetenciaResponse> crearCompetencia(@Valid @RequestBody CrearCompetenciaRequest request) {
        CrearCompetenciaCommand command = new CrearCompetenciaCommand();
        command.setNombre(request.getNombre());
        command.setTipoCompetencia(request.getTipoCompetencia());

        Competencia creada = crearCompetenciaInputPort.crearCompetencia(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(creada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompetenciaResponse> editarCompetencia(@PathVariable Long id, @Valid @RequestBody EditarCompetenciaRequest request) {
        EditarCompetenciaCommand command = new EditarCompetenciaCommand();
        command.setNombre(request.getNombre());
        command.setTipoCompetencia(request.getTipoCompetencia());

        Competencia editada = editarCompetenciaInputPort.editarCompetencia(command, id);
        return ResponseEntity.ok(toResponse(editada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCompetencia(@PathVariable Long id) {
        eliminarCompetenciaInputPort.eliminarCompetencia(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CompetenciaResponse>> listarCompetencias() {
        List<Competencia> competencias = listarCompetenciasInputPort.listarCompetencia();
        return ResponseEntity.ok(competencias.stream().map(this::toResponse).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompetenciaResponse> obtenerCompetencia(@PathVariable Long id) {
        Competencia competencia = obtenerCompetenciaInputPort.obtenerCompetencia(id);
        return ResponseEntity.ok(toResponse(competencia));
    }

    private CompetenciaResponse toResponse(Competencia competencia) {
        CompetenciaResponse response = new CompetenciaResponse();
        response.setId(competencia.getId());
        response.setNombre(competencia.getNombre());
        response.setTipoCompetencia(competencia.getTipoCompetencia());
        return response;
    }
}
