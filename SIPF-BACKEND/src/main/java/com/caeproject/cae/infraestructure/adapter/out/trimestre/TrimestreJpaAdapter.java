package com.caeproject.cae.infraestructure.adapter.out.trimestre;

import com.caeproject.cae.domain.ports.model.Trimestre;
import com.caeproject.cae.domain.ports.out.TrimestreRepository;
import com.caeproject.cae.infraestructure.mappers.TrimestreMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TrimestreJpaAdapter implements TrimestreRepository {

    private final TrimestreJpaRepository trimestreJpaRepository;
    private final TrimestreMapper trimestreMapper;

    public TrimestreJpaAdapter(TrimestreJpaRepository trimestreJpaRepository, TrimestreMapper trimestreMapper) {
        this.trimestreJpaRepository = trimestreJpaRepository;
        this.trimestreMapper = trimestreMapper;
    }

    @Override
    public Trimestre savetrimestre(Trimestre trimestre) {
        TrimestreEntity entity = trimestreMapper.toEntity(trimestre);
        TrimestreEntity savedEntity = trimestreJpaRepository.save(entity);
        return trimestreMapper.toDomain(savedEntity);
    }

    @Override
    public boolean existsByFicha(Long fichaId) {
        return trimestreJpaRepository.existsByFichaId(fichaId);
    }

    @Override
    public Optional<Trimestre> findById(Long id) {
        return trimestreJpaRepository.findById(id).map(trimestreMapper::toDomain);
    }

    @Override
    public void eliminarTrimestre(Long id) {
        trimestreJpaRepository.deleteById(id);
    }

    @Override
    public List<Trimestre> findAll() {
        return trimestreJpaRepository.findAll().stream()
                .map(trimestreMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Trimestre> findByFicha(Long fichaId) {
        return trimestreJpaRepository.findByFichaId(fichaId).stream()
                .map(trimestreMapper::toDomain)
                .collect(Collectors.toList());
    }
}
