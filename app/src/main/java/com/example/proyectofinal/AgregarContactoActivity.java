package com.example.proyectofinal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AgregarContactoActivity extends AppCompatActivity {

    private EditText editNombre, editApellido, editCorreo, editApodo;
    private RadioGroup radioTipo;
    private RadioButton radioPrivada, radioPersona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_contacto);

        // Referencias a los elementos
        editNombre = findViewById(R.id.editNombre);
        editApellido = findViewById(R.id.editApellido);
        editCorreo = findViewById(R.id.editCorreo);
        editApodo = findViewById(R.id.editApodo);
        radioTipo = findViewById(R.id.radioTipo);
        radioPrivada = findViewById(R.id.radioPrivada);
        radioPersona = findViewById(R.id.radioPersona);

        Button btnGuardar = findViewById(R.id.btnGuardar);

        btnGuardar.setOnClickListener(v -> {
            String nombre = editNombre.getText().toString().trim();
            String apellido = editApellido.getText().toString().trim();
            String correo = editCorreo.getText().toString().trim();
            String apodo = editApodo.getText().toString().trim();

            // Obtener tipo de contacto
            String tipo = "";
            int selectedId = radioTipo.getCheckedRadioButtonId();
            if (selectedId == R.id.radioPrivada) {
                tipo = "Privada";
            } else if (selectedId == R.id.radioPersona) {
                tipo = "Persona";
            }

            // Validación
            if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || apodo.isEmpty() || tipo.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Mostrar resumen
            String mensaje = "Contacto agregado:\n" +
                    "Nombre: " + nombre + " " + apellido + "\n" +
                    "Tipo: " + tipo + "\n" +
                    "Correo: " + correo + "\n" +
                    "Apodo: " + apodo;

            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();

            // Cerrar activity
            finish();
        });
    }
}