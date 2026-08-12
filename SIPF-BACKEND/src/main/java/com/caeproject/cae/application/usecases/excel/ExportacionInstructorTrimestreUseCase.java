package com.caeproject.cae.application.usecases.excel;

import com.caeproject.cae.domain.ports.in.disponibilidadinstructor.ExportacionInstructorTrimestreInputPort;
import com.caeproject.cae.domain.ports.out.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ExportacionInstructorTrimestreUseCase implements ExportacionInstructorTrimestreInputPort {

    private static final Logger log = LoggerFactory.getLogger(ExportacionInstructorTrimestreUseCase.class);

    private final ExportadorInstructorTrimestreRepository exportadorInstructorTrimestreRepository;

    public ExportacionInstructorTrimestreUseCase(ExportadorInstructorTrimestreRepository exportadorInstructorTrimestreRepository) {
        this.exportadorInstructorTrimestreRepository = exportadorInstructorTrimestreRepository;
    }


    public List<ExportadorInstructorTrimestreRepository.DatosExportacion> ejecutarExportacion(Long usuarioId, Long trimestreId) {
        List<ExportadorInstructorTrimestreRepository.DatosExportacion> exportador = exportadorInstructorTrimestreRepository.obtenerDatosExportacion(usuarioId, trimestreId);
        if (exportador.isEmpty()){
            log.warn("Use case: No se encontraron datos para el instructor ID {} en el trimestre ID {}", usuarioId, trimestreId);
        } else {
            log.info("Use case: Se recabó la información exitosamente (Total registros: {})", exportador.size());
        }

        return exportador;

}
}
