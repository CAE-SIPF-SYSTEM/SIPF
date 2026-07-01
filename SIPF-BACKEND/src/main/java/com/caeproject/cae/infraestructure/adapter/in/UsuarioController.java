package com.caeproject.cae.infraestructure.adapter.in;

import com.caeproject.cae.domain.ports.in.usuario.*;
import com.caeproject.cae.domain.ports.model.enums.Rol;
import com.caeproject.cae.domain.ports.model.perfilbase.PerfilBase;
import com.caeproject.cae.domain.ports.model.usuario.Usuario;
import com.caeproject.cae.application.usecases.usuario.commands.CrearUsuarioCommand;
import com.caeproject.cae.application.usecases.usuario.commands.EditarUsuarioCommand;
import com.caeproject.cae.infraestructure.dtos.usuario.CrearUsuarioRequest;
import com.caeproject.cae.infraestructure.dtos.usuario.EditarUsuarioRequest;
import com.caeproject.cae.infraestructure.dtos.usuario.UsuarioResponse;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final CrearUsuarioInputPort crearUsuarioInputPort;
    private final LIstarUsuariosInputPort listarUsuariosInputPort;
    private final ObtenerUsuarioInputPort obtenerUsuarioInputPort;
    private final EditarUsuarioInputPort editarUsuarioInputPort;
    private final EliminarUsuarioInputPort eliminarUsuarioInputPort;
    private final InhabilitarUsuarioInputPort inhabilitarUsuarioInputPort;
    private final HabilitarUsuarioInputPort HabilitarUsuarioInputPort;

    public UsuarioController(
            CrearUsuarioInputPort crearUsuarioInputPort,
            LIstarUsuariosInputPort listarUsuariosInputPort,
            ObtenerUsuarioInputPort obtenerUsuarioInputPort,
            EditarUsuarioInputPort editarUsuarioInputPort,
            EliminarUsuarioInputPort eliminarUsuarioInputPort,
            InhabilitarUsuarioInputPort inhabilitarUsuarioInputPort,
            HabilitarUsuarioInputPort HabilitarUsuarioInputPort) {

        this.inhabilitarUsuarioInputPort = inhabilitarUsuarioInputPort;
        this.HabilitarUsuarioInputPort = HabilitarUsuarioInputPort;
        this.crearUsuarioInputPort = crearUsuarioInputPort;
        this.listarUsuariosInputPort = listarUsuariosInputPort;
        this.obtenerUsuarioInputPort = obtenerUsuarioInputPort;
        this.editarUsuarioInputPort = editarUsuarioInputPort;
        this.eliminarUsuarioInputPort = eliminarUsuarioInputPort;

    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> crearUsuario(@RequestBody CrearUsuarioRequest request) {
        CrearUsuarioCommand command = new CrearUsuarioCommand();
        command.setCorreo(request.getCorreo());
        command.setContrasena(request.getContrasena());
        command.setRol(request.getRol());
        command.setNombre(request.getNombre());
        command.setApellido(request.getApellido());
        command.setDocumentoIdentidad(request.getDocumentoIdentidad());
        command.setTelefono(request.getTelefono());
        command.setTipoContrato(request.getTipoContrato());

        Usuario creado = crearUsuarioInputPort.crearUsuario(command);

        UsuarioResponse response = buildSimpleResponse(creado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        List<Usuario> usuarios = listarUsuariosInputPort.listarUsuarios();
        List<UsuarioResponse> responses = usuarios.stream()
                .map(this::buildSimpleResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> obtenerUsuario(@PathVariable Long id) {
        Usuario usuario = obtenerUsuarioInputPort.obtenerUsuario(id);
        return ResponseEntity.ok(buildSimpleResponse(usuario));
    }

    @GetMapping("/correo/{correo}")
    public ResponseEntity<UsuarioResponse> buscarPorCorreo(@PathVariable String correo) {
        Optional<Usuario> usuario = obtenerUsuarioInputPort.porCorreo(correo);
        return usuario.map(u -> ResponseEntity.ok(buildSimpleResponse(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<UsuarioResponse>> buscarPorRol(@PathVariable Rol rol) {
        List<Usuario> usuarios = obtenerUsuarioInputPort.porRol(rol);
        List<UsuarioResponse> responses = usuarios.stream()
                .map(this::buildSimpleResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PostMapping ("/{id}/inhabilitar")
    public ResponseEntity<Void> inhabilitarUsuario(@PathVariable Long id){
        inhabilitarUsuarioInputPort.inhabilitarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping ("/{id}/habilitar")
    public ResponseEntity<Void> habilitarUsuario(@PathVariable Long id){
        HabilitarUsuarioInputPort.habilitarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> editarUsuario(@PathVariable Long id,
                                                          @RequestBody EditarUsuarioRequest request) {
        EditarUsuarioCommand command = new EditarUsuarioCommand();
        command.setCorreo(request.getCorreo());
        command.setContrasena(request.getContrasena());
        command.setRol(request.getRol());
        command.setEstado(request.getEstado());
        command.setNombre(request.getNombre());
        command.setApellido(request.getApellido());
        command.setTelefono(request.getTelefono());

        Usuario editado = editarUsuarioInputPort.editarUsuario(command, id);
        return ResponseEntity.ok(buildSimpleResponse(editado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        eliminarUsuarioInputPort.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    private UsuarioResponse buildSimpleResponse(Usuario usuario) {
        UsuarioResponse response = new UsuarioResponse();
        response.setId(usuario.getId());
        response.setCorreo(usuario.getCorreo());
        response.setRol(usuario.getRol());
        response.setEstado(usuario.isEstado());
        
        if (usuario.getPerfilBase() != null) {
            response.setNombre(usuario.getPerfilBase().getNombre());
            response.setApellido(usuario.getPerfilBase().getApellido());
            response.setDocumentoIdentidad(usuario.getPerfilBase().getCc());
            response.setTelefono(usuario.getPerfilBase().getTelefono());
            response.setTipoContrato(usuario.getPerfilBase().getTipoContrato());
        }
        
        return response;
    }
}
