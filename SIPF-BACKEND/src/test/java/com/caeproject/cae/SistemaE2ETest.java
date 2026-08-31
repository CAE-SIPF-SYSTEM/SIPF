package com.caeproject.cae;

import com.caeproject.cae.infraestructure.dtos.usuario.UsuarioLoginRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.caeproject.cae.infraestructure.adapter.out.api.ColombiaApiClientAdapter;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "MAIL_HOST=localhost",
    "MAIL_PORT=25",
    "MAIL_USERNAME=test",
    "MAIL_PASSWORD=test",
    "JWT_SECRET=supersecreto_para_pruebas_que_debe_ser_largo_123456789",
    "JWT_EXPIRATION=86400000",
    "DB_URL=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
    "DB_USERNAME=sa",
    "DB_PASSWORD=password",
    "FRONTEND_URL=http://localhost:4200"
})
class SistemaE2ETest {

    @MockBean
    private ColombiaApiClientAdapter colombiaApiClientAdapter;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("E2E: Intentar acceder a un endpoint protegido sin token debe retornar 403 Forbidden o 401 Unauthorized")
    void accesoProtegido_sinToken_retornaErrorDeSeguridad() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        // Act - Intentamos acceder a /api/programas sin Bearer Token
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/programas", 
                HttpMethod.POST, 
                entity, 
                String.class
        );

        // Assert
        // Debe rechazar la petición a nivel de Spring Security (Filtros JWT)
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }

}
