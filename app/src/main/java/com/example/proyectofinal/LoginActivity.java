package com.example.proyectofinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText txtUsuario, txtContrasena;
    Button btnIniciarSesion, btnCrearCuenta;
    DBTareas dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verificar sesión activa
        boolean logueado = getSharedPreferences("sesion", MODE_PRIVATE)
                .getBoolean("logueado", false);
        if (logueado) {
            // Si ya está logueado, ir directo a MainActivity
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        txtUsuario = findViewById(R.id.txtUsuario);
        txtContrasena = findViewById(R.id.txtContrasena);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta);

        dbHelper = new DBTareas(this);

        btnIniciarSesion.setOnClickListener(v -> {
            String usuario = txtUsuario.getText().toString().trim();
            String contrasena = txtContrasena.getText().toString().trim();

            if (usuario.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.validarUsuario(usuario, contrasena)) {
                // Guardar estado de sesión activo
                getSharedPreferences("sesion", MODE_PRIVATE)
                        .edit()
                        .putBoolean("logueado", true)
                        .apply();

                Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                irAMain();
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        });

        btnCrearCuenta.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, CrearCuenta.class);
            startActivity(intent);
        });
    }

    private void irAMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
