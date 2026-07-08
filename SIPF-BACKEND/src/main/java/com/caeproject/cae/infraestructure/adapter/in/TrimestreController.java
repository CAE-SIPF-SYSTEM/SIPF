package com.caeproject.cae.infraestructure.adapter.in;

import com.caeproject.cae.application.usecases.trimestre.commands.CrearTrimestreCommand;
import com.caeproject.cae.application.usecases.trimestre.commands.EditarTrimestreCommand;
import com.caeproject.cae.domain.ports.in.trimestre.*;
import com.caeproject.cae.domain.ports.model.Trimestre;
import com.caeproject.cae.infraestructure.dtos.trimestre.TrimestreRequestDTO;
import com.caeproject.cae.infraestructure.dtos.trimestre.TrimestreResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trimestres")
public class TrimestreController {

    private final CrearTrimestreInputPort crearTrimestreInputPort;
    private final EditarTrimestreInputPort editarTrimestreInputPort;
    private final EliminarTrimestreInputPort eliminarTrimestreInputPort;
    private final ListarTrimestreInputPort listarTrimestreInputPort;
    private final ObtenerTrimestreInputPort obtenerTrimestreInputPort;

    public TrimestreController(
            CrearTrimestreInputPort crearTrimestreInputPort,
            EditarTrimestreInputPort editarTrimestreInputPort,
            EliminarTrimestreInputPort eliminarTrimestreInputPort,
            ListarTrimestreInputPort listarTrimestreInputPort,
            ObtenerTrimestreInputPort obtenerTrimestreInputPort
    ) {
        this.crearTrimestreInputPort = crearTrimestreInputPort;
        this.editarTrimestreInputPort = editarTrimestreInputPort;
        this.eliminarTrimestreInputPort = eliminarTrimestreInputPort;
        this.listarTrimestreInputPort = listarTrimestreInputPort;
        this.obtenerTrimestreInputPort = obtenerTrimestreInputPort;
    }

    @PostMapping
    public ResponseEntity<TrimestreResponseDTO> crearTrimestre(@RequestBody TrimestreRequestDTO requestDTO) {
        CrearTrimestreCommand command = new CrearTrimestreCommand();
        command.setFichaId(requestDTO.getFichaId());
        command.setAnio(requestDTO.getAnio());
        command.setNumeroTrimestre(requestDTO.getNumeroTrimestre());
        command.setFechaInicio(requestDTO.getFechaInicio());
        command.setFechaFin(requestDTO.getFechaFin());
        Trimestre trimestre = crearTrimestreInputPort.crearTrimestre(command);
        return new ResponseEntity<>(toResponseDTO(trimestre), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrimestreResponseDTO> editarTrimestre(@PathVariable Long id, @RequestBody TrimestreRequestDTO requestDTO) {
        EditarTrimestreCommand command = new EditarTrimestreCommand();
        command.setNumeroTrimestre(requestDTO.getNumeroTrimestre());
        command.setFechaInicio(requestDTO.getFechaInicio());
        command.setFechaFin(requestDTO.getFechaFin());
        Trimestre trimestre = editarTrimestreInputPort.editarTrimestre(command, id);
        return ResponseEntity.ok(toResponseDTO(trimestre));
    }

    @GetMapping
    public ResponseEntity<List<TrimestreResponseDTO>> listarTrimestres() {
        List<TrimestreResponseDTO> trimestres = listarTrimestreInputPort.listarTrimestres().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(trimestres);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrimestreResponseDTO> obtenerTrimestrePorId(@PathVariable Long id) {
        Trimestre trimestre = obtenerTrimestreInputPort.obtenerTrimestre(id);
        return ResponseEntity.ok(toResponseDTO(trimestre));
    }

    @GetMapping("/ficha/{fichaId}")
    public ResponseEntity<List<TrimestreResponseDTO>> obtenerTrimestresPorFicha(@PathVariable Long fichaId) {
        List<TrimestreResponseDTO> trimestres = obtenerTrimestreInputPort.obtenerTrimestreFicha(fichaId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(trimestres);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTrimestre(@PathVariable Long id) {
        eliminarTrimestreInputPort.eliminarTrimestre(id);
        return ResponseEntity.noContent().build();
    }

    private TrimestreResponseDTO toResponseDTO(Trimestre trimestre) {
        TrimestreResponseDTO dto = new TrimestreResponseDTO();
        dto.setId(trimestre.getId());
        dto.setFichaId(trimestre.getFichaId());
        dto.setAnio(trimestre.getAnio());
        dto.setNumeroTrimestre(trimestre.getNumeroTrimestre());
        dto.setFechaInicio(trimestre.getFechaInicio());
        dto.setFechaFin(trimestre.getFechaFin());
        return dto;
    }
}
