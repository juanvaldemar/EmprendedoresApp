package com.valdemar.emprendedores.view.ui.proyectos.lista;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.valdemar.emprendedores.R;

public class ProyectosFragment extends Fragment {
    private RecyclerView mRecyclerMisLecturas;
    private DatabaseReference mDatabaseMisLecturas;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;


    private RecyclerView mRecyclerAsesinos,mRecyclerFantasmas,
            mRecyclerLeyendasUrbandas,mRecyclerCreepypastas,
            mRecyclerTerror,mRecyclerEpisodiosPerdidos;


    public ProyectosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_proyectos, container, false);
        initView(root);
        initCategoria(root);
        return root;

    }

    private void initCategoria(View root) {

        Spinner spinner5,spinner6;

        spinner5 = root.findViewById(R.id.spinner_pais);
        spinner6 = root.findViewById(R.id.spinner_ciudad);

        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(getActivity(),
                R.array.pais, android.R.layout.simple_spinner_item);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner5.setAdapter(adapter5);

        ArrayAdapter<CharSequence> adapter6 = ArrayAdapter.createFromResource(getActivity(),
                R.array.ciudad, android.R.layout.simple_spinner_item);
        adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner6.setAdapter(adapter6);


        mDatabase = FirebaseDatabase.getInstance().getReference().child("categorias");

        Query queryEpisodiosPerdidos = mDatabase.orderByChild("category").equalTo("EpisodiosPerdidos");

        mRecyclerEpisodiosPerdidos = (RecyclerView) root.findViewById(R.id.recyclerEpisodiosPerdidos);
        mRecyclerEpisodiosPerdidos = (RecyclerView) root.findViewById(R.id.recyclerEpisodiosPerdidos);
        mRecyclerEpisodiosPerdidos.setHasFixedSize(true);

        LinearLayoutManager layoutManagermRecyclerEpisodiosPerdidos
                = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        layoutManagermRecyclerEpisodiosPerdidos.setReverseLayout(true);
        layoutManagermRecyclerEpisodiosPerdidos.setStackFromEnd(true);

        mRecyclerEpisodiosPerdidos.setLayoutManager(layoutManagermRecyclerEpisodiosPerdidos);

        FirebaseRecyclerAdapter<Category, CategoryViewHolder> firebaseRecyclerAdaptermRecyclerEpisodiosPerdidos =
                new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(
                        Category.class,
                        R.layout.album_card,
                        CategoryViewHolder.class,
                        mDatabase
                ) {
                    @Override
                    protected void populateViewHolder(CategoryViewHolder viewHolder, Category model, int position) {
                        final String post_key = getRef(position).getKey();

                        viewHolder.setTitle(model.getTitulo());
                        viewHolder.setSendBy(model.getCategoria());

                        viewHolder.setImage(getActivity().getApplicationContext(),
                                model.getImagen());

                        Log.v("Seguimiento","dentro");

                        viewHolder.mViewStructure_h.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                               // viewDetails(post_key);
                            }
                        });

                    }
                };

        mRecyclerEpisodiosPerdidos.setAdapter(firebaseRecyclerAdaptermRecyclerEpisodiosPerdidos);

    }

    private void initView(View root) {

        mProgress = new ProgressDialog(getContext());
        mDatabaseMisLecturas = FirebaseDatabase.getInstance().getReference().child("Proyectos");

        // mAdView = (AdView) root.findViewById(R.id.adView);

        // mAdView.loadAd(adRequest);
        LinearLayoutManager layoutManagerMisLecturas
                = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        layoutManagerMisLecturas.setReverseLayout(true);
        layoutManagerMisLecturas.setStackFromEnd(true);

        mRecyclerMisLecturas = (RecyclerView) root.findViewById(R.id.fragmento_memes);
        mRecyclerMisLecturas.setHasFixedSize(true);

        mRecyclerMisLecturas.setLayoutManager(layoutManagerMisLecturas);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // String userId = user.getUid();


        //Query queryRef = mDatabaseMisLecturas.orderByChild("IdMiLectura").equalTo(userId);
        //Query queryRef = mDatabaseMisLecturas.orderByChild("IdMiLectura").equalTo(userId);


        FirebaseRecyclerAdapter<ItemFeed, RelatoViewHolderStructureMemes>
                firebaseRecyclerAdapterMyLecturas = new FirebaseRecyclerAdapter<ItemFeed, RelatoViewHolderStructureMemes>(
                ItemFeed.class,
                R.layout.design_structure_relato_menu,
                RelatoViewHolderStructureMemes.class,
                mDatabaseMisLecturas) {
            @Override
            protected void populateViewHolder(RelatoViewHolderStructureMemes viewHolder, final ItemFeed model, final int position) {
                final String post_key = getRef(position).getKey();
                viewHolder.setTitle(model.getNombre());
                viewHolder.setCatergory(model.getPais()+" - "+model.getCiudad());

                viewHolder.setImage(getActivity().getApplicationContext(), model.getImagen());

                viewHolder.mViewStructure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mProgress.setMessage("Accediendo...");
                        mProgress.show();
                        //Toast.makeText(getContext(),"Identificador "+post_key,Toast.LENGTH_SHORT).show();

                    }
                });
            }
        };

        mRecyclerMisLecturas.setAdapter(firebaseRecyclerAdapterMyLecturas);

    }



}