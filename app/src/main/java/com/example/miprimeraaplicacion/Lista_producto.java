package com.example.miprimeraaplicacion;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class Lista_producto extends Activity {
    Bundle parametros = new Bundle();
    ListView ltsProducto;
    Cursor cProducto;
    DB db;
    final ArrayList<Producto> alProducto = new ArrayList<>();
    final ArrayList<Producto> alProductoCopia = new ArrayList<>();
    JSONArray jsonArray;
    JSONObject jsonObject;
    Producto misProducto;
    FloatingActionButton fab;
    int posicion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_producto);

        parametros.putString("accion", "nuevo");
        db = new DB(this);

        fab = findViewById(R.id.fabAgregarProducto);
        fab.setOnClickListener(view -> abrirVentana());
        obtenerDatosProductos();
        buscarProductos();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        try {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            posicion = info.position;
            menu.setHeaderTitle(jsonArray.getJSONObject(posicion).getString("descripcion"));
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        try {
            if (item.getItemId() == R.id.mnxNuevo) {
                abrirVentana();
            } else if (item.getItemId() == R.id.mnxModifica) {
                parametros.putString("accion", "modificar");
                parametros.putString("producto", jsonArray.getJSONObject(posicion).toString());
                abrirVentana();
            } else if (item.getItemId() == R.id.mnxEliminar) {
                eliminarProducto();
            }
            return true;
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
            return super.onContextItemSelected(item);
        }
    }

    private void eliminarProducto() {
        try {
            String descripcion = jsonArray.getJSONObject(posicion).getString("descripcion");
            AlertDialog.Builder confirmacion = new AlertDialog.Builder(this);
            confirmacion.setTitle("¿Está seguro de eliminar el producto?");
            confirmacion.setMessage(descripcion);
            confirmacion.setPositiveButton("Sí", (dialog, which) -> {
                try {
                    String respuesta = db.administrar_productos("eliminar",
                            new String[]{jsonArray.getJSONObject(posicion).getString("idProducto")});
                    if (respuesta.equals("ok")) {
                        obtenerDatosProductos();
                        mostrarMsg("Producto eliminado con éxito.");
                    } else {
                        mostrarMsg("Error: " + respuesta);
                    }
                } catch (Exception e) {
                    mostrarMsg("Error: " + e.getMessage());
                }
            });
            confirmacion.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            confirmacion.create().show();
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }

    private void abrirVentana() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(parametros);
        startActivity(intent);
    }

    private void obtenerDatosProductos() {
        try {
            cProducto = db.lista_productos();
            if (cProducto.moveToFirst()) {
                jsonArray = new JSONArray();
                do {
                    jsonObject = new JSONObject();
                    jsonObject.put("idProducto", cProducto.getString(0));
                    jsonObject.put("codigo", cProducto.getString(1));
                    jsonObject.put("descripcion", cProducto.getString(2));
                    jsonObject.put("marca", cProducto.getString(3));
                    jsonObject.put("presentacion", cProducto.getString(4));
                    jsonObject.put("precio", cProducto.getString(5));
                    jsonObject.put("foto", cProducto.getString(6));
                    jsonArray.put(jsonObject);
                } while (cProducto.moveToNext());
                mostrarDatosProducto();
            } else {
                mostrarMsg("No hay productos registrados.");
                abrirVentana();
            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }

    private void mostrarDatosProducto() {
        try {
            if (jsonArray.length() > 0) {
                ltsProducto = findViewById(R.id.ltsProductos);
                alProducto.clear();
                alProductoCopia.clear();

                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    misProducto = new Producto(
                            jsonObject.getString("idProducto"),
                            jsonObject.getString("codigo"),
                            jsonObject.getString("descripcion"),
                            jsonObject.getString("marca"),
                            jsonObject.getString("presentacion"),
                            jsonObject.getString("precio"),
                            jsonObject.getString("foto")
                    );
                    alProducto.add(misProducto);
                }
                alProductoCopia.addAll(alProducto);
                ltsProducto.setAdapter(new AdaptadorProducto(this, alProducto));
                registerForContextMenu(ltsProducto);
            } else {
                mostrarMsg("No hay productos registrados.");
                abrirVentana();
            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }

    private void buscarProductos() {
        TextView tempVal = findViewById(R.id.txtBuscarProductos);
        tempVal.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                alProducto.clear();
                String buscar = tempVal.getText().toString().trim().toLowerCase();
                if (buscar.length() <= 0) {
                    alProducto.addAll(alProductoCopia);
                } else {
                    for (Producto item : alProductoCopia) {
                        if (item.getDescripcion().toLowerCase().contains(buscar) ||
                                item.getCodigo().toLowerCase().contains(buscar) ||
                                item.getMarca().toLowerCase().contains(buscar)) {
                            alProducto.add(item);
                        }
                    }
                    ltsProducto.setAdapter(new AdaptadorProducto(getApplicationContext(), alProducto));
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {
            }
        });
    }

    private void mostrarMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}


