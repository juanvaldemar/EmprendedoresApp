package com.valdemar.emprendedores.view.ui.proyectos;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.valdemar.emprendedores.R;
import com.valdemar.emprendedores.view.ui.proyectos.lista.ItemFeed;
import com.valdemar.emprendedores.view.ui.proyectos.lista.buscador.IModal;
import com.valdemar.emprendedores.view.ui.proyectos.lista.buscador.SearchPlaceAdapter;

import java.util.ArrayList;

public class MisProyectosCreadosFragment extends Fragment {

    private RecyclerView mRecyclerMisLecturas;
    private DatabaseReference mDatabaseMisLecturas,mRef;
    private ProgressDialog mProgress;
    private Dialog MyDialog;
    private Button mBtnPendiente,
            mBtnCancelado,
            mBtnFinalizado;
    private ArrayList<ItemFeed> arrayLists = new ArrayList<>();
    private SearchPlaceAdapter mAdapter;

    private String estado_general = "";

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

        if(estado_general.isEmpty()){
            estado_general = "ACTIVO";
        }
        initFiltrarEstados(estado_general, root);

        mBtnPendiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initllamada("ACTIVO",root,queryRef);
                estado_general = "ACTIVO";
                initFiltrarEstados(estado_general, root);
            }
        });
        mBtnCancelado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  initllamada("DEBAJA",root,queryRef);
                estado_general = "DEBAJA";
                initFiltrarEstados(estado_general, root);
            }
        });
        mBtnFinalizado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initllamada("FINALIZADO",root,queryRef);
                estado_general = "FINALIZADO";
                initFiltrarEstados(estado_general, root);
            }
        });


    }

    private void viewDetails(String post_key, View view){

        Bundle args = new Bundle();
        args.putString("blog_id", post_key);

        Navigation.findNavController(view).navigate(R.id.next_action_desc,args);

    }
    private void initFiltrarEstados(final String estado, final View view) {


        final IModal listener = new IModal() {
            @Override
            public void modalIniciar(String nombre, String url, String uidUser) {

            }

            @Override
            public void modalIniciarDetail(String id) {
                viewDetails(id,view);
            }

            @Override
            public void modalAceptar(String id) {

            }
        };
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userId = user.getUid();

        mRef = FirebaseDatabase.getInstance().getReference().child("Proyectos");
        mRef.orderByChild("id_emprendedor").equalTo(userId);

        mRef.limitToFirst(50).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayLists.clear();
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    String ids = eventSnapshot.getKey();
                    ItemFeed category = eventSnapshot.getValue(ItemFeed.class);
                    category.setId(ids);

                    if (category.getId_emprendedor() != null){
                        if (category.getId_emprendedor().equalsIgnoreCase(userId)) {
                            if (category.getEstadoTrazabilidad() != null) {
                                if (category.getEstadoTrazabilidad().equalsIgnoreCase(estado)) {
                                    arrayLists.add(category);
                                }
                            }
                        }
                    }


                }

                // Collections.reverse(arrayLists);
                mAdapter = new SearchPlaceAdapter(getContext(), arrayLists,listener);
                mRecyclerMisLecturas.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                mProgress.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initllamada(final String estado, final View root, Query queryRef) {
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

                viewHolder.mViewStructure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mProgress.setMessage("Accediendo...");
                        mProgress.show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mProgress.hide();
                                mProgress.dismiss();
                                Bundle args = new Bundle();
                                args.putString("blog_id", post_key);

                                Navigation.findNavController(root).navigate(R.id.next_action_desc,args);

                            }
                        }, 100);
                    }
                });

            }
        };

        mRecyclerMisLecturas.setAdapter(firebaseRecyclerAdapterMyLecturas);
    }

}