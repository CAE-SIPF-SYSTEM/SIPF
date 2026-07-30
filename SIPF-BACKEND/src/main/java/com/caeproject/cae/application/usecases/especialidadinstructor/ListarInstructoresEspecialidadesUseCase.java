package com.caeproject.cae.application.usecases.especialidadinstructor;

import com.caeproject.cae.domain.ports.in.instructorespecialidad.ListarInstructoresEspecialidadesInputPort;
import com.caeproject.cae.domain.ports.model.InstructorEspecialidad;
import com.caeproject.cae.domain.ports.out.InstructorEspecialidadRepository;

import java.util.List;

public class ListarInstructoresEspecialidadesUseCase implements ListarInstructoresEspecialidadesInputPort {
    private final InstructorEspecialidadRepository instructorEspecialidadRepository;

    public ListarInstructoresEspecialidadesUseCase(InstructorEspecialidadRepository instructorEspecialidadRepository) {
        this.instructorEspecialidadRepository = instructorEspecialidadRepository;
    }


    @Override
    public List<InstructorEspecialidad> listarInstructorEspecialidad() {
        return instructorEspecialidadRepository.findAll();
    }
}
