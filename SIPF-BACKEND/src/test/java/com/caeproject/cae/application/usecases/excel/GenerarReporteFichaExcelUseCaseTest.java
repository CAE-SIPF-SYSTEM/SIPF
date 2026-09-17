package com.caeproject.cae.application.usecases.excel;

import com.caeproject.cae.domain.ports.model.Ficha;
import com.caeproject.cae.domain.ports.out.ExportadorInstructorTrimestreRepository;
import com.caeproject.cae.domain.ports.out.ExportadorInstructorTrimestreRepository.DatosExportacion;
import com.caeproject.cae.domain.ports.out.FichaRepository;
import com.caeproject.cae.domain.ports.out.GeneradorExcelPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class GenerarReporteFichaExcelUseCaseTest {

    @InjectMocks
    private GenerarReporteFichaExcelUseCase useCase;

    @Mock
    private ExportadorInstructorTrimestreRepository exportadorRepository;

    @Mock
    private FichaRepository fichaRepository;

    @Mock
    private GeneradorExcelPort generadorExcelPort;

    @Test
    @DisplayName("Ejecutar generar reporte ficha exitoso")
    void ejecutar_generarReporteFichaExitoso() {
        Long fichaId = 10L;
        Long trimestreId = 1L;

        Ficha ficha = new Ficha();
        ficha.setId(fichaId);
        ficha.setCodigoFicha("2996315");

        List<DatosExportacion> datos = Collections.emptyList();
        byte[] expectedBytes = new byte[]{4, 5, 6};

        given(exportadorRepository.obtenerDatosExportacionPorFicha(fichaId, trimestreId)).willReturn(datos);
        given(fichaRepository.findById(fichaId)).willReturn(Optional.of(ficha));
        given(generadorExcelPort.generarReporteFicha("2996315", datos)).willReturn(expectedBytes);

        byte[] resultado = useCase.ejecutar(fichaId, trimestreId);

        assertNotNull(resultado);
        assertEquals(3, resultado.length);
        then(generadorExcelPort).should().generarReporteFicha("2996315", datos);

        System.out.println("Reporte de ficha generado con exito para codigo ficha: 2996315 Bytes: " + resultado.length);
    }

    @Test
    @DisplayName("Ejecutar ficha no encontrada lanza excepcion")
    void ejecutar_fichaNoEncontrada_lanzaExcepcion() {
        Long fichaId = 99L;
        Long trimestreId = 1L;

        given(exportadorRepository.obtenerDatosExportacionPorFicha(fichaId, trimestreId)).willReturn(Collections.emptyList());
        given(fichaRepository.findById(fichaId)).willReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> useCase.ejecutar(fichaId, trimestreId));
        assertTrue(ex.getMessage().contains("Ficha no encontrada"));
        then(generadorExcelPort).should(never()).generarReporteFicha(any(), any());

        System.out.println("Excepcion Ficha no encontrada lanzada correctamente para ID: " + fichaId);
    }
}
