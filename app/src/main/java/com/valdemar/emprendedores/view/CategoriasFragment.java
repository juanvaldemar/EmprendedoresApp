package com.valdemar.emprendedores.view;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.valdemar.emprendedores.R;
import com.valdemar.emprendedores.model.CategoriaProyecto;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoriasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoriasFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView rvCategorias;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categorias, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View root) {
        rvCategorias = (RecyclerView) root.findViewById(R.id.recycler_view_categorias);
        GridLayoutManager glmCategorias = new GridLayoutManager(getActivity(), 3);
        rvCategorias.setLayoutManager(glmCategorias);
        CategoriasAdapter categoriasAdapter = new CategoriasAdapter(generarListaCategorias());
        rvCategorias.setAdapter(categoriasAdapter);
        Button btnSiguiente = root.findViewById(R.id.btn_categorias_sgte);
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.next_action);
            }
        });
    }

    private void navegarSiguiente() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.nav_host_fragment, CrearProyectoFragment.newInstance("",""));
        ft.addToBackStack(null);
        ft.commit();
    }

    private List<CategoriaProyecto> generarListaCategorias(){
        List<CategoriaProyecto> listaCategorias = new ArrayList<>();

        listaCategorias.add(new CategoriaProyecto("Comida", R.drawable.ic_categoria_comida));
        listaCategorias.add(new CategoriaProyecto("Ropa", R.drawable.ic_categoria_ropa));
        listaCategorias.add(new CategoriaProyecto("Tecnología", R.drawable.ic_categoria_tecnologia));
        listaCategorias.add(new CategoriaProyecto("Salud", R.drawable.ic_categoria_salud));
        listaCategorias.add(new CategoriaProyecto("Entretenimiento", R.drawable.ic_categoria_entretenimiento));
        listaCategorias.add(new CategoriaProyecto("Deportes", R.drawable.ic_categoria_deportes));
        listaCategorias.add(new CategoriaProyecto("Videojuegos", R.drawable.ic_categoria_videojuegos));
        listaCategorias.add(new CategoriaProyecto("Consultorías", R.drawable.ic_categoria_consultorias));
        listaCategorias.add(new CategoriaProyecto("Transportes", R.drawable.ic_categoria_transportes));
        listaCategorias.add(new CategoriaProyecto("Marketing", R.drawable.ic_categoria_marketing));
        listaCategorias.add(new CategoriaProyecto("Finanzas", R.drawable.ic_categoria_finanzas));
        listaCategorias.add(new CategoriaProyecto("Hogar", R.drawable.ic_categoria_hogar));
        listaCategorias.add(new CategoriaProyecto("Otros", R.drawable.ic_categoria_otros));

        return listaCategorias;
    }
}