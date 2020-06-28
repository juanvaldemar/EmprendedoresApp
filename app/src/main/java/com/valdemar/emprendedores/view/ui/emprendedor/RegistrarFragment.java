package com.valdemar.emprendedores.view.ui.emprendedor;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.valdemar.emprendedores.R;
import com.valdemar.emprendedores.view.CategoriasFragment;

import java.sql.Timestamp;

import static android.app.Activity.RESULT_OK;

/**
 * @author Valdemar
 */
public class RegistrarFragment extends Fragment {
    private static final int GALLERY_REQUEST = 0;
    private Uri mImageUri = null;

    private ProgressDialog mProgresDialog;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Task<Uri> mRuta;
    private View mRoot;


    private ImageView mImgFoto;

    private EditText edt_nombres_emprendedor,edt_apellidos_emprendedor,
            edt_num_emprendedor,edt_dni_emprendedor,edt_direccion_emprendedor;
    private  Spinner spinner_dia,spinner_mes,spinner_anio,spinner_genero,
            spinner_pais,spinner_ciudad;
    private Button btn_registrar_emprendedor;

    public RegistrarFragment() {
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
        mRoot = inflater.inflate(R.layout.fragment_registrar, container, false);
        mAuth = FirebaseAuth.getInstance();
        initListSpinner(mRoot);
        initView(mRoot);


        return mRoot;
    }

