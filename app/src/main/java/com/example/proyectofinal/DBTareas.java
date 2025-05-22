package com.example.proyectofinal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBTareas extends SQLiteOpenHelper {

    private static final String NOMBRE_BASE_DATOS = "DBTareas.db";
    private static final int VERSION_BASE_DATOS = 1;

    public static final String TABLA_TAREAS = "tareas";
    public static final String TABLA_GRUPO = "grupo";
    public static final String TABLA_USUARIOS = "usuarios";

    public static final String COLUMNA_ID = "id";
    public static final String COLUMNA_TITULO = "titulo";
    public static final String COLUMNA_DESCRIPCION = "descripcion";
    public static final String COLUMNA_GRUPO = "grupo";
    public static final String COLUMNA_FECHA_LIMITE = "fecha_limite";
    public static final String COLUMNA_REALIZADA = "realizada";

    public DBTareas(Context context) {
        super(context, NOMBRE_BASE_DATOS, null, VERSION_BASE_DATOS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla tareas
        String CREATE_TABLE_TAREAS = "CREATE TABLE " + TABLA_TAREAS + " (" +
                COLUMNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMNA_TITULO + " TEXT, " +
                COLUMNA_DESCRIPCION + " TEXT, " +
                COLUMNA_GRUPO + " TEXT, " +
                COLUMNA_FECHA_LIMITE + " TEXT, " +
                COLUMNA_REALIZADA + " INTEGER)";
        db.execSQL(CREATE_TABLE_TAREAS);

        // Crear tabla grupo
        String CREATE_TABLE_GRUPO = "CREATE TABLE " + TABLA_GRUPO + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT NOT NULL)";
        db.execSQL(CREATE_TABLE_GRUPO);

        // Crear tabla usuarios
        String CREATE_TABLE_USUARIOS = "CREATE TABLE " + TABLA_USUARIOS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "usuario TEXT NOT NULL UNIQUE, " +  // UNIQUE para evitar duplicados
                "contrasena TEXT NOT NULL)";
        db.execSQL(CREATE_TABLE_USUARIOS);

        // Insertar usuario por defecto
        db.execSQL("INSERT INTO " + TABLA_USUARIOS + " (usuario, contrasena) VALUES ('admin', '1234')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_TAREAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_GRUPO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_USUARIOS);
        onCreate(db);
    }

    public boolean validarUsuario(String usuario, String contrasena) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLA_USUARIOS +
                " WHERE usuario = ? AND contrasena = ?", new String[]{usuario, contrasena});
        boolean existe = cursor.moveToFirst();
        cursor.close();
        db.close();
        return existe;
    }

    public void agregarUsuario(String usuario, String contrasena) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO " + TABLA_USUARIOS + " (usuario, contrasena) VALUES (?, ?)",
                new Object[]{usuario, contrasena});
        db.close();
    }

    public boolean usuarioExiste(String usuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLA_USUARIOS + " WHERE usuario = ?", new String[]{usuario});
        boolean existe = cursor.moveToFirst();
        cursor.close();
        db.close();
        return existe;
    }

}

