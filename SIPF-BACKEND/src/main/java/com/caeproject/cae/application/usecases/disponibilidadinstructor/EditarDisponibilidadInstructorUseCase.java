package com.caeproject.cae.application.usecases.disponibilidadinstructor;

import com.caeproject.cae.application.usecases.disponibilidadinstructor.commands.EditarDisponibilidadCommand;
import com.caeproject.cae.domain.ports.exceptions.disponibilidadinstructorexception.DisponibilidadNoEncontradaException;
import com.caeproject.cae.domain.ports.in.disponibilidadinstructor.EditarDisponibilidadInstructorInputPort;
import com.caeproject.cae.domain.ports.model.DisponibilidadInstructor;
import com.caeproject.cae.domain.ports.out.DisponibilidadInstructorRepository;

public class EditarDisponibilidadInstructorUseCase implements EditarDisponibilidadInstructorInputPort {

    private final DisponibilidadInstructorRepository disponibilidadInstructorRepository;

    public EditarDisponibilidadInstructorUseCase(DisponibilidadInstructorRepository disponibilidadInstructorRepository) {
        this.disponibilidadInstructorRepository = disponibilidadInstructorRepository;
    }

    @Override
    public DisponibilidadInstructor editarDisponibilidad(EditarDisponibilidadCommand command, Long usuarioId) {
       DisponibilidadInstructor disponibilidadInstructor = disponibilidadInstructorRepository.findById(usuarioId)
               .orElseThrow(()-> new DisponibilidadNoEncontradaException(usuarioId));
       if (command.getDiasDisponibles() != null){
           disponibilidadInstructor.setDiasDisponibles(command.getDiasDisponibles());
       }

       if (command.getJornada() != null) {
           disponibilidadInstructor.setJornada(command.getJornada());
       }
       if (command.getMunicipios() != null) {
           disponibilidadInstructor.setMunicipios(command.getMunicipios());
       }
       if (command.getJornada() != null) {
           disponibilidadInstructor.setJornada(command.getJornada());
       }
       return disponibilidadInstructorRepository.saveDisponibilidad(disponibilidadInstructor);
    }
}
