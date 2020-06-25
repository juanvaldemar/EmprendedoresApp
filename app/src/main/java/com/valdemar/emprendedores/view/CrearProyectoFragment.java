package com.valdemar.emprendedores.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.valdemar.emprendedores.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CrearProyectoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrearProyectoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LinearLayout mLLSocio2;
    private LinearLayout mLLSocio3;
    private LinearLayout mLLSocio4;
    private LinearLayout mLLSocio5;
    private int nroSociosActivos = 1;
    private int maxSociosActivos = 5;

    public CrearProyectoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CrearProyectoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CrearProyectoFragment newInstance(String param1, String param2) {
        CrearProyectoFragment fragment = new CrearProyectoFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crear_proyecto, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {

        mLLSocio2 = (LinearLayout) view.findViewById(R.id.ll_socio2);
        mLLSocio3 = (LinearLayout) view.findViewById(R.id.ll_socio3);
        mLLSocio4 = (LinearLayout) view.findViewById(R.id.ll_socio4);
        mLLSocio5 = (LinearLayout) view.findViewById(R.id.ll_socio5);
        Button btnAgregarSocio1 = view.findViewById(R.id.btn_agregar_socio1);
        btnAgregarSocio1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarSocio();
            }
        });
        Button btnAgregarSocio2 = view.findViewById(R.id.btn_agregar_socio2);
        btnAgregarSocio2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarSocio();
            }
        });
        Button btnAgregarSocio3 = view.findViewById(R.id.btn_agregar_socio3);
        btnAgregarSocio3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarSocio();
            }
        });
        Button btnAgregarSocio4 = view.findViewById(R.id.btn_agregar_socio4);
        btnAgregarSocio4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarSocio();
            }
        });


    }

    public void agregarSocio() {
        if (nroSociosActivos < maxSociosActivos){
            nroSociosActivos++;
            switch (nroSociosActivos){
                case 2: mLLSocio2.setVisibility(View.VISIBLE); break;
                case 3: mLLSocio3.setVisibility(View.VISIBLE); break;
                case 4: mLLSocio4.setVisibility(View.VISIBLE); break;
                case 5: mLLSocio5.setVisibility(View.VISIBLE);
            }
        }
    }


}