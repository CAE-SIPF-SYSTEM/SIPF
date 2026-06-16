package com.caeproject.cae.application.usecases.ficha;

import com.caeproject.cae.domain.ports.in.ficha.RegistrarFichaInputPort;
import com.caeproject.cae.domain.ports.out.FichaRepository;
import com.caeproject.cae.domain.ports.out.ProgramaRepository;
import com.caeproject.cae.domain.ports.model.ficha.Ficha;
import com.caeproject.cae.domain.ports.exceptions.fichas_ProgramasException.FichaInvalidaException;
import com.caeproject.cae.domain.ports.exceptions.fichas_ProgramasException.FichaDuplicadaException;
import com.caeproject.cae.domain.ports.exceptions.fichas_ProgramasException.ProgramaNoEncontradoException;

public class CrearFichaUseCase implements RegistrarFichaInputPort {

    private final FichaRepository fichaRepository;
    private final ProgramaRepository programaRepository;

    public CrearFichaUseCase(FichaRepository fichaRepository, ProgramaRepository programaRepository) {
        this.fichaRepository = fichaRepository;
        this.programaRepository = programaRepository;
    }

    @Override
    public Ficha registrarFicha(Ficha ficha) {

        if (ficha.getFechaInicio() == null || ficha.getFechaFin() == null) {
            throw new FichaInvalidaException("La fecha de inicio y la fecha de fin son obligatorias.");
        }
        if (ficha.getFechaFin().before(ficha.getFechaInicio())) {
            throw new FichaInvalidaException("El sistema impide la creación de la ficha porque la fecha de fin es anterior a la fecha de inicio.");
        }


        if (fichaRepository.existByCodigoFicha(ficha.getCodigoFicha())) {
            throw new FichaDuplicadaException(ficha.getCodigoFicha());
        }


        if (ficha.getProgramaId() == null) {
            throw new FichaInvalidaException("La ficha debe estar vinculada obligatoriamente a un Programa de Formación.");
        }
        

        programaRepository.findById(ficha.getProgramaId())
                .orElseThrow(() -> new ProgramaNoEncontradoException(ficha.getProgramaId()));
        return fichaRepository.saveFicha(ficha);
    }
}
