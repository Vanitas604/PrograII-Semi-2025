package com.example.miprimeraaplicacion;

public class amigos {
    String idAmigo;
    String nombre;
    String direccion;
    String telefono;
    String email;
    String dui;
    String foto;
    String urlCompletaFotoFirestore;
    String miToken;

    public amigos() {
    }

    public amigos(String idAmigo, String nombre, String direccion, String telefono, String email, String dui, String foto, String urlCompletaFotoFirestore, String miToken) {
        this.idAmigo = idAmigo;
        this.nombre = nombre;
        this.direccion = direccion;
        @ @ -19, 55 + 21, 59 @@
        this.dui = dui;
        this.foto = foto;
        this.miToken = miToken;
        this.urlCompletaFotoFirestore = urlCompletaFotoFirestore;
    }

    public String getUrlCompletaFotoFirestore() {
        return urlCompletaFotoFirestore;
    }

    public void setUrlCompletaFotoFirestore(String urlCompletaFotoFirestore) {
        this.urlCompletaFotoFirestore = urlCompletaFotoFirestore;
    }

    public String getMiToken() {
        return miToken;
    }

    public void setMiToken(String miToken) {
        this.miToken = miToken;
    }

    public String getIdAmigo() {
        return idAmigo;
    }

    public void setIdAmigo(String idAmigo) {
        this.idAmigo = idAmigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}

