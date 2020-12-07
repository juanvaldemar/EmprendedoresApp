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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.valdemar.emprendedores.R;
import com.valdemar.emprendedores.view.ui.proyectos.lista.ItemFeed;
import com.valdemar.emprendedores.view.ui.proyectos.lista.buscador.IModal;
import com.valdemar.emprendedores.view.ui.proyectos.lista.buscador.SearchPlaceAdapter;

import java.util.ArrayList;


public class InteresadosFragment extends Fragment {

    private RecyclerView mRecyclerProyectosInteresados;
    private DatabaseReference mDBRefProyectosInteresados, mDBRefIDsProyectosInteresados;
    private ProgressDialog mProgress;
    private Dialog MyDialog;
    private Button mBtnPendiente,
            mBtnCancelado,
            mBtnFinalizado;
    private ArrayList<ItemFeed> arrayLists = new ArrayList<>();
    private ArrayList<ItemFeed> listaProyectosInteresados = new ArrayList<>();
    private SearchPlaceAdapter mAdapter;

    private String estado_general = "";

    public InteresadosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_interesados, container, false);
        initView(view);
        return view;
    }

    private void initView(final View root) {
        mProgress = new ProgressDialog(getContext());
        // mAdView = (AdView) root.findViewById(R.id.adView);
        // mAdView.loadAd(adRequest);
        LinearLayoutManager layoutManagerMisLecturas
                = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        layoutManagerMisLecturas.setReverseLayout(true);
        layoutManagerMisLecturas.setStackFromEnd(true);

        mRecyclerProyectosInteresados = (RecyclerView) root.findViewById(R.id.fragmento_mis_lecturas);
        mRecyclerProyectosInteresados.setHasFixedSize(true);

        mRecyclerProyectosInteresados.setLayoutManager(layoutManagerMisLecturas);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();


        //Query queryRef = mDatabaseMisLecturas.orderByChild("IdMiLectura").equalTo(userId);


        mBtnPendiente = root.findViewById(R.id.mBtnPendiente);
        mBtnCancelado= root.findViewById(R.id.mBtnCancelado);
        mBtnFinalizado = root.findViewById(R.id.mBtnFinalizado);

            estado_general = "ACTIVO";
        initFiltrarEstados(estado_general, root);


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



        mDBRefProyectosInteresados = FirebaseDatabase.getInstance().getReference().child("Proyectos");
        mDBRefProyectosInteresados.orderByChild("id_emprendedor").equalTo(userId);

        mDBRefProyectosInteresados.limitToFirst(50).addValueEventListener(new ValueEventListener() {
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

                listaProyectosInteresados.clear();
                for (final ItemFeed proyecto: arrayLists) {
                    mDBRefIDsProyectosInteresados = FirebaseDatabase.getInstance().getReference().child("HistoriasDetalle").child("count");
                    mDBRefIDsProyectosInteresados.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(proyecto.getId()))
                                listaProyectosInteresados.add(proyecto);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new SearchPlaceAdapter(getContext(), listaProyectosInteresados,listener);
                        mRecyclerProyectosInteresados.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        mProgress.dismiss();
                    }
                },1000);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }


}