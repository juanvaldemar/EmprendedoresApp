package com.valdemar.emprendedores.view.ui.proyectos.lista;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.valdemar.emprendedores.R;

public class EmpresaViewHolder extends RecyclerView.ViewHolder{

    public View mViewStructure_h;
    public TextView mItem_recycler_structure_title_h;
    public TextView mItem_recycler_structure_send_by_h;
    public ImageView mPost_image_h;


    public EmpresaViewHolder(View itemView) {
        super(itemView);
        mViewStructure_h = itemView ;
    }

    public void setNombre(String title){
        //mItem_recycler_structure_title.setTypeface(Pacifico);
        mItem_recycler_structure_title_h = mViewStructure_h.findViewById(R.id.item_recycler_structure_title);
        mItem_recycler_structure_title_h.setText(title);
    }
    public void setRazon(String title){
        //mItem_recycler_structure_title.setTypeface(Pacifico);
        mItem_recycler_structure_send_by_h = mViewStructure_h.findViewById(R.id.item_recycler_structure_category);
        mItem_recycler_structure_send_by_h.setText(title);
    }




}
