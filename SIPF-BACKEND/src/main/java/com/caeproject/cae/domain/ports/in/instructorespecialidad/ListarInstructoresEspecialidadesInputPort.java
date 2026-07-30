package com.caeproject.cae.domain.ports.in.InstructorEspecialidad;

import com.caeproject.cae.domain.ports.model.InstructorEspecialidad;

import java.util.List;

public interface ListarInstructoresEspecialidadesInputPort {
    List<InstructorEspecialidad> listarInstructorEspecialidad();
}
