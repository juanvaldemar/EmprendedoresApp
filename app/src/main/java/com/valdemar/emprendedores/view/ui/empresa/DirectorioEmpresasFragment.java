package com.valdemar.emprendedores.view.ui.empresa;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.ScrollingTabContainerView;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

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
    private SearchView mSearch;
    private Spinner mSpinnerCiudad;
    private DatabaseReference mDatabaseCategoriasEmpresa;
    private FirebaseRecyclerAdapter<Empresa, EmpresaViewHolder> firebaseEmpresasRecyclerAdapter;

    private ProgressDialog mProgress;
    LinearLayoutManager layoutManagerEmpresas;

    public DirectorioEmpresasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View root = inflater.inflate(R.layout.fragment_listar, container, false);

        mSearch = (SearchView) root.findViewById(R.id.mSearch);
        mSpinnerCiudad = root.findViewById(R.id.spinner_ciudad);

        ArrayAdapter<CharSequence> adapterCiudad = ArrayAdapter.createFromResource(getActivity(),
                R.array.ciudad, android.R.layout.simple_spinner_item);
        adapterCiudad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCiudad.setAdapter(adapterCiudad);

        mSpinnerCiudad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!mSpinnerCiudad.getSelectedItem().toString().equalsIgnoreCase("ciudad")){
                    Query queryCiudad = mDatabaseEmpresas.orderByChild("ciudad").equalTo(mSpinnerCiudad.getSelectedItem().toString());
                    setFirebaseEmpresasRecyclerAdapter(queryCiudad, root);
                } else {
                    setFirebaseEmpresasRecyclerAdapter(mDatabaseEmpresas.orderByChild("nombre"), root);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final EditText txtSearch = ((EditText)mSearch.findViewById(R.id.search_src_text));
        txtSearch.setHintTextColor(Color.GRAY);
        txtSearch.setTextColor(Color.WHITE);
        txtSearch.setLinkTextColor(Color.GRAY);
        txtSearch.setPadding(10,0,0,0);

        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(final String newText) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Query queryBusquedaEmpresa = mDatabaseEmpresas.orderByChild("nombre")
                                .startAt(newText).endAt(newText + "\uf8ff");

                        setFirebaseEmpresasRecyclerAdapter(queryBusquedaEmpresa, root);
                    }
                },600);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;

            }
        });

        mSearch.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });

        mRecyclerListaEmpresas = (RecyclerView) root.findViewById(R.id.recyclerViewList);
        mRecyclerListaEmpresas.setHasFixedSize(true);

        mDatabaseEmpresas = FirebaseDatabase.getInstance().getReference().child("Empresa");
        Query queryNombres = mDatabaseEmpresas.orderByChild("nombre");

        // RecyclerView de la Lista de Empresas
        layoutManagerEmpresas
                = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        mRecyclerListaEmpresas.setLayoutManager(layoutManagerEmpresas);
        setFirebaseEmpresasRecyclerAdapter(queryNombres, root);

        // RecyclerView de las Categorias de las empresas
        LinearLayoutManager layoutManagermRecyclerCategoriasEmpresas
                = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        mRecyclerCategoriasEmpresa = (RecyclerView) root.findViewById(R.id.asd);
        mRecyclerCategoriasEmpresa.setHasFixedSize(true);
        mRecyclerCategoriasEmpresa.setLayoutManager(layoutManagermRecyclerCategoriasEmpresas);

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

                        viewHolder.setImage(getActivity().getApplicationContext(),obtenerCategoriaImagen(model.getCategoria()));

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

        mProgress = new ProgressDialog(getActivity());
        mProgress.setMessage("Cargando ...");
        mProgress.setCancelable(false);
        mProgress.show();
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
                        mProgress.dismiss();
                    }
                };

        mRecyclerListaEmpresas.setAdapter(firebaseEmpresasRecyclerAdapter);
        layoutManagerEmpresas.setReverseLayout(false);
        //LinearLayoutManager linearLayoutManager1 = (LinearLayoutManager) mRecyclerListaEmpresas.getLayoutManager();
        //linearLayoutManager1.setReverseLayout(false);
    }

    private int obtenerCategoriaImagen(String categoria) {

        switch (categoria){
            case "Comida": return R.drawable.ic_categoria_comida;
            case "Ropa": return R.drawable.ic_categoria_ropa;
            case "Tecnologia": return R.drawable.ic_categoria_tecnologia;
            case "Salud": return R.drawable.ic_categoria_salud;
            case "Entretenimiento": return R.drawable.ic_categoria_entretenimiento;
            case "Deportes": return R.drawable.ic_categoria_deportes;
            case "Videojuegos": return R.drawable.ic_categoria_videojuegos;
            case "Consultorias": return R.drawable.ic_categoria_consultorias;
            case "Transportes": return R.drawable.ic_categoria_transportes;
            case "Hogar": return R.drawable.ic_categoria_hogar;
            case "Import/Export": return R.drawable.ic_categoria_importexport;
            case "Bienes Raices": return R.drawable.ic_categoria_bienesraices;
            case "Otros": return R.drawable.ic_categoria_otros;
        }
        return R.drawable.ic_categoria_comida;
    }

    private void viewDetails(String post_key, View view) {
        Bundle args = new Bundle();
        args.putString("blog_id", post_key);
        Navigation.findNavController(view).navigate(R.id.next_action_desc, args);
    }
}