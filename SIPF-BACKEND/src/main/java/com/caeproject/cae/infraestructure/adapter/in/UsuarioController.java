package com.caeproject.cae.infraestructure.adapter.in;

import com.caeproject.cae.domain.ports.in.usuario.CrearUsuarioInputPort;
import com.caeproject.cae.domain.ports.in.usuario.EditarUsuarioInputPort;
import com.caeproject.cae.domain.ports.in.usuario.EliminarUsuarioInputPort;
import com.caeproject.cae.domain.ports.in.usuario.LIstarUsuariosInputPort;
import com.caeproject.cae.domain.ports.in.usuario.ObtenerUsuarioInputPort;
import com.caeproject.cae.domain.ports.model.enums.Rol;
import com.caeproject.cae.domain.ports.model.perfil_base.PerfilBase;
import com.caeproject.cae.domain.ports.model.usuario.Usuario;
import com.caeproject.cae.infraestructure.dtos.CrearUsuarioRequest;
import com.caeproject.cae.infraestructure.dtos.EditarUsuarioRequest;
import com.caeproject.cae.infraestructure.dtos.UsuarioResponse;
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

    public UsuarioController(
            CrearUsuarioInputPort crearUsuarioInputPort,
            LIstarUsuariosInputPort listarUsuariosInputPort,
            ObtenerUsuarioInputPort obtenerUsuarioInputPort,
            EditarUsuarioInputPort editarUsuarioInputPort,
            EliminarUsuarioInputPort eliminarUsuarioInputPort) {
        this.crearUsuarioInputPort = crearUsuarioInputPort;
        this.listarUsuariosInputPort = listarUsuariosInputPort;
        this.obtenerUsuarioInputPort = obtenerUsuarioInputPort;
        this.editarUsuarioInputPort = editarUsuarioInputPort;
        this.eliminarUsuarioInputPort = eliminarUsuarioInputPort;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> crearUsuario(@RequestBody CrearUsuarioRequest request) {
        Usuario usuario = new Usuario();
        usuario.setCorreo(request.getCorreo());
        usuario.setContrasena(request.getContrasena());
        usuario.setRol(request.getRol());
        usuario.setEstado(true);

        PerfilBase perfilBase = new PerfilBase();
        perfilBase.setNombre(request.getNombre());
        perfilBase.setApellido(request.getApellido());
        perfilBase.setCc(request.getDocumentoIdentidad());
        perfilBase.setTelefono(request.getTelefono());
        perfilBase.setTipoContrato(request.getTipoContrato());

        Usuario creado = crearUsuarioInputPort.crearUsuario(usuario, perfilBase);

        UsuarioResponse response = buildResponse(creado, perfilBase);
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

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> editarUsuario(@PathVariable Long id,
                                                          @RequestBody EditarUsuarioRequest request) {
        Usuario usuario = new Usuario();
        usuario.setCorreo(request.getCorreo());
        usuario.setContrasena(request.getContrasena());
        usuario.setRol(request.getRol());
        if (request.getEstado() != null) {
            usuario.setEstado(request.getEstado());
        }

        Usuario editado = editarUsuarioInputPort.editarUsuario(usuario, id);
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
        return response;
    }

    private UsuarioResponse buildResponse(Usuario usuario, PerfilBase perfilBase) {
        UsuarioResponse response = buildSimpleResponse(usuario);
        response.setNombre(perfilBase.getNombre());
        response.setApellido(perfilBase.getApellido());
        response.setDocumentoIdentidad(perfilBase.getCc());
        response.setTelefono(perfilBase.getTelefono());
        response.setTipoContrato(perfilBase.getTipoContrato());
        return response;
    }
}
