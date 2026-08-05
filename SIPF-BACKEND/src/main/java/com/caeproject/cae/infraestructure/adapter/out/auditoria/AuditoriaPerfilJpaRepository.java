package com.caeproject.cae.infraestructure.adapter.out.auditoria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditoriaPerfilJpaRepository extends JpaRepository<AuditoriaPerfilEntity, Long> {
    java.util.List<AuditoriaPerfilEntity> findByUsuarioIdOrderByFechaModificacionDesc(Long usuarioId);
    java.util.List<AuditoriaPerfilEntity> findAllByOrderByFechaModificacionDesc();
}
