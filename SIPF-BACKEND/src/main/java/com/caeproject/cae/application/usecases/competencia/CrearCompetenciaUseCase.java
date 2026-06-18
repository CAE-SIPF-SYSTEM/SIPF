package com.caeproject.cae.application.usecases.competencia;

import com.caeproject.cae.application.usecases.competencia.commands.CrearCompetenciaCommand;
import com.caeproject.cae.domain.ports.exceptions.competenciaexception.CompetenciaDuplicadaException;
import com.caeproject.cae.domain.ports.in.competencia.CrearCompetenciaInputPort;
import com.caeproject.cae.domain.ports.model.competencia.Competencia;
import com.caeproject.cae.domain.ports.out.CompetenciaRepository;

public class CrearCompetenciaUseCase implements CrearCompetenciaInputPort {
    private final CompetenciaRepository competenciaRepository;

    public CrearCompetenciaUseCase (CompetenciaRepository competenciaRepository){
        this.competenciaRepository = competenciaRepository;
    }

    @Override
    public Competencia crearCompetencia(CrearCompetenciaCommand command) {
        String nombre = command.getNombre();
        if(competenciaRepository.existByName(nombre)){
            throw new CompetenciaDuplicadaException(nombre);
        }

        Competencia competencia = new Competencia();
        competencia.setNombre(command.getNombre());
        competencia.setTipoCompetencia(command.getTipoCompetencia());

        return competenciaRepository.saveCompetencia(competencia);
    }
}
