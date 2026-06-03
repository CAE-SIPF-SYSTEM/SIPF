package com.caeproject.cae.infraestructure.adapter.in;

import com.caeproject.cae.domain.ports.in.recuperacion.RestablecerContrasenaInputPort;
import com.caeproject.cae.domain.ports.in.recuperacion.SolicitarRecuperacionInputPort;
import com.caeproject.cae.domain.ports.in.usuario.LoginInputPort;
import com.caeproject.cae.domain.ports.model.usuario.Usuario;
import com.caeproject.cae.infraestructure.dtos.email.MensajeResponse;
import com.caeproject.cae.infraestructure.dtos.email.RestablecerContrasenaRequest;
import com.caeproject.cae.infraestructure.dtos.email.SolicitarRecuperacionRequest;
import com.caeproject.cae.infraestructure.dtos.usuario.UsuarioLoginRequest;
import com.caeproject.cae.infraestructure.dtos.usuario.UsuarioLoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final LoginInputPort loginInputPort;
    private final SolicitarRecuperacionInputPort solicitarRecuperacionInputPort;
    private final RestablecerContrasenaInputPort restablecerContrasenaInputPort;

    public AuthController(LoginInputPort loginInputPort,
                          SolicitarRecuperacionInputPort solicitarRecuperacionInputPort,
                          RestablecerContrasenaInputPort restablecerContrasenaInputPort) {
        this.loginInputPort = loginInputPort;
        this.solicitarRecuperacionInputPort = solicitarRecuperacionInputPort;
        this.restablecerContrasenaInputPort = restablecerContrasenaInputPort;
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioLoginResponse> login(@RequestBody UsuarioLoginRequest request) {

        Usuario usuarioValidado = loginInputPort.loginSistema(request.getCorreo(), request.getContrasena());

        //map frontend
        UsuarioLoginResponse response = new UsuarioLoginResponse();
        response.setId(usuarioValidado.getId());
        response.setCorreo(usuarioValidado.getCorreo());
        response.setRol(usuarioValidado.getRol());
        response.setToken(usuarioValidado.getJwtToken());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/recuperar-contrasena")
    public ResponseEntity<MensajeResponse> solicitarRecuperacion(
            @RequestBody SolicitarRecuperacionRequest request) {

        solicitarRecuperacionInputPort.solicitarRecuperacion(request.getCorreo());

        MensajeResponse response = new MensajeResponse(
                "Si el correo está registrado, recibirás un enlace para restablecer tu contraseña."
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/restablecer-contrasena")
    public ResponseEntity<MensajeResponse> restablecerContrasena(
            @RequestBody RestablecerContrasenaRequest request) {

        restablecerContrasenaInputPort.restablecerContrasena(
                request.getToken(),
                request.getNuevaContrasena()
        );

        MensajeResponse response = new MensajeResponse("Contraseña actualizada exitosamente.");
        return ResponseEntity.ok(response);
    }
}
