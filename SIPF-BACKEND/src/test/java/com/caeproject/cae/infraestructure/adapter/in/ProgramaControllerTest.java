package com.caeproject.cae.infraestructure.adapter.in;

import com.caeproject.cae.application.usecases.programa.commands.CrearProgramaCommand;
import com.caeproject.cae.domain.ports.in.programa.*;
import com.caeproject.cae.domain.ports.model.Municipio;
import com.caeproject.cae.domain.ports.model.Programa;
import com.caeproject.cae.domain.ports.model.enums.Jornada;
import com.caeproject.cae.domain.ports.model.enums.NivelFormacion;
import com.caeproject.cae.infraestructure.dtos.programa.CrearProgramaRequest;
import com.caeproject.cae.domain.ports.out.UsuarioRepository;
import com.caeproject.cae.infraestructure.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProgramaController.class)
@AutoConfigureMockMvc(addFilters = false) // Deshabilita la seguridad temporalmente para pruebas unitarias de Controller
class ProgramaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private CrearProgramaInputPort crearProgramaPort;

    @MockBean
    private ListarProgramaInputPort listarProgramaPort;

    @MockBean
    private ObtenerProgramaInputPort obtenerProgramaPort;

    @MockBean
    private EditarProgramaInputPort editarProgramaPort;

    @MockBean
    private EliminarProgramaInputPort eliminarProgramaPort;

    @Test
    @DisplayName("POST /api/programas con request valido retorna 201 Created")
    void crearPrograma_requestValido_retorna201() throws Exception {
        // Arrange
        Municipio municipio = new Municipio();
        municipio.setId(1L);

        CrearProgramaRequest request = new CrearProgramaRequest();
        request.setNombre("TECNICO EN SISTEMAS");
        request.setMunicipio(municipio);
        request.setJornada(Jornada.MAÑANA);
        request.setNivelFormacion(NivelFormacion.TECNICO);
        request.setDuracionpracticas(6);

        Programa programaCreado = new Programa();
        programaCreado.setId(10L);
        programaCreado.setNombre("TECNICO EN SISTEMAS");
        programaCreado.setMunicipio(municipio);
        programaCreado.setJornada(Jornada.MAÑANA);
        programaCreado.setNivelFormacion(NivelFormacion.TECNICO);
        programaCreado.setDuracionpracticas(6);

        given(crearProgramaPort.crearPrograma(any(CrearProgramaCommand.class))).willReturn(programaCreado);

        // Act & Assert
        mockMvc.perform(post("/api/programas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.nombre").value("TECNICO EN SISTEMAS"));
    }

    @Test
    @DisplayName("POST /api/programas sin nombre retorna 400 Bad Request")
    void crearPrograma_sinNombre_retorna400() throws Exception {
        // Arrange
        Municipio municipio = new Municipio();
        municipio.setId(1L);

        CrearProgramaRequest request = new CrearProgramaRequest();
        // nombre es null
        request.setMunicipio(municipio);
        request.setJornada(Jornada.MAÑANA);
        request.setNivelFormacion(NivelFormacion.TECNICO);
        request.setDuracionpracticas(6);

        // Act & Assert
        mockMvc.perform(post("/api/programas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/programas con duracion menor a 1 retorna 400 Bad Request")
    void crearPrograma_duracionInvalida_retorna400() throws Exception {
        // Arrange
        Municipio municipio = new Municipio();
        municipio.setId(1L);

        CrearProgramaRequest request = new CrearProgramaRequest();
        request.setNombre("TECNICO EN SISTEMAS");
        request.setMunicipio(municipio);
        request.setJornada(Jornada.MAÑANA);
        request.setNivelFormacion(NivelFormacion.TECNICO);
        request.setDuracionpracticas(0); // Invalido segun @Min(1)

        // Act & Assert
        mockMvc.perform(post("/api/programas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
