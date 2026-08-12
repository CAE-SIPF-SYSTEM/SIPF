package com.caeproject.cae.infraestructure.adapter.out.excel.exportacionExcel;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.io.InputStream;
@Component
public class ExcelImagenHelper {
    private static final Logger log = LoggerFactory.getLogger(ExcelImagenHelper.class);
    public void insertarImagen(XSSFSheet hoja, XSSFWorkbook workbook, String rutaImagenResource, int col1, int col2, int row1, int row2) {
        try {
            InputStream imagenStream = getClass().getClassLoader().getResourceAsStream(rutaImagenResource);
            if (imagenStream == null) {
                log.warn("Imagen no encontrada en la ruta: {}", rutaImagenResource);
                return;
            }
            byte[] byteImages = IOUtils.toByteArray(imagenStream);
            int imgIdx = workbook.addPicture(byteImages, Workbook.PICTURE_TYPE_PNG);
            imagenStream.close();
            Drawing<?> drawing = hoja.createDrawingPatriarch();
            ClientAnchor anchor = workbook.getCreationHelper().createClientAnchor();
            anchor.setCol1(col1);
            anchor.setCol2(col2);
            anchor.setRow1(row1);
            anchor.setRow2(row2);
            drawing.createPicture(anchor, imgIdx);
        } catch (Exception e) {
            log.error("Error al insertar la imagen: {}", rutaImagenResource, e);
        }
    }
}