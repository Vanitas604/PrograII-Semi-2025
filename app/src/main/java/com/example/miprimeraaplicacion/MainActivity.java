package com.example.miprimeraaplicacion;


import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {
    TextView tempVal;
    Button btn;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tempVal = findViewById(R.id.lblReproductorMusica);
        reproductorMusca();
    }

    void reproductorMusca(){
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.audio);
        mediaPlayer.start();
        btn = findViewById(R.id.btnIniciar);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciar();
            }
        });
        btn = findViewById(R.id.btnPausar);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausar();
            }
        });
        btn = findViewById(R.id.btnParar);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detener();
            }
        });
    }
    void reproductorMusca(){
        mediaPlayer = MediaPlayer.create(this, R.raw.audio);
    }
    void iniciar(){
        mediaPlayer.start();
        tempVal.setText("Reproduciendo...");
    }
    void pausar(){
        mediaPlayer.pause();
        tempVal.setText("Pausado...");
    }
    void detener(){
        mediaPlayer.stop();
        tempVal.setText("Detenido...");
        reproductorMusca();
    }

}




