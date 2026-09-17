package com.caeproject.cae.application.usecases.programa;

import com.caeproject.cae.application.usecases.programa.commands.CrearProgramaCommand;
import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.ProgramaDuplicadoException;
import com.caeproject.cae.domain.ports.in.programa.CrearProgramaInputPort;
import com.caeproject.cae.domain.ports.model.Municipio;
import com.caeproject.cae.domain.ports.model.Programa;
import com.caeproject.cae.domain.ports.out.ProgramaRepository;
import com.caeproject.cae.domain.ports.out.UbicacionRepository;
import jakarta.transaction.Transactional;

public class CrearProgramaUseCase implements CrearProgramaInputPort {
    private final ProgramaRepository programaRepository;
    private final UbicacionRepository ubicacionRepository;
    public CrearProgramaUseCase (ProgramaRepository programaRepository,UbicacionRepository ubicacionRepository){
        this.programaRepository = programaRepository;
        this.ubicacionRepository = ubicacionRepository;
    }

    @Override
    @Transactional
    public Programa crearPrograma(CrearProgramaCommand command) {
        if (command.getJornada() == null || command.getNivelFormacion() == null || command.getDuracionpracticas() == null) {
            throw new IllegalArgumentException("La jornada, el nivel de formación y la duración de prácticas son obligatorios.");
        }
        Municipio municipio = ubicacionRepository.obtenerMunicipioPorId(command.getMunicipio().getId())
                .orElseThrow(() -> new RuntimeException("El municipio con ID " + command.getMunicipio().getId() + " no existe"));
        String name = command.getNombre();
        if (programaRepository.existByName(name)){
            throw new ProgramaDuplicadoException(name);
        }

        Programa programa = new Programa();
        programa.setNombre(command.getNombre());
        programa.setMunicipio(municipio);
        programa.setNivelFormacion(command.getNivelFormacion());
        programa.setJornada(command.getJornada());
        programa.setDuracionpracticas(command.getDuracionpracticas());

        return programaRepository.savePrograma(programa);
    }
}
