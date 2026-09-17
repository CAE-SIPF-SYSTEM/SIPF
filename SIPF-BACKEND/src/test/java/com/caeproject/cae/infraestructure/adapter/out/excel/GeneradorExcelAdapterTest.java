package com.caeproject.cae.infraestructure.adapter.out.excel;

import com.caeproject.cae.domain.ports.model.*;
import com.caeproject.cae.domain.ports.model.enums.Jornada;
import com.caeproject.cae.domain.ports.out.ExportadorInstructorTrimestreRepository.DatosExportacion;
import com.caeproject.cae.infraestructure.adapter.out.excel.exportacionExcel.ExcelEstilosHelper;
import com.caeproject.cae.infraestructure.adapter.out.excel.exportacionExcel.ExcelImagenHelper;
import com.caeproject.cae.infraestructure.adapter.out.excel.exportacionExcel.GeneradorExcelAdapter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GeneradorExcelAdapterTest {

    private GeneradorExcelAdapter generadorExcelAdapter;

    @BeforeEach
    void setUp() {
        ExcelEstilosHelper estilosHelper = new ExcelEstilosHelper();
        ExcelImagenHelper imagenHelper = new ExcelImagenHelper();
        generadorExcelAdapter = new GeneradorExcelAdapter(estilosHelper, imagenHelper);
    }

    @Test
    @DisplayName("Generar reporte instructor datos validos")
    void generarReporteInstructor_datosValidos() throws IOException {
        Ficha ficha = new Ficha();
        ficha.setId(1L);
        ficha.setCodigoFicha("2996315");

        Programa programa = new Programa();
        programa.setId(2L);
        programa.setNombre("ADSO");
        programa.setJornada(Jornada.MAÑANA);

        Competencia competencia = new Competencia();
        competencia.setId(3L);
        competencia.setNombre("Desarrollo de Software");

        Rap rap = new Rap();
        rap.setId(4L);
        rap.setDescripcion("Construir componentes");

        Trimestre trimestre = new Trimestre();
        trimestre.setId(1L);

        ProgramacionAcademica pa = new ProgramacionAcademica();
        pa.setId(5L);

        DatosExportacion dato = new DatosExportacion(ficha, programa, competencia, rap, trimestre, pa, 40);

        byte[] excelBytes = generadorExcelAdapter.generarReporteInstructor("Jhon Prada", List.of(dato));

        assertNotNull(excelBytes);
        assertTrue(excelBytes.length > 0);

        try (XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(excelBytes))) {
            XSSFSheet sheet = workbook.getSheet("Horario Instructor");
            assertNotNull(sheet);
            assertEquals("REPORTE ACADÉMICO: Jhon Prada", sheet.getRow(0).getCell(1).getStringCellValue());
        }

        System.out.println("Reporte de instructor generado exitosamente. Tamañode archivo en bytes: " + excelBytes.length);
    }

    @Test
    @DisplayName("Generar reporte instructor datos con valores nulos")
    void generarReporteInstructor_datosConValoresNulos() throws IOException {
        DatosExportacion datoNulo = new DatosExportacion(null, null, null, null, null, null, null);

        byte[] excelBytes = generadorExcelAdapter.generarReporteInstructor("Instructor Prueba", List.of(datoNulo));

        assertNotNull(excelBytes);
        assertTrue(excelBytes.length > 0);

        try (XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(excelBytes))) {
            XSSFSheet sheet = workbook.getSheet("Horario Instructor");
            assertNotNull(sheet);
            assertEquals("N/A", sheet.getRow(5).getCell(0).getStringCellValue());
            assertEquals("N/A", sheet.getRow(5).getCell(1).getStringCellValue());
        }

        System.out.println("Reporte generado correctamente manejando valores nulos con N/A. Bytes: " + excelBytes.length);
    }

    @Test
    @DisplayName("Generar reporte ficha datos validos")
    void generarReporteFicha_datosValidos() throws IOException {
        Ficha ficha = new Ficha();
        ficha.setId(10L);
        ficha.setCodigoFicha("2996315");

        Programa programa = new Programa();
        programa.setNombre("ADSO");

        DatosExportacion dato = new DatosExportacion(ficha, programa, null, null, null, null, 20);

        byte[] excelBytes = generadorExcelAdapter.generarReporteFicha("2996315", List.of(dato));

        assertNotNull(excelBytes);
        assertTrue(excelBytes.length > 0);

        try (XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(excelBytes))) {
            XSSFSheet sheet = workbook.getSheet("Horario Ficha");
            assertNotNull(sheet);
            assertEquals("REPORTE DE PROGRAMACIÓN - FICHA: 2996315", sheet.getRow(0).getCell(1).getStringCellValue());
        }

        System.out.println("Reporte de ficha generado exitosamente para la ficha: " + ficha.getCodigoFicha() + " Bytes: " + excelBytes.length);
    }

    @Test
    @DisplayName("Generar reporte ficha lista vacia")
    void generarReporteFicha_listaVacia() throws IOException {
        byte[] excelBytes = generadorExcelAdapter.generarReporteFicha("2996315", Collections.emptyList());

        assertNotNull(excelBytes);
        assertTrue(excelBytes.length > 0);

        try (XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(excelBytes))) {
            XSSFSheet sheet = workbook.getSheet("Horario Ficha");
            assertNotNull(sheet);
            assertEquals(4, sheet.getLastRowNum());
        }

        System.out.println("Reporte de ficha generado solo con encabezados para lista vacia. Bytes: " + excelBytes.length);
    }
}
