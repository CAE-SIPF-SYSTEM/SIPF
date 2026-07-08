package com.caeproject.cae.application.usecases.especialidad;

import com.caeproject.cae.application.usecases.especialidad.commands.CrearEspecialidadCommand;
import com.caeproject.cae.domain.ports.exceptions.especialidadexception.EspecialidadEnUsoException;
import com.caeproject.cae.domain.ports.in.Especialidad.CrearEspecialidadInputPort;
import com.caeproject.cae.domain.ports.model.Especialidad;
import com.caeproject.cae.domain.ports.out.EspecialidadRepository;

public class CrearEspecialidadUseCase implements CrearEspecialidadInputPort {

    private final EspecialidadRepository especialidadRepository;

    public CrearEspecialidadUseCase(EspecialidadRepository especialidadRepository) {
        this.especialidadRepository = especialidadRepository;
    }

    @Override
    public Especialidad crearEspecialidad(CrearEspecialidadCommand command) {
        String nombre = command.getNombreEspecialidad();
        if (especialidadRepository.existByNombre(nombre)){
            throw new EspecialidadEnUsoException(nombre);
        }

        Especialidad especialidad = new Especialidad();
        especialidad.setNombreEspecialidad(nombre);

        return especialidadRepository.saveEspecialidad(especialidad);
    }
}
