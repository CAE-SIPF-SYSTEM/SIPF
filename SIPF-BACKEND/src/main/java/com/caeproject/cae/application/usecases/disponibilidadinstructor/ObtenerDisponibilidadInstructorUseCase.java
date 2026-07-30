package com.caeproject.cae.application.usecases.disponibilidadinstructor;

import com.caeproject.cae.domain.ports.exceptions.disponibilidadinstructorexception.DisponibilidadNoEncontradaException;
import com.caeproject.cae.domain.ports.in.disponibilidadinstructor.ObtenerDisponibilidadInstructorInputPort;
import com.caeproject.cae.domain.ports.model.DisponibilidadInstructor;
import com.caeproject.cae.domain.ports.model.enums.DiasDisponibles;
import com.caeproject.cae.domain.ports.out.DisponibilidadInstructorRepository;

import java.util.List;

public class ObtenerDisponibilidadInstructorUseCase implements ObtenerDisponibilidadInstructorInputPort {

    private final DisponibilidadInstructorRepository disponibilidadInstructorRepository;

    public ObtenerDisponibilidadInstructorUseCase(DisponibilidadInstructorRepository disponibilidadInstructorRepository) {
        this.disponibilidadInstructorRepository = disponibilidadInstructorRepository;
    }

    @Override
    public DisponibilidadInstructor obtenerDisponiblidad(Long usuarioId) {
        return disponibilidadInstructorRepository.findById(usuarioId)
                .orElseThrow(()-> new DisponibilidadNoEncontradaException(usuarioId));
    }

    @Override
    public List<DisponibilidadInstructor> obtenerPorDiasDisponibles(DiasDisponibles diasDisponibles) {
        List<DisponibilidadInstructor> disponibilidadInstructors = disponibilidadInstructorRepository.findByDiasDisponibles(diasDisponibles);
        return disponibilidadInstructors;
    }
}
