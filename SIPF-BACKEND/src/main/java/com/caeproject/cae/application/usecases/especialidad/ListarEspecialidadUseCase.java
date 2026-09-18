package com.caeproject.cae.application.usecases.especialidad;

import com.caeproject.cae.domain.ports.in.especialidad.ListarEspecialidadInputPort;
import com.caeproject.cae.domain.ports.model.Especialidad;
import com.caeproject.cae.domain.ports.out.EspecialidadRepository;

import java.util.List;

public class ListarEspecialidadUseCase implements ListarEspecialidadInputPort {

    private final EspecialidadRepository especialidadRepository;

    public ListarEspecialidadUseCase(EspecialidadRepository especialidadRepository) {
        this.especialidadRepository = especialidadRepository;
    }


    @Override
    public List<Especialidad> listarEspecialidades() {
        return especialidadRepository.findAll();
    }
}
