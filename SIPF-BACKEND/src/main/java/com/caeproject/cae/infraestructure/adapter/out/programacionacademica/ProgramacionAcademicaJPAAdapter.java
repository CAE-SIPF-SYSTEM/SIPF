package com.caeproject.cae.infraestructure.adapter.out.programacionacademica;

import com.caeproject.cae.domain.ports.model.ProgramacionAcademica;
import com.caeproject.cae.domain.ports.out.ProgramacionAcademicaRepository;
import com.caeproject.cae.infraestructure.mappers.ProgragramacionAcademicaMapper;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProgramacionAcademicaJPAAdapter implements ProgramacionAcademicaRepository {

    private final ProgramacionAcademicaJpaRepository jpaRepository;
    private final ProgragramacionAcademicaMapper mapper;

    public ProgramacionAcademicaJPAAdapter(ProgramacionAcademicaJpaRepository jpaRepository, ProgragramacionAcademicaMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<ProgramacionAcademica> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProgramacionAcademica> findById(Long id) { return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<ProgramacionAcademica> findByTrimestre(Long trimestreId) {
        return jpaRepository.findByTrimestreId(trimestreId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }


    @Override
    public List<ProgramacionAcademica> findByUserId(Long usuarioId) {
        return jpaRepository.findByUsuarioId(usuarioId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void eliminarProgramacionAcademica(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByRapIdAndTrimestreId(Long rapId, Long trimestreId) {
        return jpaRepository.existsByRapIdAndTrimestreId(rapId, trimestreId);
    }

    @Override
    public ProgramacionAcademica saveProgramacion(ProgramacionAcademica programacionAcademica) {
        ProgramacionAcademicaEntity entity = mapper.toEntity(programacionAcademica);
        ProgramacionAcademicaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }


}
