package com.caeproject.cae.infraestructure.adapter.in;

import com.caeproject.cae.domain.ports.in.programa.*;
import com.caeproject.cae.domain.ports.model.programa.Programa;
import com.caeproject.cae.infraestructure.dtos.programa.CrearProgramaRequest;
import com.caeproject.cae.infraestructure.dtos.programa.ProgramaResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/programas")
public class ProgramaController {

    private final CrearProgramaInputPort crearProgramaPort;
    private final ListarProgramaInputPort listarProgramaPort;
    private final ObtenerProgramaInputPort obtenerProgramaPort;
    private final EditarProgramaInputPort editarProgramaPort;
    private final EliminarProgramaInputPort eliminarProgramaPort;

    public ProgramaController(CrearProgramaInputPort crearProgramaPort,
                              ListarProgramaInputPort listarProgramaPort,
                              ObtenerProgramaInputPort obtenerProgramaPort,
                              EditarProgramaInputPort editarProgramaPort,
                              EliminarProgramaInputPort eliminarProgramaPort) {
        this.crearProgramaPort = crearProgramaPort;
        this.listarProgramaPort = listarProgramaPort;
        this.obtenerProgramaPort = obtenerProgramaPort;
        this.editarProgramaPort = editarProgramaPort;
        this.eliminarProgramaPort = eliminarProgramaPort;
    }

    @PostMapping
    public ResponseEntity<ProgramaResponse> crearPrograma(@Valid @RequestBody CrearProgramaRequest request) {
        Programa programa = new Programa();
        programa.setNombre(request.getNombre());
        programa.setMunicipio(request.getMunicipio());
        programa.setNivelFormacion(request.getNivelFormacion());
        programa.setJornada(request.getJornada());
        programa.setDuracionpracticas(request.getDuracionpracticas());

        Programa creado = crearProgramaPort.crearPrograma(programa);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(creado));
    }

    @GetMapping
    public ResponseEntity<List<ProgramaResponse>> listarProgramas() {
        List<Programa> programas = listarProgramaPort.listarProgramas();
        return ResponseEntity.ok(programas.stream().map(this::toResponse).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramaResponse> obtenerPrograma(@PathVariable Long id) {
        Programa programa = obtenerProgramaPort.obtenerPrograma(id);
        return ResponseEntity.ok(toResponse(programa));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPrograma(@PathVariable Long id) {
        eliminarProgramaPort.eliminarPrograma(id);
        return ResponseEntity.noContent().build();
    }

    private ProgramaResponse toResponse(Programa programa) {
        ProgramaResponse response = new ProgramaResponse();
        response.setId(programa.getId());
        response.setNombre(programa.getNombre());
        response.setMunicipio(programa.getMunicipio());
        response.setNivelFormacion(programa.getNivelFormacion());
        response.setJornada(programa.getJornada());
        response.setDuracionpracticas(programa.getDuracionpracticas());
        return response;
    }
}
