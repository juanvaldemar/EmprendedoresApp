package com.valdemar.emprendedores.view.ui.emprendedor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.valdemar.emprendedores.R;
import com.valdemar.emprendedores.model.Emprendedor;

import java.util.HashMap;

public class PerfilEmprendedorFragment extends Fragment {


    private ImageView mImgPerfil;
    private TextView mTxtNombres;
    private TextView mTxtApellidos;
    private TextView mTxtPais;
    private TextView mTxtCiudad;
    private boolean mEmprendedorRegistrado;
    private Emprendedor mEmprendedor = new Emprendedor();
    private ImageButton mButtonFB;
    private ImageButton mButtonIG;
    private ImageButton mButtonTW;
    private String mLinkFB = "";
    private String mLinkIG = "";
    private String mLinkTW = "";

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
        mImgPerfil = (ImageView) view.findViewById(R.id.img_foto_emprendedor);
        mTxtNombres = (TextView) view.findViewById(R.id.txt_nombres);
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

        cargarPerfil();
    }

    private void cargarPerfil() {
        mEmprendedorRegistrado = false;
        final DatabaseReference mEmprendedorReference;
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mEmprendedorReference = FirebaseDatabase.getInstance().getReference().child("Emprendedor");
        mEmprendedorReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot itemSpanshot : dataSnapshot.getChildren()) {
                    HashMap<String, Emprendedor> hashMapEmprendedor= (HashMap<String, Emprendedor>) itemSpanshot.getValue();
                    String id_emprendedor = (String) itemSpanshot.child("id_emprendedor").getValue();
                    mEmprendedor.setId_emprendedor(id_emprendedor);
                    if (mEmprendedor != null && mEmprendedor.getId_emprendedor().equals(user.getUid())) {

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

                        String ocupacion =(String) itemSpanshot.child("spinner_ocupacion").getValue();
                        mEmprendedor.setSpinner_ocupacion(obtenerCampoNoNulo(ocupacion));

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
                    if(!mEmprendedor.getImagen().equals("Vacio"))
                        Glide.with(getActivity().getApplicationContext())
                            .load(mEmprendedor.getImagen())
                            .into(mImgPerfil);
                    mTxtNombres.setText(mEmprendedor.getEdt_nombres_emprendedor());
                    mTxtApellidos.setText(mEmprendedor.getEdt_apellidos_emprendedor());
                    mTxtPais.setText(mEmprendedor.getSpinner_pais());
                    mTxtCiudad.setText(mEmprendedor.getSpinner_ciudad());
                    mLinkFB = mEmprendedor.getEdt_facebook();
                    mLinkIG = mEmprendedor.getEdt_instagram();
                    mLinkTW = mEmprendedor.getEdt_twitter();
                } else {
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_categorias);
                    Toast.makeText(getActivity(), "Primero debe registrarse como emprendedor", Toast.LENGTH_LONG).show();
                }
            }
        }, 1000);
    }

    public String obtenerCampoNoNulo(String campo) {
        return (campo != null) ? campo : "";
    }
}