package com.caeproject.cae.domain.ports.out;

import com.caeproject.cae.domain.ports.model.PerfilBase;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PerfilBaseRepository {
    Optional<PerfilBase> findByCC(Long cc);
    PerfilBase save (PerfilBase perfilBase);
    Optional<PerfilBase> findById(Long id);
}
