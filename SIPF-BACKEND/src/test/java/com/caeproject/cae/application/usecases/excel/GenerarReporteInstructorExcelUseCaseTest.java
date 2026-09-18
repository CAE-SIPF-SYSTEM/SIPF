package com.caeproject.cae.application.usecases.excel;

import com.caeproject.cae.domain.ports.exceptions.usuarioexceptions.UsuarioNoEncontradoException;
import com.caeproject.cae.domain.ports.model.PerfilBase;
import com.caeproject.cae.domain.ports.out.ExportadorInstructorTrimestreRepository;
import com.caeproject.cae.domain.ports.out.ExportadorInstructorTrimestreRepository.DatosExportacion;
import com.caeproject.cae.domain.ports.out.GeneradorExcelPort;
import com.caeproject.cae.domain.ports.out.PerfilBaseRepository;
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
public class GenerarReporteInstructorExcelUseCaseTest {

    @InjectMocks
    private GenerarReporteInstructorExcelUseCase useCase;

    @Mock
    private ExportadorInstructorTrimestreRepository exportadorRepository;

    @Mock
    private PerfilBaseRepository perfilBaseRepository;

    @Mock
    private GeneradorExcelPort generadorExcelPort;

    @Test
    @DisplayName("Ejecutar generar reporte instructor exitoso")
    void ejecutar_generarReporteInstructorExitoso() {
        Long usuarioId = 1L;
        Long trimestreId = 2L;

        PerfilBase perfilBase = new PerfilBase();
        perfilBase.setUsuarioId(usuarioId);
        perfilBase.setNombre("Jhon");
        perfilBase.setApellido("Prada");

        List<DatosExportacion> datos = Collections.emptyList();
        byte[] expectedBytes = new byte[]{1, 2, 3};

        given(exportadorRepository.obtenerDatosExportacion(usuarioId, trimestreId)).willReturn(datos);
        given(perfilBaseRepository.findById(usuarioId)).willReturn(Optional.of(perfilBase));
        given(generadorExcelPort.generarReporteInstructor("Jhon Prada", datos)).willReturn(expectedBytes);

        byte[] resultado = useCase.ejecutar(usuarioId, trimestreId);

        assertNotNull(resultado);
        assertEquals(3, resultado.length);
        then(generadorExcelPort).should().generarReporteInstructor("Jhon Prada", datos);

        System.out.println("Reporte instructor generado con exito. Usuario: Jhon Prada Bytes: " + resultado.length);
    }

    @Test
    @DisplayName("Ejecutar usuario no encontrado lanza excepcion")
    void ejecutar_usuarioNoEncontrado_lanzaExcepcion() {
        Long usuarioId = 99L;
        Long trimestreId = 2L;

        given(exportadorRepository.obtenerDatosExportacion(usuarioId, trimestreId)).willReturn(Collections.emptyList());
        given(perfilBaseRepository.findById(usuarioId)).willReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class, () -> useCase.ejecutar(usuarioId, trimestreId));
        then(generadorExcelPort).should(never()).generarReporteInstructor(any(), any());

        System.out.println("Excepcion UsuarioNoEncontradoException lanzada correctamente para ID: " + usuarioId);
    }
}
