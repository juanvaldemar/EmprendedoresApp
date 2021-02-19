package com.valdemar.emprendedores.view.ui.empresa;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.valdemar.emprendedores.R;
import com.valdemar.emprendedores.view.ui.proyectos.lista.Category;
import com.valdemar.emprendedores.view.ui.proyectos.lista.CategoryViewHolder;
import com.valdemar.emprendedores.view.ui.proyectos.lista.Empresa;
import com.valdemar.emprendedores.view.ui.proyectos.lista.EmpresaViewHolder;

public class ListarFragment extends Fragment {
    private RecyclerView recyclerViewList;
    private DatabaseReference mDatabase;

    public ListarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_listar, container, false);

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
                        empresaViewHolder.setNombre(modelo.getNombre());
                        empresaViewHolder.setRazon(modelo.getDescripcion());
                    }
                };

        recyclerViewList.setAdapter(firebaseRecyclerAdaptermRecyclerEpisodiosPerdidos);
        return root;

    }


}