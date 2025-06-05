package com.example.proyectofinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AdaptadorTareas extends RecyclerView.Adapter<AdaptadorTareas.ViewHolder> {

    private final Context context;
    private final List<Tareas> listaTareas;
    private final OnTareaClickListener clickListener;
    private final OnTareaLongClickListener longClickListener;

    public interface OnTareaClickListener {
        void onTareaClick(Tareas tarea);
    }

    public interface OnTareaLongClickListener {
        void onTareaLongClick(View vista, Tareas tarea);
    }

    public AdaptadorTareas(Context context, List<Tareas> listaTareas,
                           OnTareaClickListener clickListener,
                           OnTareaLongClickListener longClickListener) {
        this.context = context;
        this.listaTareas = listaTareas;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(context).inflate(R.layout.item_tarea, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tareas tarea = listaTareas.get(position);

        holder.txtTitulo.setText(tarea.getTitulo());
        holder.txtDescripcion.setText(tarea.getDescripcion());
        holder.txtGrupo.setText(tarea.getGrupo());
        holder.txtFechaLimite.setText("Fecha límite: " + tarea.getFechaLimite());
        holder.txtHoraRecordatorio.setText("Hora: " + tarea.getHoraRecordatorio());
        holder.txtRepetirDiariamente.setText("Repetir diariamente: " +
                (tarea.isRepetirDiariamente() ? "Sí" : "No"));

        // Para evitar múltiples listeners al reciclar la vista
        holder.chkRealizada.setOnCheckedChangeListener(null);

        holder.chkRealizada.setChecked(tarea.isRealizada());

        if (tarea.isRealizada()) {
            // ✅ Tarea ya realizada: no se puede desmarcar
            holder.chkRealizada.setEnabled(false);
        } else {
            // 🟢 Tarea no realizada: permitir marcar
            holder.chkRealizada.setEnabled(true);
            holder.chkRealizada.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Aquí puedes agregar lógica para marcarla como realizada, si quieres
                tarea.setRealizada(isChecked);  // Solo permite marcarla
                notifyItemChanged(position);
            });
        }

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onTareaClick(tarea);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onTareaLongClick(v, tarea);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return listaTareas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtDescripcion, txtGrupo, txtFechaLimite;
        TextView txtHoraRecordatorio, txtRepetirDiariamente;
        CheckBox chkRealizada;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
            txtGrupo = itemView.findViewById(R.id.txtGrupo);
            txtFechaLimite = itemView.findViewById(R.id.txtFechaLimite);
            txtHoraRecordatorio = itemView.findViewById(R.id.txtHoraRecordatorio);
            txtRepetirDiariamente = itemView.findViewById(R.id.txtRepetirDiariamente);
            chkRealizada = itemView.findViewById(R.id.chkRealizada);
        }
    }
}
