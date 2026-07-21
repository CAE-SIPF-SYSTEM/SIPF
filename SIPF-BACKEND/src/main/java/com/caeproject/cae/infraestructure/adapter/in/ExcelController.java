package com.caeproject.cae.infraestructure.adapter.in;

import com.caeproject.cae.application.usecases.excel.AlimentacionCrUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/excel")
public class ExcelController {
    private final AlimentacionCrUseCase alimentacionCrUseCase;

    public ExcelController(AlimentacionCrUseCase alimentacionCrUseCase) {
        this.alimentacionCrUseCase = alimentacionCrUseCase;
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
}

