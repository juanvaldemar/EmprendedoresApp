package com.valdemar.emprendedores.view.ui.emprendedor;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.valdemar.emprendedores.R;
import com.valdemar.emprendedores.model.Emprendedor;
import com.valdemar.emprendedores.view.ui.proyectos.lista.ItemFeed;
import com.valdemar.emprendedores.view.ui.proyectos.lista.buscador.IModal;
import com.valdemar.emprendedores.view.ui.proyectos.lista.buscador.SearchPlaceAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class PerfilEmprendedorFragment extends Fragment {


    public static final String ARG_EMPRENDEDOR_DETALLE = "arg_emprendedor_detalle";
    private ImageView mImgPerfil;
    private TextView mTxtNombres;
    private TextView mTxtGradoAcademico;
    private TextView tituloIntereses;
    private TextView mTxtIntereses;
    private TextView mTxtApellidos;
    private TextView mTxtPais;
    private TextView mTxtCiudad;
    private TextView mTxtDedicas;
    private boolean mEmprendedorRegistrado;
    private Emprendedor mEmprendedor = new Emprendedor();
    private ImageButton mButtonFB;
    private ImageButton mButtonIG;
    private ImageButton mButtonTW;
    private String mLinkFB = "";
    private String mLinkIG = "";
    private String mLinkTW = "";
    private String grado_academico_ = "";
    private String intereses_ = "";
    private ImageView imgchat;
    private DataSnapshot mItemSnapshot;
    Button btnVerProyectosCreados,btnEditarperfil;

    private RecyclerView mRecyclerMisLecturas;
    private DatabaseReference mRef;
    private ArrayList<ItemFeed> arrayLists = new ArrayList<>();
    private SearchPlaceAdapter mAdapter;

    private FirebaseUser user;

    private ImageView imgChat;

    public PerfilEmprendedorFragment() {
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
        View view = inflater.inflate(R.layout.fragment_perfil_emprendedor, container, false);
        initUI(view);
        return view;
    }


    private void initUI(View view) {
        imgchat = view.findViewById(R.id.imgchat);

        mImgPerfil = (ImageView) view.findViewById(R.id.img_foto_emprendedor);
        mTxtNombres = (TextView) view.findViewById(R.id.txt_nombres);
        mTxtDedicas = (TextView) view.findViewById(R.id.txt_dedicas);
        mTxtGradoAcademico = (TextView) view.findViewById(R.id.txt_gradoAcademico);
        mTxtIntereses = (TextView) view.findViewById(R.id.mTxtIntereses);
        tituloIntereses = (TextView) view.findViewById(R.id.tituloIntereses);
        mTxtApellidos = (TextView) view.findViewById(R.id.txt_apellidos);
        mTxtPais = (TextView) view.findViewById(R.id.txt_pais);
        mTxtCiudad = (TextView) view.findViewById(R.id.txt_ciudad);
        mButtonFB = (ImageButton) view.findViewById(R.id.btn_facebook);
        mButtonIG = (ImageButton) view.findViewById(R.id.btn_instagram);
        mButtonTW = (ImageButton) view.findViewById(R.id.btn_twitter);
        mButtonFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLinkFB.isEmpty()) {
                    Toast.makeText(getContext(), "No hay cuenta registrada", Toast.LENGTH_LONG).show();
                } else {
                    startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://www.facebook.com/" + mLinkFB)));
                }
            }
        });
        mButtonIG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLinkIG.isEmpty()) {
                    Toast.makeText(getContext(), "No hay cuenta registrada", Toast.LENGTH_LONG).show();
                } else {
                    startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://www.instagram.com/" + mLinkIG)));
                }
            }
        });
        mButtonTW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLinkTW.isEmpty()) {
                    Toast.makeText(getContext(), "No hay cuenta registrada", Toast.LENGTH_LONG).show();
                } else {
                    startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://www.twitter.com/" + mLinkTW)));
                }
            }
        });

        imgChat = (ImageView) view.findViewById(R.id.imgchat);
        imgChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_mis_proyectos_creados);
            }
        });

        btnVerProyectosCreados = (Button) view.findViewById(R.id.btn_ver_proyectos);
        btnVerProyectosCreados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_mis_proyectos_creados);
            }
        });
        btnEditarperfil = view.findViewById(R.id.btn_editar_perfil);
        btnEditarperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                String json = new Gson().toJson(mEmprendedor);
                args.putString(ARG_EMPRENDEDOR_DETALLE, json);
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.next_action_to_lista,args);
            }
        });

        Bundle datosRecuperados = getArguments();
        if (datosRecuperados == null) {
            // No hay datos, manejar excepción
            return;
        }
        String idEmprendedor = datosRecuperados.getString("idEmprendedor");


        if(idEmprendedor != null){
            cargarPerfil2(idEmprendedor.trim());

        }else{
            cargarPerfil();
        }

        LinearLayoutManager layoutManagerMisLecturas
                = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        layoutManagerMisLecturas.setReverseLayout(true);
        layoutManagerMisLecturas.setStackFromEnd(true);
        mRecyclerMisLecturas = (RecyclerView) view.findViewById(R.id.fragmento_mis_lecturas);
        mRecyclerMisLecturas.setHasFixedSize(true);
        mRecyclerMisLecturas.setLayoutManager(layoutManagerMisLecturas);


        Boolean visitante = (Boolean) datosRecuperados.getBoolean("visitante");
        if(visitante){
            btnEditarperfil.setVisibility(View.GONE);
            btnVerProyectosCreados.setVisibility(View.GONE);
        } else {
            TextView titulo_mis_lecturas = view.findViewById(R.id.titulo_mis_lecturas);
            mRecyclerMisLecturas.setVisibility(View.GONE);
            titulo_mis_lecturas.setVisibility(View.GONE);
        }
       // Toast.makeText(getActivity(),""+visitante,Toast.LENGTH_LONG).show();

        if(idEmprendedor == null) {
            idEmprendedor = user.getUid();
            initFiltrarEstados("ACTIVO", view,idEmprendedor);

        }else{
            idEmprendedor.trim();
            initFiltrarEstados("ACTIVO", view,idEmprendedor);

        }


    }

    private void initFiltrarEstados(final String estado, final View view, final String idEmprendedor) {

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


        mRef = FirebaseDatabase.getInstance().getReference().child("Proyectos");
       // final String finalIdEmprendedor = idEmprendedor.trim();
        mRef.limitToFirst(50).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayLists.clear();
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    String ids = eventSnapshot.getKey();
                    ItemFeed category = eventSnapshot.getValue(ItemFeed.class);
                    category.setId(ids);
                    if(category.getId_emprendedor() != null){
                        if(category.getId_emprendedor().equalsIgnoreCase(idEmprendedor)){
                            if(category.getEstadoTrazabilidad() != null){
                                if(category.getEstadoTrazabilidad().equalsIgnoreCase(estado)){
                                    arrayLists.add(category);
                                }
                            }
                        }
                    }

                }

                // Collections.reverse(arrayLists);
                mAdapter = new SearchPlaceAdapter(getContext(), arrayLists,listener);
                mRecyclerMisLecturas.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                //mProgress.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void cargarPerfil2(final String uId) {
        mEmprendedorRegistrado = false;
        final DatabaseReference mEmprendedorReference;
        mEmprendedorReference = FirebaseDatabase.getInstance().getReference().child("Emprendedor");
        mEmprendedorReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot itemSpanshot : dataSnapshot.getChildren()) {

                    String id_emprendedor = (String) itemSpanshot.child("id_emprendedor").getValue();
                    mEmprendedor.setId_emprendedor(id_emprendedor);
                    if (mEmprendedor.getId_emprendedor().equalsIgnoreCase(uId)) {

                        mEmprendedor.setKey(itemSpanshot.getKey());

                        String imagenUrl = obtenerCampoNoNulo((String) itemSpanshot.child("imagen").getValue());
                        mEmprendedor.setImagen(imagenUrl);

                        String gradoAcademico = obtenerCampoNoNulo((String) itemSpanshot.child("grado_academico").getValue());
                        mEmprendedor.setGrado_academico(gradoAcademico);

                        String intereses = obtenerCampoNoNulo((String) itemSpanshot.child("intereses").getValue());
                        mEmprendedor.setIntereses(intereses);

                        String dedicas = obtenerCampoNoNulo((String) itemSpanshot.child("dedicas").getValue());
                        mEmprendedor.setDedicas(dedicas);

                        String nombres = obtenerCampoNoNulo((String) itemSpanshot.child("edt_nombres_emprendedor").getValue());
                        mEmprendedor.setEdt_nombres_emprendedor(nombres);

                        String apellidos = obtenerCampoNoNulo((String) itemSpanshot.child("edt_apellidos_emprendedor").getValue());
                        mEmprendedor.setEdt_apellidos_emprendedor(apellidos);

                        String dni = (String) itemSpanshot.child("edt_dni_emprendedor").getValue();
                        mEmprendedor.setEdt_dni_emprendedor(obtenerCampoNoNulo(dni));

                        String direccion = (String) itemSpanshot.child("edt_direccion_emprendedor").getValue();
                        mEmprendedor.setEdt_direccion_emprendedor(obtenerCampoNoNulo(direccion));

                        mLinkFB = obtenerCampoNoNulo((String) itemSpanshot.child("edt_facebook").getValue());
                        mEmprendedor.setEdt_facebook(mLinkFB);

                        mLinkIG = obtenerCampoNoNulo((String) itemSpanshot.child("edt_instagram").getValue());
                        mEmprendedor.setEdt_instagram(mLinkIG);

                        mLinkTW = obtenerCampoNoNulo((String) itemSpanshot.child("edt_twitter").getValue());
                        mEmprendedor.setEdt_twitter(mLinkTW);

                        String telefono = (String) itemSpanshot.child("edt_num_emprendedor").getValue();
                        mEmprendedor.setEdt_num_emprendedor(obtenerCampoNoNulo(telefono));

                        String anio = (String) itemSpanshot.child("spinner_anio").getValue();
                        mEmprendedor.setSpinner_anio(obtenerCampoNoNulo(anio));

                        String mes = (String) itemSpanshot.child("spinner_mes").getValue();
                        mEmprendedor.setSpinner_mes(obtenerCampoNoNulo(mes));

                        String dia = (String) itemSpanshot.child("spinner_dia").getValue();
                        mEmprendedor.setSpinner_dia(obtenerCampoNoNulo(dia));

                        String pais = obtenerCampoNoNulo((String) itemSpanshot.child("spinner_pais").getValue());
                        mEmprendedor.setSpinner_pais(pais);

                        String ciudad = obtenerCampoNoNulo((String) itemSpanshot.child("spinner_ciudad").getValue());
                        mEmprendedor.setSpinner_ciudad(ciudad);


                        String genero = obtenerCampoNoNulo((String) itemSpanshot.child("spinner_genero").getValue());
                        mEmprendedor.setSpinner_genero(genero.isEmpty()?"Masculino":genero);

                        mEmprendedorRegistrado = true;

                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mEmprendedor.getImagen()!=null && !mEmprendedor.getImagen().isEmpty()){
                        Glide.with(getActivity().getApplicationContext())
                                .load(mEmprendedor.getImagen())
                                .into(mImgPerfil);
                }
                    mTxtNombres.setText(mEmprendedor.getEdt_nombres_emprendedor());
                    mTxtDedicas.setText(mEmprendedor.getDedicas());
                    mTxtApellidos.setText(mEmprendedor.getEdt_apellidos_emprendedor());
                    mTxtPais.setText(mEmprendedor.getSpinner_pais());
                    mTxtCiudad.setText(mEmprendedor.getSpinner_ciudad());
                    mLinkFB = mEmprendedor.getEdt_facebook();
                    mLinkIG = mEmprendedor.getEdt_instagram();
                    mLinkTW = mEmprendedor.getEdt_twitter();
                    grado_academico_ = mEmprendedor.getGrado_academico();

                    String i_ = mEmprendedor.getIntereses().replace("[","");
                    String i__ = i_.replace("]","");


                   // intereses_ = mEmprendedor.getIntereses();
                    mTxtGradoAcademico.setText(grado_academico_);
                    mTxtIntereses.setText(i__);
                    if(mTxtIntereses.getText().toString().isEmpty()){
                        mTxtIntereses.setVisibility(View.GONE);
                        tituloIntereses.setVisibility(View.GONE);
                    }
                    
            }
        }, 1000);
    }

    private void cargarPerfil() {
        mEmprendedorRegistrado = false;
        final DatabaseReference mEmprendedorReference;
        user = FirebaseAuth.getInstance().getCurrentUser();
        mEmprendedorReference = FirebaseDatabase.getInstance().getReference().child("Emprendedor");
        mEmprendedorReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot itemSpanshot : dataSnapshot.getChildren()) {

                    String id_emprendedor = (String) itemSpanshot.child("id_emprendedor").getValue();
                    mEmprendedor.setId_emprendedor(id_emprendedor);
                    if (mEmprendedor != null && mEmprendedor.getId_emprendedor().equals(user.getUid())) {

                        mEmprendedor.setKey(itemSpanshot.getKey());

                        String imagenUrl = obtenerCampoNoNulo((String) itemSpanshot.child("imagen").getValue());
                        mEmprendedor.setImagen(imagenUrl);

                        String nombres = obtenerCampoNoNulo((String) itemSpanshot.child("edt_nombres_emprendedor").getValue());
                        mEmprendedor.setEdt_nombres_emprendedor(nombres);

                        String apellidos = obtenerCampoNoNulo((String) itemSpanshot.child("edt_apellidos_emprendedor").getValue());
                        mEmprendedor.setEdt_apellidos_emprendedor(apellidos);

                        String dni = (String) itemSpanshot.child("edt_dni_emprendedor").getValue();
                        mEmprendedor.setEdt_dni_emprendedor(obtenerCampoNoNulo(dni));

                        String direccion = (String) itemSpanshot.child("edt_direccion_emprendedor").getValue();
                        mEmprendedor.setEdt_direccion_emprendedor(obtenerCampoNoNulo(direccion));

                        String dedicas = obtenerCampoNoNulo((String) itemSpanshot.child("dedicas").getValue());
                        mEmprendedor.setDedicas(dedicas);

                        mLinkFB = obtenerCampoNoNulo((String) itemSpanshot.child("edt_facebook").getValue());
                        mEmprendedor.setEdt_facebook(mLinkFB);

                        mLinkIG = obtenerCampoNoNulo((String) itemSpanshot.child("edt_instagram").getValue());
                        mEmprendedor.setEdt_instagram(mLinkIG);

                        mLinkTW = obtenerCampoNoNulo((String) itemSpanshot.child("edt_twitter").getValue());
                        mEmprendedor.setEdt_twitter(mLinkTW);

                        String gradoAcademico = obtenerCampoNoNulo((String) itemSpanshot.child("grado_academico").getValue());
                        mEmprendedor.setGrado_academico(gradoAcademico);

                        String intereses = obtenerCampoNoNulo((String) itemSpanshot.child("intereses").getValue());
                        mEmprendedor.setIntereses(intereses);

                        String telefono = (String) itemSpanshot.child("edt_num_emprendedor").getValue();
                        mEmprendedor.setEdt_num_emprendedor(obtenerCampoNoNulo(telefono));

                        String anio = (String) itemSpanshot.child("spinner_anio").getValue();
                        mEmprendedor.setSpinner_anio(obtenerCampoNoNulo(anio));

                        String mes = (String) itemSpanshot.child("spinner_mes").getValue();
                        mEmprendedor.setSpinner_mes(obtenerCampoNoNulo(mes));

                        String dia = (String) itemSpanshot.child("spinner_dia").getValue();
                        mEmprendedor.setSpinner_dia(obtenerCampoNoNulo(dia));

                        String pais = obtenerCampoNoNulo((String) itemSpanshot.child("spinner_pais").getValue());
                        mEmprendedor.setSpinner_pais(pais);

                        String ciudad = obtenerCampoNoNulo((String) itemSpanshot.child("spinner_ciudad").getValue());
                        mEmprendedor.setSpinner_ciudad(ciudad);


                        String genero = obtenerCampoNoNulo((String) itemSpanshot.child("spinner_genero").getValue());
                        mEmprendedor.setSpinner_genero(genero.isEmpty()?"Masculino":genero);

                        mEmprendedorRegistrado = true;

                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mEmprendedorRegistrado) {
                    if(!mEmprendedor.getImagen().isEmpty())
                        Glide.with(getActivity().getApplicationContext())
                                .load(mEmprendedor.getImagen())
                                .into(mImgPerfil);
                    mTxtNombres.setText(mEmprendedor.getEdt_nombres_emprendedor());
                    mTxtDedicas.setText(mEmprendedor.getDedicas());
                    mTxtApellidos.setText(mEmprendedor.getEdt_apellidos_emprendedor());
                    mTxtPais.setText(mEmprendedor.getSpinner_pais());
                    mTxtCiudad.setText(mEmprendedor.getSpinner_ciudad());
                    mLinkFB = mEmprendedor.getEdt_facebook();
                    mLinkIG = mEmprendedor.getEdt_instagram();
                    mLinkTW = mEmprendedor.getEdt_twitter();
                    grado_academico_ = mEmprendedor.getGrado_academico();
                    intereses_ = mEmprendedor.getIntereses();
                    mTxtGradoAcademico.setText(grado_academico_);
                    String i_ = mEmprendedor.getIntereses().replace("[","");
                    String i__ = i_.replace("]","");

                    mTxtIntereses.setText(i__);
                    if(mTxtIntereses.getText().toString().isEmpty()){
                        mTxtIntereses.setVisibility(View.GONE);
                        tituloIntereses.setVisibility(View.GONE);
                    }
                } else {
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_categorias);
                    Toast.makeText(getActivity(), "Primero debe registrarse como emprendedor", Toast.LENGTH_LONG).show();
                }
            }
        }, 1000);
    }

    private void viewDetails(String post_key, View view){
        Bundle args = new Bundle();
        args.putString("blog_id", post_key);
        Navigation.findNavController(view).navigate(R.id.next_action_desc,args);
    }

    public String obtenerCampoNoNulo(String campo) {
        return (campo != null) ? campo : "";
    }
}