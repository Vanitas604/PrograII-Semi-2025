package com.example.miprimeraaplicacion;

public class Producto {
    private String idProducto;
    private String codigo;
    private String descripcion;
    private String marca;
    private String presentacion;
    private double precio;
    private String foto;

    // Constructor
    public Producto(String idProducto, String codigo, String descripcion,
                    String marca, String presentacion, double precio, String foto) {
        this.idProducto = idProducto;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.marca = marca;
        this.presentacion = presentacion;
        this.precio = precio;
        this.foto = foto;
    }

    // Constructor de copia (para evitar referencias duplicadas)
    public Producto(Producto otro) {
        this.idProducto = otro.idProducto;
        this.codigo = otro.codigo;
        this.descripcion = otro.descripcion;
        this.marca = otro.marca;
        this.presentacion = otro.presentacion;
        this.precio = otro.precio;
        this.foto = otro.foto;
    }

    // Getters y setters
    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getPrecio() {
        return String.valueOf(precio);
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    // metodo toString() para obtener una representación en cadena del producto
    @Override
    public String toString() {
        return "Producto{" +
                "idProducto='" + idProducto + '\'' +
                ", codigo='" + codigo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", marca='" + marca + '\'' +
                ", presentacion='" + presentacion + '\'' +
                ", precio='" + precio + '\'' +
                ", foto='" + foto + '\'' +
                '}';
    }

    public static void add(com.example.miprimeraaplicacion.Producto producto) {
    }
}

