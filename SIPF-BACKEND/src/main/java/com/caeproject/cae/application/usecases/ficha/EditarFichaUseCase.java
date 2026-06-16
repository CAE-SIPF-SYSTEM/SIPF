package com.caeproject.cae.application.usecases.ficha;

import com.caeproject.cae.domain.ports.in.ficha.EditarFichaInputPort;
import com.caeproject.cae.domain.ports.out.FichaRepository;
import com.caeproject.cae.domain.ports.out.ProgramaRepository;
import com.caeproject.cae.domain.ports.model.ficha.Ficha;
import com.caeproject.cae.domain.ports.exceptions.fichas_ProgramasException.FichaNoEncontradaException;
import com.caeproject.cae.domain.ports.exceptions.fichas_ProgramasException.FichaDuplicadaException;
import com.caeproject.cae.domain.ports.exceptions.fichas_ProgramasException.FichaInvalidaException;
import com.caeproject.cae.domain.ports.exceptions.fichas_ProgramasException.ProgramaNoEncontradoException;

public class EditarFichaUseCase implements EditarFichaInputPort {
    
    private final FichaRepository fichaRepository;
    private final ProgramaRepository programaRepository;

    public EditarFichaUseCase(FichaRepository fichaRepository, ProgramaRepository programaRepository) {
        this.fichaRepository = fichaRepository;
        this.programaRepository = programaRepository;
    }

    @Override
    public Ficha editarFicha(Ficha ficha, Long id) {

        Ficha fichaExistente = fichaRepository.findById(id)
            .orElseThrow(() -> new FichaNoEncontradaException(id));

        if (ficha.getFechaInicio() != null && ficha.getFechaFin() != null) {
            if (ficha.getFechaFin().before(ficha.getFechaInicio())) {
                throw new FichaInvalidaException("El sistema impide la edición porque la fecha de fin es anterior a la fecha de inicio.");
            }
            fichaExistente.setFechaInicio(ficha.getFechaInicio());
            fichaExistente.setFechaFin(ficha.getFechaFin());
        }

        if (ficha.getCodigoFicha() != null && !ficha.getCodigoFicha().equals(fichaExistente.getCodigoFicha())) {
            if (fichaRepository.existByCodigoFicha(ficha.getCodigoFicha())) {
                throw new FichaDuplicadaException(ficha.getCodigoFicha());
            }
            fichaExistente.setCodigoFicha(ficha.getCodigoFicha());
        }

        if (ficha.getProgramaId() != null && !ficha.getProgramaId().equals(fichaExistente.getProgramaId())) {
            programaRepository.findById(ficha.getProgramaId())
                    .orElseThrow(() -> new ProgramaNoEncontradoException(ficha.getProgramaId()));
            fichaExistente.setProgramaId(ficha.getProgramaId());
        }

        return fichaRepository.saveFicha(fichaExistente);
    }
}
