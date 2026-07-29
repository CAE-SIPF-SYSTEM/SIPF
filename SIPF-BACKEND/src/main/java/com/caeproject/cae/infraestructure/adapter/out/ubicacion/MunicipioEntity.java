package com.caeproject.cae.infraestructure.adapter.out.ubicacion;

import com.caeproject.cae.domain.ports.model.Departamento;
import jakarta.persistence.*;


@Entity
public class MunicipioEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column (name= "NombreMunicipio", nullable = false)
    private String nombre;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departamento_id", nullable = false)
    private DepartamentoEntity departamento;


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

    public DepartamentoEntity getDepartamento() {
        return departamento;
    }

    public void setDepartamento(DepartamentoEntity departamento) {
        this.departamento = departamento;
    }
}
