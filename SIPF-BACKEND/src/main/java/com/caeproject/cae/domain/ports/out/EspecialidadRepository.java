package com.caeproject.cae.domain.ports.out;

import com.caeproject.cae.domain.ports.model.Especialidad;

import java.util.List;
import java.util.Optional;

public interface EspecialidadRepository {
    List<Especialidad> findAll();
    Optional<Especialidad> findById(Long id);
    void eliminarEspecialidad (Long id);
    List<Especialidad>findByNombre(String nombreEspecialidad);
    boolean existByNombre(String nombreEspecialidad);
    Especialidad saveEspecialidad(Especialidad especialidad);

}
