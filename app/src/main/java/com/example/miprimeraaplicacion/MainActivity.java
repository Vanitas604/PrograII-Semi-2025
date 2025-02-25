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
    TabHost tbh;

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tbh = findViewById(R.id.tbhMetros);
        tbh.setup();

        tbh.addTab(tbh.newTabSpec("Metros").setContent(R.id.tabMetros).setIndicator("Metros", null));
        tbh.addTab(tbh.newTabSpec("Area").setContent(R.id.tabConversor).setIndicator("Area", null));


        btn = findViewById(R.id.btnMetrosConvertir);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempVal = findViewById(R.id.txtMetrosCantidad);
                double num1 = Double.parseDouble(tempVal.getText().toString());


                double Calculo = 0;

                if(num1 <= 18 ){
                    Calculo = 6;
                }else if (num1 <=28){
                    Calculo = (num1 - 18) * 0.45 + 6;
                }else if (num1 >=29) {
                    Calculo = (num1 - 28) * 0.65 + 4.5 + 6;


                }
                tempVal = findViewById(R.id.lblrespuesta);
                tempVal.setText("Precio: "+ Calculo +"$");
            }
        });
    }
}


