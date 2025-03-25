package com.example.miprimeraaplicacion;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
    Producto misProducto;
    FloatingActionButton fab;
    int posicion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_producto);

        parametros.putString("accion", "nuevo");
        db = new DB(this);

        ltsProducto = findViewById(R.id.ltsProductos);
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
            menu.setHeaderTitle(alProducto.get(posicion).getDescripcion());
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
                parametros.putString("producto", alProducto.get(posicion).toString());
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
            String descripcion = alProducto.get(posicion).getDescripcion();
            AlertDialog.Builder confirmacion = new AlertDialog.Builder(this);
            confirmacion.setTitle("¿Está seguro de eliminar el producto?");
            confirmacion.setMessage(descripcion);
            confirmacion.setPositiveButton("Sí", this::onClick);
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

    @SuppressLint("Range")
    private void obtenerDatosProductos() {
        try {
            cProducto = (Cursor) db.lista_productos();
            if (cProducto != null && cProducto.moveToFirst()) {
                alProducto.clear();
                do {
                    misProducto = new Producto(
                            cProducto.getString(cProducto.getColumnIndex("idProducto")),
                            cProducto.getString(cProducto.getColumnIndex("codigo")),
                            cProducto.getString(cProducto.getColumnIndex("descripcion")),
                            cProducto.getString(cProducto.getColumnIndex("marca")),
                            cProducto.getString(cProducto.getColumnIndex("presentacion")),
                            cProducto.getString(cProducto.getColumnIndex("precio")),
                            cProducto.getString(cProducto.getColumnIndex("foto"))
                    );
                    alProducto.add(misProducto);
                } while (cProducto.moveToNext());
                mostrarDatosProducto();
            } else {
                mostrarMsg("No hay productos registrados.");
                abrirVentana();
            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        } finally {
            if (cProducto != null && !cProducto.isClosed()) {
                cProducto.close(); // Asegurarse de cerrar el Cursor
            }
        }
    }

    private void mostrarDatosProducto() {
        try {
            if (!alProducto.isEmpty()) {
                alProductoCopia.clear();
                alProductoCopia.addAll(alProducto);
                AdaptadorProducto adaptador = new AdaptadorProducto(this, alProducto);
                ltsProducto.setAdapter(adaptador);
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String buscar = tempVal.getText().toString().trim().toLowerCase();
                alProducto.clear();

                if (buscar.isEmpty()) {
                    alProducto.addAll(alProductoCopia);
                } else {
                    for (Producto item : alProductoCopia) {
                        if (item.getDescripcion().toLowerCase().contains(buscar) ||
                                item.getCodigo().toLowerCase().contains(buscar) ||
                                item.getMarca().toLowerCase().contains(buscar)) {
                            alProducto.add(item); // No es necesario crear un nuevo objeto Producto
                        }
                    }
                }

                // Actualizar la lista sin crear un nuevo adaptador
                ((AdaptadorProducto) ltsProducto.getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

    private void mostrarMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    private void onClick(DialogInterface dialog, int which) {
        try {
            String respuesta = db.administrar_productos("eliminar",
                    new String[]{alProducto.get(posicion).getIdProducto()});
            if (respuesta.equals("ok")) {
                obtenerDatosProductos(); // Refrescar la lista
                mostrarMsg("Producto eliminado con éxito.");
            } else {
                mostrarMsg("Error: " + respuesta);
            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }
}
