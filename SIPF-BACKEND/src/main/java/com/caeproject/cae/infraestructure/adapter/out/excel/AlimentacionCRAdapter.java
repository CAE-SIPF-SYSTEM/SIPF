package com.caeproject.cae.infraestructure.adapter.out.excel;

import com.caeproject.cae.domain.ports.model.competencia.Competencia;
import com.caeproject.cae.domain.ports.model.enums.TipoCompetencia;
import com.caeproject.cae.domain.ports.model.rap.Rap;
import com.caeproject.cae.domain.ports.out.AlimentacionCRRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class AlimentacionCRAdapter implements AlimentacionCRRepository {

    private static final Logger log = LoggerFactory.getLogger(AlimentacionCRAdapter.class);

    private static final String[] COLUMNAS_REQUERIDAS = {
            "CODIGO COMPETENCIA",
            "DENOMINACION COMPETENCIA",
            "TIPO COMPETENCIA",
            "CODIGO RAP",
            "DESCRIPCION RESULTADO DE APRENDIZAJE (RAP)",
            "INTENSIDAD HORARIA",
            "ESTADO"
    };

    @Override
    public List<CompetenciaRap> extraerAlimentacion(InputStream alimentacionExcel) {
        List<CompetenciaRap> datos = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(alimentacionExcel)) {
            Sheet sheet = workbook.getSheetAt(0);
            Row rowHeader = sheet.getRow(0);

            if (rowHeader == null) {
                throw new IllegalArgumentException("El archivo Excel está vacío o no tiene encabezados.");
            }

            Map<String, Integer> columnas = mapearColumnas(rowHeader);
            validarColumnas(columnas);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                CompetenciaRap filaProcesada = procesarFila(row, columnas, i);
                if (filaProcesada != null) {
                    datos.add(filaProcesada);
                }
            }

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("Error al procesar el Excel: " + e.getMessage(), e);
        }

        return datos;
    }

    private Map<String, Integer> mapearColumnas(Row rowHeader) {
        Map<String, Integer> columnas = new HashMap<>();
        for (Cell cell : rowHeader) {
            String nombreColumna = obtenerValorCelda(cell);
            if (nombreColumna != null && !nombreColumna.trim().isEmpty()) {
                columnas.put(nombreColumna.trim().toUpperCase(), cell.getColumnIndex());
            }
        }
        log.info("NUMERO DE CELDAS EN EXCEL: {}", columnas.keySet());
        return columnas;
    }

    private void validarColumnas(Map<String, Integer> columnas) {
        for (String col : COLUMNAS_REQUERIDAS) {
            if (!columnas.containsKey(col)) {
                throw new IllegalArgumentException("Columna requerida no encontrada en el Excel: " + col);
            }
        }
    }

    private CompetenciaRap procesarFila(Row row, Map<String, Integer> columnas, int i) {
        if (row == null) {
            return null;
        }

        String codigoCompetencia = obtenerValorCelda(row.getCell(columnas.get("CODIGO COMPETENCIA")));
        if (codigoCompetencia == null || codigoCompetencia.isEmpty()) {
            log.warn(">>> Fila {} saltada: Código competencia está vacío", i);
            return null;
        }

        Competencia competencia = construirCompetencia(row, columnas, codigoCompetencia, i);
        Rap rap = construirRap(row, columnas, i);

        log.info(">>> Fila {} leída: Competencia {} [{}], RAP {} ({}h)", i, 
                 codigoCompetencia, competencia.getTipoCompetencia(), 
                 rap.getId(), rap.getHorasPresenciales());

        return new CompetenciaRap(competencia, List.of(rap));
    }

    private Competencia construirCompetencia(Row row, Map<String, Integer> columnas, String codigoCompetencia, int i) {
        String nombreCompetencia = obtenerValorCelda(row.getCell(columnas.get("DENOMINACION COMPETENCIA")));
        String tipoCompetenciaStr = obtenerValorCelda(row.getCell(columnas.get("TIPO COMPETENCIA")));

        Competencia competencia = new Competencia();
        competencia.setCodigo(codigoCompetencia);
        competencia.setNombre(nombreCompetencia);
        
        if (tipoCompetenciaStr != null && !tipoCompetenciaStr.isEmpty()) {
            String clean = tipoCompetenciaStr.trim()
                    .toUpperCase()
                    .replace("Á", "A")
                    .replace("É", "E")
                    .replace("Í", "I")
                    .replace("Ó", "O")
                    .replace("Ú", "U");
            try {
                competencia.setTipoCompetencia(TipoCompetencia.valueOf(clean));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Tipo de competencia inválido en la fila " + i + ": '" + tipoCompetenciaStr + "'. Debe ser 'Técnica' o 'Transversal'.");
            }
        } else {
            throw new IllegalArgumentException("El tipo de competencia en la fila " + i + " no puede estar vacío.");
        }
        return competencia;
    }

    private Rap construirRap(Row row, Map<String, Integer> columnas, int i) {
        String codigoRap = obtenerValorCelda(row.getCell(columnas.get("CODIGO RAP")));
        String descripcionRap = obtenerValorCelda(row.getCell(columnas.get("DESCRIPCION RESULTADO DE APRENDIZAJE (RAP)")));
        String horasRapStr = obtenerValorCelda(row.getCell(columnas.get("INTENSIDAD HORARIA")));
        String estado = obtenerValorCelda(row.getCell(columnas.get("ESTADO")));

        Rap rap = new Rap();
        if (codigoRap != null && !codigoRap.isEmpty()) {
            rap.setId(parseToLong(codigoRap));
        }
        rap.setDescripcion(descripcionRap);
        
        if (horasRapStr != null && !horasRapStr.isEmpty()) {
            try {
                rap.setHorasPresenciales(Integer.parseInt(horasRapStr));
            } catch (NumberFormatException e) {
                log.warn("Horas presenciales inválidas en fila {}: {}", i, horasRapStr);
            }
        }
        rap.setEstado(Boolean.parseBoolean(estado) || "ACTIVO".equalsIgnoreCase(estado));
        return rap;
    }

    private Long parseToLong(String value) {
        if (value == null) return null;
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String obtenerValorCelda(Cell cell) {
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                double valor = cell.getNumericCellValue();
                if (valor == Math.floor(valor) && !Double.isInfinite(valor)) {
                    return String.valueOf((long) valor);
                }
                return String.valueOf(valor);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getStringCellValue().trim();
            case BLANK:
                return "";
            default:
                return "";
        }
    }
}