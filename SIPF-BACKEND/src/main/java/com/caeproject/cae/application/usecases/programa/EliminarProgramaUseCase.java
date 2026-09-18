package com.caeproject.cae.application.usecases.programa;

import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.ProgramaNoEncontradoException;
import com.caeproject.cae.domain.ports.in.programa.EliminarProgramaInputPort;
import com.caeproject.cae.domain.ports.out.ProgramaRepository;

public class EliminarProgramaUseCase implements EliminarProgramaInputPort {
    private final ProgramaRepository programaRepository;

    public EliminarProgramaUseCase (ProgramaRepository programaRepository){
        this.programaRepository = programaRepository;
    }


    @Override
    public void eliminarPrograma(Long id) {
        programaRepository.findById(id)
                .orElseThrow(()-> new ProgramaNoEncontradoException(id));
        programaRepository.deletePrograma(id);

    }
}

