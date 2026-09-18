package com.caeproject.cae.infraestructure.adapter.out.excel.exportacionExcel;

import com.caeproject.cae.domain.ports.exceptions.usuarioexceptions.UsuarioNoEncontradoException;
import com.caeproject.cae.domain.ports.model.*;
import com.caeproject.cae.domain.ports.out.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ExportadorInstructorTrimestreAdapter implements ExportadorInstructorTrimestreRepository {

    private static final Logger log = LoggerFactory.getLogger(ExportadorInstructorTrimestreAdapter.class);

    private final ProgramacionAcademicaRepository programacionAcademicaRepository;
    private final PerfilBaseRepository perfilBaseRepository;
    private final InstructorEspecialidadRepository instructorEspecialidadRepository;
    private final TrimestreRepository trimestreRepository;
    private final DisponibilidadInstructorRepository disponibilidadInstructorRepository;
    private final FichaRepository fichaRepository;
    private final ProgramaRepository programaRepository;
    private final CompetenciaRepository competenciaRepository;
    private final RapRepository rapRepository;
    private final DiseñoCurricularRepository disenoCurricularRepository; // 👈 Sin la 'ñ' para Sonar
    private final ExcelEstilosHelper estilosHelper;
    private final ExcelImagenHelper imagenHelper;

    public ExportadorInstructorTrimestreAdapter(ProgramacionAcademicaRepository programacionAcademicaRepository,
                                                PerfilBaseRepository perfilBaseRepository,
                                                InstructorEspecialidadRepository instructorEspecialidadRepository,
                                                TrimestreRepository trimestreRepository,
                                                DisponibilidadInstructorRepository disponibilidadInstructorRepository,
                                                FichaRepository fichaRepository,
                                                ProgramaRepository programaRepository,
                                                CompetenciaRepository competenciaRepository,
                                                RapRepository rapRepository,
                                                DiseñoCurricularRepository disenoCurricularRepository,
                                                ExcelEstilosHelper estilosHelper,
                                                ExcelImagenHelper imagenHelper) {
        this.programacionAcademicaRepository = programacionAcademicaRepository;
        this.perfilBaseRepository = perfilBaseRepository;
        this.instructorEspecialidadRepository = instructorEspecialidadRepository;
        this.trimestreRepository = trimestreRepository;
        this.disponibilidadInstructorRepository = disponibilidadInstructorRepository;
        this.fichaRepository = fichaRepository;
        this.programaRepository = programaRepository;
        this.competenciaRepository = competenciaRepository;
        this.rapRepository = rapRepository;
        this.disenoCurricularRepository = disenoCurricularRepository;
        this.estilosHelper = estilosHelper;
        this.imagenHelper = imagenHelper;
    }

    @Override
    public List<DatosExportacion> obtenerDatosExportacion(Long usuarioId, Long trimestreId) {

        PerfilBase perfilBase = perfilBaseRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException(usuarioId));
        String nombreCompletoInstructor = perfilBase.getNombre() + " " + perfilBase.getApellido();

        InstructorEspecialidad especialidad = instructorEspecialidadRepository.findByInstructorId(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException(usuarioId));

        DisponibilidadInstructor disponibilidad = disponibilidadInstructorRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException(usuarioId));

        Trimestre trimestre = trimestreRepository.findById(trimestreId)
                .orElseThrow(() -> new RuntimeException("TRIMESTRE NO ENCONTRADO"));

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet hoja = workbook.createSheet("RESUMEN GENERAL"); // 👈 Sin cast innecesario

        XSSFCellStyle estilocabeceraInit = estilosHelper.crearEstiloRojoTabla(workbook);

        // 👈 Encabezado extraído a método privado
        escribirEncabezadoResumen(hoja, workbook, perfilBase, especialidad, disponibilidad, trimestre, nombreCompletoInstructor);

        List<DatosExportacion> datos = cargarDatosExportacion(usuarioId, trimestreId, trimestre);

        int filaInicial = 12;
        escribirTablaInfoGeneral(hoja, filaInicial, datos, estilocabeceraInit);

        generarPestañasFichas(workbook, datos, estilocabeceraInit);

        ajustarAnchoColumnas(hoja);

        guardarArchivoExcel(workbook, nombreCompletoInstructor);

        return datos;
    }

    @Override
    public List<DatosExportacion> obtenerDatosExportacionPorFicha(Long fichaId, Long trimestreId) {
        Trimestre trimestre = trimestreRepository.findById(trimestreId)
                .orElseThrow(() -> new RuntimeException("TRIMESTRE NO ENCONTRADO"));
        List<ProgramacionAcademica> programaciones = programacionAcademicaRepository.findByFichaIdAndTrimestreId(fichaId, trimestreId);
        List<DatosExportacion> datos = new ArrayList<>();

        for (ProgramacionAcademica pa : programaciones) {
            Ficha ficha = (pa.getFichaId() != null) ? fichaRepository.findById(pa.getFichaId()).orElse(null) : null;
            Programa programa = (ficha != null && ficha.getProgramaId() != null) ? programaRepository.findById(ficha.getProgramaId()).orElse(null) : null;
            Rap rap = (pa.getRapId() != null) ? rapRepository.findById(pa.getRapId()).orElse(null) : null;
            Competencia competencia = (rap != null && rap.getCompetenciaId() != null) ? competenciaRepository.findById(rap.getCompetenciaId()).orElse(null) : null;

            Integer horas = 40;
            if (programa != null && pa.getRapId() != null) {
                DiseñoCurricular dc = disenoCurricularRepository.findByProgramaIdAndRapId(programa.getId(), pa.getRapId()).orElse(null);
                if (dc != null && dc.getHoraspresenciales() != null) {
                    horas = dc.getHoraspresenciales();
                }
            }
            datos.add(new DatosExportacion(ficha, programa, competencia, rap, trimestre, pa, horas));
        }
        return datos;
    }

    private void escribirEncabezadoResumen(XSSFSheet hoja, XSSFWorkbook workbook, PerfilBase perfilBase,
                                           InstructorEspecialidad especialidad, DisponibilidadInstructor disponibilidad,
                                           Trimestre trimestre, String nombreCompletoInstructor) {
        XSSFCellStyle estiloVerdeFila = estilosHelper.crearEstiloVerdeCabecera(workbook);
        XSSFCellStyle estiloTitulo = estilosHelper.crearEstiloTitulo(workbook);

        for (int i = 0; i <= 2; i++) {
            XSSFRow fila = hoja.createRow(i);
            for (int c = 1; c <= 2; c++) fila.createCell(c);
        }

        imagenHelper.insertarImagen(hoja, workbook, "static/imgs/SenaLogo.png", 0, 1, 0, 3);
        imagenHelper.insertarImagen(hoja, workbook, "static/imgs/Logo.png", 3, 4, 0, 3);

        hoja.addMergedRegion(new CellRangeAddress(0, 2, 1, 2));
        hoja.getRow(0).getCell(1).setCellValue("RESUMEN GENERAL TRIMESTRE");

        for (int r = 0; r <= 2; r++) {
            for (int c = 1; c <= 2; c++) {
                hoja.getRow(r).getCell(c).setCellStyle(estiloTitulo);
            }
        }

        // Datos instructor
        XSSFRow fila6 = hoja.createRow(5);
        fila6.createCell(0).setCellValue("NOMBRE INSTRUCTOR");
        fila6.createCell(1).setCellValue("NUMERO DOCUMENTO");
        fila6.createCell(2).setCellValue("ESPECIALIDAD");
        fila6.createCell(3).setCellValue("TIPOCONTRATO");
        fila6.setHeightInPoints(25);
        estilosHelper.aplicarEstiloAFila(fila6, 0, 4, estiloVerdeFila);

        XSSFRow fila7 = hoja.createRow(6);
        fila7.createCell(0).setCellValue(nombreCompletoInstructor);
        fila7.createCell(1).setCellValue(perfilBase.getCc() != null ? perfilBase.getCc().toString() : "N/A");
        fila7.createCell(2).setCellValue(especialidad.getEspecialidadId().toString());
        fila7.createCell(3).setCellValue(perfilBase.getTipoContrato().toString());

        // Trimestre
        XSSFRow fila8 = hoja.createRow(7);
        fila8.createCell(0).setCellValue("TRIMESTRE");
        fila8.createCell(1).setCellValue("Fecha inicio");
        fila8.createCell(2).setCellValue("Fecha fin");
        fila8.setHeightInPoints(25);
        estilosHelper.aplicarEstiloAFila(fila8, 0, 4, estiloVerdeFila);

        XSSFRow fila9 = hoja.createRow(8);
        fila9.createCell(0).setCellValue(trimestre.getId().toString());
        fila9.createCell(1).setCellValue(trimestre.getFechaInicio() != null ? trimestre.getFechaInicio().toString() : "N/A");
        fila9.createCell(2).setCellValue(trimestre.getFechaFin() != null ? trimestre.getFechaFin().toString() : "N/A");

        // Horas
        XSSFRow fila10 = hoja.createRow(9);
        fila10.createCell(0).setCellValue("HORAS TOTALES SEGUN CONTRATO");
        fila10.createCell(1).setCellValue("HORAS ASIGNADAS");
        fila10.setHeightInPoints(25);
        estilosHelper.aplicarEstiloAFila(fila10, 0, 4, estiloVerdeFila);

        XSSFRow fila11 = hoja.createRow(10);
        fila11.createCell(0).setCellValue(disponibilidad.getHorasMaximas() != null ? disponibilidad.getHorasMaximas().toString() : "0");
        fila11.createCell(1).setCellValue(disponibilidad.getHorasAsignadas() != null ? disponibilidad.getHorasAsignadas().toString() : "0");
    }

    private List<DatosExportacion> cargarDatosExportacion(Long usuarioId, Long trimestreId, Trimestre trimestre) {
        List<ProgramacionAcademica> programaciones = programacionAcademicaRepository.findByUserIdAndTrimestreId(usuarioId, trimestreId);
        List<DatosExportacion> datos = new ArrayList<>();

        for (ProgramacionAcademica pa : programaciones) {
            Ficha ficha = (pa.getFichaId() != null) ? fichaRepository.findById(pa.getFichaId()).orElse(null) : null;
            Programa programa = (ficha != null && ficha.getProgramaId() != null) ? programaRepository.findById(ficha.getProgramaId()).orElse(null) : null;
            Rap rap = (pa.getRapId() != null) ? rapRepository.findById(pa.getRapId()).orElse(null) : null;
            Competencia competencia = (rap != null && rap.getCompetenciaId() != null) ? competenciaRepository.findById(rap.getCompetenciaId()).orElse(null) : null;

            Integer horas = 40;
            if (programa != null && pa.getRapId() != null) {
                DiseñoCurricular dc = disenoCurricularRepository.findByProgramaIdAndRapId(programa.getId(), pa.getRapId()).orElse(null);
                if (dc != null && dc.getHoraspresenciales() != null) {
                    horas = dc.getHoraspresenciales();
                }
            }
            datos.add(new DatosExportacion(ficha, programa, competencia, rap, trimestre, pa, horas));
        }
        return datos;
    }

    private void generarPestañasFichas(XSSFWorkbook workbook, List<DatosExportacion> datos, XSSFCellStyle estilocabeceraInit) {
        Map<String, List<DatosExportacion>> datosPorFicha = datos.stream()
                .filter(d -> d.ficha() != null)
                .collect(Collectors.groupingBy(d -> d.ficha().getCodigoFicha()));

        for (Map.Entry<String, List<DatosExportacion>> entry : datosPorFicha.entrySet()) {
            String nombrePestana = "FICHA " + entry.getKey();
            if (nombrePestana.length() > 31) nombrePestana = nombrePestana.substring(0, 31);

            XSSFSheet hojaficha = workbook.createSheet(nombrePestana);
            escribirTablaInfoGeneral(hojaficha, 0, entry.getValue(), estilocabeceraInit);
            ajustarAnchoColumnas(hojaficha);
        }
    }

    private void ajustarAnchoColumnas(XSSFSheet hoja) {
        for (int i = 0; i <= 4; i++) {
            hoja.autoSizeColumn(i);
            hoja.setColumnWidth(i, hoja.getColumnWidth(i) + 1024);
        }
    }

    private void guardarArchivoExcel(XSSFWorkbook workbook, String nombreCompletoInstructor) {
        Path rutaDownloads = Paths.get(System.getProperty("user.home"), "Downloads", "RESUMEN_GENERAL - " + nombreCompletoInstructor + ".xlsx");
        try (FileOutputStream fileout = new FileOutputStream(rutaDownloads.toFile())) {
            workbook.write(fileout);
            log.info("Excel generado exitosamente en: {}", rutaDownloads.toAbsolutePath());
        } catch (Exception e) {
            log.error("Error al guardar archivo local Excel", e);
        }
    }

    private void escribirTablaInfoGeneral(XSSFSheet hoja, int filaInicial, List<DatosExportacion> datos, XSSFCellStyle estilocabecera) {
        XSSFRow rowheader = hoja.createRow(filaInicial);
        rowheader.setHeightInPoints(26);
        String[] columnas = {"FICHA", "PROGRAMA", "JORNADA", "HORAS", "RAPS"};

        for (int i = 0; i < columnas.length; i++) {
            XSSFCell cell = rowheader.createCell(i);
            cell.setCellValue(columnas[i]);
            if (estilocabecera != null) cell.setCellStyle(estilocabecera);
        }

        int numeroFila = filaInicial + 1;
        for (DatosExportacion items : datos) {
            XSSFRow row = hoja.createRow(numeroFila++);
            row.setHeightInPoints(20);

            String fichaStr = (items.ficha() != null) ? items.ficha().getCodigoFicha() : "N/A";
            String jornadaStr = (items.programa() != null && items.programa().getJornada() != null) ? String.valueOf(items.programa().getJornada()) : "N/A";
            String horasString = (items.horasPresenciales() != null) ? String.valueOf(items.horasPresenciales()) : "N/A";

            row.createCell(0).setCellValue(fichaStr);
            row.createCell(1).setCellValue(items.programa() != null ? items.programa().getNombre() : "N/A");
            row.createCell(2).setCellValue(jornadaStr);
            row.createCell(3).setCellValue(horasString);
            row.createCell(4).setCellValue(items.rap() != null && items.rap().getDescripcion() != null ? items.rap().getDescripcion() : "N/A");
        }
    }
}