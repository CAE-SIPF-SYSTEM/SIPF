package com.caeproject.cae.application.usecases.excel;

import com.caeproject.cae.domain.ports.model.Competencia;
import com.caeproject.cae.domain.ports.model.enums.TipoCompetencia;
import com.caeproject.cae.domain.ports.model.Rap;
import com.caeproject.cae.domain.ports.out.AlimentacionCRRepository;
import com.caeproject.cae.domain.ports.out.CompetenciaRepository;
import com.caeproject.cae.domain.ports.out.RapRepository;
import com.caeproject.cae.domain.ports.model.DiseñoCurricular;
import com.caeproject.cae.domain.ports.out.DiseñoCurricularRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.InputStream;
import java.util.List;

public class AlimentacionCrUseCase {
    private final AlimentacionCRRepository alimentacionCRRepository;
    private final CompetenciaRepository competenciaRepository;
    private final RapRepository rapRepository;
    private final DiseñoCurricularRepository diseñoCurricularRepository;
    private static final Logger log = LoggerFactory.getLogger(AlimentacionCrUseCase.class);

    public AlimentacionCrUseCase(AlimentacionCRRepository alimentacionCRRepository,
                                 CompetenciaRepository competenciaRepository,
                                 RapRepository rapRepository,
                                 DiseñoCurricularRepository diseñoCurricularRepository) {
        this.alimentacionCRRepository = alimentacionCRRepository;
        this.competenciaRepository = competenciaRepository;
        this.rapRepository = rapRepository;
        this.diseñoCurricularRepository = diseñoCurricularRepository;
    }

    public void ejecutar(InputStream alimentacionExcel, Long programaId) {

        List<AlimentacionCRRepository.CompetenciaRap> competenciaRaps = alimentacionCRRepository.extraerAlimentacion(alimentacionExcel);

        log.info(">>> UseCase: Se recibieron {} competencias con sus RAPs.", competenciaRaps.size());

        for (AlimentacionCRRepository.CompetenciaRap registro : competenciaRaps) {
            try {
                Competencia competencia = registro.competencia();
                List<Rap> raps = registro.raps();

                String codigoCompetencia = competencia.getCodigo();
                String nombreCompetencia = competencia.getNombre();
                TipoCompetencia tipoCompetencia = competencia.getTipoCompetencia();

                log.info("---------------------------");
                log.info("Procesando Competencia: {} - {} - {}", codigoCompetencia, nombreCompetencia, tipoCompetencia);

                // Guardar la competencia si no existe
                Competencia competenciaGuardada;
                java.util.Optional<Competencia> existente;
                
                if (codigoCompetencia == null || codigoCompetencia.trim().isEmpty() || "SIN_CODIGO".equals(codigoCompetencia)) {
                    existente = competenciaRepository.findByNombre(nombreCompetencia);
                } else {
                    existente = competenciaRepository.findByCodigo(codigoCompetencia);
                }
                
                if (existente.isEmpty()) {
                    competenciaGuardada = competenciaRepository.saveCompetencia(competencia);
                    log.info("Competencia guardada con éxito. DB ID: {}, Código: {}", competenciaGuardada.getId(), codigoCompetencia);
                } else {
                    competenciaGuardada = existente.get();
                    log.info("La competencia ya existe en la base de datos. DB ID: {}, Código: {}", competenciaGuardada.getId(), codigoCompetencia);
                }

                // duplicados
                List<Rap> rapsExistentes = rapRepository.findByCompetencia(competenciaGuardada.getId());

                for (Rap rap : raps) {
                    Long idcompetencia = competenciaGuardada.getId();
                    String descripcionRap = rap.getDescripcion();
                    
                    //verificacion de duplicacion rap x descripion
                    boolean existeRap = rapsExistentes.stream()
                        .anyMatch(r -> r.getDescripcion() != null && r.getDescripcion().equalsIgnoreCase(descripcionRap));

                    if (existeRap) {
                        log.info("   -> RAP omitido (ya existe): {}", descripcionRap);
                        continue;
                    }

                    Boolean estado = rap.getEstado();
                    Integer horasPresenciales = rap.getHorasPresenciales();

                    log.info("   -> Guardando RAP: {} {} {} {}", idcompetencia, descripcionRap, estado, horasPresenciales);

                    rap.setCompetenciaId(idcompetencia);
                    
                    rap.setId(null);

                    Rap rapGuardado = rapRepository.saveRap(rap);

                    // --- NUEVO: Guardar en Diseño Curricular ---
                    DiseñoCurricular dc = new DiseñoCurricular();
                    dc.setProgramaId(programaId);
                    dc.setRapId(rapGuardado.getId());
                    dc.setNumeroTrimestre(registro.trimestre());
                    diseñoCurricularRepository.saveDiseñoCurricular(dc);
                    log.info("     -> Plantilla Diseño Curricular guardada (Programa: {}, Trimestre: {}, RAP: {})", programaId, registro.trimestre(), rapGuardado.getId());
                }

            } catch (Exception e) {
                log.error(">>> UseCase Error procesando registro de competencia: {}", registro.competencia().getCodigo(), e);
                throw new IllegalStateException("Error al guardar la competencia o sus RAPs: " + e.getMessage(), e);
            }
        }
    }
}


