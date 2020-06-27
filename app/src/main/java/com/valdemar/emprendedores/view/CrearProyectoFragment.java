package com.valdemar.emprendedores.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.valdemar.emprendedores.MenuLateralActivity;
import com.valdemar.emprendedores.R;
import com.valdemar.emprendedores.SplashActivity;
import com.valdemar.emprendedores.auth.AccessRelato;
import com.valdemar.emprendedores.model.CategoriaProyecto;
import com.valdemar.emprendedores.model.Proyecto;
import java.sql.Timestamp;

import static android.app.Activity.RESULT_OK;

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
    private static final int GALLERY_REQUEST = 0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LinearLayout mLLSocio2;
    private LinearLayout mLLSocio3;
    private LinearLayout mLLSocio4;
    private LinearLayout mLLSocio5;
    private ImageView mImgFoto;
    private EditText mEdtNombreProyecto;
    private EditText mEdtDescripcionProyecto;
    private EditText mEdtSocio1;
    private EditText mEdtDescripcionSocio1;
    private EditText mEdtSocio2;
    private EditText mEdtDescripcionSocio2;
    private EditText mEdtSocio3;
    private EditText mEdtDescripcionSocio3;
    private EditText mEdtSocio4;
    private EditText mEdtDescripcionSocio4;
    private EditText mEdtSocio5;
    private EditText mEdtDescripcionSocio5;

    private int nroSociosActivos = 1;
    private int maxSociosActivos = 5;

    private CategoriaProyecto mCategoria;


    private ProgressDialog mProgresDialog;
    private StorageReference mStorage;
    private  DatabaseReference mDatabase;
    private Task<Uri> mRuta;

    private Uri mImageUri = null;



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
        args.putString(CategoriasFragment.ARG_CATEGORIA_SELECCIONADA, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            String jsonString = getArguments().getString(CategoriasFragment.ARG_CATEGORIA_SELECCIONADA);
            if (!TextUtils.isEmpty(jsonString)) {
                mCategoria = (new Gson()).fromJson(jsonString, CategoriaProyecto.class);
            }
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

        mEdtNombreProyecto = (EditText) view.findViewById(R.id.edt_nombre_proyecto);
        mEdtDescripcionProyecto = (EditText) view.findViewById(R.id.edt_descripcion_proyecto);
        mEdtSocio1 = (EditText) view.findViewById(R.id.edt_socio1);
        mEdtDescripcionSocio1 = (EditText) view.findViewById(R.id.edt_descripcion_socio1);
        mEdtSocio2 = (EditText) view.findViewById(R.id.edt_socio2);
        mEdtDescripcionSocio2 = (EditText) view.findViewById(R.id.edt_descripcion_socio2);
        mEdtSocio3 = (EditText) view.findViewById(R.id.edt_socio3);
        mEdtDescripcionSocio3 = (EditText) view.findViewById(R.id.edt_descripcion_socio3);
        mEdtSocio4 = (EditText) view.findViewById(R.id.edt_socio4);
        mEdtDescripcionSocio4 = (EditText) view.findViewById(R.id.edt_descripcion_socio4);
        mEdtSocio4 = (EditText) view.findViewById(R.id.edt_socio4);
        mEdtDescripcionSocio5 = (EditText) view.findViewById(R.id.edt_descripcion_socio5);
        ImageButton btnSubirFotoVideo = (ImageButton) view.findViewById(R.id.btn_subir_foto_video) ;
        mImgFoto = (ImageView) view.findViewById(R.id.img_foto_proyecto);
        btnSubirFotoVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarOpcionesSubir();
            }
        });

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

        Button btnPublicarProyecto = (Button) view.findViewById(R.id.btn_registrar_proyecto);
        btnPublicarProyecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        registrarProyecto(v);

                    }
                },1000);

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

    private void mostrarOpcionesSubir() {
        final CharSequence[] opciones = {"Subir foto", "Subir Video", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Elige una opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (opciones[which] == "Subir foto") {
                    abrirGaleria("F");
                } else if (opciones[which] == "Subir Video") {
                    abrirGaleria("V");
                } else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void abrirGaleria(String opcion) {
        switch (opcion){
            case "F":
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
                break;
            case "V":

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
             mImageUri = data.getData();

            //mPostImageSelect.setImageURI(mImageUri);
            Glide.with(getActivity().getApplicationContext())
                    .load(mImageUri)
                    .into(mImgFoto);

        }
    }

    private void registrarProyecto(View v) {

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Proyectos");
        mProgresDialog= new ProgressDialog(getActivity());

        if( mImageUri !=null){
            mProgresDialog.setMessage("Publicando Proyectos");
            mProgresDialog.setCancelable(false);
            mProgresDialog.show();
            final StorageReference filepath = mStorage
                    .child("Proyectos_images")
                    .child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                    //filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                       @Override
                       public void onSuccess(Uri uri) {
                           Uri downloadUrl = uri;

                           mDatabase = FirebaseDatabase.getInstance().getReference().child("Proyectos");

                           DatabaseReference newPost = mDatabase.push();

                           newPost.child("titulo").setValue("Android sexo");
                           newPost.child("imagen").setValue(downloadUrl.toString());

                           //Timestamp fechaRegistro = getFecha();
                           //newPost.child("fechaRegistro").setValue(fechaRegistro);
                           mProgresDialog.dismiss();
                       }
                    });


                }

            });
}


    }

    public Timestamp getFecha(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        return timestamp;
    }


    private boolean validarCampos() {
        return true;
    }

    private boolean validarEditText(EditText edt){
        return (edt!=null && !edt.getText().toString().isEmpty()) ? true : false;
    }
}