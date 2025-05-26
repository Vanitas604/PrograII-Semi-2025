package com.example.proyectofinal;

public class Tareas {
    private int id;
    private String titulo;
    private String descripcion;
    private String grupo;
    private String fechaLimite;
    private boolean realizada;
    private String horaRecordatorio;
    private boolean repetirDiariamente;

    public Tareas() {
    }

    public Tareas(int id, String titulo, String descripcion, String grupo, String fechaLimite, boolean realizada,
                  String horaRecordatorio, boolean repetirDiariamente) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.grupo = grupo;
        this.fechaLimite = fechaLimite;
        this.realizada = realizada;
        this.horaRecordatorio = horaRecordatorio;
        this.repetirDiariamente = repetirDiariamente;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getGrupo() { return grupo; }
    public void setGrupo(String grupo) { this.grupo = grupo; }

    public String getFechaLimite() { return fechaLimite; }
    public void setFechaLimite(String fechaLimite) { this.fechaLimite = fechaLimite; }

    public boolean isRealizada() { return realizada; }
    public void setRealizada(boolean realizada) { this.realizada = realizada; }

    public String getHoraRecordatorio() { return horaRecordatorio; }
    public void setHoraRecordatorio(String horaRecordatorio) { this.horaRecordatorio = horaRecordatorio; }

    public boolean isRepetirDiariamente() { return repetirDiariamente; }
    public void setRepetirDiariamente(boolean repetirDiariamente) { this.repetirDiariamente = repetirDiariamente; }
}
