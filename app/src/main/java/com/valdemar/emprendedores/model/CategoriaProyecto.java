package com.valdemar.emprendedores.model;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CategoriaProyecto implements Serializable{
    @SerializedName("idCategoria")
    int idCategoria;
    @SerializedName("nombre")
    String nombre;
    @SerializedName("imagen")
    int imagen;

    public CategoriaProyecto() {
    }

    public CategoriaProyecto(int idCategoria,String nombre, int imagen) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.imagen = imagen;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }
}