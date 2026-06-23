package com.caeproject.cae.application.usecases.programa;

import com.caeproject.cae.application.usecases.programa.commands.EditarProgramaCommand;
import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.ProgramaNoEncontradoException;
import com.caeproject.cae.domain.ports.in.programa.EditarProgramaInputPort;
import com.caeproject.cae.domain.ports.model.programa.Programa;
import com.caeproject.cae.domain.ports.out.ProgramaRepository;

public class EditarProgramaUseCase implements EditarProgramaInputPort {
    private final ProgramaRepository programaRepository;

    public EditarProgramaUseCase (ProgramaRepository programaRepository){
        this.programaRepository = programaRepository;
    }

    @Override
    public Programa editarPrograma(EditarProgramaCommand command, Long id) {
        Programa programaExistente = programaRepository.findById(id)
                .orElseThrow(() -> new ProgramaNoEncontradoException(id));

        if (command.getNombre() != null) {
            programaExistente.setNombre(command.getNombre());
        }
        if (command.getMunicipio() != null) {
            programaExistente.setMunicipio(command.getMunicipio());
        }
        if (command.getNivelFormacion() != null) {
            programaExistente.setNivelFormacion(command.getNivelFormacion());
        }
        if (command.getJornada() != null) {
            programaExistente.setJornada(command.getJornada());
        }
        if (command.getDuracionpracticas() != null) {
            programaExistente.setDuracionpracticas(command.getDuracionpracticas());
        }

        return programaRepository.savePrograma(programaExistente);
    }
}
