package com.caeproject.cae.application.usecases.disenocurricular;

import com.caeproject.cae.domain.ports.model.DiseñoCurricular;
import com.caeproject.cae.domain.ports.out.DiseñoCurricularRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class DiseñoCurricularUseCasesTest {

    @Mock
    private DiseñoCurricularRepository diseñoCurricularRepository;

    @InjectMocks
    private LIstarDiseñoCurricularUseCase listarDiseñoCurricularUseCase;

    @InjectMocks
    private EliminarDiseñoCurricularUseCase eliminarDiseñoCurricularUseCase;

    @Test
    @DisplayName("Listar diseno curricular")
    void listar_diseno_curricular() {
        List<DiseñoCurricular> lista = new ArrayList<>();

        DiseñoCurricular d1 = new DiseñoCurricular();
        d1.setId(1L);
        d1.setProgramaId(10L);
        d1.setRapId(100L);
        d1.setNumeroTrimestre(1);
        d1.setHoraspresenciales(40);

        DiseñoCurricular d2 = new DiseñoCurricular();
        d2.setId(2L);
        d2.setProgramaId(10L);
        d2.setRapId(101L);
        d2.setNumeroTrimestre(2);
        d2.setHoraspresenciales(60);

        lista.add(d1);
        lista.add(d2);

        given(diseñoCurricularRepository.findAll()).willReturn(lista);

        List<DiseñoCurricular> resultado = listarDiseñoCurricularUseCase.listarDiseñoCurricular();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        resultado.forEach(d -> System.out.println("Diseno curricular listado: id " + d.getId() + " programaId " + d.getProgramaId() + " rapId " + d.getRapId() + " horas " + d.getHoraspresenciales()));
        then(diseñoCurricularRepository).should().findAll();
    }

    @Test
    @DisplayName("Listar diseno curricular vacio")
    void listar_diseno_curricular_vacio() {
        List<DiseñoCurricular> listaVacia = new ArrayList<>();

        given(diseñoCurricularRepository.findAll()).willReturn(listaVacia);

        List<DiseñoCurricular> resultado = listarDiseñoCurricularUseCase.listarDiseñoCurricular();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        System.out.println("Se lista diseno curricular vacio correctamente: " + resultado);
        then(diseñoCurricularRepository).should().findAll();
    }

    @Test
    @DisplayName("Intenta eliminar diseno curricular no encontrado")
    void intento_eliminar_diseno_curricular_noEncontrado() {
        given(diseñoCurricularRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> eliminarDiseñoCurricularUseCase.eliminarDiseñoCurricular(1L));

        then(diseñoCurricularRepository).should(never()).eliminarDiseñoCurricular(any());
        System.out.println("Se lanza RuntimeException correctamente al intentar eliminar diseno curricular inexistente");
    }

    @Test
    @DisplayName("Eliminar diseno curricular")
    void eliminar_diseno_curricular() {
        DiseñoCurricular d = new DiseñoCurricular();
        d.setId(1L);
        d.setProgramaId(10L);
        d.setRapId(100L);
        d.setNumeroTrimestre(1);
        d.setHoraspresenciales(40);

        given(diseñoCurricularRepository.findById(1L)).willReturn(Optional.of(d));

        eliminarDiseñoCurricularUseCase.eliminarDiseñoCurricular(1L);

        System.out.println("Diseno curricular eliminado correctamente id " + d.getId());
        then(diseñoCurricularRepository).should().eliminarDiseñoCurricular(1L);
    }
}
