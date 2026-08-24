package com.caeproject.cae.application.usecases.programa;

import com.caeproject.cae.application.usecases.programa.commands.CrearProgramaCommand;
import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.ProgramaDuplicadoException;
import com.caeproject.cae.domain.ports.model.Municipio;
import com.caeproject.cae.domain.ports.model.Programa;
import com.caeproject.cae.domain.ports.model.enums.Jornada;
import com.caeproject.cae.domain.ports.model.enums.NivelFormacion;
import com.caeproject.cae.domain.ports.out.ProgramaRepository;
import com.caeproject.cae.domain.ports.out.UbicacionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class CrearProgramaUseCaseTest {

    @Mock
    private ProgramaRepository programaRepository;

    @Mock
    private UbicacionRepository ubicacionRepository;

    @InjectMocks
    private CrearProgramaUseCase crearProgramaUseCase;

    @Test
    @DisplayName("crearPrograma con municipio inexistente lanza RuntimeException")
    void crearPrograma_municipioInexistente_lanzaRuntimeException() {
        // Given (Arrange)
        Municipio municipio = new Municipio();
        municipio.setId(99L);
        
        CrearProgramaCommand command = new CrearProgramaCommand();
        command.setMunicipio(municipio);

        given(ubicacionRepository.obtenerMunicipioPorId(99L)).willReturn(Optional.empty());

        // When (Act) & Then (Assert)
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> crearProgramaUseCase.crearPrograma(command));
            
        assertEquals("El municipio con ID 99 no existe", exception.getMessage());
        
        then(programaRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("crearPrograma con nombre duplicado lanza ProgramaDuplicadoException")
    void crearPrograma_nombreDuplicado_lanzaProgramaDuplicadoException() {
        // Given
        Municipio municipio = new Municipio();
        municipio.setId(1L);
        
        CrearProgramaCommand command = new CrearProgramaCommand();
        command.setNombre("TECNICO EN SISTEMAS");
        command.setMunicipio(municipio);

        given(ubicacionRepository.obtenerMunicipioPorId(1L)).willReturn(Optional.of(municipio));
        given(programaRepository.existByName("TECNICO EN SISTEMAS")).willReturn(true);

        // When & Then
        assertThrows(ProgramaDuplicadoException.class, 
            () -> crearProgramaUseCase.crearPrograma(command));
            
        then(programaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("crearPrograma con datos validos retorna programa guardado")
    void crearPrograma_datosValidos_retornaProgramaGuardado() {
        // Given
        Municipio municipio = new Municipio();
        municipio.setId(1L);
        municipio.setNombre("BOGOTA");
        
        CrearProgramaCommand command = new CrearProgramaCommand();
        command.setNombre("TECNICO EN SISTEMAS");
        command.setMunicipio(municipio);
        command.setNivelFormacion(NivelFormacion.TECNICO);
        command.setJornada(Jornada.MAÑANA);
        command.setDuracionpracticas(6);

        Programa programaEsperado = new Programa();
        programaEsperado.setId(100L);
        programaEsperado.setNombre("TECNICO EN SISTEMAS");

        given(ubicacionRepository.obtenerMunicipioPorId(1L)).willReturn(Optional.of(municipio));
        given(programaRepository.existByName("TECNICO EN SISTEMAS")).willReturn(false);
        given(programaRepository.savePrograma(any(Programa.class))).willReturn(programaEsperado);

        // When
        Programa resultado = crearProgramaUseCase.crearPrograma(command);

        // Then
        assertNotNull(resultado);
        assertEquals(100L, resultado.getId());
        assertEquals("TECNICO EN SISTEMAS", resultado.getNombre());
        
        then(programaRepository).should().savePrograma(any(Programa.class));
    }
}
