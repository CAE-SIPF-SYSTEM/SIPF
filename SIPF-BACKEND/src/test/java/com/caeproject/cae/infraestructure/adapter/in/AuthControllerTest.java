package com.caeproject.cae.infraestructure.adapter.in;

import com.caeproject.cae.domain.ports.in.recuperacion.RestablecerContrasenaInputPort;
import com.caeproject.cae.domain.ports.in.recuperacion.SolicitarRecuperacionInputPort;
import com.caeproject.cae.domain.ports.in.usuario.LoginInputPort;
import com.caeproject.cae.domain.ports.model.PerfilBase;
import com.caeproject.cae.domain.ports.model.Usuario;
import com.caeproject.cae.domain.ports.model.enums.Rol;
import com.caeproject.cae.infraestructure.dtos.usuario.UsuarioLoginRequest;
import com.caeproject.cae.infraestructure.security.JwtUtil;
import com.caeproject.cae.domain.ports.out.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private LoginInputPort loginInputPort;

    @MockBean
    private SolicitarRecuperacionInputPort solicitarRecuperacionInputPort;

    @MockBean
    private RestablecerContrasenaInputPort restablecerContrasenaInputPort;

    @Test
    @DisplayName("POST /api/auth/login con credenciales validas retorna 200 y JWT Token")
    void login_credencialesValidas_retorna200YToken() throws Exception {
        // Arrange
        UsuarioLoginRequest request = new UsuarioLoginRequest();
        request.setCorreo("admin@sena.edu.co");
        request.setContrasena("password123");

        PerfilBase perfil = new PerfilBase();
        perfil.setNombre("Carlos");
        perfil.setApellido("Perez");

        Usuario usuarioValidado = new Usuario();
        usuarioValidado.setId(1L);
        usuarioValidado.setCorreo("admin@sena.edu.co");
        usuarioValidado.setRol(Rol.ADMINISTRADOR);
        usuarioValidado.setJwtToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...");
        usuarioValidado.setPerfilBase(perfil);

        given(loginInputPort.loginSistema(anyString(), anyString())).willReturn(usuarioValidado);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."))
                .andExpect(jsonPath("$.correo").value("admin@sena.edu.co"))
                .andExpect(jsonPath("$.rol").value("ADMINISTRADOR"))
                .andExpect(jsonPath("$.nombre").value("Carlos Perez"));
    }
}
