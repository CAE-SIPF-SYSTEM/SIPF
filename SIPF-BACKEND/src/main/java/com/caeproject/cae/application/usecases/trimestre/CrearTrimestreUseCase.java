package com.caeproject.cae.application.usecases.trimestre;

import com.caeproject.cae.application.usecases.trimestre.commands.CrearTrimestreCommand;
import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.FichaDuplicadaException;
import com.caeproject.cae.domain.ports.in.trimestre.CrearTrimestreInputPort;
import com.caeproject.cae.domain.ports.model.Trimestre;
import com.caeproject.cae.domain.ports.out.TrimestreRepository;

public class CrearTrimestreUseCase  implements CrearTrimestreInputPort {
    private final TrimestreRepository trimestreRepository;

    public CrearTrimestreUseCase(TrimestreRepository trimestreRepository){
    this.trimestreRepository = trimestreRepository;
    }
    @Override
    public Trimestre crearTrimestre(CrearTrimestreCommand command) {
        if (command.getAnio() == null) {
            throw new IllegalArgumentException("El año no puede ser nulo");
        }
        if (command.getFechaInicio() == null || command.getFechaFin() == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas");
        }
        if (command.getFechaInicio().after(command.getFechaFin())) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha fin");
        }
        Long fichaID = command.getFichaId();
        if (trimestreRepository.existsByFicha(fichaID)){
            throw new FichaDuplicadaException("Ficha duplicada con id  " + fichaID);
        }
        Trimestre trimestre = new Trimestre();
        trimestre.setAnio(command.getAnio());
        trimestre.setNumeroTrimestre(command.getNumeroTrimestre());
        trimestre.setFichaId(command.getFichaId());
        trimestre.setFechaInicio(command.getFechaInicio());
        trimestre.setFechaFin(command.getFechaFin());
        return trimestreRepository.savetrimestre(trimestre);
    }


}
