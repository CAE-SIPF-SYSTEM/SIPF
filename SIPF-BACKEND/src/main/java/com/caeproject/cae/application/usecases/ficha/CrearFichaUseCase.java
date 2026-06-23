package com.caeproject.cae.application.usecases.ficha;

import com.caeproject.cae.application.usecases.ficha.commands.RegistrarFichaCommand;
import com.caeproject.cae.domain.ports.in.ficha.RegistrarFichaInputPort;
import com.caeproject.cae.domain.ports.out.FichaRepository;
import com.caeproject.cae.domain.ports.out.ProgramaRepository;
import com.caeproject.cae.domain.ports.model.ficha.Ficha;
import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.FichaInvalidaException;
import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.FichaDuplicadaException;
import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.ProgramaNoEncontradoException;

public class CrearFichaUseCase implements RegistrarFichaInputPort {

    private final FichaRepository fichaRepository;
    private final ProgramaRepository programaRepository;

    public CrearFichaUseCase(FichaRepository fichaRepository, ProgramaRepository programaRepository) {
        this.fichaRepository = fichaRepository;
        this.programaRepository = programaRepository;
    }

    @Override
    public Ficha registrarFicha(RegistrarFichaCommand command) {

        if (command.getFechaInicio() == null || command.getFechaFin() == null) {
            throw new FichaInvalidaException("La fecha de inicio y la fecha de fin son obligatorias.");
        }
        if (command.getFechaFin().before(command.getFechaInicio())) {
            throw new FichaInvalidaException("El sistema impide la creación de la ficha porque la fecha de fin es anterior a la fecha de inicio.");
        }


        if (fichaRepository.existByCodigoFicha(command.getCodigoFicha())) {
            throw new FichaDuplicadaException(command.getCodigoFicha());
        }


        if (command.getProgramaId() == null) {
            throw new FichaInvalidaException("La ficha debe estar vinculada obligatoriamente a un Programa de Formación.");
        }
        

        programaRepository.findById(command.getProgramaId())
                .orElseThrow(() -> new ProgramaNoEncontradoException(command.getProgramaId()));
                
        Ficha ficha = new Ficha();
        ficha.setCodigoFicha(command.getCodigoFicha());
        ficha.setFechaInicio(command.getFechaInicio());
        ficha.setFechaFin(command.getFechaFin());
        ficha.setProgramaId(command.getProgramaId());
        
        return fichaRepository.saveFicha(ficha);
    }
}
