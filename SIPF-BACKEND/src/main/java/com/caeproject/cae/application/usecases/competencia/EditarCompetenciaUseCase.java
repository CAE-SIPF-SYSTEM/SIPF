package com.caeproject.cae.application.usecases.competencia;

import com.caeproject.cae.application.usecases.competencia.commands.EditarCompetenciaCommand;
import com.caeproject.cae.domain.ports.exceptions.competenciaexception.CompetenciaNoEncontradaException;
import com.caeproject.cae.domain.ports.in.competencia.EditarCompetenciaInputPort;
import com.caeproject.cae.domain.ports.model.competencia.Competencia;
import com.caeproject.cae.domain.ports.out.CompetenciaRepository;

public class EditarCompetenciaUseCase implements EditarCompetenciaInputPort {
     final CompetenciaRepository competenciaRepository;

    public EditarCompetenciaUseCase (CompetenciaRepository competenciaRepository){
        this.competenciaRepository = competenciaRepository;

    }
    @Override
    public Competencia editarCompetencia(EditarCompetenciaCommand command, Long id) {
        Competencia competenciaExistente = competenciaRepository.findById(id)
                .orElseThrow(() -> new CompetenciaNoEncontradaException(id));

        if (command.getNombre() != null) {
            competenciaExistente.setNombre(command.getNombre());
        }
        if (command.getTipoCompetencia() != null) {
            competenciaExistente.setTipoCompetencia(command.getTipoCompetencia());
        }

        return competenciaRepository.saveCompetencia(competenciaExistente);
    }
}
