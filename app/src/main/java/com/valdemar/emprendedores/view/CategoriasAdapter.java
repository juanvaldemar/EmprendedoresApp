package com.valdemar.emprendedores.view;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.valdemar.emprendedores.R;
import com.valdemar.emprendedores.model.CategoriaProyecto;

import java.util.List;

public class CategoriasAdapter extends RecyclerView.Adapter<CategoriasAdapter.ViewHolder> {

    private Context mContext;
    private List<CategoriaProyecto> mListaCategorias;
    private int indexSeleccionado = -1;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        CategoriaProyecto categoriaProyecto = mListaCategorias.get(position);
        holder.getImagenCategoria().setImageResource(categoriaProyecto.getImagen());
        holder.getTvNombreCategoria().setText(categoriaProyecto.getNombre());
        Log.e("index", "index: "+ position+ " indexSeleccionado: " + indexSeleccionado);
        if (categoriaProyecto.getIdCategoria() == indexSeleccionado)
            holder.getLLCategoria().setBackgroundColor(mContext.getResources().getColor(R.color.gray));
        else
            holder.getLLCategoria().setBackgroundColor(mContext.getResources().getColor(R.color.white));
        holder.getLLCategoria().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indexSeleccionado = mListaCategorias.get(position).getIdCategoria();
                mListener.onCategoriaClick(mListaCategorias.get(position));
            }
        });
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
        LinearLayout llCategoria;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenCategoria = (ImageView) itemView.findViewById(R.id.imgbtn_categoria);
            tvNombreCategoria = (TextView) itemView.findViewById(R.id.tv_nombre_categoria);
            llCategoria = (LinearLayout) itemView.findViewById(R.id.ll_item_categoria);
        }

        public ImageView getImagenCategoria() {
            return imagenCategoria;
        }

        public TextView getTvNombreCategoria() {
            return tvNombreCategoria;
        }


        public LinearLayout getLLCategoria() {
            return llCategoria;
        }

        @Override
        public void onClick(View v) {
            Log.e("probando", "onClick: entra aqui 2");
        }
    }
}
