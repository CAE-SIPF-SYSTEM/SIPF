package com.caeproject.cae.infraestructure.adapter.out.excel;

import com.caeproject.cae.domain.ports.model.enums.TipoCompetencia;
import com.caeproject.cae.domain.ports.out.AlimentacionCRRepository.CompetenciaRap;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AlimentacionCRAdapterTest {

    private AlimentacionCRAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new AlimentacionCRAdapter();
    }

    private InputStream crearExcelInputStream(String[] columnas, String[][] filasData) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Alimentacion");
            
            if (columnas != null) {
                Row header = sheet.createRow(0);
                for (int i = 0; i < columnas.length; i++) {
                    header.createCell(i).setCellValue(columnas[i]);
                }
            }

            if (filasData != null) {
                for (int r = 0; r < filasData.length; r++) {
                    Row row = sheet.createRow(r + 1);
                    for (int c = 0; c < filasData[r].length; c++) {
                        if (filasData[r][c] != null) {
                            row.createCell(c).setCellValue(filasData[r][c]);
                        }
                    }
                }
            }

            workbook.write(baos);
            return new ByteArrayInputStream(baos.toByteArray());
        }
    }

    @Test
    @DisplayName("Extraer alimentacion excel valido")
    void extraerAlimentacion_excelValido() throws IOException {
        String[] columnas = {
                "CODIGO COMPETENCIA",
                "DENOMINACION COMPETENCIA",
                "TIPO COMPETENCIA",
                "DESCRIPCION RESULTADO DE APRENDIZAJE (RAP)",
                "TRIMESTRE",
                "HORAS PRESENCIALES"
        };
        String[][] filas = {
                {"220501001", "Desarrollo de Software", "TECNICA", "Desarrollar componentes de software", "1", "40"}
        };

        InputStream in = crearExcelInputStream(columnas, filas);
        List<CompetenciaRap> resultado = adapter.extraerAlimentacion(in);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("220501001", resultado.get(0).competencia().getCodigo());
        assertEquals("Desarrollo de Software", resultado.get(0).competencia().getNombre());
        assertEquals(TipoCompetencia.TECNICA, resultado.get(0).competencia().getTipoCompetencia());
        assertEquals(1, resultado.get(0).trimestre());
        assertEquals(40, resultado.get(0).raps().get(0).horasPresenciales());

        System.out.println("Excel procesado exitosamente. Competencia: " + resultado.get(0).competencia().getNombre() + 
                           " Codigo: " + resultado.get(0).competencia().getCodigo() + 
                           " Tipo: " + resultado.get(0).competencia().getTipoCompetencia());
    }

    @Test
    @DisplayName("Extraer alimentacion celda denominacion competencia vacia omite fila")
    void extraerAlimentacion_celdaDenominacionCompetenciaVacia_omiteFila() throws IOException {
        String[] columnas = {
                "CODIGO COMPETENCIA",
                "DENOMINACION COMPETENCIA",
                "TIPO COMPETENCIA",
                "DESCRIPCION RESULTADO DE APRENDIZAJE (RAP)",
                "TRIMESTRE",
                "HORAS PRESENCIALES"
        };
        String[][] filas = {
                {"220501001", "", "TECNICA", "Desarrollar componentes de software", "1", "40"}
        };

        InputStream in = crearExcelInputStream(columnas, filas);
        List<CompetenciaRap> resultado = adapter.extraerAlimentacion(in);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        System.out.println("Se omitio la fila correctamente debido a que la denominacion de la competencia estaba vacia");
    }

    @Test
    @DisplayName("Extraer alimentacion tipo competencia vacio lanza excepcion")
    void extraerAlimentacion_tipoCompetenciaVacio_lanzaExcepcion() throws IOException {
        String[] columnas = {
                "CODIGO COMPETENCIA",
                "DENOMINACION COMPETENCIA",
                "TIPO COMPETENCIA",
                "DESCRIPCION RESULTADO DE APRENDIZAJE (RAP)",
                "TRIMESTRE",
                "HORAS PRESENCIALES"
        };
        String[][] filas = {
                {"220501001", "Desarrollo de Software", "", "Desarrollar componentes de software", "1", "40"}
        };

        InputStream in = crearExcelInputStream(columnas, filas);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> adapter.extraerAlimentacion(in));
        assertTrue(ex.getMessage().contains("no puede estar vacío"));

        System.out.println("Excepcion capturada correctamente por tipo de competencia vacio: " + ex.getMessage());
    }

    @Test
    @DisplayName("Extraer alimentacion tipo competencia invalido lanza excepcion")
    void extraerAlimentacion_tipoCompetenciaInvalido_lanzaExcepcion() throws IOException {
        String[] columnas = {
                "CODIGO COMPETENCIA",
                "DENOMINACION COMPETENCIA",
                "TIPO COMPETENCIA",
                "DESCRIPCION RESULTADO DE APRENDIZAJE (RAP)",
                "TRIMESTRE",
                "HORAS PRESENCIALES"
        };
        String[][] filas = {
                {"220501001", "Desarrollo de Software", "INVALIDO", "Desarrollar componentes", "1", "40"}
        };

        InputStream in = crearExcelInputStream(columnas, filas);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> adapter.extraerAlimentacion(in));
        assertTrue(ex.getMessage().contains("Tipo de competencia inválido"));

        System.out.println("Excepcion capturada correctamente por tipo de competencia invalido: " + ex.getMessage());
    }

    @Test
    @DisplayName("Extraer alimentacion horas presenciales no numericas asigna cero default")
    void extraerAlimentacion_horasPresencialesNoNumericas_asignaCeroDefault() throws IOException {
        String[] columnas = {
                "CODIGO COMPETENCIA",
                "DENOMINACION COMPETENCIA",
                "TIPO COMPETENCIA",
                "DESCRIPCION RESULTADO DE APRENDIZAJE (RAP)",
                "TRIMESTRE",
                "HORAS PRESENCIALES"
        };
        String[][] filas = {
                {"220501001", "Desarrollo de Software", "TRANSVERSAL", "Promover la cultura fisica", "1", "cuarenta"}
        };

        InputStream in = crearExcelInputStream(columnas, filas);
        List<CompetenciaRap> resultado = adapter.extraerAlimentacion(in);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(0, resultado.get(0).raps().get(0).horasPresenciales());

        System.out.println("Horas presenciales asignadas por defecto en 0 debido a texto invalido: " + resultado.get(0).raps().get(0).horasPresenciales());
    }

    @Test
    @DisplayName("Extraer alimentacion excel sin encabezados lanza excepcion")
    void extraerAlimentacion_excelSinEncabezados_lanzaExcepcion() throws IOException {
        InputStream in = crearExcelInputStream(null, null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> adapter.extraerAlimentacion(in));
        assertTrue(ex.getMessage().contains("El archivo Excel está vacío o no tiene encabezados"));

        System.out.println("Excepcion capturada por archivo excel vacio: " + ex.getMessage());
    }

    @Test
    @DisplayName("Extraer alimentacion falta columna requerida lanza excepcion")
    void extraerAlimentacion_faltaColumnaRequerida_lanzaExcepcion() throws IOException {
        String[] columnas = {
                "CODIGO COMPETENCIA",
                "DENOMINACION COMPETENCIA"
        };
        String[][] filas = {
                {"220501001", "Desarrollo de Software"}
        };

        InputStream in = crearExcelInputStream(columnas, filas);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> adapter.extraerAlimentacion(in));
        assertTrue(ex.getMessage().contains("Columna requerida no encontrada"));

        System.out.println("Excepcion capturada por falta de columna requerida: " + ex.getMessage());
    }
}
