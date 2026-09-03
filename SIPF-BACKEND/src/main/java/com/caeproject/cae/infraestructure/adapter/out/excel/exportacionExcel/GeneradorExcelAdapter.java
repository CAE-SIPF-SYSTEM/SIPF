package com.caeproject.cae.infraestructure.adapter.out.excel.exportacionExcel;

import com.caeproject.cae.domain.ports.out.ExportadorInstructorTrimestreRepository.DatosExportacion;
import com.caeproject.cae.domain.ports.out.GeneradorExcelPort;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class GeneradorExcelAdapter implements GeneradorExcelPort {

    private final ExcelEstilosHelper estilosHelper;
    private final ExcelImagenHelper imagenHelper;

    public GeneradorExcelAdapter(ExcelEstilosHelper estilosHelper, ExcelImagenHelper imagenHelper) {
        this.estilosHelper = estilosHelper;
        this.imagenHelper = imagenHelper;
    }

    @Override
    public byte[] generarReporteInstructor(String nombreInstructor, List<DatosExportacion> datos) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            XSSFSheet sheet = workbook.createSheet("Horario Instructor");
            crearEncabezados(sheet, workbook, "REPORTE ACADÉMICO: " + nombreInstructor);
            escribirDatos(sheet, workbook, datos);
            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar reporte de instructor en Excel", e);
        }
    }

    @Override
    public byte[] generarReporteFicha(String codigoFicha, List<DatosExportacion> datos) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            XSSFSheet sheet = workbook.createSheet("Horario Ficha");
            crearEncabezados(sheet, workbook, "REPORTE DE PROGRAMACIÓN - FICHA: " + codigoFicha);
            escribirDatos(sheet, workbook, datos);
            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar reporte de ficha en Excel", e);
        }
    }

    private void crearEncabezados(XSSFSheet hoja, XSSFWorkbook workbook, String titulo) {
        XSSFCellStyle estiloVerdeFila = estilosHelper.crearEstiloVerdeCabecera(workbook);
        XSSFCellStyle estiloTitulo = estilosHelper.crearEstiloTitulo(workbook);

        for (int i = 0; i <= 2; i++) {
            XSSFRow fila = hoja.createRow(i);
            for (int c = 0; c <= 5; c++) fila.createCell(c);
        }


        imagenHelper.insertarImagen(hoja, workbook, "static/imgs/SenaLogo.png", 0, 1, 0, 3);
        

        imagenHelper.insertarImagen(hoja, workbook, "static/imgs/Logo.png", 5, 6, 0, 3);


        hoja.addMergedRegion(new CellRangeAddress(0, 2, 1, 4));
        hoja.getRow(0).getCell(1).setCellValue(titulo);

        for (int r = 0; r <= 2; r++) {
            for (int c = 1; c <= 4; c++) {
                hoja.getRow(r).getCell(c).setCellStyle(estiloTitulo);
            }
        }
    }

    private void escribirDatos(XSSFSheet hoja, XSSFWorkbook workbook, List<DatosExportacion> datos) {
        XSSFCellStyle estilocabeceraInit = estilosHelper.crearEstiloRojoTabla(workbook);

        int filaInicial = 4;
        XSSFRow rowheader = hoja.createRow(filaInicial);
        rowheader.setHeightInPoints(26);
        String[] columnas = {"FICHA", "PROGRAMA", "COMPETENCIA", "RAP", "TRIMESTRE", "HORAS PRESENCIALES"};

        for (int i = 0; i < columnas.length; i++) {
            XSSFCell cell = rowheader.createCell(i);
            cell.setCellValue(columnas[i]);
            if (estilocabeceraInit != null) cell.setCellStyle(estilocabeceraInit);
        }

        int rowIdx = filaInicial + 1;
        for (DatosExportacion d : datos) {
            XSSFRow row = hoja.createRow(rowIdx++);
            row.setHeightInPoints(20);
            row.createCell(0).setCellValue(d.ficha() != null ? d.ficha().getCodigoFicha() : "N/A");
            row.createCell(1).setCellValue(d.programa() != null ? d.programa().getNombre() : "N/A");
            row.createCell(2).setCellValue(d.competencia() != null ? d.competencia().getNombre() : "N/A");
            row.createCell(3).setCellValue(d.rap() != null ? d.rap().getDescripcion() : "N/A");
            row.createCell(4).setCellValue(d.trimestre() != null ? "T" + d.trimestre().getId() : "N/A");
            row.createCell(5).setCellValue(d.horasPresenciales() != null ? d.horasPresenciales() : 0);
        }

        for (int i = 0; i < columnas.length; i++) {
            hoja.autoSizeColumn(i);
            hoja.setColumnWidth(i, hoja.getColumnWidth(i) + 1024);
        }
    }
}
