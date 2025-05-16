package com.example.miprimeraaplicacion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdaptadorTareas extends RecyclerView.Adapter<AdaptadorTareas.ViewHolder> {

    public interface OnTareaClickListener {
        void onTareaClick(Tareas tarea);
    }

    public interface OnTareaLongClickListener {
        void onTareaLongClick(View vista, Tareas tarea);
    }

    private final Context context;
    private final List<Tareas> listaTareas;
    private final OnTareaClickListener clickListener;
    private final OnTareaLongClickListener longClickListener;

    public AdaptadorTareas(Context context,
                           List<Tareas> listaTareas,
                           OnTareaClickListener listener,
                           OnTareaLongClickListener longClickListener) {

        this.context = context;
        this.listaTareas = listaTareas;
        this.clickListener = listener;
        this.longClickListener = longClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView txtTitulo, txtDescripcion, txtGrupo, txtFechaLimite;
        final CheckBox chkRealizada;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
            txtGrupo = itemView.findViewById(R.id.txtGrupo);
            txtFechaLimite = itemView.findViewById(R.id.txtFechaLimite);
            chkRealizada = itemView.findViewById(R.id.chkRealizada);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(context).inflate(R.layout.item_tarea, parent, false);
        ViewHolder holder = new ViewHolder(vista);

        // Click simple
        holder.itemView.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION && clickListener != null) {
                clickListener.onTareaClick(listaTareas.get(pos));
            }
        });

        // Long‑click (menú contextual)
        holder.itemView.setOnLongClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION && longClickListener != null) {
                longClickListener.onTareaLongClick(v, listaTareas.get(pos));
                return true;
            }
            return false;
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tareas tarea = listaTareas.get(position);

        holder.txtTitulo.setText(tarea.getTitulo());
        holder.txtDescripcion.setText(tarea.getDescripcion());
        holder.txtGrupo.setText(tarea.getGrupo());
        holder.txtFechaLimite.setText(tarea.getFechaLimite());

        // Removemos el listener antes de cambiar el estado para evitar ejecuciones no deseadas
        holder.chkRealizada.setOnCheckedChangeListener(null);

        holder.chkRealizada.setChecked(tarea.isRealizada());

        // Ponemos el listener en onBindViewHolder para actualizar el estado
        holder.chkRealizada.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                Tareas tareaActualizada = listaTareas.get(pos);
                tareaActualizada.setRealizada(isChecked);

                // Actualizamos la base de datos con el nuevo estado
                new DBTareas(context).actualizarEstadoRealizada(tareaActualizada.getId(), isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaTareas.size();
    }
}


