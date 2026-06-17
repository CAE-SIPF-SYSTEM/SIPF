package com.caeproject.cae.application.usecases.programa;

import com.caeproject.cae.domain.ports.exceptions.fichas_ProgramasException.ProgramaNoEncontradoException;
import com.caeproject.cae.domain.ports.in.programa.EditarProgramaInputPort;
import com.caeproject.cae.domain.ports.model.programa.Programa;
import com.caeproject.cae.domain.ports.out.ProgramaRepository;

public class EditarProgramaUseCase implements EditarProgramaInputPort {
    private final ProgramaRepository programaRepository;

    public EditarProgramaUseCase (ProgramaRepository programaRepository){
        this.programaRepository = programaRepository;
    }


    @Override
    public Programa editarPrograma(Programa programa, Long id) {
        if (programa.getId() == null){
            throw new ProgramaNoEncontradoException(id);
        }
        return programaRepository.savePrograma(programa);
    }
}
