package com.caeproject.cae.domain.ports.out;

import com.caeproject.cae.domain.ports.model.DisponibilidadInstructor;
import com.caeproject.cae.domain.ports.model.enums.DiasDisponibles;

import java.util.List;
import java.util.Optional;

public interface DisponibilidadInstructorRepository {
    Optional<DisponibilidadInstructor> findById(Long usuarioId);
    List<DisponibilidadInstructor> findByDiasDisponibles (DiasDisponibles diasDisponibles);
    List<DisponibilidadInstructor> findAll();
    void deleteDisponibilidadInstrucor(Long usuarioId);
    DisponibilidadInstructor saveDisponibilidad(DisponibilidadInstructor disponibilidadInstructor);
    }