    private void initView(View root) {


        mImgFoto = root.findViewById(R.id.img_foto_emprendedor);
        edt_nombres_emprendedor = root.findViewById(R.id.edt_nombres_emprendedor);
        edt_apellidos_emprendedor = root.findViewById(R.id.edt_apellidos_emprendedor);
        spinner_dia = root.findViewById(R.id.spinner_dia);
        spinner_mes = root.findViewById(R.id.spinner_mes);
        spinner_anio = root.findViewById(R.id.spinner_anio);
        spinner_genero = root.findViewById(R.id.spinner_genero);
        edt_num_emprendedor = root.findViewById(R.id.edt_num_emprendedor);
        edt_dni_emprendedor = root.findViewById(R.id.edt_dni_emprendedor);
        spinner_pais = root.findViewById(R.id.spinner_pais);
        spinner_ciudad = root.findViewById(R.id.spinner_ciudad);
        edt_direccion_emprendedor = root.findViewById(R.id.edt_direccion_emprendedor);
        btn_registrar_emprendedor = root.findViewById(R.id.btn_registrar_emprendedor);



        mImgFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarOpcionesSubir();
            }
        });

        btn_registrar_emprendedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarProyecto(view);
            }
        });
    }


    private void registrarProyecto(final View v) {

        if(validarCampos()){
            mStorage = FirebaseStorage.getInstance().getReference();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Emprendedor");
            mProgresDialog= new ProgressDialog(getActivity());
            mProgresDialog.setMessage("Registrando Emprendedor");
            mProgresDialog.setCancelable(false);
            mProgresDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if(mImageUri !=null){

                        final StorageReference filepath = mStorage
                                .child("Emprendedor_images")
                                .child(mImageUri.getLastPathSegment());
                        filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                                //filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Uri downloadUrl = uri;
                                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Emprendedor");
                                        DatabaseReference newPost = mDatabase.push();

                                        String user_id = mAuth.getCurrentUser().getUid();

                                        newPost.child("id_emprendedor").setValue(user_id);

                                        newPost.child("edt_nombres_emprendedor").setValue(edt_nombres_emprendedor.getText().toString().trim());
                                        newPost.child("edt_apellidos_emprendedor").setValue(edt_apellidos_emprendedor.getText().toString().trim());
                                        newPost.child("spinner_dia").setValue(spinner_dia.getSelectedItem().toString());
                                        newPost.child("spinner_mes").setValue(spinner_mes.getSelectedItem().toString());
                                        newPost.child("spinner_anio").setValue(spinner_anio.getSelectedItem().toString());
                                        newPost.child("spinner_genero").setValue(spinner_genero.getSelectedItem().toString());
                                        newPost.child("edt_num_emprendedor").setValue(edt_num_emprendedor.getText().toString().trim());
                                        newPost.child("edt_dni_emprendedor").setValue(edt_dni_emprendedor.getText().toString().trim());
                                        newPost.child("spinner_pais").setValue(spinner_pais.getSelectedItem().toString());
                                        newPost.child("spinner_ciudad").setValue(spinner_ciudad.getSelectedItem().toString());
                                        newPost.child("edt_direccion_emprendedor").setValue(edt_direccion_emprendedor.getText().toString().trim());

                                        newPost.child("imagen").setValue(downloadUrl.toString());

                                        Timestamp fechaRegistro = getFecha();
                                        newPost.child("fechaRegistro").setValue(fechaRegistro);
                                        mProgresDialog.dismiss();
                                        Navigation.findNavController(mRoot).navigate(R.id.next_action_to_lista);
                                    }
                                });


                            }

                        });
                    }else{
                        DatabaseReference newPost = mDatabase.push();

                        String user_id = mAuth.getCurrentUser().getUid();

                        newPost.child("id_emprendedor").setValue(user_id);

                        newPost.child("edt_nombres_emprendedor").setValue(edt_nombres_emprendedor.getText().toString().trim());
                        newPost.child("edt_apellidos_emprendedor").setValue(edt_apellidos_emprendedor.getText().toString().trim());
                        newPost.child("spinner_dia").setValue(spinner_dia.getSelectedItem().toString());
                        newPost.child("spinner_mes").setValue(spinner_mes.getSelectedItem().toString());
                        newPost.child("spinner_anio").setValue(spinner_anio.getSelectedItem().toString());
                        newPost.child("spinner_genero").setValue(spinner_genero.getSelectedItem().toString());
                        newPost.child("edt_num_emprendedor").setValue(edt_num_emprendedor.getText().toString().trim());
                        newPost.child("edt_dni_emprendedor").setValue(edt_dni_emprendedor.getText().toString().trim());
                        newPost.child("spinner_pais").setValue(spinner_pais.getSelectedItem().toString());
                        newPost.child("spinner_ciudad").setValue(spinner_ciudad.getSelectedItem().toString());
                        newPost.child("edt_direccion_emprendedor").setValue(edt_direccion_emprendedor.getText().toString().trim());

                        newPost.child("imagen").setValue("Vacio");

                        Timestamp fechaRegistro = getFecha();
                        newPost.child("fechaRegistro").setValue(fechaRegistro);
                        mProgresDialog.dismiss();
                        Navigation.findNavController(mRoot).navigate(R.id.next_action_to_lista);


                    }

                }
            },1000);
        }


    }


    private boolean validarCampos() {

        String s=spinner_dia.getSelectedItem().toString();

        if (!(validarEditText(edt_nombres_emprendedor)
                && validarEditText(edt_apellidos_emprendedor)
                && validarEditText(edt_dni_emprendedor) && validarEditText(edt_num_emprendedor)
                && !spinner_dia.getSelectedItem().toString().equals("Día")
                && !spinner_mes.getSelectedItem().toString().equals("Mes")
                && !spinner_anio.getSelectedItem().toString().equals("Año")
                && !spinner_genero.getSelectedItem().toString().equals("Género")
                && !spinner_pais.getSelectedItem().toString().equals("País")
                && !spinner_ciudad.getSelectedItem().toString().equals("Ciudad")
                )) {
            showSnackBar("Campos incompletos");
            return false;
        }

        return true;
    }

    private boolean validarEditText(EditText edt) {
        return (!edt.getText().toString().isEmpty()) ? true : false;
    }

    public void showSnackBar(String msg) {
        Snackbar
                .make(getActivity().findViewById(R.id.constraint_registrar_emprendedor), msg, Snackbar.LENGTH_SHORT)
                .show();
    }


    public Timestamp getFecha(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        return timestamp;
    }

    private void mostrarOpcionesSubir() {
        final CharSequence[] opciones = {"Subir foto","Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Elige una opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (opciones[which] == "Subir foto") {
                    abrirGaleria("F");
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

    private void initListSpinner(View root) {
        Spinner spinner1,spinner2,spinner3,spinner4,spinner5,spinner6;
        spinner1 = (Spinner) root.findViewById(R.id.spinner_dia);
        spinner2 = (Spinner) root.findViewById(R.id.spinner_mes);
        spinner3 = (Spinner) root.findViewById(R.id.spinner_anio);
        spinner4 = (Spinner) root.findViewById(R.id.spinner_genero);
        spinner5 = (Spinner) root.findViewById(R.id.spinner_pais);
        spinner6 = (Spinner) root.findViewById(R.id.spinner_ciudad);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(),
                R.array.dia, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(),
                R.array.mes, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner2.setAdapter(adapter2);

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getActivity(),
                R.array.anio, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner3.setAdapter(adapter3);

        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(getActivity(),
                R.array.genero, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner4.setAdapter(adapter4);


        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(getActivity(),
                R.array.pais, android.R.layout.simple_spinner_item);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner5.setAdapter(adapter5);

        ArrayAdapter<CharSequence> adapter6 = ArrayAdapter.createFromResource(getActivity(),
                R.array.ciudad, android.R.layout.simple_spinner_item);
        adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner6.setAdapter(adapter6);

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

}