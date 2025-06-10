package com.example.proyectofinal;
public class Contacto {
    private int id;
    private String nombre;
    private String apellido;
    private String correo;
    private String apodo;
    private String tipo;


    public Contacto(int id, String nombre, String apellido, String correo, String apodo, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.apodo = apodo;
        this.tipo = tipo;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getCorreo() { return correo; }
    public String getApodo() { return apodo; }
    public String getTipo() { return tipo; }
}
