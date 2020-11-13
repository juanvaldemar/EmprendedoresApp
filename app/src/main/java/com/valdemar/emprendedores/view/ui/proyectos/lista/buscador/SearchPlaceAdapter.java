package com.valdemar.emprendedores.view.ui.proyectos.lista.buscador;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.valdemar.emprendedores.R;
import com.valdemar.emprendedores.view.ui.proyectos.lista.Category;
import com.valdemar.emprendedores.view.ui.proyectos.lista.ItemFeed;

import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.functions.Function1;

public class SearchPlaceAdapter extends RecyclerView.Adapter<SearchPlaceAdapter.SearchPlaceAdapterViewHolder> implements Filterable {

    Context mCntx;
    public ArrayList<ItemFeed> arrayList;
    public ArrayList<ItemFeed> arrayListFiltered;
    private IModal listener;
    private final DatabaseReference mReactionsRef= FirebaseDatabase.getInstance().getReference().child("Reacciones");



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

    private Integer[] reactionsQuantity = {0,0,0};
    private final String[] reaction_keys = {
            "me_gusta", "me_encanta", "me_asombra"
    };
    private final String[] strings = {
            "me gusta", "me encanta", "wow"
    };

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

        Log.e("EJEMPLO", "onReactionClick: ");

        ReactionPopup popup = new ReactionPopup(
                mCntx,
                new ReactionsConfigBuilder(mCntx)
                        .withReactions(new int[]{
                                R.drawable.ic_fb_like,
                                R.drawable.ic_fb_love,
                                R.drawable.ic_fb_wow,
                        })
                        .withReactionTexts(new Function1<Integer, CharSequence>() {
                            @Override
                            public CharSequence invoke(Integer position) {
                                return strings[position];
                            }
                        })
                        .withTextBackground(new ColorDrawable(Color.TRANSPARENT))
                        .withTextColor(Color.GRAY)
                        .withTextHorizontalPadding(0)
                        .withTextVerticalPadding(0)
                        .withTextSize(mCntx.getResources().getDimension(R.dimen.reactions_text_size))
                        .build(),
                new Function1<Integer, Boolean>() {
                    @Override
                    public Boolean invoke(Integer position) {
                        return true;
                    }
                });

        final String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        popup.setReactionSelectedListener(new Function1<Integer, Boolean>() {
            @Override
            public Boolean invoke(Integer position) {
                Log.i("Reactions", "Selection position ahora =" + position);
                if (position != -1) {
                    reactionsQuantity[position]++;
                    Log.i("Reactions", strings[position] + " = " + reactionsQuantity[position]);
                    // reacciones->
                        //       idproyecto1->
                            //          me_gusta ->
                                        //          idEmprendedor1
                                        //          idEmprendedor2
                                        //          idEmprendedor3
                            //          me_encanta ->
                                                //          idEmprendedor1
                                                //          idEmprendedor2
                                                //          idEmprendedor3
                        //       idproyecto2->
                            //          me_gusta ->
                                        //          idEmprendedor1
                                        //          idEmprendedor2
                                        //          idEmprendedor3
                            //          me_encanta ->
                                                //          idEmprendedor1
                                                //          idEmprendedor2
                                                //          idEmprendedor3


                    //mReactionsRef.child(reaction_keys[position]).child(uID);
                }

                // Close selector if not invalid item (testing purpose)
                return true;
            }
        });

        holder.likeImage.setOnTouchListener(popup);
    }


    public class SearchPlaceAdapterViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtPlace;
        ImageView image;
        TextView cardPlace;
        ImageView likeImage;

        public SearchPlaceAdapterViewHolder(View itemView) {
            super(itemView);
            txtPlace = (TextView) itemView.findViewById(R.id.item_recycler_structure_title);
            image = (ImageView) itemView.findViewById(R.id.item_recycler_structure_imagen);
            cardPlace = (TextView) itemView.findViewById(R.id.item_recycler_structure_category);
            likeImage = itemView.findViewById(R.id.like_icon);
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
                    }else if (item.getDescripcion().toLowerCase().contains(filterPattern)) {
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