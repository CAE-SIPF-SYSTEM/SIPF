package com.caeproject.cae.application.usecases.EspecialidadInstructor;

import com.caeproject.cae.domain.ports.in.InstructorEspecialidad.DesasignarEspecialidadInstructorInputPort;
import com.caeproject.cae.domain.ports.out.InstructorEspecialidadRepository;
import com.caeproject.cae.domain.ports.exceptions.instructorespecialidadexception.InstructorEspecialidadNoEncontradaException;

public class DesasignarEspecialidadInstructorUseCase implements DesasignarEspecialidadInstructorInputPort {

    private final InstructorEspecialidadRepository instructorEspecialidadRepository;

    public DesasignarEspecialidadInstructorUseCase(InstructorEspecialidadRepository instructorEspecialidadRepository) {
        this.instructorEspecialidadRepository = instructorEspecialidadRepository;
    }


    @Override
    public void desasignarInstructorEspecialidad(Long usuarioId) {
        if (instructorEspecialidadRepository.findByInstructorId(usuarioId).isEmpty()){
             throw new InstructorEspecialidadNoEncontradaException("Especialidad del instructor no encontrada");
        }
        instructorEspecialidadRepository.eliminarInstructorEspecialidad(usuarioId);
    }
}
