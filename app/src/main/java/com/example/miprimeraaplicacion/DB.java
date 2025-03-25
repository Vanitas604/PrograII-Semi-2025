package com.example.miprimeraaplicacion;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tienda";
    private static final int DATABASE_VERSION = 1;
    private static final String SQLdb = "CREATE TABLE productos (" +
            "idProducto INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "codigo TEXT, " +
            "descripcion TEXT, " +
            "marca TEXT, " +
            "presentacion TEXT, " +
            "precio REAL, " +
            "urlFoto TEXT)";

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLdb);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Si hay cambios en la estructura de la base de datos, actualizarlos aquí
    }

    // Método para administrar los productos (insertar, modificar, eliminar)
    public String administrar_productos(String accion, String[] datos) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            String mensaje = "ok";
            String sql = "";

            switch (accion) {
                case "nuevo":
                    sql = "INSERT INTO productos (codigo, descripcion, marca, presentacion, precio, urlFoto) VALUES (?, ?, ?, ?, ?, ?)";
                    db.execSQL(sql, new Object[]{datos[1], datos[2], datos[3], datos[4], datos[5], datos[6]});
                    break;

                case "modificar":
                    sql = "UPDATE productos SET codigo = ?, descripcion = ?, marca = ?, presentacion = ?, precio = ?, urlFoto = ? WHERE idProducto = ?";
                    db.execSQL(sql, new Object[]{datos[1], datos[2], datos[3], datos[4], datos[5], datos[6], datos[0]});
                    break;

                case "eliminar":
                    sql = "DELETE FROM productos WHERE idProducto = ?";
                    db.execSQL(sql, new Object[]{datos[0]});
                    break;
            }
            return mensaje;
        } catch (Exception e) {
            return e.getMessage();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    // Método para obtener los productos de la base de datos
    public ArrayList<Producto> lista_productos() {
        ArrayList<Producto> productos = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = null;
        try {
            c = db.rawQuery("SELECT * FROM productos", null);
            if (c.moveToFirst()) {
                do {
                    @SuppressLint("Range") com.example.miprimeraaplicacion.Producto producto = new Producto(
                            c.getString(c.getColumnIndex("idProducto")),
                            c.getString(c.getColumnIndex("codigo")),
                            c.getString(c.getColumnIndex("descripcion")),
                            c.getString(c.getColumnIndex("marca")),
                            c.getString(c.getColumnIndex("presentacion")),
                            c.getDouble(c.getColumnIndex("precio")),
                            c.getString(c.getColumnIndex("urlFoto"))
                    );
                    Producto.add(producto);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DB", "Error al obtener productos: " + e.getMessage());
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }
        return productos;
    }
}
