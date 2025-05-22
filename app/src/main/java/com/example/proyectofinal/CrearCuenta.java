package com.example.proyectofinal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CrearCuenta extends AppCompatActivity {

    EditText txtNuevoUsuario, txtNuevaContrasena;
    Button btnRegistrarCuenta;
    DBTareas dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        txtNuevoUsuario = findViewById(R.id.txtNuevoUsuario);
        txtNuevaContrasena = findViewById(R.id.txtNuevaContrasena);
        btnRegistrarCuenta = findViewById(R.id.btnRegistrarCuenta);

        dbHelper = new DBTareas(this);

        btnRegistrarCuenta.setOnClickListener(v -> {
            String usuario = txtNuevoUsuario.getText().toString().trim();
            String contrasena = txtNuevaContrasena.getText().toString().trim();

            if (usuario.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!existeCuenta(usuario)) {
                dbHelper.agregarUsuario(usuario, contrasena);
                Toast.makeText(this, "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show();
                irALogin();
            } else {
                Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Usamos el método usuarioExiste() de DBTareas
    private boolean existeCuenta(String usuario) {
        return dbHelper.usuarioExiste(usuario);  // Cambié esta línea para usar usuarioExiste
    }

    private void irALogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
