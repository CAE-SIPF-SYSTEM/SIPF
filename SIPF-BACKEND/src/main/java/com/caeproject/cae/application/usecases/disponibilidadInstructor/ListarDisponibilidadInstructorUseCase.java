package com.caeproject.cae.application.usecases.disponibilidadInstructor;

import com.caeproject.cae.domain.ports.in.DisponibilidadInstructor.ListarDisponibilidadInstructorInputPort;
import com.caeproject.cae.domain.ports.model.DisponibilidadInstructor;
import com.caeproject.cae.domain.ports.out.CompetenciaRepository;
import com.caeproject.cae.domain.ports.out.DisponibilidadInstructorRepository;

import java.util.List;

public class ListarDisponibilidadInstructorUseCase implements ListarDisponibilidadInstructorInputPort {

    final DisponibilidadInstructorRepository disponibilidadInstructorRepository;

    public ListarDisponibilidadInstructorUseCase(DisponibilidadInstructorRepository disponibilidadInstructorRepository) {
        this.disponibilidadInstructorRepository = disponibilidadInstructorRepository;
    }

    @Override
    public List<DisponibilidadInstructor> listarDisponibilidad() {
        return disponibilidadInstructorRepository.findAll();
    }
}
