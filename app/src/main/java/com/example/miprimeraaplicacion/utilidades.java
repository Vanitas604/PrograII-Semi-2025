package com.example.miprimeraaplicacion;

import android.os.Build;
import androidx.annotation.RequiresApi;
import java.util.Base64;

@RequiresApi(api = Build.VERSION_CODES.O)
public class utilidades {

    // URL para consultar la vista "todos_los_productos" en el diseño "jennifer"
    static String url_consulta = "http://192.168.179.253:5984/productosdb/_design/jennifer/_view/todos_los_productos";

    // URL base para realizar operaciones CRUD
    static String url_mto = "http://192.168.179.253:5984/productosdb";

    // Credenciales
    static String user = "admin";
    static String passwd = "1234";

    // Codificación en base64 para autenticación básica
    static String credencialesCodificadas = Base64.getEncoder()
            .encodeToString((user + ":" + passwd).getBytes());

    // Generar un ID único para nuevos documentos
    public String generarUnicoId() {
        return java.util.UUID.randomUUID().toString();
    }
}

