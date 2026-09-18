package com.caeproject.cae.infraestructure.adapter.out.disponibilidadinstructor;

import com.caeproject.cae.domain.ports.model.DisponibilidadInstructor;
import com.caeproject.cae.domain.ports.model.enums.DiasDisponibles;
import com.caeproject.cae.domain.ports.out.DisponibilidadInstructorRepository;
import com.caeproject.cae.infraestructure.mappers.DisponibilidadInstructorMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DisponibilidadInstructorJpaAdapter implements DisponibilidadInstructorRepository {

    private final DisponibilidadInstructorJpaRepository jpaRepository;
    private final DisponibilidadInstructorMapper mapper;

    public DisponibilidadInstructorJpaAdapter(DisponibilidadInstructorJpaRepository jpaRepository, DisponibilidadInstructorMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }


    @Override
    public Optional<DisponibilidadInstructor> findById(Long usuarioId) {
        return jpaRepository.findById(usuarioId).map(mapper::toDomain);
    }

    @Override
    public List<DisponibilidadInstructor> findByDiasDisponibles(DiasDisponibles diasDisponibles) {
        return jpaRepository.findByDiasDisponibles(diasDisponibles).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<DisponibilidadInstructor> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteDisponibilidadInstrucor(Long usuarioId) {
jpaRepository.deleteById(usuarioId);
    }

    @Override
    public DisponibilidadInstructor saveDisponibilidad(DisponibilidadInstructor disponibilidadInstructor) {
       DisponibilidadInstructorEntity entity = mapper.toEntity(disponibilidadInstructor);
       return mapper.toDomain(jpaRepository.save(entity));
    }
}
