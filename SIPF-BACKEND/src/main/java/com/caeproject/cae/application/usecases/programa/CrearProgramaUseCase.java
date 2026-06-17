package com.caeproject.cae.application.usecases.programa;

import com.caeproject.cae.domain.ports.exceptions.fichas_ProgramasException.ProgramaDuplicadoException;
import com.caeproject.cae.domain.ports.in.programa.CrearProgramaInputPort;
import com.caeproject.cae.domain.ports.model.programa.Programa;
import com.caeproject.cae.domain.ports.out.ProgramaRepository;

public class CrearProgramaUseCase implements CrearProgramaInputPort {
    private final ProgramaRepository programaRepository;

    public CrearProgramaUseCase (ProgramaRepository programaRepository){
        this.programaRepository = programaRepository;
    }


    @Override
    public Programa crearPrograma(Programa programa) {
        String name = programa.getNombre();
       if (programaRepository.existByName(name)){
            throw new ProgramaDuplicadoException(name);
       }
       programaRepository.savePrograma(programa);
       return programa;
    }
}
