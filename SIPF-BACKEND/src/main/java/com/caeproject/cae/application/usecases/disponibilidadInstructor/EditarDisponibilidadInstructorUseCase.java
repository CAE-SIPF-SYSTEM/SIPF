package com.caeproject.cae.application.usecases.disponibilidadInstructor;

import com.caeproject.cae.application.usecases.disponibilidadInstructor.commands.EditarDisponibilidadCommand;
import com.caeproject.cae.domain.ports.exceptions.disponibilidadinstructorexception.DisponibilidadNoEncontradaException;
import com.caeproject.cae.domain.ports.in.DisponibilidadInstructor.EditarDisponibilidadInstructorInputPort;
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
       if (command.getHorasMaximas() != null) {
           disponibilidadInstructor.setHorasMaximas(command.getHorasMaximas());
       }
       return disponibilidadInstructorRepository.saveDisponibilidad(disponibilidadInstructor);
    }
}
