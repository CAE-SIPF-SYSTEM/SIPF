package com.caeproject.cae.infraestructure.adapter.in;

import com.caeproject.cae.application.usecases.especialidadinstructor.commands.AsignarEspecialidadInstructorCommand;
import com.caeproject.cae.application.usecases.especialidadinstructor.commands.EditarEspecialidadInstructorCommand;
import com.caeproject.cae.domain.ports.in.instructorespecialidad.AsignarEspecialidadInstructorInputPort;
import com.caeproject.cae.domain.ports.in.instructorespecialidad.DesasignarEspecialidadInstructorInputPort;
import com.caeproject.cae.domain.ports.in.instructorespecialidad.EditarEspecialidadInstructorInputPort;
import com.caeproject.cae.domain.ports.in.instructorespecialidad.ListarInstructoresEspecialidadesInputPort;
import com.caeproject.cae.domain.ports.model.InstructorEspecialidad;
import com.caeproject.cae.infraestructure.dtos.instructorespecialidad.InstructorEspecialidadRequestDTO;
import com.caeproject.cae.infraestructure.dtos.instructorespecialidad.InstructorEspecialidadResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/instructor-especialidad")
@CrossOrigin(origins = "*")
public class InstructorEspecialidadController {

    private final AsignarEspecialidadInstructorInputPort asignarInputPort;
    private final EditarEspecialidadInstructorInputPort editarInputPort;
    private final DesasignarEspecialidadInstructorInputPort desasignarInputPort;
    private final ListarInstructoresEspecialidadesInputPort listarInputPort;

    public InstructorEspecialidadController(
            AsignarEspecialidadInstructorInputPort asignarInputPort,
            EditarEspecialidadInstructorInputPort editarInputPort,
            DesasignarEspecialidadInstructorInputPort desasignarInputPort,
            ListarInstructoresEspecialidadesInputPort listarInputPort) {
        this.asignarInputPort = asignarInputPort;
        this.editarInputPort = editarInputPort;
        this.desasignarInputPort = desasignarInputPort;
        this.listarInputPort = listarInputPort;
    }

    @GetMapping
    public ResponseEntity<List<InstructorEspecialidadResponseDTO>> listar() {
        List<InstructorEspecialidad> lista = listarInputPort.listarInstructorEspecialidad();
        List<InstructorEspecialidadResponseDTO> dtos = lista.stream().map(this::toResponseDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<InstructorEspecialidadResponseDTO> asignar(@RequestBody InstructorEspecialidadRequestDTO dto) {
        AsignarEspecialidadInstructorCommand command = new AsignarEspecialidadInstructorCommand();
        command.setUsuarioId(dto.getUsuarioId());
        command.setEspecialidadId(dto.getEspecialidadId());

        InstructorEspecialidad creado = asignarInputPort.asignarEspecialidadInstructor(command);
        return ResponseEntity.ok(toResponseDTO(creado));
    }

    @PutMapping("/{usuarioId}")
    public ResponseEntity<InstructorEspecialidadResponseDTO> editar(@PathVariable Long usuarioId, @RequestBody InstructorEspecialidadRequestDTO dto) {
        EditarEspecialidadInstructorCommand command = new EditarEspecialidadInstructorCommand();
        command.setEspecialidadId(dto.getEspecialidadId());

        InstructorEspecialidad actualizado = editarInputPort.editarInstructorEspecialidad(command, usuarioId);
        return ResponseEntity.ok(toResponseDTO(actualizado));
    }

    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<Void> desasignar(@PathVariable Long usuarioId) {
        desasignarInputPort.desasignarInstructorEspecialidad(usuarioId);
        return ResponseEntity.ok().build();
    }

    private InstructorEspecialidadResponseDTO toResponseDTO(InstructorEspecialidad model) {
        InstructorEspecialidadResponseDTO dto = new InstructorEspecialidadResponseDTO();
        dto.setUsuarioId(model.getUsuarioId());
        dto.setEspecialidadId(model.getEspecialidadId());
        return dto;
    }
}
