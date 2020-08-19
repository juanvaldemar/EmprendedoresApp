package com.valdemar.emprendedores.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.valdemar.emprendedores.R;
import com.valdemar.emprendedores.model.CategoriaProyecto;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoriasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoriasFragment extends Fragment implements CategoriasAdapter.OnCategoriaClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String ARG_CATEGORIA_SELECCIONADA = "categoria_seleccionada";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView rvCategorias;
    private CategoriasAdapter categoriasAdapter;
    private CategoriaProyecto mCategoriaSeleccionada;

    public CategoriasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoriasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoriasFragment newInstance(String param1, String param2) {
        CategoriasFragment fragment = new CategoriasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        verificarRegistroEmprendedor();
        
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(!mEmprendedorRegistrado) {
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_categorias);
                    Toast.makeText(getActivity(), "Primero debe registrarse como emprendedor", Toast.LENGTH_LONG).show();
                }

            }
        },1000);


    }

    View mRoot;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_categorias, container, false);
        initUI(mRoot);
        return mRoot;
    }
    private boolean mEmprendedorRegistrado;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public void verificarRegistroEmprendedor(){
        mEmprendedorRegistrado = false;
        final DatabaseReference mEmprendedorReference;
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mEmprendedorReference = FirebaseDatabase.getInstance().getReference().child("Emprendedor");
        mEmprendedorReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot itemSpanshot: dataSnapshot.getChildren()) {
                    String idEmprendedor = (String)itemSpanshot.child("id_emprendedor").getValue();
                    if(idEmprendedor!=null && idEmprendedor.equals(user.getUid())){
                        mEmprendedorRegistrado = true;
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void initUI(View root) {
        rvCategorias = (RecyclerView) root.findViewById(R.id.recycler_view_categorias);
        GridLayoutManager glmCategorias = new GridLayoutManager(getActivity(), 3);
        rvCategorias.setLayoutManager(glmCategorias);
        categoriasAdapter = new CategoriasAdapter(generarListaCategorias());
        categoriasAdapter.setOnCategoriaClickListener(this);
        rvCategorias.setAdapter(categoriasAdapter);

        Button btnSiguiente = root.findViewById(R.id.btn_categorias_sgte);
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mEmprendedorRegistrado) {
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_categorias);
                    Toast.makeText(getActivity(), "Primero debe registrarse como emprendedor", Toast.LENGTH_LONG).show();
                }else{
                    if(mCategoriaSeleccionada != null){
                        Bundle args = new Bundle();
                        String json = new Gson().toJson(mCategoriaSeleccionada);
                        args.putString(ARG_CATEGORIA_SELECCIONADA, json);
                        Navigation.findNavController(v).navigate(R.id.next_action,args);
                    } else {
                        showSnackBar("Debe seleccionar una categoría" );
                    }
                }

            }
        });
    }


    private List<CategoriaProyecto> generarListaCategorias(){
        List<CategoriaProyecto> listaCategorias = new ArrayList<>();

        listaCategorias.add(new CategoriaProyecto(0,"Comida", R.drawable.ic_categoria_comida));
        listaCategorias.add(new CategoriaProyecto(1,"Ropa", R.drawable.ic_categoria_ropa));
        listaCategorias.add(new CategoriaProyecto(2,"Tecnología", R.drawable.ic_categoria_tecnologia));
        listaCategorias.add(new CategoriaProyecto(3,"Salud", R.drawable.ic_categoria_salud));
        listaCategorias.add(new CategoriaProyecto(4,"Entretenimiento", R.drawable.ic_categoria_entretenimiento));
        listaCategorias.add(new CategoriaProyecto(5,"Deportes", R.drawable.ic_categoria_deportes));
        listaCategorias.add(new CategoriaProyecto(6,"Videojuegos", R.drawable.ic_categoria_videojuegos));
        listaCategorias.add(new CategoriaProyecto(7,"Consultorías", R.drawable.ic_categoria_consultorias));
        listaCategorias.add(new CategoriaProyecto(8,"Transportes", R.drawable.ic_categoria_transportes));
        listaCategorias.add(new CategoriaProyecto(9,"Marketing", R.drawable.ic_categoria_marketing));
        listaCategorias.add(new CategoriaProyecto(10,"Finanzas", R.drawable.ic_categoria_finanzas));
        listaCategorias.add(new CategoriaProyecto(11,"Hogar", R.drawable.ic_categoria_hogar));
        listaCategorias.add(new CategoriaProyecto(12,"Otros", R.drawable.ic_categoria_otros));

        return listaCategorias;
    }

    @Override
    public void onCategoriaClick(CategoriaProyecto categoriaSeleccionada) {
        mCategoriaSeleccionada = categoriaSeleccionada;
        categoriasAdapter.notifyDataSetChanged();
    }

    public void showSnackBar(String msg) {
        Snackbar
                .make(getActivity().findViewById(R.id.constraint_categorias), msg, Snackbar.LENGTH_SHORT)
                .show();
    }
}