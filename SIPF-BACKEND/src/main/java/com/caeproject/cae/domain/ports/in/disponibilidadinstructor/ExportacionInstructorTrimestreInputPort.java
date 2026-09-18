package com.caeproject.cae.domain.ports.in.disponibilidadinstructor;

import com.caeproject.cae.domain.ports.out.ExportadorInstructorTrimestreRepository.DatosExportacion;
import java.util.List;

public interface ExportacionInstructorTrimestreInputPort {
    List<DatosExportacion> ejecutarExportacion(Long usuarioId, Long trimestreId);
}
