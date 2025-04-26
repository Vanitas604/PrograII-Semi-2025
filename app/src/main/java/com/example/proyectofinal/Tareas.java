package com.example.proyectofinal;

public class Tareas {
    private int id;
    private String titulo;
    private String descripcion;
    private String grupo;
    private String fechaLimite;
    private boolean realizada;

    // Constructor vacío (importante para Firebase o CouchDB)
    public Tareas() {
    }

    // Constructor completo
    public Tareas(int id, String titulo, String descripcion, String grupo, String fechaLimite, boolean realizada) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.grupo = grupo;
        this.fechaLimite = fechaLimite;
        this.realizada = realizada;
    }

    // Getter y Setter de id
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    // Getter y Setter de titulo
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    // Getter y Setter de descripcion
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // Getter y Setter de grupo
    public String getGrupo() {
        return grupo;
    }
    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    // Getter y Setter de fechaLimite
    public String getFechaLimite() {
        return fechaLimite;
    }
    public void setFechaLimite(String fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    // Getter y Setter de realizada
    public boolean isRealizada() {
        return realizada;
    }
    public void setRealizada(boolean realizada) {
        this.realizada = realizada;
    }
}
