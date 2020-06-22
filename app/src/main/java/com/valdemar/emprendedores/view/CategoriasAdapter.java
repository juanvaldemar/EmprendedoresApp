package com.valdemar.emprendedores.view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.valdemar.emprendedores.R;
import com.valdemar.emprendedores.model.CategoriaProyecto;

import java.util.List;

public class CategoriasAdapter extends RecyclerView.Adapter<CategoriasAdapter.ViewHolder> {

    private Context mContext;
    private List<CategoriaProyecto> mListaCategorias;
    private OnCategoriaClickListener mListener;

    public CategoriasAdapter(List<CategoriaProyecto> listaCategorias){
        mListaCategorias = listaCategorias;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categoria_list_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoriaProyecto categoriaProyecto = mListaCategorias.get(position);
        holder.getImagenCategoria().setImageResource(categoriaProyecto.getImagen());
        holder.getTvNombreCategoria().setText(categoriaProyecto.getNombre());
    }

    @Override
    public int getItemCount() {
        return mListaCategorias.size();
    }

    public void setOnCategoriaClickListener(OnCategoriaClickListener listener) {
        mListener = listener;
    }

    public interface OnCategoriaClickListener {
        void onCategoriaClick(CategoriaProyecto categoriaProyecto);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imagenCategoria;
        TextView tvNombreCategoria;
        ImageButton img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenCategoria = (ImageView) itemView.findViewById(R.id.imgbtn_categoria);
            tvNombreCategoria = (TextView) itemView.findViewById(R.id.tv_nombre_categoria);
        }

        public ImageView getImagenCategoria() {
            return imagenCategoria;
        }

        public TextView getTvNombreCategoria() {
            return tvNombreCategoria;
        }

        @Override
        public void onClick(View v) {
            imagenCategoria.setBackgroundColor(Color.parseColor("#80818B"));
        }
    }
}
