package com.caeproject.cae.infraestructure.adapter.out.ubicacion;

import com.caeproject.cae.domain.ports.model.Municipio;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class DepartamentoEntity {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "Nombre")
    private String nombre;


    @OneToMany (mappedBy = "departamento", cascade = CascadeType.ALL)
    private List<MunicipioEntity> municipios;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<MunicipioEntity> getMunicipios() {
        return municipios;
    }

    public void setMunicipios(List<MunicipioEntity> municipios) {
        this.municipios = municipios;
    }
}
