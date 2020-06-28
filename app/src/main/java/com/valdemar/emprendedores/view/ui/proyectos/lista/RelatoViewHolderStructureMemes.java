package com.valdemar.emprendedores.view.ui.proyectos.lista;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.valdemar.emprendedores.R;

public class RelatoViewHolderStructureMemes extends RecyclerView.ViewHolder {

    public View mViewStructure;
    public TextView mItem_recycler_structure_title;
    public TextView mItem_recycler_structure_category;
    public ImageView mPost_image;


    public RelatoViewHolderStructureMemes(View itemView) {
        super(itemView);
        mViewStructure = itemView;
    }

    public void setTitle(String title) {
        //mItem_recycler_structure_title.setTypeface(Pacifico);
        mItem_recycler_structure_title = mViewStructure.findViewById(R.id.item_recycler_structure_title);
        mItem_recycler_structure_title.setText(title);
    }

    public void setCatergory(String category) {
        mItem_recycler_structure_category = mViewStructure.findViewById(R.id.item_recycler_structure_category);
        mItem_recycler_structure_category.setText("" + category);
    }

    public void setImage(Context context, String image) {
        mPost_image = mViewStructure.findViewById(R.id.item_recycler_structure_imagen);

        Glide.with(context)
                .load(image)
                .into(mPost_image);
                //.thumbnail(Glide.with(context).load(R.drawable.item_placeholder))

    }

}