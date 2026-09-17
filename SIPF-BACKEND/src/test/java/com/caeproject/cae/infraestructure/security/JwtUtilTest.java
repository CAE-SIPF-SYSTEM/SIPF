package com.caeproject.cae.infraestructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    
    // Clave secreta válida en Base64 de 256 bits para HmacSha256
    private final String secretKeyBase64 = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secretKey", secretKeyBase64);
        ReflectionTestUtils.setField(jwtUtil, "expirationTime", 3600000L); // 1 hora
    }

    @Test
    @DisplayName("Fase 1: Debe generar un token JWT válido y extraer el correo del usuario")
    void generateToken_y_extractUsername_debeRetornarCorreoCorrecto() {
        String token = jwtUtil.generateToken("test@sipf.com");

        assertNotNull(token);
        assertFalse(token.isEmpty());

        String username = jwtUtil.extractUsername(token);
        assertEquals("test@sipf.com", username);
        System.out.println("[FASE 1 PASSED] JWT Token Generado: " + token.substring(0, 20) + "... | Subject Extraído: " + username);
    }

    @Test
    @DisplayName("Fase 1: Debe validar exitosamente un token JWT activo para el usuario correspondiente")
    void isTokenValid_tokenValido_debeRetornarTrue() {
        String token = jwtUtil.generateToken("admin@sipf.com");

        boolean esValido = jwtUtil.isTokenValid(token, "admin@sipf.com");

        assertTrue(esValido);
        System.out.println(" [FASE 1 PASSED] Validación JWT -> Token válido para admin@sipf.com");
    }

    @Test
    @DisplayName("Fase 1: Debe rechazar la validación si el correo no coincide con el token")
    void isTokenValid_correoDiferente_debeRetornarFalse() {
        String token = jwtUtil.generateToken("admin@sipf.com");

        boolean esValido = jwtUtil.isTokenValid(token, "otro@sipf.com");

        assertFalse(esValido);
        System.out.println(" [FASE 1 PASSED] Validación JWT -> Rechazado correctamente para otro@sipf.com");
    }

    @Test
    @DisplayName("Fase 1: Debe incluir y extraer claims personalizados (Role y SessionId)")
    void generateToken_conExtraClaims_debeExtraerClaimsPersonalizados() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "ADMINISTRADOR");
        claims.put("sessionId", "session-uuid-12345");

        String token = jwtUtil.generateToken(claims, "coordinador@sipf.com");

        String role = jwtUtil.extractClaim(token, c -> c.get("role", String.class));
        String sessionId = jwtUtil.extractClaim(token, c -> c.get("sessionId", String.class));

        assertEquals("ADMINISTRADOR", role);
        assertEquals("session-uuid-12345", sessionId);
        System.out.println("[FASE 1 PASSED] Claims Extraídos del JWT -> Role: " + role + ", SessionID: " + sessionId);
    }
}
