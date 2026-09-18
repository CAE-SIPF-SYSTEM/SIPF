package com.caeproject.cae.infraestructure.adapter.in;

import com.caeproject.cae.application.usecases.excel.AlimentacionCrUseCase;
import com.caeproject.cae.domain.ports.in.disponibilidadinstructor.ExportacionInstructorTrimestreInputPort;
import com.caeproject.cae.domain.ports.out.ExportadorInstructorTrimestreRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/excel")
public class ExcelController {
    private final AlimentacionCrUseCase alimentacionCrUseCase;
    private final ExportacionInstructorTrimestreInputPort exportacionInstructorTrimestreInputPort;
    private final com.caeproject.cae.application.usecases.excel.GenerarReporteInstructorExcelUseCase reporteInstructorExcelUseCase;
    private final com.caeproject.cae.application.usecases.excel.GenerarReporteFichaExcelUseCase reporteFichaExcelUseCase;

    public ExcelController(AlimentacionCrUseCase alimentacionCrUseCase,
                           ExportacionInstructorTrimestreInputPort exportacionInstructorTrimestreInputPort,
                           com.caeproject.cae.application.usecases.excel.GenerarReporteInstructorExcelUseCase reporteInstructorExcelUseCase,
                           com.caeproject.cae.application.usecases.excel.GenerarReporteFichaExcelUseCase reporteFichaExcelUseCase) {
        this.alimentacionCrUseCase = alimentacionCrUseCase;
        this.exportacionInstructorTrimestreInputPort = exportacionInstructorTrimestreInputPort;
        this.reporteInstructorExcelUseCase = reporteInstructorExcelUseCase;
        this.reporteFichaExcelUseCase = reporteFichaExcelUseCase;
    }

    @PostMapping("/alimentacion")
    public ResponseEntity<String> subirAlimentacion(
            @RequestParam("file") MultipartFile file,
            @RequestParam("programaId") Long programaId) {
        try {
            //validacion de tipo de documento
            String nombreArchivo = file.getOriginalFilename();
            if (nombreArchivo == null || (!nombreArchivo.endsWith(".xlsx") && !nombreArchivo.endsWith(".xls"))) {
                return ResponseEntity.badRequest()
                        .body("El archivo debe ser un Excel (.xlsx o .xls)");
            }

            alimentacionCrUseCase.ejecutar(file.getInputStream(), programaId);
            return ResponseEntity.ok("Alimentacion subida");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar el Excel: " + e.getMessage());
        }
    }

    @GetMapping("/exportacion")
    public ResponseEntity<List<ExportadorInstructorTrimestreRepository.DatosExportacion>> exportarCargaInstructor(
            @RequestParam Long usuarioId, 
            @RequestParam Long trimestreId) {
        try {
            List<ExportadorInstructorTrimestreRepository.DatosExportacion> datos = 
                    exportacionInstructorTrimestreInputPort.ejecutarExportacion(usuarioId, trimestreId);
            if (datos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(datos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/reportes/instructor/descargar")
    public ResponseEntity<byte[]> descargarReporteInstructor(@RequestParam Long usuarioId, @RequestParam Long trimestreId) {
        try {
            byte[] excelData = reporteInstructorExcelUseCase.ejecutar(usuarioId, trimestreId);
            return ResponseEntity.ok()
                    .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"reporte_instructor.xlsx\"")
                    .contentType(org.springframework.http.MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(excelData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/reportes/ficha/descargar")
    public ResponseEntity<byte[]> descargarReporteFicha(@RequestParam Long fichaId, @RequestParam Long trimestreId) {
        try {
            byte[] excelData = reporteFichaExcelUseCase.ejecutar(fichaId, trimestreId);
            return ResponseEntity.ok()
                    .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"reporte_ficha.xlsx\"")
                    .contentType(org.springframework.http.MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(excelData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

