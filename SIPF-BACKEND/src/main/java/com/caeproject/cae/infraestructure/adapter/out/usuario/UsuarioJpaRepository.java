package com.caeproject.cae.infraestructure.adapter.out.usuario;

import com.caeproject.cae.domain.ports.model.enums.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByCorreo(String correo);
    List<UsuarioEntity> findByRol(Rol rol);
    List<UsuarioEntity> findByEstado(boolean estado);
}
