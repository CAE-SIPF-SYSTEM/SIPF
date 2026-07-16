package com.caeproject.cae.application.usecases.disponibilidadInstructor;

import com.caeproject.cae.domain.ports.exceptions.disponibilidadinstructorexception.DisponibilidadNoEncontradaException;
import com.caeproject.cae.domain.ports.in.DisponibilidadInstructor.EliminarDisponibilidadInputPort;
import com.caeproject.cae.domain.ports.out.DisponibilidadInstructorRepository;

public class EliminarDisponibilidadInstructorUseCase implements EliminarDisponibilidadInputPort {

    private final   DisponibilidadInstructorRepository disponibilidadInstructorRepository;

    public EliminarDisponibilidadInstructorUseCase(DisponibilidadInstructorRepository disponibilidadInstructorRepository) {
        this.disponibilidadInstructorRepository = disponibilidadInstructorRepository;
    }

    @Override
    public void eliminarDisponibilidad(Long usuarioid) {
        disponibilidadInstructorRepository.findById(usuarioid)
                .orElseThrow(()->new DisponibilidadNoEncontradaException(usuarioid));
        disponibilidadInstructorRepository.deleteDisponibilidadInstrucor(usuarioid);
    }
}
