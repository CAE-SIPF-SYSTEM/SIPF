package com.caeproject.cae.application.usecases.excel;

import com.caeproject.cae.domain.ports.model.Competencia;
import com.caeproject.cae.domain.ports.model.DiseñoCurricular;
import com.caeproject.cae.domain.ports.model.Rap;
import com.caeproject.cae.domain.ports.model.enums.TipoCompetencia;
import com.caeproject.cae.domain.ports.out.AlimentacionCRRepository;
import com.caeproject.cae.domain.ports.out.AlimentacionCRRepository.CompetenciaRap;
import com.caeproject.cae.domain.ports.out.AlimentacionCRRepository.RapImport;
import com.caeproject.cae.domain.ports.out.CompetenciaRepository;
import com.caeproject.cae.domain.ports.out.DiseñoCurricularRepository;
import com.caeproject.cae.domain.ports.out.RapRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class AlimentacionCrUseCaseTest {

    @InjectMocks
    private AlimentacionCrUseCase alimentacionCrUseCase;

    @Mock
    private AlimentacionCRRepository alimentacionCRRepository;

    @Mock
    private CompetenciaRepository competenciaRepository;

    @Mock
    private RapRepository rapRepository;

    @Mock
    private DiseñoCurricularRepository disenoCurricularRepository;

    @Test
    @DisplayName("Ejecutar importacion exitosa completa")
    void ejecutar_importacionExitosaCompleta() {
        Long programaId = 1L;
        InputStream dummyStream = new ByteArrayInputStream(new byte[0]);

        Competencia competencia = new Competencia();
        competencia.setCodigo("220501001");
        competencia.setNombre("Desarrollo de Software");
        competencia.setTipoCompetencia(TipoCompetencia.TECNICA);

        Competencia competenciaGuardada = new Competencia();
        competenciaGuardada.setId(10L);
        competenciaGuardada.setCodigo("220501001");
        competenciaGuardada.setNombre("Desarrollo de Software");

        Rap rap = new Rap();
        rap.setDescripcion("Desarrollar componentes");
        RapImport rapImport = new RapImport(rap, 40);

        Rap rapGuardado = new Rap();
        rapGuardado.setId(50L);
        rapGuardado.setDescripcion("Desarrollar componentes");
        rapGuardado.setCompetenciaId(10L);

        CompetenciaRap registro = new CompetenciaRap(competencia, List.of(rapImport), 1);

        given(alimentacionCRRepository.extraerAlimentacion(dummyStream)).willReturn(List.of(registro));
        given(competenciaRepository.findByCodigo("220501001")).willReturn(Optional.empty());
        given(competenciaRepository.saveCompetencia(any(Competencia.class))).willReturn(competenciaGuardada);
        given(rapRepository.findByCompetencia(10L)).willReturn(Collections.emptyList());
        given(rapRepository.saveRap(any(Rap.class))).willReturn(rapGuardado);
        given(disenoCurricularRepository.findByProgramaIdAndRapId(programaId, 50L)).willReturn(Optional.empty());

        alimentacionCrUseCase.ejecutar(dummyStream, programaId);

        then(competenciaRepository).should().saveCompetencia(any(Competencia.class));
        then(rapRepository).should().saveRap(any(Rap.class));
        then(disenoCurricularRepository).should().saveDiseñoCurricular(any(DiseñoCurricular.class));

        System.out.println("Importacion ejecutada con exito. Competencia guardada ID: " + competenciaGuardada.getId() + 
                           " RAP guardado ID: " + rapGuardado.getId() + " Programa ID: " + programaId);
    }

    @Test
    @DisplayName("Ejecutar competencia ya existente reutiliza competencia")
    void ejecutar_competenciaYaExistente_reutilizaCompetencia() {
        Long programaId = 1L;
        InputStream dummyStream = new ByteArrayInputStream(new byte[0]);

        Competencia competencia = new Competencia();
        competencia.setCodigo("220501001");
        competencia.setNombre("Desarrollo de Software");

        Competencia existente = new Competencia();
        existente.setId(10L);
        existente.setCodigo("220501001");

        Rap rap = new Rap();
        rap.setDescripcion("Desarrollar componentes");
        RapImport rapImport = new RapImport(rap, 40);

        Rap rapGuardado = new Rap();
        rapGuardado.setId(50L);

        CompetenciaRap registro = new CompetenciaRap(competencia, List.of(rapImport), 1);

        given(alimentacionCRRepository.extraerAlimentacion(dummyStream)).willReturn(List.of(registro));
        given(competenciaRepository.findByCodigo("220501001")).willReturn(Optional.of(existente));
        given(rapRepository.findByCompetencia(10L)).willReturn(Collections.emptyList());
        given(rapRepository.saveRap(any(Rap.class))).willReturn(rapGuardado);
        given(disenoCurricularRepository.findByProgramaIdAndRapId(programaId, 50L)).willReturn(Optional.empty());

        alimentacionCrUseCase.ejecutar(dummyStream, programaId);

        then(competenciaRepository).should(never()).saveCompetencia(any());
        then(rapRepository).should().saveRap(any(Rap.class));
        then(disenoCurricularRepository).should().saveDiseñoCurricular(any(DiseñoCurricular.class));

        System.out.println("Competencia reutilizada correctamente ID: " + existente.getId() + " sin guardar duplicado en BD.");
    }

    @Test
    @DisplayName("Ejecutar rap ya existente omite guardado rap")
    void ejecutar_rapYaExistente_omiteGuardadoRap() {
        Long programaId = 1L;
        InputStream dummyStream = new ByteArrayInputStream(new byte[0]);

        Competencia competencia = new Competencia();
        competencia.setCodigo("220501001");

        Competencia existente = new Competencia();
        existente.setId(10L);

        Rap rap = new Rap();
        rap.setDescripcion("Desarrollar componentes");
        RapImport rapImport = new RapImport(rap, 40);

        Rap rapExistente = new Rap();
        rapExistente.setId(50L);
        rapExistente.setDescripcion("Desarrollar componentes");
        rapExistente.setCompetenciaId(10L);

        CompetenciaRap registro = new CompetenciaRap(competencia, List.of(rapImport), 1);

        given(alimentacionCRRepository.extraerAlimentacion(dummyStream)).willReturn(List.of(registro));
        given(competenciaRepository.findByCodigo("220501001")).willReturn(Optional.of(existente));
        given(rapRepository.findByCompetencia(10L)).willReturn(List.of(rapExistente));
        given(disenoCurricularRepository.findByProgramaIdAndRapId(programaId, 50L)).willReturn(Optional.empty());

        alimentacionCrUseCase.ejecutar(dummyStream, programaId);

        then(rapRepository).should(never()).saveRap(any());
        then(disenoCurricularRepository).should().saveDiseñoCurricular(any(DiseñoCurricular.class));

        System.out.println("RAP omitido correctamente por ya existir con la descripcion: " + rapExistente.getDescripcion());
    }

    @Test
    @DisplayName("Ejecutar diseno curricular ya existente omite guardado diseno")
    void ejecutar_disenoCurricularYaExistente_omiteGuardadoDiseno() {
        Long programaId = 1L;
        InputStream dummyStream = new ByteArrayInputStream(new byte[0]);

        Competencia competencia = new Competencia();
        competencia.setCodigo("220501001");

        Competencia existente = new Competencia();
        existente.setId(10L);

        Rap rap = new Rap();
        rap.setDescripcion("Desarrollar componentes");
        RapImport rapImport = new RapImport(rap, 40);

        Rap rapExistente = new Rap();
        rapExistente.setId(50L);
        rapExistente.setDescripcion("Desarrollar componentes");

        DiseñoCurricular disenoExistente = new DiseñoCurricular();
        disenoExistente.setId(100L);

        CompetenciaRap registro = new CompetenciaRap(competencia, List.of(rapImport), 1);

        given(alimentacionCRRepository.extraerAlimentacion(dummyStream)).willReturn(List.of(registro));
        given(competenciaRepository.findByCodigo("220501001")).willReturn(Optional.of(existente));
        given(rapRepository.findByCompetencia(10L)).willReturn(List.of(rapExistente));
        given(disenoCurricularRepository.findByProgramaIdAndRapId(programaId, 50L)).willReturn(Optional.of(disenoExistente));

        alimentacionCrUseCase.ejecutar(dummyStream, programaId);

        then(rapRepository).should(never()).saveRap(any());
        then(disenoCurricularRepository).should(never()).saveDiseñoCurricular(any());

        System.out.println("Diseño curricular omitido correctamente por estar ya registrado para Programa: " + programaId + " y RAP: " + rapExistente.getId());
    }
}
