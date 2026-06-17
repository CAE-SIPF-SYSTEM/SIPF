package com.caeproject.cae.application.usecases.programa;

import com.caeproject.cae.domain.ports.in.programa.ListarProgramaInputPort;
import com.caeproject.cae.domain.ports.model.programa.Programa;
import com.caeproject.cae.domain.ports.out.ProgramaRepository;

import java.util.List;

public class ListarProgramaUseCase implements ListarProgramaInputPort {
    private final ProgramaRepository programaRepository;

    public ListarProgramaUseCase(ProgramaRepository programaRepository){
        this.programaRepository = programaRepository;
    }
    @Override
    public List<Programa> listarProgramas() {
        return programaRepository.findAll();
    }
}
