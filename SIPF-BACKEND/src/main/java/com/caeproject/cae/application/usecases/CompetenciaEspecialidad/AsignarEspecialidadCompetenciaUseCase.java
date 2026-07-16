package com.caeproject.cae.application.usecases.CompetenciaEspecialidad;

import com.caeproject.cae.application.usecases.CompetenciaEspecialidad.commands.AsignarEspecialidadCompetenciaCommand;
import com.caeproject.cae.domain.ports.exceptions.competenciaespecialidadexception.CompetenciaEspecialidadDuplicadaException;
import com.caeproject.cae.domain.ports.in.CompetenciaEspecialidad.AsignarEspecialidadCompetenciaInputPort;
import com.caeproject.cae.domain.ports.model.CompetenciaEspecialidad;
import com.caeproject.cae.domain.ports.out.CompetenciaEspecialidadRepository;

public class AsignarEspecialidadCompetenciaUseCase implements AsignarEspecialidadCompetenciaInputPort {

    private final CompetenciaEspecialidadRepository competenciaEspecialidadRepository;

    public AsignarEspecialidadCompetenciaUseCase(CompetenciaEspecialidadRepository competenciaEspecialidadRepository) {
        this.competenciaEspecialidadRepository = competenciaEspecialidadRepository;
    }

    @Override
    public CompetenciaEspecialidad asignarEspecialidadCompetencia(AsignarEspecialidadCompetenciaCommand command) {
        if (competenciaEspecialidadRepository.findByCompetenciaId(command.getCompetenciaId()).isPresent()) {
            throw new CompetenciaEspecialidadDuplicadaException("La competencia ya tiene una especialidad asignada");
        }
        
        CompetenciaEspecialidad asignacion = new CompetenciaEspecialidad(
                command.getCompetenciaId(),
                command.getEspecialidadId()
        );
        return competenciaEspecialidadRepository.saveCompetenciaEspecialidad(asignacion);
    }
}
