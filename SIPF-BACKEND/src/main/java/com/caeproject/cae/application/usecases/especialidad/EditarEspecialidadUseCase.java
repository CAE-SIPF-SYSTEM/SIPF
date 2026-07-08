package com.caeproject.cae.application.usecases.especialidad;

import com.caeproject.cae.application.usecases.competencia.EditarCompetenciaUseCase;
import com.caeproject.cae.application.usecases.especialidad.commands.EditarEspecialidadCommand;
import com.caeproject.cae.domain.ports.exceptions.especialidadexception.EspecialidadNoEncontradaException;
import com.caeproject.cae.domain.ports.in.Especialidad.EditarEspecialidadInputPort;
import com.caeproject.cae.domain.ports.model.Especialidad;
import com.caeproject.cae.domain.ports.out.EspecialidadRepository;

public class EditarEspecialidadUseCase implements EditarEspecialidadInputPort {

    private final EspecialidadRepository especialidadRepository;

    public EditarEspecialidadUseCase(EspecialidadRepository especialidadRepository) {
        this.especialidadRepository = especialidadRepository;
    }

    @Override
    public Especialidad editarEspecialidad(EditarEspecialidadCommand command, Long id) {
        Especialidad especialidad = especialidadRepository.findById(id)
                .orElseThrow(()-> new EspecialidadNoEncontradaException(id));
    if (command.getNombreEspecialidad() != null) {
        especialidad.setNombreEspecialidad(command.getNombreEspecialidad());
    }
    return especialidadRepository.saveEspecialidad(especialidad);

    }

}
