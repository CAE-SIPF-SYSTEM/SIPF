package com.caeproject.cae.infraestructure.adapter.out;

import com.caeproject.cae.domain.ports.model.InstructorEspecialidad;
import com.caeproject.cae.domain.ports.out.InstructorEspecialidadRepository;
import com.caeproject.cae.infraestructure.adapter.out.persistence.JpaInstructorEspecialidadRepository;
import com.caeproject.cae.infraestructure.mappers.InstructorEspecialidadMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class InstructorEspecialidadRepositoryAdapter implements InstructorEspecialidadRepository {

    private final JpaInstructorEspecialidadRepository jpaRepository;
    private final InstructorEspecialidadMapper mapper;

    public InstructorEspecialidadRepositoryAdapter(JpaInstructorEspecialidadRepository jpaRepository, InstructorEspecialidadMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<InstructorEspecialidad> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<InstructorEspecialidad> findByInstructorId(Long usuarioId) {
        return jpaRepository.findByUsuarioId(usuarioId).map(mapper::toDomain);
    }

    @Override
    public List<InstructorEspecialidad> findByEspecialidadId(Long especialidadId) {
        return jpaRepository.findByEspecialidadId(especialidadId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public InstructorEspecialidad saveInstructorEspecialidad(InstructorEspecialidad instructorEspecialidad) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(instructorEspecialidad)));
    }

    @Override
    public void eliminarInstructorEspecialidad(Long usuarioId) {
        jpaRepository.deleteByUsuarioId(usuarioId);
    }
}
