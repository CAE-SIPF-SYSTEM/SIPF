package com.caeproject.cae.application.usecases.excel;

import com.caeproject.cae.domain.ports.model.Competencia;
import com.caeproject.cae.domain.ports.model.enums.TipoCompetencia;
import com.caeproject.cae.domain.ports.model.Rap;
import com.caeproject.cae.domain.ports.out.AlimentacionCRRepository;
import com.caeproject.cae.domain.ports.out.CompetenciaRepository;
import com.caeproject.cae.domain.ports.out.RapRepository;
import com.caeproject.cae.domain.ports.out.DiseñoCurricularRepository;
import com.caeproject.cae.domain.ports.model.DiseñoCurricular;
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
                List<AlimentacionCRRepository.RapImport> rapsImport = registro.raps();
                Integer trimestre = registro.trimestre();

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

                for (AlimentacionCRRepository.RapImport rapImport : rapsImport) {
                    Rap rap = rapImport.rap();
                    Long idcompetencia = competenciaGuardada.getId();
                    String descripcionRap = rap.getDescripcion();
                    
                    //verificacion de duplicacion rap x descripion
                    boolean existeRap = rapsExistentes.stream()
                        .anyMatch(r -> r.getDescripcion() != null && r.getDescripcion().equalsIgnoreCase(descripcionRap));

                    Rap rapGuardado;
                    if (existeRap) {
                        log.info("   -> RAP omitido (ya existe): {}", descripcionRap);
                        rapGuardado = rapsExistentes.stream()
                            .filter(r -> r.getDescripcion() != null && r.getDescripcion().equalsIgnoreCase(descripcionRap))
                            .findFirst().orElseThrow();
                    } else {
                        Boolean estado = rap.getEstado();
                        log.info("   -> Guardando RAP: {} {} {}", idcompetencia, descripcionRap, estado);
                        rap.setCompetenciaId(idcompetencia);
                        rap.setId(null);
                        rapGuardado = rapRepository.saveRap(rap);
                    }

                    // Ahora guardamos el Diseño Curricular
                    Integer horasPresenciales = rapImport.horasPresenciales();
                    DiseñoCurricular diseno = new DiseñoCurricular();
                    diseno.setProgramaId(programaId);
                    diseno.setNumeroTrimestre(trimestre);
                    diseno.setRapId(rapGuardado.getId());
                    diseno.setHoraspresenciales(horasPresenciales);
                    
                    diseñoCurricularRepository.saveDiseñoCurricular(diseno);
                    log.info("   -> Diseño Curricular guardado para RAP ID: {}, Programa ID: {}, Trimestre: {}, Horas: {}", 
                        rapGuardado.getId(), programaId, trimestre, horasPresenciales);
                }

            } catch (Exception e) {
                log.error(">>> UseCase Error procesando registro de competencia: {}", registro.competencia().getCodigo(), e);
                throw new IllegalStateException("Error al guardar la competencia o sus RAPs: " + e.getMessage(), e);
            }
        }
    }
}


