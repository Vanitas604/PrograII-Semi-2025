package com.example.proyectofinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChatUsuariosActivity extends AppCompatActivity {

    private Button btnContacto, btnChat, btnAgregarContacto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_usuario); // Asegúrate de que el nombre coincida con tu layout XML

        // Inicializar botones
        btnContacto = findViewById(R.id.btnContacto);
        btnAgregarContacto = findViewById(R.id.btnAgregarContacto);

        // Botón Contacto
        btnContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatUsuariosActivity.this, "Abrir Contactos", Toast.LENGTH_SHORT).show();
                // startActivity(new Intent(ChatUsuariosActivity.this, ContactosActivity.class));
            }
        });

        // Botón Agregar Contacto
        btnAgregarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatUsuariosActivity.this, "Agregar nuevo contacto", Toast.LENGTH_SHORT).show();
                // startActivity(new Intent(ChatUsuariosActivity.this, AgregarContactoActivity.class));
            }
        });
    }
}