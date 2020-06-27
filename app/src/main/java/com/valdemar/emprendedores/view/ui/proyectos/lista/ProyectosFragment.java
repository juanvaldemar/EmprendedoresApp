package com.valdemar.emprendedores.view.ui.proyectos.lista;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.valdemar.emprendedores.R;

public class ProyectosFragment extends Fragment {
    private RecyclerView mRecyclerMisLecturas;
    private DatabaseReference mDatabaseMisLecturas;
    private ProgressDialog mProgress;

    public ProyectosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_proyectos, container, false);
        initView(root);
        return root;

    }

    private void initView(View root) {

        mProgress = new ProgressDialog(getContext());
        mDatabaseMisLecturas = FirebaseDatabase.getInstance().getReference().child("Proyectos");

        mDatabaseMisLecturas.keepSynced(true);
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
                RelatoViewHolderStructureMemes.class,mDatabaseMisLecturas) {
            @Override
            protected void populateViewHolder(RelatoViewHolderStructureMemes viewHolder, final ItemFeed model, final int position) {
                final String post_key = getRef(position).getKey();
                viewHolder.setTitle(model.getTitulo());
                viewHolder.setCatergory("Memes");

                viewHolder.setImage(getActivity().getApplicationContext(), model.getImagen());

                viewHolder.mViewStructure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mProgress.setMessage("Accediendo...");
                        mProgress.show();
                        //Toast.makeText(getContext(),"Identificador "+post_key,Toast.LENGTH_SHORT).show();



                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mProgress.hide();
                                mProgress.dismiss();
                                //viewDetails(post_key);
                               // initValidarAccess(model.getImage());
                            }
                        }, 200);
                    }
                });
            }
        };

        mRecyclerMisLecturas.setAdapter(firebaseRecyclerAdapterMyLecturas);

    }


}