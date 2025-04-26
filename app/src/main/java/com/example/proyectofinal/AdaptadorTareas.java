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

    public AdaptadorTareas(Context context, List<Tareas> listaTareas) {
        this.context = context;
        this.listaTareas = listaTareas;
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
