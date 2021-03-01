package com.valdemar.emprendedores.model;

import com.google.gson.annotations.SerializedName;

public class Empresa {

    @SerializedName("key")
    String key;
    @SerializedName("nombre")
    String nombre;
    @SerializedName("tipoDocumento")
    String tipoDocumento;
    @SerializedName("numeroDocumento")
    String numeroDocumento;
    @SerializedName("categoria")
    String categoria;
    @SerializedName("correoElectronico")
    String correoElectronico;
    @SerializedName("telefono")
    String telefono;
    @SerializedName("celular")
    String celular;
    @SerializedName("contacto")
    String contacto;
    @SerializedName("sitioWeb")
    String sitioWeb;
    @SerializedName("modalidadEmpresa")
    String modalidadEmpresa;
    @SerializedName("comercioExterior")
    String comercioExterior;
    @SerializedName("contrataEstado")
    String contrataEstado;
    @SerializedName("descripcion")
    String descripcion;
    @SerializedName("pais")
    String pais;
    @SerializedName("ciudad")
    String ciudad;
    @SerializedName("direccion")
    String direccion;
    @SerializedName("edt_facebook")
    String edt_facebook;
    @SerializedName("edt_instagram")
    String edt_instagram;
    @SerializedName("edt_linkedin")
    String edt_linkedin;
    @SerializedName("imagen")
    String imagen;
    @SerializedName("videoSubido")
    String videoSubido;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getSitioWeb() {
        return sitioWeb;
    }

    public void setSitioWeb(String sitioWeb) {
        this.sitioWeb = sitioWeb;
    }

    public String getComercioExterior() {
        return comercioExterior;
    }

    public void setComercioExterior(String comercioExterior) {
        this.comercioExterior = comercioExterior;
    }

    public String getContrataEstado() {
        return contrataEstado;
    }

    public void setContrataEstado(String contrataEstado) {
        this.contrataEstado = contrataEstado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

    public String getEdt_linkedin() {
        return edt_linkedin;
    }

    public void setEdt_linkedin(String edt_linkedin) {
        this.edt_linkedin = edt_linkedin;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getModalidadEmpresa() {
        return modalidadEmpresa;
    }

    public void setModalidadEmpresa(String modalidadEmpresa) {
        this.modalidadEmpresa = modalidadEmpresa;
    }

    public String getVideoSubido() {
        return videoSubido;
    }

    public void setVideoSubido(String videoSubido) {
        this.videoSubido = videoSubido;
    }
}
