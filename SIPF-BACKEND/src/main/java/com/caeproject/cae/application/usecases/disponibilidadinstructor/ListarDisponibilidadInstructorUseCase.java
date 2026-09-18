package com.caeproject.cae.application.usecases.disponibilidadinstructor;

import com.caeproject.cae.domain.ports.in.disponibilidadinstructor.ListarDisponibilidadInstructorInputPort;
import com.caeproject.cae.domain.ports.model.DisponibilidadInstructor;
import com.caeproject.cae.domain.ports.out.CompetenciaRepository;
import com.caeproject.cae.domain.ports.out.DisponibilidadInstructorRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class ListarDisponibilidadInstructorUseCase implements ListarDisponibilidadInstructorInputPort {

    final DisponibilidadInstructorRepository disponibilidadInstructorRepository;

    public ListarDisponibilidadInstructorUseCase(DisponibilidadInstructorRepository disponibilidadInstructorRepository) {
        this.disponibilidadInstructorRepository = disponibilidadInstructorRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DisponibilidadInstructor> listarDisponibilidad() {
        return disponibilidadInstructorRepository.findAll();
    }
}
