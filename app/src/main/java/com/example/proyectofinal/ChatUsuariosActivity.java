package com.example.proyectofinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

public class ChatUsuariosActivity extends AppCompatActivity {

    private Button btnAgregarContacto;
    private FloatingActionButton fabAbrirContactos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_usuario);

        // Botón para agregar nuevo contacto
        btnAgregarContacto = findViewById(R.id.btnAgregarContacto);
        btnAgregarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChatUsuariosActivity.this, "Abriendo formulario de contacto...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChatUsuariosActivity.this, AgregarContactoActivity.class);
                startActivity(intent);
            }
        });

        // Botón flotante para abrir lista de contactos
        fabAbrirContactos = findViewById(R.id.fabAbrirContactos);
        fabAbrirContactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abre la nueva actividad que mostrará los contactos
                Intent intent = new Intent(ChatUsuariosActivity.this, ListaContactosActivity.class);
                startActivity(intent);
            }
        });
    }
}