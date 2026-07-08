package com.caeproject.cae.application.usecases.especialidad;

import com.caeproject.cae.domain.ports.exceptions.especialidadexception.EspecialidadNoEncontradaException;
import com.caeproject.cae.domain.ports.in.Especialidad.ObtenerEspecialidadInputPort;
import com.caeproject.cae.domain.ports.model.Especialidad;
import com.caeproject.cae.domain.ports.out.EspecialidadRepository;

public class ObtenerEspecialidadUseCase implements ObtenerEspecialidadInputPort {

    private final EspecialidadRepository especialidadRepository;

    public ObtenerEspecialidadUseCase(EspecialidadRepository especialidadRepository) {
        this.especialidadRepository = especialidadRepository;
    }


    @Override
    public Especialidad obtenerEspecialidad(Long id) {
        return especialidadRepository.findById(id)
                .orElseThrow(()-> new EspecialidadNoEncontradaException(id));
    }
}
