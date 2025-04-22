package com.example.miprimeraaplicacion;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "productos";
    private static final int DATABASE_VERSION = 5; // Actualizamos la versión de la base de datos a 5
    private static final String SQLdb = "CREATE TABLE productos (idProducto TEXT, codigo TEXT, descripcion TEXT, marca TEXT, presentacion TEXT, precio TEXT, costo REAL, stock INTEGER, urlFoto TEXT)"; // Nueva tabla con costo y stock

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLdb); // Creamos la tabla con los nuevos campos
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 5) { // Si la versión es menor a 5, agregamos los campos de costo y stock
            // Agregar la columna costo
            db.execSQL("ALTER TABLE productos ADD COLUMN costo REAL;");
            // Agregar la columna stock
            db.execSQL("ALTER TABLE productos ADD COLUMN stock INTEGER;");
        }
    }

    public String administrar_productos(String accion, String[] datos) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            String mensaje = "ok", sql = " ";
            switch (accion) {
                case "nuevo":
                    // Inserta un nuevo producto con costo y stock
                    sql = "INSERT INTO productos (idProducto, codigo, descripcion, marca, presentacion, precio, costo, stock, urlFoto) VALUES ('" +
                            datos[0] + "','" + datos[1] + "', '" + datos[2] + "', '" + datos[3] + "', '" + datos[4] + "', '" +
                            datos[5] + "', '" + datos[6] + "', '" + datos[7] + "', '" + datos[8] + "')";
                    break;
                case "modificar":
                    // Modifica un producto con costo y stock
                    sql = "UPDATE productos SET codigo = '" + datos[1] + "', descripcion = '" + datos[2] + "', marca = '" + datos[3] + "', presentacion = '" + datos[4] + "', precio = '" + datos[5] + "', costo = '" + datos[6] + "', stock = '" + datos[7] + "', urlFoto = '" + datos[8] + "' WHERE idProducto = '" + datos[0] + "'";
                    break;
                case "eliminar":
                    // Elimina un producto por id
                    sql = "DELETE FROM productos WHERE idProducto = '" + datos[0] + "'";
                    break;
                case "eliminarTodo":
                    // Elimina todos los productos
                    sql = "DELETE FROM productos";
                    break;
            }

            db.execSQL(sql);
            db.close();
            return mensaje;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String administrarActualizados(String mod, String datos, String idProducto) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            String mensaje = "ok", sql = "";
            switch (mod) {
                case "modificar":
                    sql = "UPDATE actualizado SET actualizado = '" + datos + "' WHERE id = '0'";
                    break;
                case "nuevo":
                    sql = "INSERT INTO actualizado (id, actualizado) VALUES ('" + idProducto + "' + '" + datos + "')";
                    break;
                case "eliminar":
                    sql = "DELETE FROM actualizado WHERE id != '0'";
                    break;
            }

            db.execSQL(sql);
            db.close();
            return mensaje;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public Cursor lista_productos() {
        SQLiteDatabase db = getReadableDatabase();
        // Consultamos los productos con el nuevo campo de costo y stock
        return db.rawQuery("SELECT * FROM productos", null);
    }

    public Cursor lista_productosActializados() {
        SQLiteDatabase db = getReadableDatabase();
        // Consultamos los productos con el nuevo campo de costo y stock
        return db.rawQuery("SELECT * FROM productos", null);
    }
}

