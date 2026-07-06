package com.caeproject.cae.domain.ports.out;

import com.caeproject.cae.domain.ports.model.enums.Rol;
import com.caeproject.cae.domain.ports.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {

    Optional<Usuario> findByCorreo (String correo); //busqueda pore el correo del usuario
    List<Usuario> findByRol (Rol rol); //busqueda por rol de el usuario
    List<Usuario> findByStatus (boolean estado); //busqueda por el estado de el usuario recordar que false es inhabilitado y true habilitado
    List<Usuario> findAll(); //busqueda de todos los usuarios
    Optional <Usuario> findById(Long id); //busqueda por id
    void deleteUser(Long id); // eliminar usuario
    Usuario saveUser (Usuario usuario); //guardar usuario
}
