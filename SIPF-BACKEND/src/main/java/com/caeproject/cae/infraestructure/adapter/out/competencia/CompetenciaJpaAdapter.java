package com.caeproject.cae.infraestructure.adapter.out.competencia;

import com.caeproject.cae.domain.ports.model.competencia.Competencia;
import com.caeproject.cae.domain.ports.model.enums.TipoCompetencia;
import com.caeproject.cae.domain.ports.out.CompetenciaRepository;
import com.caeproject.cae.infraestructure.mappers.CompetenciaMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CompetenciaJpaAdapter implements CompetenciaRepository {

    private final CompetenciaJpaRepository jpaRepository;
    private final CompetenciaMapper mapper;

    public CompetenciaJpaAdapter(CompetenciaJpaRepository jpaRepository, CompetenciaMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Competencia saveCompetencia(Competencia competencia) {
        CompetenciaEntity entity = mapper.toEntity(competencia);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Competencia> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Competencia> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Competencia> findByTipoCompetencia(TipoCompetencia tipoCompetencia) {
        return jpaRepository.findByTipoCompetencia(tipoCompetencia).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void eliminarCompetencia(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existByName(String nombre) {
        return jpaRepository.existsByNombre(nombre);
    }

    @Override
    public Optional<Competencia> findByCodigo(String codigo) {
        return jpaRepository.findByCodigo(codigo).map(mapper::toDomain);
    }
}
