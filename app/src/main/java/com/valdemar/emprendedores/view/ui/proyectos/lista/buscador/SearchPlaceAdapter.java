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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.valdemar.emprendedores.R;
import com.valdemar.emprendedores.view.ui.proyectos.lista.ItemFeed;

import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.functions.Function1;

public class SearchPlaceAdapter extends RecyclerView.Adapter<SearchPlaceAdapter.SearchPlaceAdapterViewHolder> implements Filterable {

    Context mCntx;
    public ArrayList<ItemFeed> arrayList;
    public ArrayList<ItemFeed> arrayListFiltered;
    private IModal listener;
    private final DatabaseReference mReactionsRef= FirebaseDatabase.getInstance().getReference()
            .child("Reacciones").child("id_proyectos");
    private long nroMeGusta = 0;
    private long nroMeEncanta = 0;
    private long nroMeAsombra = 0;



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
    public void onBindViewHolder(final SearchPlaceAdapterViewHolder holder, final int position)
    {
        final String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();

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
        final String idProyecto = arrayListFiltered.get(position).getId();

        mReactionsRef.child(idProyecto).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nroMeGusta = dataSnapshot.child("me_gusta").getChildrenCount();
                nroMeEncanta = dataSnapshot.child("me_encanta").getChildrenCount();
                nroMeAsombra = dataSnapshot.child("me_asombra").getChildrenCount();
                holder.meGustaImage.setVisibility(nroMeGusta>0?View.VISIBLE:View.GONE);
                holder.meEncantaImage.setVisibility(nroMeEncanta>0?View.VISIBLE:View.GONE);
                holder.meAsombraImage.setVisibility(nroMeAsombra>0?View.VISIBLE:View.GONE);
                long totalReacciones = nroMeGusta + nroMeEncanta + nroMeAsombra;
                if(totalReacciones>0)  {
                    holder.nroReactions.setVisibility(View.VISIBLE);
                    holder.nroReactions.setText(String.valueOf(totalReacciones));
                }else {
                    holder.nroReactions.setVisibility(View.GONE);
                    holder.nroReactions.setText(" ");
                }
                holder.reactionImage.setImageResource(R.drawable.favorite_flaco);
                holder.reactionImage.setTag("empty_reaction");
                if (dataSnapshot.child("me_gusta").hasChild(uID)) {
                    holder.reactionImage.setImageResource(R.drawable.ic_fb_like);
                    holder.reactionImage.setTag("me_gusta");
                }else if (dataSnapshot.child("me_encanta").hasChild(uID)) {
                    holder.reactionImage.setImageResource(R.drawable.ic_fb_love);
                    holder.reactionImage.setTag("me_encanta");
                } else if (dataSnapshot.child("me_asombra").hasChild(uID)) {
                    holder.reactionImage.setImageResource(R.drawable.ic_fb_wow);
                    holder.reactionImage.setTag("me_asombra");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                        .withTextColor(Color.BLUE)
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


        popup.setReactionSelectedListener(new Function1<Integer, Boolean>() {
            @Override
            public Boolean invoke(Integer reactionPosition) {
                if (reactionPosition != -1) {

                    //holder.likeImage.getBackground().getAlpha() R.drawable.ic_fb_like
                    String reaccion = (reaction_keys[reactionPosition]);
                    String tagReaction = holder.reactionImage.getTag().toString();
                    if(tagReaction.equalsIgnoreCase(reaccion)){
                        mReactionsRef.child(idProyecto).child(reaccion).child(uID).removeValue();
                    }else {
                        mReactionsRef.child(idProyecto).child(reaccion).child(uID).setValue("id_emprendedor");
                        for (int i = 0; i < reaction_keys.length; i++) {
                            if(i!=reactionPosition){
                                String reaccionDelete = (reaction_keys[i]);
                                mReactionsRef.child(idProyecto).child(reaccionDelete).child(uID).removeValue();
                            }
                        }
                    }

                    notifyItemChanged(position);
                }

                // Close selector if not invalid item (testing purpose)
                return true;
            }
        });

        holder.reactionImage.setOnTouchListener(popup);
    }


    public class SearchPlaceAdapterViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtPlace;
        ImageView image;
        TextView cardPlace;
        ImageView reactionImage; // txt_nro_reactions
        ImageView meGustaImage; // txt_nro_reactions
        ImageView meEncantaImage; // txt_nro_reactions
        ImageView meAsombraImage; // txt_nro_reactions
        TextView nroReactions;

        public SearchPlaceAdapterViewHolder(View itemView) {
            super(itemView);
            txtPlace = (TextView) itemView.findViewById(R.id.item_recycler_structure_title);
            image = (ImageView) itemView.findViewById(R.id.item_recycler_structure_imagen);
            cardPlace = (TextView) itemView.findViewById(R.id.item_recycler_structure_category);
            reactionImage = itemView.findViewById(R.id.reaction_icon);
            nroReactions = itemView.findViewById(R.id.txt_nro_reactions);
            meGustaImage = itemView.findViewById(R.id.me_gusta_icon);
            meEncantaImage = itemView.findViewById(R.id.me_encanta_icon);
            meAsombraImage = itemView.findViewById(R.id.me_asombra_icon);
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