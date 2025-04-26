package com.example.proyectofinal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBTareas extends SQLiteOpenHelper {

    private static final String NOMBRE_BASE_DATOS = "DBTareas.db";
    private static final int VERSION_BASE_DATOS = 1;

    public static final String TABLA_TAREAS = "tareas";
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
        String CREATE_TABLE_TAREAS = "CREATE TABLE " + TABLA_TAREAS + "("
                + COLUMNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMNA_TITULO + " TEXT, "
                + COLUMNA_DESCRIPCION + " TEXT, "
                + COLUMNA_GRUPO + " TEXT, "
                + COLUMNA_FECHA_LIMITE + " TEXT, "
                + COLUMNA_REALIZADA + " INTEGER" // 0 = no realizada, 1 = realizada
                + ")";
        db.execSQL(CREATE_TABLE_TAREAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Si actualizamos la versión, eliminamos la tabla anterior
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_TAREAS);
        onCreate(db);
    }
}
