package com.mamt.plumel.Models;



public class Servicio {

    private int idServicio;
    private String descripcionServicio;
    private String fechaServicio;
    private String horaServicio;
    private String referenciasServicio;
    private String tipoServicio;
    private String 	direccionServicio;
    private String latitudServicio;
    private String longuitudServicio;
    private int idCliente;
    private  String estatusServicio;

    public Servicio(int idServicio, String descripcionServicio, String fechaServicio, String horaServicio, String referenciasServicio, String tipoServicio, String direccionServicio, String latitudServicio, String longuitudServicio, int idCliente, String estatusServicio) {
        this.idServicio = idServicio;
        this.descripcionServicio = descripcionServicio;
        this.fechaServicio = fechaServicio;
        this.horaServicio = horaServicio;
        this.referenciasServicio = referenciasServicio;
        this.tipoServicio = tipoServicio;
        this.direccionServicio = direccionServicio;
        this.latitudServicio = latitudServicio;
        this.longuitudServicio = longuitudServicio;
        this.idCliente = idCliente;
        this.estatusServicio = estatusServicio;
    }

    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    public String getDescripcionServicio() {
        return descripcionServicio;
    }

    public void setDescripcionServicio(String descripcionServicio) {
        this.descripcionServicio = descripcionServicio;
    }

    public String getFechaServicio() {
        return fechaServicio;
    }

    public void setFechaServicio(String fechaServicio) {
        this.fechaServicio = fechaServicio;
    }

    public String getHoraServicio() {
        return horaServicio;
    }

    public void setHoraServicio(String horaServicio) {
        this.horaServicio = horaServicio;
    }

    public String getReferenciasServicio() {
        return referenciasServicio;
    }

    public void setReferenciasServicio(String referenciasServicio) {
        this.referenciasServicio = referenciasServicio;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public String getDireccionServicio() {
        return direccionServicio;
    }

    public void setDireccionServicio(String direccionServicio) {
        this.direccionServicio = direccionServicio;
    }

    public String getLatitudServicio() {
        return latitudServicio;
    }

    public void setLatitudServicio(String latitudServicio) {
        this.latitudServicio = latitudServicio;
    }

    public String getLonguitudServicio() {
        return longuitudServicio;
    }

    public void setLonguitudServicio(String longuitudServicio) {
        this.longuitudServicio = longuitudServicio;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getEstatusServicio() {
        return estatusServicio;
    }

    public void setEstatusServicio(String estatusServicio) {
        this.estatusServicio = estatusServicio;
    }
}
