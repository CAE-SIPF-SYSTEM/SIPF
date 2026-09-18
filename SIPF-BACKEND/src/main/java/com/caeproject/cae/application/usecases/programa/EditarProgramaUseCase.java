package com.caeproject.cae.application.usecases.programa;

import com.caeproject.cae.application.usecases.programa.commands.EditarProgramaCommand;
import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.ProgramaNoEncontradoException;
import com.caeproject.cae.domain.ports.in.programa.EditarProgramaInputPort;
import com.caeproject.cae.domain.ports.model.Municipio;
import com.caeproject.cae.domain.ports.model.Programa;
import com.caeproject.cae.domain.ports.out.ProgramaRepository;
import com.caeproject.cae.domain.ports.out.UbicacionRepository;

public class EditarProgramaUseCase implements EditarProgramaInputPort {
    private final ProgramaRepository programaRepository;
    private final UbicacionRepository ubicacionRepository;

    public EditarProgramaUseCase (ProgramaRepository programaRepository, UbicacionRepository ubicacionRepository){
        this.programaRepository = programaRepository;
        this.ubicacionRepository = ubicacionRepository;
    }

    @Override
    public Programa editarPrograma(EditarProgramaCommand command, Long id) {
        Programa programaExistente = programaRepository.findById(id)
                .orElseThrow(() -> new ProgramaNoEncontradoException(id));


        if (command.getNombre() != null) {
            programaExistente.setNombre(command.getNombre());
        }
        if (command.getMunicipio() != null && command.getMunicipio().getId() != null) {
            Municipio municipio = ubicacionRepository.obtenerMunicipioPorId(command.getMunicipio().getId())
                    .orElseThrow(() -> new RuntimeException("El municipio con ID " + command.getMunicipio().getId() + " no existe"));
            programaExistente.setMunicipio(municipio);
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
