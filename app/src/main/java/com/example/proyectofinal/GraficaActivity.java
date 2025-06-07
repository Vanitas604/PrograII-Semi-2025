package com.example.proyectofinal;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class GraficaActivity extends AppCompatActivity {

    private PieChart pieChart;
    private DBTareas dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafica);

        pieChart = findViewById(R.id.pieChart);
        dbHelper = new DBTareas(this);

        cargarGrafico();
    }

    private void cargarGrafico() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int totalPendientes = 0, totalRealizadas = 0;

        Cursor cursor = db.rawQuery("SELECT realizada FROM " + DBTareas.TABLA_TAREAS, null);
        if (cursor.moveToFirst()) {
            do {
                int realizada = cursor.getInt(0);
                if (realizada == 1) totalRealizadas++;
                else totalPendientes++;
            } while (cursor.moveToNext());
        }
        cursor.close();

        ArrayList<PieEntry> entries = new ArrayList<>();
        if (totalPendientes + totalRealizadas == 0) {
            entries.add(new PieEntry(1, "Sin datos"));
        } else {
            if (totalPendientes > 0) entries.add(new PieEntry(totalPendientes, "Pendientes"));
            if (totalRealizadas > 0) entries.add(new PieEntry(totalRealizadas, "Realizadas"));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Estado de tareas");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(16f);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Tareas");
        pieChart.setEntryLabelTextSize(14f);
        pieChart.animateY(1000);
        Legend legend = pieChart.getLegend();
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setTextSize(14f);

        pieChart.invalidate(); // Redibujar
    }
}