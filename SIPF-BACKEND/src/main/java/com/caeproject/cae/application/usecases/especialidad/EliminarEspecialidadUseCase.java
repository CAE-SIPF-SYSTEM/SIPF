package com.caeproject.cae.application.usecases.especialidad;

import com.caeproject.cae.domain.ports.exceptions.especialidadexception.EspecialidadNoEncontradaException;
import com.caeproject.cae.domain.ports.in.Especialidad.EliminarEspecialidadInputPort;
import com.caeproject.cae.domain.ports.out.EspecialidadRepository;

public class EliminarEspecialidadUseCase implements EliminarEspecialidadInputPort {

    private final EspecialidadRepository especialidadRepository;

    public EliminarEspecialidadUseCase(EspecialidadRepository especialidadRepository) {
        this.especialidadRepository = especialidadRepository;
    }

    @Override
    public void eliminarEspecialidad(Long id) {
        especialidadRepository.findById(id)
                .orElseThrow(()-> new EspecialidadNoEncontradaException(id));

        especialidadRepository.eliminarEspecialidad(id);
    }
}
