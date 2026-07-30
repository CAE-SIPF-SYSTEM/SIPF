package com.caeproject.cae.application.usecases.EspecialidadInstructor;

import com.caeproject.cae.application.usecases.EspecialidadInstructor.commands.AsignarEspecialidadInstructorCommand;
import com.caeproject.cae.domain.ports.in.InstructorEspecialidad.AsignarEspecialidadInstructorInputPort;
import com.caeproject.cae.domain.ports.model.InstructorEspecialidad;
import com.caeproject.cae.domain.ports.out.InstructorEspecialidadRepository;
import com.caeproject.cae.domain.ports.exceptions.instructorespecialidadexception.InstructorEspecialidadDuplicadaException;

public class AsignarEspecialidadInstructorUseCase implements AsignarEspecialidadInstructorInputPort {
    private final InstructorEspecialidadRepository instructorEspecialidadRepository;

    public AsignarEspecialidadInstructorUseCase(InstructorEspecialidadRepository instructorEspecialidadRepository) {
        this.instructorEspecialidadRepository = instructorEspecialidadRepository;
    }

    @Override
    public InstructorEspecialidad asignarEspecialidadInstructor(AsignarEspecialidadInstructorCommand command) {
        if (instructorEspecialidadRepository.findByInstructorId(command.getUsuarioId()).isPresent()) {
            throw new InstructorEspecialidadDuplicadaException("El instructor ya tiene una especialidad asignada");
        }

        InstructorEspecialidad instructorEspecialidad = new InstructorEspecialidad();
        instructorEspecialidad.setEspecialidadId(command.getEspecialidadId());
        instructorEspecialidad.setUsuarioId(command.getUsuarioId());

        return instructorEspecialidadRepository.saveInstructorEspecialidad(instructorEspecialidad);
    }
}
