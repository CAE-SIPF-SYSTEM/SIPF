package com.caeproject.cae.domain.ports.out;

import java.util.List;

public interface GeneradorExcelPort {
    byte[] generarReporteInstructor(String nombreInstructor, List<ExportadorInstructorTrimestreRepository.DatosExportacion> datos);
    byte[] generarReporteFicha(String codigoFicha, List<ExportadorInstructorTrimestreRepository.DatosExportacion> datos);
}
