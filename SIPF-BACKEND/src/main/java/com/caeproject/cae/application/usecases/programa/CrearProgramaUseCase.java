package com.caeproject.cae.application.usecases.programa;

import com.caeproject.cae.application.usecases.programa.commands.CrearProgramaCommand;
import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.ProgramaDuplicadoException;
import com.caeproject.cae.domain.ports.in.programa.CrearProgramaInputPort;
import com.caeproject.cae.domain.ports.model.Programa;
import com.caeproject.cae.domain.ports.out.ProgramaRepository;

public class CrearProgramaUseCase implements CrearProgramaInputPort {
    private final ProgramaRepository programaRepository;

    public CrearProgramaUseCase (ProgramaRepository programaRepository){
        this.programaRepository = programaRepository;
    }

    @Override
    public Programa crearPrograma(CrearProgramaCommand command) {
        String name = command.getNombre();
        if (programaRepository.existByName(name)){
            throw new ProgramaDuplicadoException(name);
        }

        Programa programa = new Programa();
        programa.setNombre(command.getNombre());
        programa.setMunicipio(command.getMunicipio());
        programa.setNivelFormacion(command.getNivelFormacion());
        programa.setJornada(command.getJornada());
        programa.setDuracionpracticas(command.getDuracionpracticas());

        return programaRepository.savePrograma(programa);
    }
}
