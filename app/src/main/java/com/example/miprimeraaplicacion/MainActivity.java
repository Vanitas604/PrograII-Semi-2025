package com.example.miprimeraaplicacion;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Button btnGuardar;
    EditText txtCodigo, txtDescripcion, txtMarca, txtPresentacion, txtPrecio;
    ImageView imgProducto;
    DB db;
    String accion = "nuevo", idProducto = "";
    String urlCompletaFoto = "";
    Intent tomarFotoIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgProducto = findViewById(R.id.imgFotoProducto);
        db = new DB(this);
        btnGuardar = findViewById(R.id.btnGuardarProducto);
        btnGuardar.setOnClickListener(view -> guardarProducto());

        txtCodigo = findViewById(R.id.txtCodigo);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtMarca = findViewById(R.id.txtMarca);
        txtPresentacion = findViewById(R.id.txtPresentacion);
        txtPrecio = findViewById(R.id.txtPrecio);

        mostrarDatos();
        tomarFoto();
    }

    private void mostrarDatos() {
        try {
            Bundle parametros = getIntent().getExtras();
            accion = parametros.getString("accion");
            if (accion.equals("modificar")) {
                idProducto = parametros.getString("idProducto");
                txtCodigo.setText(parametros.getString("codigo"));
                txtDescripcion.setText(parametros.getString("descripcion"));
                txtMarca.setText(parametros.getString("marca"));
                txtPresentacion.setText(parametros.getString("presentacion"));
                txtPrecio.setText(parametros.getString("precio"));
                urlCompletaFoto = parametros.getString("imagen");
                imgProducto.setImageURI(Uri.parse(urlCompletaFoto));
            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }

    private void tomarFoto() {
        imgProducto.setOnClickListener(view -> {
            tomarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File fotoProducto = null;
            try {
                fotoProducto = crearImagenProducto();
                if (fotoProducto != null) {
                    Uri uriFotoProducto = FileProvider.getUriForFile(MainActivity.this,
                            "com.example.tiendaproductos.fileprovider", fotoProducto);
                    tomarFotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriFotoProducto);
                    startActivityForResult(tomarFotoIntent, 1);
                } else {
                    mostrarMsg("No se pudo crear la imagen.");
                }
            } catch (Exception e) {
                mostrarMsg("Error: " + e.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1 && resultCode == RESULT_OK) {
                imgProducto.setImageURI(Uri.parse(urlCompletaFoto));
            } else {
                mostrarMsg("No se tomó la foto.");
            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }

    private File crearImagenProducto() throws Exception {
        String fechaHora = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "producto_" + fechaHora + "_";
        File dirAlmacenamiento = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!dirAlmacenamiento.exists()) {
            dirAlmacenamiento.mkdir();
        }
        File image = File.createTempFile(fileName, ".jpg", dirAlmacenamiento);
        urlCompletaFoto = image.getAbsolutePath();
        return image;
    }

    private void mostrarMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    private void guardarProducto() {
        String codigo = txtCodigo.getText().toString();
        String descripcion = txtDescripcion.getText().toString();
        String marca = txtMarca.getText().toString();
        String presentacion = txtPresentacion.getText().toString();
        String precio = txtPrecio.getText().toString();

        String[] datos = {idProducto, codigo, descripcion, marca, presentacion, precio, urlCompletaFoto};
        db.administrar_productos(accion, datos);
        mostrarMsg("Producto guardado con éxito.");
        abrirListaProductos();
    }

    private void abrirListaProductos() {
        Intent intent = new Intent(this, Lista_producto.class);
        startActivity(intent);
    }
}





