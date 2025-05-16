package com.example.miprimeraaplicacion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AdaptadorGrupos extends RecyclerView.Adapter<AdaptadorGrupos.ViewHolder> {

    private final Context context;
    private final List<Grupo> listaGrupos;
    private final OnGrupoClickListener listener;

    public interface OnGrupoClickListener {
        void onClick(Grupo grupo);        // Click simple para ver tareas
        void onModificar(Grupo grupo);    // Modificar grupo (menú contextual)
        void onEliminar(Grupo grupo);     // Eliminar grupo (menú contextual)
    }

    public AdaptadorGrupos(Context context, List<Grupo> listaGrupos, OnGrupoClickListener listener) {
        this.context = context;
        this.listaGrupos = listaGrupos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(context).inflate(R.layout.item_grupo, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Grupo grupo = listaGrupos.get(position);
        holder.txtNombreGrupo.setText(grupo.getNombre());

        // Click simple para abrir actividades con tareas del grupo
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(grupo);
        });

        // Long click para mostrar menú contextual (modificar, eliminar)
        holder.itemView.setOnLongClickListener(v -> {
            mostrarPopupMenu(v, grupo);
            return true;
        });
    }

    private void mostrarPopupMenu(View view, Grupo grupo) {
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_grupo_contextual, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.opModificar) {
                if (listener != null) listener.onModificar(grupo);
                return true;
            } else if (itemId == R.id.opEliminar) {
                if (listener != null) listener.onEliminar(grupo);
                return true;
            }
            return false;
        });

        popup.show();
    }

    @Override
    public int getItemCount() {
        return listaGrupos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView txtNombreGrupo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombreGrupo = itemView.findViewById(R.id.txtNombreGrupo);
        }
    }
}

