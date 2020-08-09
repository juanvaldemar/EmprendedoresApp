package com.valdemar.emprendedores.view.ui.proyectos;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.valdemar.emprendedores.R;
import com.valdemar.emprendedores.view.ui.proyectos.lista.ItemFeed;

public class MisProyectosCreadosFragment extends Fragment {

    private RecyclerView mRecyclerMisLecturas;
    private DatabaseReference mDatabaseMisLecturas;
    private ProgressDialog mProgress;
    private Dialog MyDialog;
    private Button mBtnPendiente,
            mBtnCancelado,
            mBtnFinalizado;

    public MisProyectosCreadosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mis_proyectos_creados, container, false);

        initView(view);


        return view;
    }



    private void initView(final View root) {
        mProgress = new ProgressDialog(getContext());
        mDatabaseMisLecturas = FirebaseDatabase.getInstance().getReference().child("Proyectos");
        mDatabaseMisLecturas.keepSynced(true);
        // mAdView = (AdView) root.findViewById(R.id.adView);
        // mAdView.loadAd(adRequest);
        LinearLayoutManager layoutManagerMisLecturas
                = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        layoutManagerMisLecturas.setReverseLayout(true);
        layoutManagerMisLecturas.setStackFromEnd(true);

        mRecyclerMisLecturas = (RecyclerView) root.findViewById(R.id.fragmento_mis_lecturas);
        mRecyclerMisLecturas.setHasFixedSize(true);

        mRecyclerMisLecturas.setLayoutManager(layoutManagerMisLecturas);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();


        //Query queryRef = mDatabaseMisLecturas.orderByChild("IdMiLectura").equalTo(userId);

        final Query queryRef = mDatabaseMisLecturas.orderByChild("id_emprendedor").equalTo(userId);

        mBtnPendiente = root.findViewById(R.id.mBtnPendiente);
        mBtnCancelado= root.findViewById(R.id.mBtnCancelado);
        mBtnFinalizado = root.findViewById(R.id.mBtnFinalizado);

        mBtnPendiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initllamada("ACTIVO",root,queryRef);

            }
        });
        mBtnCancelado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initllamada("DEBAJA",root,queryRef);

            }
        });
        mBtnFinalizado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initllamada("FINALIZADO",root,queryRef);

            }
        });


        initllamada("",root,queryRef);


    }

    private void initllamada(final String estado, View root, Query queryRef) {
        FirebaseRecyclerAdapter<ItemFeed, RelatoViewHolderStructure>
                firebaseRecyclerAdapterMyLecturas = new FirebaseRecyclerAdapter<ItemFeed, RelatoViewHolderStructure>(
                ItemFeed.class,
                R.layout.design_structure_relato_menu,
                RelatoViewHolderStructure.class,queryRef) {
            @Override
            protected void populateViewHolder(RelatoViewHolderStructure viewHolder, ItemFeed model, final int position) {
                final String post_key = getRef(position).getKey();

                if(!estado.isEmpty()){
                    if(model.getEstadoTrazabilidad().equalsIgnoreCase(estado)){
                        viewHolder.setTitle(model.getNombre());
                        viewHolder.setCatergory(model.getCategoria());
                        viewHolder.setAuthor(model.getNombre());
                        viewHolder.setImage(getActivity().getApplicationContext(), model.getImagen());
                    }else{
                        viewHolder.setGone();
                    }
                }else{
                    viewHolder.setTitle(model.getNombre());
                    viewHolder.setCatergory(model.getCategoria());
                    viewHolder.setAuthor(model.getNombre());
                    viewHolder.setImage(getActivity().getApplicationContext(), model.getImagen());

                }



            }
        };

        mRecyclerMisLecturas.setAdapter(firebaseRecyclerAdapterMyLecturas);
    }

}