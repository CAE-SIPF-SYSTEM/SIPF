package com.caeproject.cae.domain.ports.out;

import com.caeproject.cae.domain.ports.model.perfil_base.PerfilBase;

import java.util.Optional;

public interface PerfilBaseRepository {
    Optional<PerfilBase> findByCC(Long cc);
    PerfilBase save (PerfilBase perfilBase);
}
