package com.example.miprimeraaplicacion;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerMensajes;
    private EditText etMensaje;
    private ImageButton btnEnviar;
    private MensajeAdapter adaptador;
    private ArrayList<Mensaje> listaMensajes; // usamos la clase Mensaje

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerMensajes = findViewById(R.id.recyclerMensajes);
        etMensaje = findViewById(R.id.etMensaje);
        btnEnviar = findViewById(R.id.btnEnviar);

        listaMensajes = new ArrayList<>();
        adaptador = new MensajeAdapter(listaMensajes);
        recyclerMensajes.setLayoutManager(new LinearLayoutManager(this));
        recyclerMensajes.setAdapter(adaptador);

        // Puedes agregar mensajes iniciales simulados si quieres
        listaMensajes.add(new Mensaje("¡Hola! ¿Cómo estás?", false));  // recibido
        listaMensajes.add(new Mensaje("¡Todo bien! ¿Y tú?", true));     // enviado
        adaptador.notifyDataSetChanged();

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto = etMensaje.getText().toString().trim();
                if (!texto.isEmpty()) {
                    Mensaje nuevo = new Mensaje(texto, true); // Enviado por el usuario
                    listaMensajes.add(nuevo);
                    adaptador.notifyItemInserted(listaMensajes.size() - 1);
                    recyclerMensajes.scrollToPosition(listaMensajes.size() - 1);
                    etMensaje.setText("");
                }
            }
        });
    }
}
