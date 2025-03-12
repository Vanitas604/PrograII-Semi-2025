package com.example.miprimeraaplicacion;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.security.Key;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    Button btn;
    TextView tempVal;
    DB db;
    String accion = "nuevo", idAmigo = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DB(this);
        btn = findViewById(R.id.btnGuardarAmigo);
        btn.setOnClickListener(view->guardarAmigo());

        fab = findViewById(R.id.fabListaAmigos);
        fab.setOnClickListener(view->abrirVentana());

        mostrarDatos();
    }
    private void mostrarDatos(){
        try {
            Bundle parametros = getIntent().getExtras();
            accion = parametros.getString("accion");
            if (accion.equals("modificar")) {
                JSONObject datos = new JSONObject(parametros.getString("amigos"));
                idAmigo = datos.getString("idAmigo");

                tempVal = findViewById(R.id.txtNombre);
                tempVal.setText(datos.getString("nombre"));

                tempVal = findViewById(R.id.txtDireccion);
                tempVal.setText(datos.getString("direccion"));

                tempVal = findViewById(R.id.txtTelefono);
                tempVal.setText(datos.getString("telefono"));

                tempVal = findViewById(R.id.txtEmail);
                tempVal.setText(datos.getString("email"));

                tempVal = findViewById(R.id.txtDui);
                tempVal.setText(datos.getString("dui"));
            }
        }catch (Exception e){
            mostrarMsg("Error: "+e.getMessage());
        }
    }
    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
    private void abrirVentana(){
        Intent intent = new Intent(this, Lista_Amigos.class);
        startActivity(intent);
    }
    private void guardarAmigo() {
        tempVal = findViewById(R.id.txtNombre);
        String nombre = tempVal.getText().toString();

        tempVal = findViewById(R.id.txtDireccion);
        String direccion = tempVal.getText().toString();

        tempVal = findViewById(R.id.txtTelefono);
        String telefono = tempVal.getText().toString();
        tempVal = findViewById(R.id.txtEmail);
        String email = tempVal.getText().toString();

        tempVal = findViewById(R.id.txtDui);
        String dui = tempVal.getText().toString();

        String[] datos = {"", nombre, direccion, telefono, email, dui, ""};
        db.administrar_amigos("agregar", datos);
        Toast.makeText(getApplicationContext(), "Registro guardado con exito.", Toast.LENGTH_LONG).show();
        abrirVentana();
    }
}





