package com.caeproject.cae.application.usecases.excel;

import com.caeproject.cae.domain.ports.exceptions.usuarioexceptions.UsuarioNoEncontradoException;
import com.caeproject.cae.domain.ports.model.PerfilBase;
import com.caeproject.cae.domain.ports.out.ExportadorInstructorTrimestreRepository;
import com.caeproject.cae.domain.ports.out.GeneradorExcelPort;
import com.caeproject.cae.domain.ports.out.PerfilBaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenerarReporteInstructorExcelUseCase {

    private final ExportadorInstructorTrimestreRepository exportadorRepository;
    private final PerfilBaseRepository perfilBaseRepository;
    private final GeneradorExcelPort generadorExcelPort;

    public GenerarReporteInstructorExcelUseCase(ExportadorInstructorTrimestreRepository exportadorRepository,
                                                PerfilBaseRepository perfilBaseRepository,
                                                GeneradorExcelPort generadorExcelPort) {
        this.exportadorRepository = exportadorRepository;
        this.perfilBaseRepository = perfilBaseRepository;
        this.generadorExcelPort = generadorExcelPort;
    }

    public byte[] ejecutar(Long usuarioId, Long trimestreId) {
        List<ExportadorInstructorTrimestreRepository.DatosExportacion> datos = 
            exportadorRepository.obtenerDatosExportacion(usuarioId, trimestreId);
            
        PerfilBase perfilBase = perfilBaseRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException(usuarioId));
        String nombreInstructor = perfilBase.getNombre() + " " + perfilBase.getApellido();

        return generadorExcelPort.generarReporteInstructor(nombreInstructor, datos);
    }
}
