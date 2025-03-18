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
    Context context;
    ArrayList<Producto> listaProducto;
    Producto producto;
    LayoutInflater inflater;

    public AdaptadorProducto(Context context, ArrayList<Producto> listaProductos) {
        this.context = context;
        this.listaProducto = listaProductos;
    }

    @Override
    public int getCount() {
        return listaProducto.size();
    }

    @Override
    public Object getItem(int position) {
        return listaProducto.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.foto, parent, false);
        try {
            producto = listaProducto.get(position);

            TextView tempVal = itemView.findViewById(R.id.txtCodigo);
            tempVal.setText(producto.getCodigo());

            tempVal = itemView.findViewById(R.id.txtDescripcion);
            tempVal.setText(producto.getDescripcion());

            tempVal = itemView.findViewById(R.id.txtMarca);
            tempVal.setText(producto.getMarca());

            tempVal = itemView.findViewById(R.id.txtPresentacion);
            tempVal.setText(producto.getPresentacion());

            tempVal = itemView.findViewById(R.id.txtPrecio);
            tempVal.setText("$" + producto.getPrecio());

            ImageView img = itemView.findViewById(R.id.imgFotoAdaptador);
            Bitmap bitmap = BitmapFactory.decodeFile(producto.getFoto());
            img.setImageBitmap(bitmap);
        } catch (Exception e) {
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return itemView;
    }
}
