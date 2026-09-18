package com.caeproject.cae.infraestructure.adapter.in;

import com.caeproject.cae.application.usecases.especialidad.commands.CrearEspecialidadCommand;
import com.caeproject.cae.application.usecases.especialidad.commands.EditarEspecialidadCommand;
import com.caeproject.cae.domain.ports.in.especialidad.*;
import com.caeproject.cae.domain.ports.model.Especialidad;
import com.caeproject.cae.infraestructure.dtos.especialidad.EspecialidadResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/especialidades")
public class EspecialidadController {

    private final CrearEspecialidadInputPort crearEspecialidadPort;
    private final EditarEspecialidadInputPort editarEspecialidadPort;
    private final EliminarEspecialidadInputPort eliminarEspecialidadPort;
    private final ListarEspecialidadInputPort listarEspecialidadPort;
    private final ObtenerEspecialidadInputPort obtenerEspecialidadPort;

    public EspecialidadController(
            CrearEspecialidadInputPort crearEspecialidadPort,
            EditarEspecialidadInputPort editarEspecialidadPort,
            EliminarEspecialidadInputPort eliminarEspecialidadPort,
            ListarEspecialidadInputPort listarEspecialidadPort,
            ObtenerEspecialidadInputPort obtenerEspecialidadPort) {
        this.crearEspecialidadPort = crearEspecialidadPort;
        this.editarEspecialidadPort = editarEspecialidadPort;
        this.eliminarEspecialidadPort = eliminarEspecialidadPort;
        this.listarEspecialidadPort = listarEspecialidadPort;
        this.obtenerEspecialidadPort = obtenerEspecialidadPort;
    }

    @PostMapping
    public ResponseEntity<EspecialidadResponseDTO> crearEspecialidad(@RequestBody CrearEspecialidadCommand command) {
        Especialidad creada = crearEspecialidadPort.crearEspecialidad(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(creada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EspecialidadResponseDTO> editarEspecialidad(
            @PathVariable Long id,
            @RequestBody EditarEspecialidadCommand command) {
        Especialidad editada = editarEspecialidadPort.editarEspecialidad(command, id);
        return ResponseEntity.ok(toResponse(editada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEspecialidad(@PathVariable Long id) {
        eliminarEspecialidadPort.eliminarEspecialidad(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<EspecialidadResponseDTO>> listarEspecialidades() {
        List<EspecialidadResponseDTO> especialidades = listarEspecialidadPort.listarEspecialidades()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(especialidades);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadResponseDTO> obtenerEspecialidad(@PathVariable Long id) {
        Especialidad especialidad = obtenerEspecialidadPort.obtenerEspecialidad(id);
        return ResponseEntity.ok(toResponse(especialidad));
    }

    private EspecialidadResponseDTO toResponse(Especialidad especialidad) {
        EspecialidadResponseDTO dto = new EspecialidadResponseDTO();
        dto.setId(especialidad.getId());
        dto.setNombreEspecialidad(especialidad.getNombreEspecialidad());
        return dto;
    }
}
