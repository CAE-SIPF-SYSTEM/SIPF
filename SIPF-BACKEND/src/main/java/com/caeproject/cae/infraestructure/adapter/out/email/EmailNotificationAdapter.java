package com.caeproject.cae.infraestructure.adapter.out.email;

import com.caeproject.cae.domain.ports.out.EmailNotificationPort;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationAdapter implements EmailNotificationPort {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;

    public EmailNotificationAdapter(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void enviarEmailRecuperacion(String destinatario, String token) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(sender);
            helper.setTo(destinatario);
            helper.setSubject("SIPF - Recuperación de Contraseña");
            helper.setText(construirHtmlEmail(token), true);

            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo de recuperación: " + e.getMessage(), e);
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
                <body style="margin: 0; padding: 0; font-family: 'Plus Jakarta Sans', 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #ffffff;">
                    <table role="presentation" style="width: 100%%; max-width: 600px; margin: 30px auto; background-color: #ffffff; border-radius: 12px; border: 1px solid #eeeeee; overflow: hidden; box-shadow: 0 4px 12px rgba(0,0,0,0.05);">
                        <!-- Header con el Verde Institucional Oscuro -->
                        <tr>
                            <td style="background-color: #136E61; padding: 35px 40px; text-align: center;">
                                <h1 style="color: #ffffff; margin: 0; font-size: 26px; font-weight: 700; letter-spacing: -0.5px;">SIPF</h1>
                                <p style="color: rgba(255,255,255,0.8); margin: 6px 0 0 0; font-size: 13px; font-weight: 400; text-transform: uppercase; letter-spacing: 1px;">Sistema CAE</p>
                            </td>
                        </tr>
                
                        <!-- Cuerpo del Correo -->
                        <tr>
                            <td style="padding: 45px 40px;">
                                <h2 style="color: #136E61; margin: 0 0 15px 0; font-size: 22px; font-weight: 700;">Recuperación de Contraseña</h2>
                                <p style="color: #4B5563; font-size: 15px; line-height: 1.7; margin: 0 0 25px 0;">
                                    Hola, hemos recibido una solicitud para restablecer la contraseña de tu cuenta en la plataforma del CAE.\s
                                    Para continuar con el proceso, por favor haz clic en el siguiente botón:
                                </p>
                
                                <!-- Botón con el Verde de Acción -->
                                <div style="text-align: center; margin: 35px 0;">
                                    <a href="%s" style="display: inline-block; background-color: #179380; color: #ffffff; text-decoration: none; padding: 15px 45px; border-radius: 10px; font-size: 15px; font-weight: 600; transition: all 0.3s ease;">
                                        Restablecer Contraseña
                                    </a>
                                </div>
                
                                <p style="color: #9CA3AF; font-size: 13px; line-height: 1.5; margin: 25px 0 0 0;">
                                    Este enlace tiene una validez de <strong>15 minutos</strong> por motivos de seguridad.
                                </p>
                                <p style="color: #9CA3AF; font-size: 13px; line-height: 1.5; margin: 10px 0 0 0;">
                                    Si no realizaste esta solicitud, puedes ignorar este mensaje; tu cuenta permanecerá segura y no se realizarán cambios.
                                </p>
                
                                <hr style="border: none; border-top: 1px solid #F3F4F6; margin: 35px 0;">
                
                                <!-- Enlace de Respaldo -->
                                <p style="color: #9CA3AF; font-size: 12px; text-align: center; margin: 0; line-height: 1.6;">
                                    Si tienes problemas con el botón, copia y pega el siguiente enlace en tu navegador:<br>
                                </p>
                            </td>
                        </tr>
                
                        <!-- Footer -->
                        <tr>
                            <td style="background-color: #F9FAFB; padding: 25px 40px; text-align: center; border-top: 1px solid #F3F4F6;">
                                <p style="color: #9CA3AF; font-size: 11px; margin: 0; font-weight: 500;">
                                    © 2026 SIPF - CAE Productivity System.
                                </p>
                                <p style="color: #D1D5DB; font-size: 10px; margin: 5px 0 0 0;">
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
