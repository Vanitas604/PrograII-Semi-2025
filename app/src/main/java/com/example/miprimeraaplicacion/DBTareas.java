package com.example.miprimeraaplicacion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

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
                COLUMNA_GRUPO + " INTEGER, " +
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
                "usuario TEXT NOT NULL UNIQUE, " +
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

    // --- Gestión de usuarios ---
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

    // --- Gestión de grupos ---
    public List<Grupo> obtenerGrupos() {
        List<Grupo> listaGrupos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLA_GRUPO, null);

        if (cursor.moveToFirst()) {
            do {
                Grupo grupo = new Grupo();
                grupo.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                grupo.setNombre(cursor.getString(cursor.getColumnIndexOrThrow("nombre")));
                listaGrupos.add(grupo);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listaGrupos;
    }

    public long insertarGrupo(String nombre) {
        SQLiteDatabase DBTareas = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        long id = DBTareas.insert(TABLA_GRUPO, null, values);
        DBTareas.close();
        return id;
    }

    // --- Gestión de tareas ---
    public long insertarTarea(String titulo, String descripcion, int grupoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMNA_TITULO, titulo);
        values.put(COLUMNA_DESCRIPCION, descripcion);
        values.put(COLUMNA_GRUPO, grupoId);
        values.put(COLUMNA_FECHA_LIMITE, ""); // Puedes cambiar por una fecha real si deseas
        values.put(COLUMNA_REALIZADA, 0); // 0 = no realizada
        long id = db.insert(TABLA_TAREAS, null, values);
        db.close();
        return id;
    }

    public List<Tareas> obtenerTareasPorGrupo(int grupoId) {
        List<Tareas> listaTareas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLA_TAREAS +
                " WHERE " + COLUMNA_GRUPO + " = ?", new String[]{String.valueOf(grupoId)});

        if (cursor.moveToFirst()) {
            do {
                Tareas tarea = new Tareas();
                tarea.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMNA_ID)));
                tarea.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow(COLUMNA_TITULO)));
                tarea.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(COLUMNA_DESCRIPCION)));
                tarea.setGrupo(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMNA_GRUPO))));
                tarea.setFechaLimite(cursor.getString(cursor.getColumnIndexOrThrow(COLUMNA_FECHA_LIMITE)));
                tarea.setRealizada(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMNA_REALIZADA)) == 1);
                listaTareas.add(tarea);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listaTareas;
    }

    // ✅ Método para actualizar si una tarea está realizada o no
    public boolean actualizarEstadoRealizada(int idTarea, boolean realizada) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(COLUMNA_REALIZADA, realizada ? 1 : 0);

        int filas = db.update(
                TABLA_TAREAS,
                valores,
                COLUMNA_ID + " = ?",
                new String[]{String.valueOf(idTarea)}
        );

        db.close();
        return filas > 0;
    }
}






