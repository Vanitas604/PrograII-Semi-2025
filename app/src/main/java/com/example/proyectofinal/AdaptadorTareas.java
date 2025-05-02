package com.example.proyectofinal;

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

    private Context context;
    private List<Tareas> listaTareas;
    private OnTareaClickListener listener;

    // Interfaz para notificar a MainActivity sobre la tarea seleccionada
    public interface OnTareaClickListener {
        void onTareaClick(Tareas tarea);
    }

    public AdaptadorTareas(Context context, List<Tareas> listaTareas, OnTareaClickListener listener) {
        this.context = context;
        this.listaTareas = listaTareas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(context).inflate(R.layout.item_tarea, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tareas tareas = listaTareas.get(position);
        holder.txtTitulo.setText(tareas.getTitulo());
        holder.txtDescripcion.setText(tareas.getDescripcion());
        holder.txtGrupo.setText(tareas.getGrupo());
        holder.txtFechaLimite.setText(tareas.getFechaLimite());
        holder.chkRealizada.setChecked(tareas.isRealizada());

        // Al hacer clic en el ítem, se comunica a MainActivity la tarea seleccionada
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTareaClick(tareas);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaTareas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtDescripcion, txtGrupo, txtFechaLimite;
        CheckBox chkRealizada;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
            txtGrupo = itemView.findViewById(R.id.txtGrupo);
            txtFechaLimite = itemView.findViewById(R.id.txtFechaLimite);
            chkRealizada = itemView.findViewById(R.id.chkRealizada);
        }
    }
}



