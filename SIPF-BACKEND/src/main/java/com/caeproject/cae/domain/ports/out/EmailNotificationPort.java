package com.caeproject.cae.domain.ports.out;

public interface EmailNotificationPort {

    void enviarEmailRecuperacion(String destinatario, String token);
}
