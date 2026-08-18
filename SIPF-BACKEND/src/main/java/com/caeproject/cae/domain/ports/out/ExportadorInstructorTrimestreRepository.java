package com.caeproject.cae.domain.ports.out;

import com.caeproject.cae.domain.ports.model.*;

import java.util.List;

public interface ExportadorInstructorTrimestreRepository {

    record DatosExportacion(
            Ficha ficha,
            Programa programa,
            Competencia competencia,
            Rap rap,
            Trimestre trimestre,
            ProgramacionAcademica programacionAcademica,
            Integer horasPresenciales
    ) {}

    List<DatosExportacion>obtenerDatosExportacion(Long usuarioId, Long trimestreId);

}
