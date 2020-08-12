package com.valdemar.emprendedores.view.ui.proyectos.lista.buscador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.valdemar.emprendedores.R;
import com.valdemar.emprendedores.view.ui.proyectos.lista.Category;
import com.valdemar.emprendedores.view.ui.proyectos.lista.ItemFeed;

import java.util.ArrayList;
import java.util.List;

public class SearchPlaceAdapter extends RecyclerView.Adapter<SearchPlaceAdapter.SearchPlaceAdapterViewHolder> implements Filterable {

    Context mCntx;
    public ArrayList<ItemFeed> arrayList;
    public ArrayList<ItemFeed> arrayListFiltered;
    private IModal listener;



    public SearchPlaceAdapter(Context mCntx, ArrayList<ItemFeed> arrayList, IModal listener)
    {
        this.mCntx = mCntx;
        this.arrayList = arrayList;
        arrayListFiltered = arrayList;
        /*arrayListFiltered = new ArrayList<>(arrayList);*/
        this.listener = listener;

    }


    public SearchPlaceAdapter(Context mCntx, ArrayList<ItemFeed> arrayList)
    {
        this.mCntx = mCntx;
        this.arrayList = arrayList;
        arrayListFiltered = arrayList;
        /*arrayListFiltered = new ArrayList<>(arrayList);*/

    }


    @Override
    public int getItemCount()
    {
        return arrayList.size();
    }

    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public SearchPlaceAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_structure_relato_menu, parent, false);

        SearchPlaceAdapterViewHolder viewHolder = new SearchPlaceAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SearchPlaceAdapterViewHolder holder, final int position)
    {


        holder.txtPlace.setText(arrayListFiltered.get(position).getNombre());
        holder.cardPlace.setText(arrayListFiltered.get(position).getPais() +" - " +arrayListFiltered.get(position).getCiudad());

        Glide.with(mCntx).load(arrayListFiltered.get(position).getImagen()).into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                listener.modalIniciarDetail(arrayListFiltered.get(position).getId());

            }
        });
    }


    public class SearchPlaceAdapterViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtPlace;
        ImageView image;
        TextView cardPlace;

        public SearchPlaceAdapterViewHolder(View itemView) {
            super(itemView);
            txtPlace = (TextView) itemView.findViewById(R.id.item_recycler_structure_title);
            image = (ImageView) itemView.findViewById(R.id.item_recycler_structure_imagen);
            cardPlace = (TextView) itemView.findViewById(R.id.item_recycler_structure_category);
        }
    }

    public Filter getFilter()
    {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ItemFeed> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0 || constraint == "") {

                filteredList.addAll(arrayList);
            } else {
                String filterPattern = constraint.toString().toLowerCase();

                for (ItemFeed item : arrayList) {
                    if (item.getPais().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }else if(item.getCiudad().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                    else if (item.getCategoria().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                    else if (item.getNombre().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }else{
                        System.out.println("else");
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            notifyDataSetChanged();
            arrayList.clear();
            arrayList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };


}