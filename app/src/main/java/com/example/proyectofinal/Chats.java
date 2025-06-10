package com.example.proyectofinal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Chats extends Activity {

    ImageView img;
    TextView tempVal;
    String a = "", from = "", usuario = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chats);

        img = findViewById(R.id.imgAtras);
        img.setOnClickListener(view -> abrirVentana());

        tempVal = findViewById(R.id.lblToChats);

        Bundle parametros = getIntent().getExtras();
        if (parametros != null) {
            a = parametros.getString("a", "");
            from = parametros.getString("from", "");
            usuario = parametros.getString("nombre", "");
            tempVal.setText(usuario);
        }
    }

    private void mostrarMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    private void abrirVentana() {
        Intent intent = new Intent(this, ListaContactosActivity.class);
        startActivity(intent);
        finish();
    }
}