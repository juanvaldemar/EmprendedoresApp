package com.valdemar.emprendedores.model;

import java.sql.Timestamp;

public class Emprendedor {
    private String id_emprendedor;
    private String edt_nombres_emprendedor;
    private String edt_apellidos_emprendedor;
    private String edt_dni_emprendedor;
    private String edt_direccion_emprendedor;
    private String imagen;
    private String spinner_anio;
    private String spinner_mes;
    private String spinner_dia;
    private String spinner_pais;
    private String spinner_ciudad;
    private String spinner_ocupacion;
    private String edt_num_emprendedor;
    private String edt_facebook;
    private String edt_instagram;
    private String edt_twitter;
    private Timestamp fechaRegistro;

    public String getId_emprendedor() {
        return id_emprendedor;
    }

    public void setId_emprendedor(String id_emprendedor) {
        this.id_emprendedor = id_emprendedor;
    }

    public String getEdt_nombres_emprendedor() {
        return edt_nombres_emprendedor;
    }

    public void setEdt_nombres_emprendedor(String edt_nombres_emprendedor) {
        this.edt_nombres_emprendedor = edt_nombres_emprendedor;
    }

    public String getEdt_apellidos_emprendedor() {
        return edt_apellidos_emprendedor;
    }

    public void setEdt_apellidos_emprendedor(String edt_apellidos_emprendedor) {
        this.edt_apellidos_emprendedor = edt_apellidos_emprendedor;
    }

    public String getEdt_dni_emprendedor() {
        return edt_dni_emprendedor;
    }

    public void setEdt_dni_emprendedor(String edt_dni_emprendedor) {
        this.edt_dni_emprendedor = edt_dni_emprendedor;
    }

    public String getEdt_direccion_emprendedor() {
        return edt_direccion_emprendedor;
    }

    public void setEdt_direccion_emprendedor(String edt_direccion_emprendedor) {
        this.edt_direccion_emprendedor = edt_direccion_emprendedor;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getSpinner_anio() {
        return spinner_anio;
    }

    public void setSpinner_anio(String spinner_anio) {
        this.spinner_anio = spinner_anio;
    }

    public String getSpinner_mes() {
        return spinner_mes;
    }

    public void setSpinner_mes(String spinner_mes) {
        this.spinner_mes = spinner_mes;
    }

    public String getSpinner_dia() {
        return spinner_dia;
    }

    public void setSpinner_dia(String spinner_dia) {
        this.spinner_dia = spinner_dia;
    }

    public String getSpinner_pais() {
        return spinner_pais;
    }

    public void setSpinner_pais(String spinner_pais) {
        this.spinner_pais = spinner_pais;
    }

    public String getSpinner_ciudad() {
        return spinner_ciudad;
    }

    public void setSpinner_ciudad(String spinner_ciudad) {
        this.spinner_ciudad = spinner_ciudad;
    }

    public String getEdt_num_emprendedor() {
        return edt_num_emprendedor;
    }

    public void setEdt_num_emprendedor(String edt_num_emprendedor) {
        this.edt_num_emprendedor = edt_num_emprendedor;
    }

    public String getEdt_facebook() {
        return edt_facebook;
    }

    public void setEdt_facebook(String edt_facebook) {
        this.edt_facebook = edt_facebook;
    }

    public String getEdt_instagram() {
        return edt_instagram;
    }

    public void setEdt_instagram(String edt_instagram) {
        this.edt_instagram = edt_instagram;
    }

    public String getEdt_twitter() {
        return edt_twitter;
    }

    public void setEdt_twitter(String edt_twitter) {
        this.edt_twitter = edt_twitter;
    }

    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getSpinner_ocupacion() {
        return spinner_ocupacion;
    }

    public void setSpinner_ocupacion(String spinner_ocupacion) {
        this.spinner_ocupacion = spinner_ocupacion;
    }
}
