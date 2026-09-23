package com.caeproject.cae.infraestructure.adapter.out.email;

import com.caeproject.cae.domain.ports.out.EmailNotificationPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Component
public class EmailNotificationAdapter implements EmailNotificationPort {

    @Value("${resend.api.key:re_dummy_key_please_change}")
    private String resendApiKey;

    // Resend exige 'onboarding@resend.dev' para cuentas gratis sin dominio verificado
    @Value("${resend.from.email:onboarding@resend.dev}")
    private String sender;

    @Value("${app.frontend.url:https://sipf.up.railway.app}")
    private String frontendUrl;

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public EmailNotificationAdapter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @Override
    public void enviarEmailRecuperacion(String destinatario, String token) {
        try {
            Map<String, Object> payload = new HashMap<>();
            // El remitente en la capa gratuita de resend DEBE ser onboarding@resend.dev
            payload.put("from", "SIPF CAE <" + sender + ">");
            payload.put("to", new String[]{destinatario});
            payload.put("subject", "SIPF - Recuperación de Contraseña");
            payload.put("html", construirHtmlEmail(token));

            String requestBody = objectMapper.writeValueAsString(payload);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.resend.com/emails"))
                    .timeout(Duration.ofSeconds(10))
                    .header("Authorization", "Bearer " + resendApiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                throw new RuntimeException("Error en la API de Resend (HTTP " + response.statusCode() + "): " + response.body());
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo de recuperación por API: " + e.getMessage(), e);
        }
    }

    private String construirHtmlEmail(String token) {
        String enlace = frontendUrl + "/restablecer-contrasena?token=" + token;

        return """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
                </head>
                <body style="margin: 0; padding: 0; font-family: 'Plus Jakarta Sans', 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f7f6;">
                    <table role="presentation" style="width: 100%%; max-width: 600px; margin: 30px auto; background-color: #ffffff; border-radius: 16px; border: 1px solid #e2e8f0; overflow: hidden; box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);">
                        <!-- Header -->
                        <tr>
                            <td style="background: linear-gradient(135deg, #136E61, #34857b); padding: 35px 40px; text-align: center;">
                                <h1 style="color: #ffffff; margin: 0; font-size: 26px; font-weight: 700; letter-spacing: -0.5px;">SIPF</h1>
                                <p style="color: rgba(255,255,255,0.8); margin: 6px 0 0 0; font-size: 13px; font-weight: 400; text-transform: uppercase; letter-spacing: 1px;">Sistema CAE</p>
                            </td>
                        </tr>
                
                        <!-- Cuerpo del Correo -->
                        <tr>
                            <td style="padding: 45px 40px;">
                                <h2 style="color: #1e293b; margin: 0 0 15px 0; font-size: 22px; font-weight: 700;">Recuperación de Contraseña</h2>
                                <p style="color: #64748b; font-size: 15px; line-height: 1.7; margin: 0 0 25px 0;">
                                    Hola, hemos recibido una solicitud para restablecer la contraseña de tu cuenta en la plataforma del CAE.\s
                                    Para continuar con el proceso, por favor haz clic en el siguiente botón:
                                </p>
                
                                <!-- Botón -->
                                <div style="text-align: center; margin: 35px 0;">
                                    <a href="%s" style="display: inline-block; background-color: #10b981; color: #ffffff; text-decoration: none; padding: 12px 24px; border-radius: 9999px; font-size: 14px; font-weight: 600; transition: all 0.2s ease;">
                                           Restablecer Contraseña
                                    </a>
                                </div>
                
                                <p style="color: #94a3b8; font-size: 13px; line-height: 1.5; margin: 25px 0 0 0;">
                                    Este enlace tiene una validez de <strong>15 minutos</strong> por motivos de seguridad.
                                </p>
                                <p style="color: #94a3b8; font-size: 13px; line-height: 1.5; margin: 10px 0 0 0;">
                                    Si no realizaste esta solicitud, puedes ignorar este mensaje; tu cuenta permanecerá segura y no se realizarán cambios.
                                </p>
                
                                <hr style="border: none; border-top: 1px solid #e2e8f0; margin: 35px 0;">
                
                                <!-- Enlace de Respaldo -->
                                <p style="color: #94a3b8; font-size: 12px; text-align: center; margin: 0; line-height: 1.6;">
                                    Si tienes problemas con el botón, copia y pega el siguiente enlace en tu navegador:<br>
                                    <a href="%s" style="color: #10b981; text-decoration: none; word-break: break-all;">%s</a>
                                </p>
                            </td>
                        </tr>
                
                        <!-- Footer -->
                        <tr>
                            <td style="background-color: #f8fafc; padding: 25px 40px; text-align: center; border-top: 1px solid #e2e8f0;">
                                <p style="color: #94a3b8; font-size: 11px; margin: 0; font-weight: 500;">
                                    © 2026 SIPF - CAE Productivity System.
                                </p>
                                <p style="color: #cbd5e1; font-size: 10px; margin: 5px 0 0 0;">
                                    Centro Agroecológico y Empresarial - SENA
                                </p>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """.formatted(enlace, enlace, enlace);
    }
}
