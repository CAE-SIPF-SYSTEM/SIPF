package com.caeproject.cae.infraestructure.adapter.out.excel.exportacionExcel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Component;
@Component
public class ExcelEstilosHelper {
    public XSSFCellStyle crearEstiloTitulo(XSSFWorkbook workbook) {
        XSSFCellStyle estilo = workbook.createCellStyle();
        byte[] rgbVerdeOscuro = new byte[]{(byte) 27, (byte) 94, (byte) 32};
        estilo.setFillForegroundColor(new XSSFColor(rgbVerdeOscuro, null));
        estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        estilo.setAlignment(HorizontalAlignment.CENTER);
        estilo.setVerticalAlignment(VerticalAlignment.CENTER);
        XSSFFont fuente = workbook.createFont();
        fuente.setBold(true);
        fuente.setColor(new XSSFColor(new byte[]{(byte) 255, (byte) 255, (byte) 255}, null));
        fuente.setFontHeightInPoints((short) 14);
        estilo.setFont(fuente);
        return estilo;
    }
    public XSSFCellStyle crearEstiloVerdeCabecera(XSSFWorkbook workbook) {
        XSSFCellStyle estilo = workbook.createCellStyle();
        estilo.setFillForegroundColor(new XSSFColor(new byte[]{(byte) 0, (byte) 204, (byte) 0}, null));
        estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        estilo.setBorderTop(BorderStyle.THIN);
        estilo.setBorderRight(BorderStyle.THIN);
        estilo.setBorderLeft(BorderStyle.THIN);
        estilo.setBorderBottom(BorderStyle.THIN);
        XSSFFont fuente = workbook.createFont();
        fuente.setBold(true);
        fuente.setColor(new XSSFColor(new byte[]{(byte) 255, (byte) 255, (byte) 255}, null));
        estilo.setFont(fuente);
        return estilo;
    }
    public XSSFCellStyle crearEstiloRojoTabla(XSSFWorkbook workbook) {
        XSSFCellStyle estilo = workbook.createCellStyle();
        byte[] rgbRojo = new byte[]{(byte) 204, (byte) 0, (byte) 0};
        estilo.setFillForegroundColor(new XSSFColor(rgbRojo, null));
        estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        estilo.setAlignment(HorizontalAlignment.CENTER);
        estilo.setVerticalAlignment(VerticalAlignment.CENTER);
        estilo.setBorderBottom(BorderStyle.MEDIUM);
        estilo.setBorderTop(BorderStyle.MEDIUM);
        estilo.setBorderLeft(BorderStyle.MEDIUM);
        estilo.setBorderRight(BorderStyle.MEDIUM);
        XSSFFont fuente = workbook.createFont();
        fuente.setBold(true);
        fuente.setColor(new XSSFColor(new byte[]{(byte) 255, (byte) 255, (byte) 255}, null));
        estilo.setFont(fuente);
        return estilo;
    }
    public void aplicarEstiloAFila(XSSFRow fila, int colInicio, int colFin, XSSFCellStyle estilo) {
        if (fila == null || estilo == null) return;
        for (int i = colInicio; i <= colFin; i++) {
            XSSFCell cell = fila.getCell(i);
            if (cell == null) {
                cell = fila.createCell(i);
            }
            cell.setCellStyle(estilo);
        }
    }
}
