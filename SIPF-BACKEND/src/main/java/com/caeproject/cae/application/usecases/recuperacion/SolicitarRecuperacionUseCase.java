package com.caeproject.cae.application.usecases.recuperacion;

import com.caeproject.cae.domain.ports.in.recuperacion.SolicitarRecuperacionInputPort;
import com.caeproject.cae.domain.ports.model.TokenRecuperacion;
import com.caeproject.cae.domain.ports.model.Usuario;
import com.caeproject.cae.domain.ports.out.EmailNotificationPort;
import com.caeproject.cae.domain.ports.out.TokenRecuperacionRepository;
import com.caeproject.cae.domain.ports.out.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class SolicitarRecuperacionUseCase implements SolicitarRecuperacionInputPort {

    private final UsuarioRepository usuarioRepository;
    private final TokenRecuperacionRepository tokenRecuperacionRepository;
    private final EmailNotificationPort emailNotificationPort;

    public SolicitarRecuperacionUseCase(UsuarioRepository usuarioRepository,
                                         TokenRecuperacionRepository tokenRecuperacionRepository,
                                         EmailNotificationPort emailNotificationPort) {
        this.usuarioRepository = usuarioRepository;
        this.tokenRecuperacionRepository = tokenRecuperacionRepository;
        this.emailNotificationPort = emailNotificationPort;
    }

    @Override
    public void solicitarRecuperacion(String correo) {
        // Buscar usuario por correo
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);


        if (usuarioOpt.isEmpty()) {
            return;
        }

        Usuario usuario = usuarioOpt.get();

        // Eliminar tokens
        tokenRecuperacionRepository.eliminarPorUsuarioId(usuario.getId());

        // Generar nuevo token UUID
        String token = UUID.randomUUID().toString();

        //Token con expiracion de 15 minutos
        TokenRecuperacion tokenRecuperacion = new TokenRecuperacion();
        tokenRecuperacion.setToken(token);
        tokenRecuperacion.setUsuarioId(usuario.getId());
        tokenRecuperacion.setFechaExpiracion(LocalDateTime.now().plusMinutes(15));

        // Guardar token
        tokenRecuperacionRepository.guardar(tokenRecuperacion);

        // Enviar email con el token
        emailNotificationPort.enviarEmailRecuperacion(correo, token);
    }
}
