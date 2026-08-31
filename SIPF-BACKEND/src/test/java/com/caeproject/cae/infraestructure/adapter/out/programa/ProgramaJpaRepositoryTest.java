package com.caeproject.cae.infraestructure.adapter.out.programa;

import com.caeproject.cae.domain.ports.model.enums.Jornada;
import com.caeproject.cae.domain.ports.model.enums.NivelFormacion;
import com.caeproject.cae.infraestructure.adapter.out.ubicacion.DepartamentoEntity;
import com.caeproject.cae.infraestructure.adapter.out.ubicacion.MunicipioEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProgramaJpaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProgramaJpaRepository repository;

    @Test
    @DisplayName("Guarda un Programa y verifica que existe por nombre")
    void save_y_existsByNombre_retornaTrue() {
        // Arrange
        DepartamentoEntity departamento = new DepartamentoEntity();
        departamento.setNombre("CUNDINAMARCA");
        entityManager.persist(departamento);

        MunicipioEntity municipio = new MunicipioEntity();
        municipio.setNombre("BOGOTA");
        municipio.setDepartamento(departamento);
        entityManager.persist(municipio);

        ProgramaEntity programa = new ProgramaEntity();
        programa.setNombre("TECNICO EN SISTEMAS");
        programa.setMunicipio(municipio);
        programa.setNivelFormacion(NivelFormacion.TECNICO);
        programa.setJornada(Jornada.MAÑANA);
        programa.setDuracionpracticas(6);
        
        entityManager.persist(programa);
        entityManager.flush();

        // Act
        boolean existe = repository.existsByNombre("TECNICO EN SISTEMAS");
        boolean noExiste = repository.existsByNombre("OTRO PROGRAMA");

        // Assert
        assertThat(existe).isTrue();
        assertThat(noExiste).isFalse();
    }
}
