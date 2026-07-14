package com.caeproject.cae.infraestructure.adapter.out.diseñocurricular;

import com.caeproject.cae.domain.ports.model.DiseñoCurricular;
import com.caeproject.cae.domain.ports.out.DiseñoCurricularRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class DiseñoCurricularJpaAdapter implements DiseñoCurricularRepository {

    private final DiseñoCurricularJpaRepository jpaRepository;
    private final com.caeproject.cae.infraestructure.mappers.DiseñoCurricularMapper mapper;

    public DiseñoCurricularJpaAdapter(DiseñoCurricularJpaRepository jpaRepository, com.caeproject.cae.infraestructure.mappers.DiseñoCurricularMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }


    @Override
    public List<DiseñoCurricular> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DiseñoCurricular> findyById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public void eliminarDiseñoCurricular(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public DiseñoCurricular saveDiseñoCurricular(DiseñoCurricular diseñoCurricular) {
        DiseñoCurricularEntity entity = mapper.toEntity(diseñoCurricular);
        DiseñoCurricularEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
}