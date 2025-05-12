package com.example.miprimeraaplicacion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MensajeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Mensaje> mensajes;

    private static final int TIPO_ENVIADO = 0;
    private static final int TIPO_RECIBIDO = 1;

    public MensajeAdapter(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    @Override
    public int getItemViewType(int position) {
        return mensajes.get(position).esEnviado() ? TIPO_ENVIADO : TIPO_RECIBIDO;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TIPO_ENVIADO) {
            View vista = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.mensaje_item_enviado, parent, false); // nombre correcto
            return new EnviadoViewHolder(vista);
        } else {
            View vista = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.mensaje_item_recibido, parent, false);
            return new RecibidoViewHolder(vista);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Mensaje mensaje = mensajes.get(position);
        if (holder.getItemViewType() == TIPO_ENVIADO) {
            ((EnviadoViewHolder) holder).txtMensaje.setText(mensaje.getTexto());
        } else {
            ((RecibidoViewHolder) holder).txtMensajeRecibido.setText(mensaje.getTexto());
        }
    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    // ViewHolder para mensajes enviados
    public static class EnviadoViewHolder extends RecyclerView.ViewHolder {
        TextView txtMensaje;

        public EnviadoViewHolder(View itemView) {
            super(itemView);
            txtMensaje = itemView.findViewById(R.id.txtMensaje);
        }
    }

    // ViewHolder para mensajes recibidos
    public static class RecibidoViewHolder extends RecyclerView.ViewHolder {
        TextView txtMensajeRecibido;

        public RecibidoViewHolder(View itemView) {
            super(itemView);
            txtMensajeRecibido = itemView.findViewById(R.id.txtMensaje);
        }
    }
}