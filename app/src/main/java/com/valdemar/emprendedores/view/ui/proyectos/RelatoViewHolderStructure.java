package com.valdemar.emprendedores.view.ui.proyectos;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.valdemar.emprendedores.R;


/**
 * Created by CORAIMA on 21/11/2017.
 */


public class RelatoViewHolderStructure extends RecyclerView.ViewHolder{

    public View mViewStructure;
    public TextView mItem_recycler_structure_title;
    public TextView mItem_recycler_structure_category;
    public TextView mItem_recycler_structure_author;
    public ImageView mPost_image;
    public RelativeLayout relative_item_recycler_structure;


    public RelatoViewHolderStructure(View itemView) {
        super(itemView);
        mViewStructure = itemView ;
    }

    public void setGone(){

        relative_item_recycler_structure = mViewStructure.findViewById(R.id.relative_item_recycler_structure);
        mItem_recycler_structure_title = mViewStructure.findViewById(R.id.item_recycler_structure_title);
        mItem_recycler_structure_category = mViewStructure.findViewById(R.id.item_recycler_structure_category);
        mItem_recycler_structure_author = mViewStructure.findViewById(R.id.item_recycler_structure_author);
        mPost_image = mViewStructure.findViewById(R.id.item_recycler_structure_imagen);

        mItem_recycler_structure_title.setVisibility(View.GONE);
        mItem_recycler_structure_category.setVisibility(View.GONE);
        mItem_recycler_structure_author.setVisibility(View.GONE);
        mPost_image.setVisibility(View.GONE);
        relative_item_recycler_structure.setVisibility(View.GONE);
        mViewStructure.setVisibility(View.GONE);
    }


    public void setTitle(String title){
        //mItem_recycler_structure_title.setTypeface(Pacifico);
        mItem_recycler_structure_title = mViewStructure.findViewById(R.id.item_recycler_structure_title);
        mItem_recycler_structure_title.setText(title);
    }

    public void setCatergory(String category){
        mItem_recycler_structure_category = mViewStructure.findViewById(R.id.item_recycler_structure_category);
        mItem_recycler_structure_category.setText("Categoria: "+category);
    }
    public void setAuthor(String author){
        mItem_recycler_structure_author = mViewStructure.findViewById(R.id.item_recycler_structure_author);
        mItem_recycler_structure_author.setText("Escrito por: "+author);
    }

    public void setImage(Context context, String image){
        mPost_image = mViewStructure.findViewById(R.id.item_recycler_structure_imagen);

        Glide.with(context)
                .load(image)
                .thumbnail(Glide.with(context).load(R.color.white))
                .into(mPost_image);

    }




}