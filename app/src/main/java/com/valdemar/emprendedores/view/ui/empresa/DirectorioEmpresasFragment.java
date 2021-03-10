package com.valdemar.emprendedores.view.ui.empresa;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
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
import com.google.firebase.database.Query;
import com.valdemar.emprendedores.R;
import com.valdemar.emprendedores.view.ui.proyectos.lista.Category;
import com.valdemar.emprendedores.view.ui.proyectos.lista.CategoryViewHolder;
import com.valdemar.emprendedores.view.ui.proyectos.lista.Empresa;
import com.valdemar.emprendedores.view.ui.proyectos.lista.EmpresaViewHolder;

public class DirectorioEmpresasFragment extends Fragment {

    private RecyclerView mRecyclerListaEmpresas;
    private RecyclerView mRecyclerCategoriasEmpresa;
    private DatabaseReference mDatabaseEmpresas;
    private DatabaseReference mDatabaseCategoriasEmpresa;
    private FirebaseRecyclerAdapter<Empresa, EmpresaViewHolder> firebaseEmpresasRecyclerAdapter;

    public DirectorioEmpresasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View root = inflater.inflate(R.layout.fragment_listar, container, false);
        mRecyclerListaEmpresas = (RecyclerView) root.findViewById(R.id.recyclerViewList);
        mRecyclerListaEmpresas.setHasFixedSize(true);

        mDatabaseEmpresas = FirebaseDatabase.getInstance().getReference().child("Empresa");
        Query queryNombres = mDatabaseEmpresas.orderByChild("nombre");

        // RecyclerView de la Lista de Empresas
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        mRecyclerListaEmpresas.setLayoutManager(layoutManager);
        setFirebaseEmpresasRecyclerAdapter(queryNombres, root);

        // RecyclerView de las Categorias de las empresas
        LinearLayoutManager layoutManagermRecyclerEpisodiosPerdidos
                = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        mRecyclerCategoriasEmpresa = (RecyclerView) root.findViewById(R.id.asd);
        mRecyclerCategoriasEmpresa.setHasFixedSize(true);
        mRecyclerCategoriasEmpresa.setLayoutManager(layoutManagermRecyclerEpisodiosPerdidos);

        mDatabaseCategoriasEmpresa = FirebaseDatabase.getInstance().getReference().child("categoriasEmpresa");

        FirebaseRecyclerAdapter<Category, CategoryViewHolder> firebaseRecyclerAdaptermRecyclerEpisodiosPerdidos =
                new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(
                        Category.class,
                        R.layout.album_card,
                        CategoryViewHolder.class ,
                        mDatabaseCategoriasEmpresa
                ) {
                    @Override
                    protected void populateViewHolder(CategoryViewHolder viewHolder, final Category model, int position) {
                        //post_key = getRef(position).getKey();
                        viewHolder.setTitle(model.getCategoria());
                        viewHolder.setSendBy(model.getCategoria());

                        viewHolder.setImage(getActivity().getApplicationContext(),
                                model.getImagen());

                        Log.v("Seguimiento","dentro");

                        viewHolder.mPost_image_h.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Query queryCategorias = mDatabaseEmpresas.orderByChild("categoria").equalTo(model.getCategoria());
                                setFirebaseEmpresasRecyclerAdapter(queryCategorias, root);
                            }
                        });

                    }
                };

        mRecyclerCategoriasEmpresa.setAdapter(firebaseRecyclerAdaptermRecyclerEpisodiosPerdidos);
        return root;

    }

    private void setFirebaseEmpresasRecyclerAdapter(Query query, final View root) {

        firebaseEmpresasRecyclerAdapter =
                new FirebaseRecyclerAdapter<Empresa, EmpresaViewHolder>(
                        Empresa.class,
                        R.layout.design_structure_relato_menu_2,
                        EmpresaViewHolder.class,
                        query
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

        mRecyclerListaEmpresas.setAdapter(firebaseEmpresasRecyclerAdapter);

    }

    private void viewDetails(String post_key, View view) {
        Bundle args = new Bundle();
        args.putString("blog_id", post_key);
        Navigation.findNavController(view).navigate(R.id.next_action_desc, args);
    }
}