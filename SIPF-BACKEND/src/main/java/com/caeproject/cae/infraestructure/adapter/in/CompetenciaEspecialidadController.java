package com.caeproject.cae.infraestructure.adapter.in;

import com.caeproject.cae.application.usecases.CompetenciaEspecialidad.commands.AsignarEspecialidadCompetenciaCommand;
import com.caeproject.cae.application.usecases.CompetenciaEspecialidad.commands.EditarEspecialidadCompetenciaCommand;
import com.caeproject.cae.domain.ports.in.CompetenciaEspecialidad.AsignarEspecialidadCompetenciaInputPort;
import com.caeproject.cae.domain.ports.in.CompetenciaEspecialidad.DesasignarEspecialidadCompetenciaInputPort;
import com.caeproject.cae.domain.ports.in.CompetenciaEspecialidad.EditarEspecialidadCompetenciaInputPort;
import com.caeproject.cae.domain.ports.in.CompetenciaEspecialidad.ListarEspecialidadesCompetenciaInputPort;
import com.caeproject.cae.domain.ports.model.CompetenciaEspecialidad;
import com.caeproject.cae.infraestructure.dtos.competenciaespecialidad.CompetenciaEspecialidadRequestDTO;
import com.caeproject.cae.infraestructure.dtos.competenciaespecialidad.CompetenciaEspecialidadResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/competencia-especialidad")
@CrossOrigin(origins = "*")
public class CompetenciaEspecialidadController {

    private final AsignarEspecialidadCompetenciaInputPort asignarInputPort;
    private final EditarEspecialidadCompetenciaInputPort editarInputPort;
    private final DesasignarEspecialidadCompetenciaInputPort desasignarInputPort;
    private final ListarEspecialidadesCompetenciaInputPort listarInputPort;

    public CompetenciaEspecialidadController(
            AsignarEspecialidadCompetenciaInputPort asignarInputPort,
            EditarEspecialidadCompetenciaInputPort editarInputPort,
            DesasignarEspecialidadCompetenciaInputPort desasignarInputPort,
            ListarEspecialidadesCompetenciaInputPort listarInputPort) {
        this.asignarInputPort = asignarInputPort;
        this.editarInputPort = editarInputPort;
        this.desasignarInputPort = desasignarInputPort;
        this.listarInputPort = listarInputPort;
    }

    @GetMapping
    public ResponseEntity<List<CompetenciaEspecialidadResponseDTO>> listar() {
        List<CompetenciaEspecialidad> lista = listarInputPort.listarEspecialidadesCompetencias();
        List<CompetenciaEspecialidadResponseDTO> dtos = lista.stream().map(this::toResponseDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<CompetenciaEspecialidadResponseDTO> asignar(@RequestBody CompetenciaEspecialidadRequestDTO dto) {
        AsignarEspecialidadCompetenciaCommand command = new AsignarEspecialidadCompetenciaCommand();
        command.setCompetenciaId(dto.getCompetenciaId());
        command.setEspecialidadId(dto.getEspecialidadId());

        CompetenciaEspecialidad creado = asignarInputPort.asignarEspecialidadCompetencia(command);
        return ResponseEntity.ok(toResponseDTO(creado));
    }

    @PutMapping("/{competenciaId}")
    public ResponseEntity<CompetenciaEspecialidadResponseDTO> editar(@PathVariable Long competenciaId, @RequestBody CompetenciaEspecialidadRequestDTO dto) {
        EditarEspecialidadCompetenciaCommand command = new EditarEspecialidadCompetenciaCommand();
        command.setEspecialidadId(dto.getEspecialidadId());

        CompetenciaEspecialidad actualizado = editarInputPort.editarEspecialidadCompetencia(command, competenciaId);
        return ResponseEntity.ok(toResponseDTO(actualizado));
    }

    @DeleteMapping("/{competenciaId}")
    public ResponseEntity<Void> desasignar(@PathVariable Long competenciaId) {
        desasignarInputPort.desasignarEspecialidadCompetencia(competenciaId);
        return ResponseEntity.ok().build();
    }

    private CompetenciaEspecialidadResponseDTO toResponseDTO(CompetenciaEspecialidad model) {
        CompetenciaEspecialidadResponseDTO dto = new CompetenciaEspecialidadResponseDTO();
        dto.setCompetenciaId(model.getCompetenciaId());
        dto.setEspecialidadId(model.getEspecialidadId());
        return dto;
    }
}
