package com.example.miprimeraaplicacion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class AdaptadorProducto extends BaseAdapter {
    private Context context;
    private ArrayList<Producto> listaProducto;
    private LayoutInflater inflater;

    public AdaptadorProducto(Context context, ArrayList<Producto> listaProductos) {
        this.context = context;
        this.listaProducto = (listaProductos != null) ? listaProductos : new ArrayList<>(); // Evita null
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listaProducto.size();
    }

    @Override
    public Object getItem(int position) {
        return (position >= 0 && position < listaProducto.size()) ? listaProducto.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = inflater.inflate(R.layout.foto, parent, false); // Usando foto.xml
        }

        try {
            Producto producto = listaProducto.get(position);

            TextView txtCodigo = itemView.findViewById(R.id.txtCodigo);
            TextView txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
            TextView txtMarca = itemView.findViewById(R.id.txtMarca);
            TextView txtPresentacion = itemView.findViewById(R.id.txtPresentacion);
            TextView txtPrecio = itemView.findViewById(R.id.txtPrecio);
            ImageView imgFoto = itemView.findViewById(R.id.imgFotoAdaptador);

            // Seteando los datos del producto
            txtCodigo.setText(producto.getCodigo());
            txtDescripcion.setText(producto.getDescripcion());
            txtMarca.setText(producto.getMarca());
            txtPresentacion.setText(producto.getPresentacion());
            txtPrecio.setText("$" + producto.getPrecio());

            // Verificar y cargar la imagen correctamente
            if (producto.getFoto() != null && !producto.getFoto().isEmpty()) {
                Bitmap bitmap = BitmapFactory.decodeFile(producto.getFoto());
                if (bitmap != null) {
                    imgFoto.setImageBitmap(bitmap);
                } else {
                    imgFoto.setImageResource(R.mipmap.ic_launcher); // Imagen de respaldo si falla la carga
                }
            } else {
                imgFoto.setImageResource(R.mipmap.ic_launcher); // Imagen de respaldo si no hay foto
            }

        } catch (Exception e) {
            Toast.makeText(context, "Error en Adaptador: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return itemView;
    }
}

