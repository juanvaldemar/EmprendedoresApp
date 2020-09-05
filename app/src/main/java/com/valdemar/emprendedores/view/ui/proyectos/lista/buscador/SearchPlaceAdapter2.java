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
import com.valdemar.emprendedores.view.ui.proyectos.lista.ItemFeed;

import java.util.ArrayList;
import java.util.List;

public class SearchPlaceAdapter2 extends RecyclerView.Adapter<SearchPlaceAdapter2.SearchPlaceAdapterViewHolder> implements Filterable {

    Context mCntx;
    public ArrayList<ItemFeed> arrayList;
    public ArrayList<ItemFeed> arrayListFiltered;
    private IModal listener;



    public SearchPlaceAdapter2(Context mCntx, ArrayList<ItemFeed> arrayList, IModal listener)
    {
        this.mCntx = mCntx;
        this.arrayList = arrayList;
        arrayListFiltered = arrayList;
        /*arrayListFiltered = new ArrayList<>(arrayList);*/
        this.listener = listener;

    }


    public SearchPlaceAdapter2(Context mCntx, ArrayList<ItemFeed> arrayList)
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_structure_profile, parent, false);

        SearchPlaceAdapterViewHolder viewHolder = new SearchPlaceAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SearchPlaceAdapterViewHolder holder, final int position)
    {


        holder.txtNameProfile.setText(arrayListFiltered.get(position).getNombre());

        holder.txtNameProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                listener.modalIniciarDetail(arrayListFiltered.get(position).getId_emprendedor());

            }
        });
    }


    public class SearchPlaceAdapterViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtNameProfile;

        public SearchPlaceAdapterViewHolder(View itemView) {
            super(itemView);
            txtNameProfile = (TextView) itemView.findViewById(R.id.item_name);
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
                    filteredList.add(item);
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