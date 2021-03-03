package com.valdemar.emprendedores.view.ui.empresa;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.valdemar.emprendedores.R;
import com.valdemar.emprendedores.view.ui.proyectos.lista.Empresa;
import com.valdemar.emprendedores.view.ui.proyectos.lista.EmpresaViewHolder;

public class DirectorioEmpresasFragment extends Fragment {

    private RecyclerView recyclerViewList;
    private DatabaseReference mDatabase;

    public DirectorioEmpresasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View root = inflater.inflate(R.layout.fragment_listar, container, false);
        recyclerViewList = (RecyclerView) root.findViewById(R.id.recyclerViewList);
        recyclerViewList.setHasFixedSize(true);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Empresa");

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerViewList.setLayoutManager(layoutManager);

        FirebaseRecyclerAdapter<Empresa, EmpresaViewHolder> firebaseRecyclerAdaptermRecyclerEpisodiosPerdidos =
                new FirebaseRecyclerAdapter<Empresa, EmpresaViewHolder>(
                        Empresa.class,
                        R.layout.design_structure_relato_menu_2,
                        EmpresaViewHolder.class,
                        mDatabase
                ) {
                    @Override
                    protected void populateViewHolder(EmpresaViewHolder empresaViewHolder, Empresa modelo, int i) {
                        final String post_key = getRef(i).getKey();
                        empresaViewHolder.setNombre(modelo.getNombre());
                        empresaViewHolder.setRazon(modelo.getDescripcion());
                        empresaViewHolder.setCategoria("Categoria: " + modelo.getCategoria());
                        empresaViewHolder.setCelular("Celular: " + modelo.getCelular());
                        empresaViewHolder.mViewStructure_h.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Bundle args = new Bundle();
                                        args.putString("key_empresa", post_key);
                                        Navigation.findNavController(root).navigate(R.id.next_action_empresa_desc,args);
                                    }
                                }, 100);
                            }
                        });
                    }
                };

        recyclerViewList.setAdapter(firebaseRecyclerAdaptermRecyclerEpisodiosPerdidos);
        return root;

    }

    private void viewDetails(String post_key, View view) {
        Bundle args = new Bundle();
        args.putString("blog_id", post_key);
        Navigation.findNavController(view).navigate(R.id.next_action_desc, args);
    }
}