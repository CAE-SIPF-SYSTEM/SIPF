package com.caeproject.cae.domain.ports.out;

import com.caeproject.cae.domain.ports.model.DiseñoCurricular;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface DiseñoCurricularRepository {
    List<DiseñoCurricular> findAll();
    Stream<DiseñoCurricular> findByRapId(Long rapId);
    Optional<DiseñoCurricular> findyById(Long id);
    void eliminarDiseñoCurricular(Long id);
    DiseñoCurricular saveDiseñoCurricular(DiseñoCurricular diseñoCurricular);
    Integer sumarHorasPorCompetenciaYPrograma(Long competenciaId, Long programaId);
    Optional<DiseñoCurricular>findByProgramaIdAndRapId(Long programaId, Long rapId);
    List<DiseñoCurricular>findByProgramaIdandTrimestreId(Long programaId, Long trimestreId);
}
