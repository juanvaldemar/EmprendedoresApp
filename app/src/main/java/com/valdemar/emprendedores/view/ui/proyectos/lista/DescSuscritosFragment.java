package com.valdemar.emprendedores.view.ui.proyectos.lista;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.valdemar.emprendedores.R;
import com.valdemar.emprendedores.model.Proyecto;
import com.valdemar.emprendedores.view.ui.proyectos.lista.buscador.IModal;
import com.valdemar.emprendedores.view.ui.proyectos.lista.buscador.SearchPlaceAdapter;
import com.valdemar.emprendedores.view.ui.proyectos.lista.buscador.SearchPlaceAdapter2;

import java.util.ArrayList;

public class DescSuscritosFragment extends Fragment {
    private DatabaseReference mDatabaseLike, mDatabaseLikeCount;
    private TextView txt_cantidad_socios_suscritos;
    private ArrayList<ItemFeed> arrayLists = new ArrayList<>();
    private Proyecto mDetalleProyecto = new Proyecto();
    private String mPost_key = null;
    private RecyclerView mRecyclerMisLecturas;
    private SearchPlaceAdapter2 mAdapter;

    public DescSuscritosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_desc_suscritos, container, false);

        initView(view);
        initListado(view);
        return view;
    }

    private void initView(View view) {
        Bundle datosRecuperados = getArguments();
        if (datosRecuperados == null) {
            // No hay datos, manejar excepción
            return;
        }
        mPost_key = datosRecuperados.getString("blog_id");
        LinearLayoutManager layoutManagerMisLecturas
                = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        layoutManagerMisLecturas.setReverseLayout(true);
        layoutManagerMisLecturas.setStackFromEnd(true);

        mRecyclerMisLecturas = (RecyclerView) view.findViewById(R.id.fragmento_mis_lecturas);
        mRecyclerMisLecturas.setHasFixedSize(true);

        mRecyclerMisLecturas.setLayoutManager(layoutManagerMisLecturas);


        mDatabaseLikeCount = FirebaseDatabase.getInstance().getReference().child("HistoriasDetalle").child("count").child(mPost_key);
        mDatabaseLikeCount.keepSynced(true);

        txt_cantidad_socios_suscritos = (TextView) view.findViewById(R.id.txt_cantidad_socios_suscritos);

    }

    public void initListado(final View view){

        final IModal listener = new IModal() {
            @Override
            public void modalIniciar(String nombre, String url, String uidUser) {

            }

            @Override
            public void modalIniciarDetail(String id) {
             //   viewDetails(id,view);
                String idEmprendedor = id;
                Bundle args = new Bundle();
                args.putString("idEmprendedor", idEmprendedor);
                args.putBoolean("visitante", true);
                Navigation.findNavController(view).navigate(R.id.next_action_desc,args);

            }
        };

        mDatabaseLikeCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                txt_cantidad_socios_suscritos.setText(dataSnapshot.getChildrenCount()+" ");
                String asd = dataSnapshot.child(getId()+"").toString();
                arrayLists.clear();
                //llMain.removeView(textView);

                for (final DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    final String ids = eventSnapshot.getKey();
                    //TextView textView = new TextView(getActivity());
                    Object category = eventSnapshot.getValue(Object.class);

                    String convertedToString = String.valueOf(category);

                    String[] segmentacionCanalSplit = convertedToString.split(",");
                    ItemFeed data_ = new ItemFeed();
                    data_.setId_emprendedor(segmentacionCanalSplit[0]);
                    data_.setNombre(segmentacionCanalSplit[1]);

                    arrayLists.add(data_);


                }


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                //if(user.getUid().equalsIgnoreCase(mDetalleProyecto.getId_emprendedor())){
                   // iniProfileModal(view,arrayLists);

                    mAdapter = new SearchPlaceAdapter2(getContext(), arrayLists,listener);
                    mRecyclerMisLecturas.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                //}


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}