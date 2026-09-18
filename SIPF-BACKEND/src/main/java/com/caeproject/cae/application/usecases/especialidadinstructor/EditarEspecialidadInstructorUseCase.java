package com.caeproject.cae.application.usecases.especialidadinstructor;

import com.caeproject.cae.application.usecases.especialidadinstructor.commands.EditarEspecialidadInstructorCommand;
import com.caeproject.cae.application.usecases.especialidad.commands.EditarEspecialidadCommand;
import com.caeproject.cae.domain.ports.in.especialidad.EditarEspecialidadInputPort;
import com.caeproject.cae.domain.ports.in.instructorespecialidad.EditarEspecialidadInstructorInputPort;
import com.caeproject.cae.domain.ports.model.Especialidad;
import com.caeproject.cae.domain.ports.model.InstructorEspecialidad;
import com.caeproject.cae.domain.ports.out.InstructorEspecialidadRepository;
import com.caeproject.cae.domain.ports.exceptions.instructorespecialidadexception.InstructorEspecialidadNoEncontradaException;

public class EditarEspecialidadInstructorUseCase implements EditarEspecialidadInstructorInputPort {
    private final InstructorEspecialidadRepository instructorEspecialidadRepository;

    public EditarEspecialidadInstructorUseCase(InstructorEspecialidadRepository instructorEspecialidadRepository) {
        this.instructorEspecialidadRepository = instructorEspecialidadRepository;
    }


    @Override
    public InstructorEspecialidad editarInstructorEspecialidad(EditarEspecialidadInstructorCommand command, Long usuarioId) {
        InstructorEspecialidad instructorEspecialidadexistente = instructorEspecialidadRepository.findByInstructorId(usuarioId)
                .orElseThrow(()-> new InstructorEspecialidadNoEncontradaException("Especialidad del instructor no encontrada"));
        if (command.getEspecialidadId() != null){
            instructorEspecialidadexistente.setEspecialidadId(command.getEspecialidadId());
        }
        return instructorEspecialidadRepository.saveInstructorEspecialidad(instructorEspecialidadexistente);
    }
}
