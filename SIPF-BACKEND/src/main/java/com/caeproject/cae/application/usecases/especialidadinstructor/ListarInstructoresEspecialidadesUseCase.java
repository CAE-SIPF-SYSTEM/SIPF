package com.caeproject.cae.application.usecases.EspecialidadInstructor;

import com.caeproject.cae.domain.ports.in.InstructorEspecialidad.ListarInstructoresEspecialidadesInputPort;
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
