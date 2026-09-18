package com.caeproject.cae.domain.ports.out;

import com.caeproject.cae.domain.ports.model.InstructorEspecialidad;

import java.util.List;
import java.util.Optional;

public interface InstructorEspecialidadRepository {
    List<InstructorEspecialidad>findAll();
    Optional<InstructorEspecialidad> findByInstructorId(Long usuarioId);
    List<InstructorEspecialidad> findByEspecialidadId(Long especialidadId);
    InstructorEspecialidad saveInstructorEspecialidad (InstructorEspecialidad instructorEspecialidad);
    void eliminarInstructorEspecialidad (Long usuarioId);

}
