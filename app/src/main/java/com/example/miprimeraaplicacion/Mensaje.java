package com.example.miprimeraaplicacion;

public class Mensaje {
    private String texto;
    private boolean esEnviado;

    public Mensaje(String texto, boolean esEnviado) {
        this.texto = texto;
        this.esEnviado = esEnviado;
    }

    public String getTexto() {
        return texto;
    }

    public boolean esEnviado() {
        return esEnviado;
    }
}