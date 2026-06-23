package com.caeproject.cae.application.usecases.excel;

import com.caeproject.cae.domain.ports.model.competencia.Competencia;
import com.caeproject.cae.domain.ports.model.enums.TipoCompetencia;
import com.caeproject.cae.domain.ports.model.rap.Rap;
import com.caeproject.cae.domain.ports.out.AlimentacionCRRepository;
import com.caeproject.cae.domain.ports.out.CompetenciaRepository;
import com.caeproject.cae.domain.ports.out.RapRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.InputStream;
import java.util.List;

public class AlimentacionCrUseCase {
    private final AlimentacionCRRepository alimentacionCRRepository;
    private final CompetenciaRepository competenciaRepository;
    private final RapRepository rapRepository;
    private static final Logger log = LoggerFactory.getLogger(AlimentacionCrUseCase.class);

    public AlimentacionCrUseCase(AlimentacionCRRepository alimentacionCRRepository,
                                 CompetenciaRepository competenciaRepository,
                                 RapRepository rapRepository) {
        this.alimentacionCRRepository = alimentacionCRRepository;
        this.competenciaRepository = competenciaRepository;
        this.rapRepository = rapRepository;
    }

    public void ejecutar(InputStream alimentacionExcel) {

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
                java.util.Optional<Competencia> existente = competenciaRepository.findByCodigo(codigoCompetencia);
                
                if (existente.isEmpty()) {
                    competenciaGuardada = competenciaRepository.saveCompetencia(competencia);
                    log.info("Competencia guardada con éxito. DB ID: {}, Código: {}", competenciaGuardada.getId(), codigoCompetencia);
                } else {
                    competenciaGuardada = existente.get();
                    log.info("La competencia ya existe en la base de datos. DB ID: {}, Código: {}", competenciaGuardada.getId(), codigoCompetencia);
                }

                for (Rap rap : raps) {
                    Long idcompetencia = competenciaGuardada.getId();
                    String descripcionRap = rap.getDescripcion();
                    Boolean estado = rap.getEstado();
                    Integer horasPresenciales = rap.getHorasPresenciales();

                    log.info("   -> Guardando RAP: {} {} {} {}", idcompetencia, descripcionRap, estado, horasPresenciales);

                    // Enlazar al padre (competencia) usando el ID generado por la BD
                    rap.setCompetenciaId(idcompetencia);
                    
                    // Colocar id en null para que la BD genere el auto-incremental y no sobreescriba registros antiguos
                    rap.setId(null);
                    
                    rapRepository.saveRap(rap);
                }

            } catch (Exception e) {
                log.error(">>> UseCase Error procesando registro de competencia: {}", registro.competencia().getCodigo(), e);
                throw new IllegalStateException("Error al guardar la competencia o sus RAPs: " + e.getMessage(), e);
            }
        }
    }
}


