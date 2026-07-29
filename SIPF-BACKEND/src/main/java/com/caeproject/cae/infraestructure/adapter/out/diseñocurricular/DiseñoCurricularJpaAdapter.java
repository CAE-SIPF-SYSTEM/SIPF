package com.caeproject.cae.infraestructure.adapter.out.diseñocurricular;

import com.caeproject.cae.domain.ports.model.DiseñoCurricular;
import com.caeproject.cae.domain.ports.out.DiseñoCurricularRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public Stream<DiseñoCurricular> findByRapId(Long rapId) {
        return jpaRepository.findByRapId(rapId).stream().map(mapper::toDomain);
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

    @Override
    public Integer sumarHorasPorCompetenciaYPrograma(Long competenciaId, Long programaId) {
        return jpaRepository.sumarHorasPorCompetenciaYPrograma(competenciaId,programaId);
    }

    @Override
    public Optional<DiseñoCurricular> findByProgramaIdAndRapId(Long programaId, Long rapId) {
        return jpaRepository.findByProgramaIdAndRapId(programaId, rapId).map(mapper::toDomain);
    }

    @Override
    public List<DiseñoCurricular> findByProgramaIdandTrimestreId(Long programaId, Long trimestreId) {
        return jpaRepository.findByProgramaIdAndNumeroTrimestre(programaId, Math.toIntExact(trimestreId)).stream().map(mapper::toDomain).collect(Collectors.toList());
    }
}