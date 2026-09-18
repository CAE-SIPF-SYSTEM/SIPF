package com.caeproject.cae.application.usecases.ficha;

import com.caeproject.cae.application.usecases.ficha.commands.EditarFichaCommand;
import com.caeproject.cae.domain.ports.in.ficha.EditarFichaInputPort;
import com.caeproject.cae.domain.ports.out.FichaRepository;
import com.caeproject.cae.domain.ports.out.ProgramaRepository;
import com.caeproject.cae.domain.ports.model.Ficha;
import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.FichaNoEncontradaException;
import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.FichaDuplicadaException;
import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.FichaInvalidaException;
import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.ProgramaNoEncontradoException;

public class EditarFichaUseCase implements EditarFichaInputPort {
    
    private final FichaRepository fichaRepository;
    private final ProgramaRepository programaRepository;

    public EditarFichaUseCase(FichaRepository fichaRepository, ProgramaRepository programaRepository) {
        this.fichaRepository = fichaRepository;
        this.programaRepository = programaRepository;
    }

    @Override
    public Ficha editarFicha(EditarFichaCommand command, Long id) {

        Ficha fichaExistente = fichaRepository.findById(id)
            .orElseThrow(() -> new FichaNoEncontradaException(id));

        if (command.getFechaInicio() != null && command.getFechaFin() != null) {
            if (command.getFechaFin().before(command.getFechaInicio())) {
                throw new FichaInvalidaException("El sistema impide la edición porque la fecha de fin es anterior a la fecha de inicio.");
            }
            fichaExistente.setFechaInicio(command.getFechaInicio());
            fichaExistente.setFechaFin(command.getFechaFin());
        }

        if (command.getCodigoFicha() != null && !command.getCodigoFicha().equals(fichaExistente.getCodigoFicha())) {
            if (fichaRepository.existByCodigoFicha(command.getCodigoFicha())) {
                throw new FichaDuplicadaException(command.getCodigoFicha());
            }
            fichaExistente.setCodigoFicha(command.getCodigoFicha());
        }

        if (command.getProgramaId() != null && !command.getProgramaId().equals(fichaExistente.getProgramaId())) {
            programaRepository.findById(command.getProgramaId())
                    .orElseThrow(() -> new ProgramaNoEncontradoException(command.getProgramaId()));
            fichaExistente.setProgramaId(command.getProgramaId());
        }

        return fichaRepository.saveFicha(fichaExistente);
    }
}
