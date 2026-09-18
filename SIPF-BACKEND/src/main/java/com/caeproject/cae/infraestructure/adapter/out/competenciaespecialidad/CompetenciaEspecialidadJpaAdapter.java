package com.caeproject.cae.infraestructure.adapter.out.competenciaespecialidad;

import com.caeproject.cae.domain.ports.model.CompetenciaEspecialidad;
import com.caeproject.cae.domain.ports.out.CompetenciaEspecialidadRepository;
import com.caeproject.cae.infraestructure.mappers.CompetenciaEspecialidadMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CompetenciaEspecialidadJpaAdapter implements CompetenciaEspecialidadRepository {

    private final JpaCompetenciaEspecialidadRepository jpaRepository;
    private final CompetenciaEspecialidadMapper mapper;

    public CompetenciaEspecialidadJpaAdapter(JpaCompetenciaEspecialidadRepository jpaRepository, CompetenciaEspecialidadMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<CompetenciaEspecialidad> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CompetenciaEspecialidad> findByCompetenciaId(Long competenciaId) {
        return jpaRepository.findByCompetenciaId(competenciaId).map(mapper::toDomain);
    }

    @Override
    public List<CompetenciaEspecialidad> findByEspecialidadId(Long especialidadId) {
        return jpaRepository.findByEspecialidadId(especialidadId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public CompetenciaEspecialidad saveCompetenciaEspecialidad(CompetenciaEspecialidad competenciaEspecialidad) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(competenciaEspecialidad)));
    }

    @Override
    public void eliminarCompetenciaEspecialidad(Long competenciaId) {
        jpaRepository.deleteByCompetenciaId(competenciaId);
    }
}
