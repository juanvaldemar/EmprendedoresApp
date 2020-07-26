package com.valdemar.emprendedores.view.ui.proyectos.lista;

public class ItemFeed {
    private String id;
    private String nombre;
    private String imagen;
    private String categoria;
    private String pais;
    private String ciudad;
    private String estadoTrazabilidad;


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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
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

    public String getEstadoTrazabilidad() {
        return estadoTrazabilidad;
    }

    public void setEstadoTrazabilidad(String estadoTrazabilidad) {
        this.estadoTrazabilidad = estadoTrazabilidad;
    }
}
