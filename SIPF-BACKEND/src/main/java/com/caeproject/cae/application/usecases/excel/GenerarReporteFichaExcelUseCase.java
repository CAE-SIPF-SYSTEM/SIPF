package com.caeproject.cae.application.usecases.excel;

import com.caeproject.cae.domain.ports.model.Ficha;
import com.caeproject.cae.domain.ports.out.ExportadorInstructorTrimestreRepository;
import com.caeproject.cae.domain.ports.out.FichaRepository;
import com.caeproject.cae.domain.ports.out.GeneradorExcelPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenerarReporteFichaExcelUseCase {

    private final ExportadorInstructorTrimestreRepository exportadorRepository;
    private final FichaRepository fichaRepository;
    private final GeneradorExcelPort generadorExcelPort;

    public GenerarReporteFichaExcelUseCase(ExportadorInstructorTrimestreRepository exportadorRepository,
                                           FichaRepository fichaRepository,
                                           GeneradorExcelPort generadorExcelPort) {
        this.exportadorRepository = exportadorRepository;
        this.fichaRepository = fichaRepository;
        this.generadorExcelPort = generadorExcelPort;
    }

    public byte[] ejecutar(Long fichaId, Long trimestreId) {
        List<ExportadorInstructorTrimestreRepository.DatosExportacion> datos = 
            exportadorRepository.obtenerDatosExportacionPorFicha(fichaId, trimestreId);
            
        Ficha ficha = fichaRepository.findById(fichaId)
                .orElseThrow(() -> new RuntimeException("Ficha no encontrada: " + fichaId));
                
        return generadorExcelPort.generarReporteFicha(ficha.getCodigoFicha(), datos);
    }
}
