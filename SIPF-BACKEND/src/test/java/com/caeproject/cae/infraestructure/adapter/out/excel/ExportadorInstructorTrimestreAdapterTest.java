package com.caeproject.cae.infraestructure.adapter.out.excel;

import com.caeproject.cae.domain.ports.model.*;
import com.caeproject.cae.domain.ports.model.enums.Jornada;
import com.caeproject.cae.domain.ports.model.enums.TIpoContrato;
import com.caeproject.cae.domain.ports.out.*;
import com.caeproject.cae.infraestructure.adapter.out.excel.exportacionExcel.ExcelEstilosHelper;
import com.caeproject.cae.infraestructure.adapter.out.excel.exportacionExcel.ExcelImagenHelper;
import com.caeproject.cae.infraestructure.adapter.out.excel.exportacionExcel.ExportadorInstructorTrimestreAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExportadorInstructorTrimestreAdapterTest {

    private ProgramacionAcademicaRepository programacionAcademicaRepository;
    private PerfilBaseRepository perfilBaseRepository;
    private InstructorEspecialidadRepository instructorEspecialidadRepository;
    private TrimestreRepository trimestreRepository;
    private DisponibilidadInstructorRepository disponibilidadInstructorRepository;
    private FichaRepository fichaRepository;
    private ProgramaRepository programaRepository;
    private CompetenciaRepository competenciaRepository;
    private RapRepository rapRepository;
    private DiseñoCurricularRepository diseñoCurricularRepository;

    private ExportadorInstructorTrimestreAdapter adapter;

    @BeforeEach
    void setUp() {
        programacionAcademicaRepository = mock(ProgramacionAcademicaRepository.class);
        perfilBaseRepository = mock(PerfilBaseRepository.class);
        instructorEspecialidadRepository = mock(InstructorEspecialidadRepository.class);
        trimestreRepository = mock(TrimestreRepository.class);
        disponibilidadInstructorRepository = mock(DisponibilidadInstructorRepository.class);
        fichaRepository = mock(FichaRepository.class);
        programaRepository = mock(ProgramaRepository.class);
        competenciaRepository = mock(CompetenciaRepository.class);
        rapRepository = mock(RapRepository.class);
        diseñoCurricularRepository = mock(DiseñoCurricularRepository.class);

        ExcelEstilosHelper estilosHelper = new com.caeproject.cae.infraestructure.adapter.out.excel.exportacionExcel.ExcelEstilosHelper();
        ExcelImagenHelper imagenHelper = new com.caeproject.cae.infraestructure.adapter.out.excel.exportacionExcel.ExcelImagenHelper();

        adapter = new ExportadorInstructorTrimestreAdapter(
                programacionAcademicaRepository,
                perfilBaseRepository,
                instructorEspecialidadRepository,
                trimestreRepository,
                disponibilidadInstructorRepository,
                fichaRepository,
                programaRepository,
                competenciaRepository,
                rapRepository,
                diseñoCurricularRepository,
                estilosHelper,
                imagenHelper
        );
    }

    @Test
    void debeGenerarArchivoExcelYDevolverListaExportacion() {
        // Arrange
        Long usuarioId = 1L;
        Long trimestreId = 2L;

        PerfilBase perfilBase = new PerfilBase();
        perfilBase.setNombre("Jhon");
        perfilBase.setApellido("Prada");
        perfilBase.setCc(12345678L);
        perfilBase.setTipoContrato(TIpoContrato.PLANTA);

        InstructorEspecialidad especialidad = new InstructorEspecialidad();
        especialidad.setUsuarioId(usuarioId);
        especialidad.setEspecialidadId(10L);

        DisponibilidadInstructor disponibilidad = new DisponibilidadInstructor();
        disponibilidad.setUsuarioId(usuarioId);
        disponibilidad.setHorasMaximas(144L);
        disponibilidad.setHorasAsignadas(40L);

        Trimestre trimestre = new Trimestre();
        trimestre.setId(trimestreId);
        trimestre.setFechaInicio(new Date());
        trimestre.setFechaFin(new Date());

        ProgramacionAcademica pa = new ProgramacionAcademica();
        pa.setUsuarioId(usuarioId);
        pa.setTrimestreId(trimestreId);
        pa.setFichaId(100L);
        pa.setRapId(50L);

        Ficha ficha = new Ficha();
        ficha.setId(100L);
        ficha.setCodigoFicha("2996315");
        ficha.setProgramaId(200L);

        Programa programa = new Programa();
        programa.setId(200L);
        programa.setNombre("ADSO");
        programa.setJornada(Jornada.MAÑANA);

        Rap rap = new Rap();
        rap.setId(50L);
        rap.setDescripcion("DESARROLLO DE TESTEOS");
        rap.setCompetenciaId(300L);



        Competencia competencia = new Competencia();
        competencia.setId(300L);
        competencia.setNombre("Desarrollo de Software");

        when(perfilBaseRepository.findById(usuarioId)).thenReturn(Optional.of(perfilBase));
        when(instructorEspecialidadRepository.findByInstructorId(usuarioId)).thenReturn(Optional.of(especialidad));
        when(disponibilidadInstructorRepository.findById(usuarioId)).thenReturn(Optional.of(disponibilidad));
        when(trimestreRepository.findById(trimestreId)).thenReturn(Optional.of(trimestre));
        when(programacionAcademicaRepository.findByUserIdAndTrimestreId(usuarioId, trimestreId)).thenReturn(List.of(pa));
        when(fichaRepository.findById(100L)).thenReturn(Optional.of(ficha));
        when(programaRepository.findById(200L)).thenReturn(Optional.of(programa));
        when(rapRepository.findById(50L)).thenReturn(Optional.of(rap));
        when(competenciaRepository.findById(300L)).thenReturn(Optional.of(competencia));

        // Act
        List<ExportadorInstructorTrimestreRepository.DatosExportacion> resultado = adapter.obtenerDatosExportacion(usuarioId, trimestreId);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("2996315", resultado.get(0).ficha().getCodigoFicha());
        assertEquals("ADSO", resultado.get(0).programa().getNombre());
    }
}
