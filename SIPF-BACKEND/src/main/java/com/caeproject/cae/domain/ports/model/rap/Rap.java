package com.caeproject.cae.domain.ports.model.rap;

public class Rap {

    private Long id;
    private Long competenciaId;
    private String descripcion;
    private Integer horasPresenciales;
    private Boolean estado;

    public Long getId() {
        return id;}

    public void setId(Long id) {
        this.id = id;}

    public Long getCompetenciaId() {
        return competenciaId;}

    public void setCompetenciaId(Long competenciaId) {
        this.competenciaId = competenciaId;}

    public String getDescripcion() {
        return descripcion;}

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;}

    public Integer getHorasPresenciales() {
        return horasPresenciales;}

    public void setHorasPresenciales(Integer horasPresenciales) {
        this.horasPresenciales = horasPresenciales;}

    public Boolean getEstado() {
        return estado;}

    public void setEstado(Boolean estado) {
        this.estado = estado;}
}