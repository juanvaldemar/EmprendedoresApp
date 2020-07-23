package com.valdemar.emprendedores.model;

public class Proyecto {
    String id;
    String nombre;
    String categoria;
    String descripcion;
    String socio1;
    String descripcionSocio1;
    String socio2;
    String descripcionSocio2;
    String socio3;
    String descripcionSocio3;
    String socio4;
    String descripcionSocio4;
    String socio5;
    String descripcionSocio5;
    String pais;
    String ciudad;
    String imagen;
    String id_emprendedor;
    // la imagen o video se guardara en el storage de firebase con el id del proyecto
    // falta agregar usuario
    public Proyecto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getSocio1() {
        return socio1;
    }

    public void setSocio1(String socio1) {
        this.socio1 = socio1;
    }

    public String getDescripcionSocio1() {
        return descripcionSocio1;
    }

    public void setDescripcionSocio1(String descripcionSocio1) {
        this.descripcionSocio1 = descripcionSocio1;
    }

    public String getSocio2() {
        return socio2;
    }

    public void setSocio2(String socio2) {
        this.socio2 = socio2;
    }

    public String getDescripcionSocio2() {
        return descripcionSocio2;
    }

    public void setDescripcionSocio2(String descripcionSocio2) {
        this.descripcionSocio2 = descripcionSocio2;
    }

    public String getSocio3() {
        return socio3;
    }

    public void setSocio3(String socio3) {
        this.socio3 = socio3;
    }

    public String getDescripcionSocio3() {
        return descripcionSocio3;
    }

    public void setDescripcionSocio3(String descripcionSocio3) {
        this.descripcionSocio3 = descripcionSocio3;
    }

    public String getSocio4() {
        return socio4;
    }

    public void setSocio4(String socio4) {
        this.socio4 = socio4;
    }

    public String getDescripcionSocio4() {
        return descripcionSocio4;
    }

    public void setDescripcionSocio4(String descripcionSocio4) {
        this.descripcionSocio4 = descripcionSocio4;
    }

    public String getSocio5() {
        return socio5;
    }

    public void setSocio5(String socio5) {
        this.socio5 = socio5;
    }

    public String getDescripcionSocio5() {
        return descripcionSocio5;
    }

    public void setDescripcionSocio5(String descripcionSocio5) {
        this.descripcionSocio5 = descripcionSocio5;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getId_emprendedor() {
        return id_emprendedor;
    }

    public void setId_emprendedor(String id_emprendedor) {
        this.id_emprendedor = id_emprendedor;
    }
}
