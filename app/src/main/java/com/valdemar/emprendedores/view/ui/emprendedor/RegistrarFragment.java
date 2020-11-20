package com.valdemar.emprendedores.view.ui.emprendedor;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.valdemar.emprendedores.model.Emprendedor;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

    private EditText edt_nombres_emprendedor, edt_apellidos_emprendedor,
            edt_num_emprendedor, edt_dni_emprendedor, edt_direccion_emprendedor,
            edt_facebook, edt_instagram, edt_twitter,edt_a_que_te_dedicas_emprendedor;

    private Spinner spinner_dia, spinner_mes, spinner_anio, spinner_genero,
            spinner_pais, spinner_ciudad, spinner_grado_academico, spinner_intereses;
    private Button btn_registrar_emprendedor;
    private Emprendedor mEmprendedor;
    private boolean mActualizarEmprendedor;

    private CheckBox checkBoxComida, checkBoxRopa, checkBoxTecnologia, checkBoxSalud, checkBoxEntrenimiento, checkBoxDeportes,
            checkBoxVideojuegos, checkBoxConsultorias, checkBoxTransportes, checkBoxMarketing, checkBoxFinanzas, checkBoxHogar;
    private ArrayList<String> items;
    private SharedPreferences prefs_notificacion = null;

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
        mActualizarEmprendedor = false;
        mRoot = inflater.inflate(R.layout.fragment_registrar, container, false);
        mAuth = FirebaseAuth.getInstance();
        initView(mRoot);

        return mRoot;
    }

    private void initView(View root) {
        prefs_notificacion = getActivity().getSharedPreferences("com.valdemar.spook.intereses", getActivity().MODE_PRIVATE);


        checkBoxComida = root.findViewById(R.id.checkoutBoxComida);
        checkBoxRopa = root.findViewById(R.id.checkoutBoxRopa);
        checkBoxTecnologia = root.findViewById(R.id.checkoutBoxTecnologia);
        checkBoxSalud = root.findViewById(R.id.checkoutBoxSalud);
        checkBoxEntrenimiento = root.findViewById(R.id.checkoutBoxEntretenimiento);
        checkBoxDeportes = root.findViewById(R.id.checkoutBoxDeportes);
        checkBoxVideojuegos = root.findViewById(R.id.checkoutBoxVideojuegos);
        checkBoxConsultorias = root.findViewById(R.id.checkoutBoxConsultorias);
        checkBoxTransportes = root.findViewById(R.id.checkoutBoxTransportes);
        checkBoxMarketing = root.findViewById(R.id.checkoutBoxMarketing);
        checkBoxFinanzas = root.findViewById(R.id.checkoutBoxFinanzas);
        checkBoxHogar = root.findViewById(R.id.checkoutBoxHogar);


        mImgFoto = root.findViewById(R.id.img_foto_emprendedor);
        edt_nombres_emprendedor = root.findViewById(R.id.edt_nombres_emprendedor);
        edt_a_que_te_dedicas_emprendedor = root.findViewById(R.id.edt_a_que_te_dedicas_emprendedor);
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
        spinner_grado_academico = root.findViewById(R.id.spinner_grado_academico);
        spinner_intereses = root.findViewById(R.id.spinner_intereses);
        edt_facebook = root.findViewById(R.id.edt_facebook);
        edt_instagram = root.findViewById(R.id.edt_instagram);
        edt_twitter = root.findViewById(R.id.edt_twitter);
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
                registrarEmprendedor(view);

                    if (getActivity().getCurrentFocus() != null) { InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                    }

            }
        });

        initListSpinner(root);

        if (getArguments() != null) {
            String jsonString = getArguments().getString(PerfilEmprendedorFragment.ARG_EMPRENDEDOR_DETALLE);
            if (!TextUtils.isEmpty(jsonString)) {
                mActualizarEmprendedor = true;
                mEmprendedor = (new Gson()).fromJson(jsonString, Emprendedor.class);
                cargarEmprendedor();
            }
        }
    }

    private void cargarEmprendedor() {
        if (!mEmprendedor.getImagen().isEmpty())
            Glide.with(getActivity().getApplicationContext())
                    .load(mEmprendedor.getImagen())
                    .into(mImgFoto);

        edt_nombres_emprendedor.setText(mEmprendedor.getEdt_nombres_emprendedor());
        edt_a_que_te_dedicas_emprendedor.setText(mEmprendedor.getDedicas());
        edt_apellidos_emprendedor.setText(mEmprendedor.getEdt_apellidos_emprendedor());

        ArrayAdapter<CharSequence> spnGeneroAdapter = (ArrayAdapter<CharSequence>) spinner_genero.getAdapter();
        spinner_genero.setSelection(spnGeneroAdapter.getPosition(mEmprendedor.getSpinner_genero()));

        ArrayAdapter<CharSequence> spAnioAdapter = (ArrayAdapter<CharSequence>) spinner_anio.getAdapter();
        spinner_anio.setSelection(spAnioAdapter.getPosition(mEmprendedor.getSpinner_anio()));

        ArrayAdapter<CharSequence> spMesAdapter = (ArrayAdapter<CharSequence>) spinner_mes.getAdapter();
        spinner_mes.setSelection(spMesAdapter.getPosition(mEmprendedor.getSpinner_mes()));

        ArrayAdapter<CharSequence> spDiaAdapter = (ArrayAdapter<CharSequence>) spinner_dia.getAdapter();
        spinner_dia.setSelection(spDiaAdapter.getPosition(mEmprendedor.getSpinner_dia()));

        edt_dni_emprendedor.setText(mEmprendedor.getEdt_dni_emprendedor());
        edt_num_emprendedor.setText(mEmprendedor.getEdt_num_emprendedor());
        edt_direccion_emprendedor.setText(mEmprendedor.getEdt_direccion_emprendedor());

        ArrayAdapter<CharSequence> spnGradoAdapter = (ArrayAdapter<CharSequence>) spinner_grado_academico.getAdapter();
        spinner_grado_academico.setSelection(spnGradoAdapter.getPosition(mEmprendedor.getGrado_academico()));

      ArrayAdapter<CharSequence> spnInteresAdapter = (ArrayAdapter<CharSequence>) spinner_intereses.getAdapter();
        spinner_intereses.setSelection(spnInteresAdapter.getPosition(mEmprendedor.getGrado_academico()));

        cargarIntereses(mEmprendedor.getIntereses());

        ArrayAdapter<CharSequence> spnPaisAdapter = (ArrayAdapter<CharSequence>) spinner_pais.getAdapter();
        spinner_pais.setSelection(spnPaisAdapter.getPosition(mEmprendedor.getSpinner_pais()));

        ArrayAdapter<CharSequence> spnCiudadAdapter = (ArrayAdapter<CharSequence>) spinner_ciudad.getAdapter();
        spinner_ciudad.setSelection(spnCiudadAdapter.getPosition(mEmprendedor.getSpinner_ciudad()));

        edt_facebook.setText(mEmprendedor.getEdt_facebook());
        edt_twitter.setText(mEmprendedor.getEdt_twitter());
        edt_instagram.setText(mEmprendedor.getEdt_instagram());

        String fechaRegistroStr = mEmprendedor.getFechaRegistro();
        int diasRegistrados = calcularDiasRegistrado(fechaRegistroStr);
        if (diasRegistrados < 31){
            edt_nombres_emprendedor.setEnabled(false);
            edt_apellidos_emprendedor.setEnabled(false);
        }

    }

    private void cargarIntereses(String intereses) {
        if(intereses!=null && !intereses.isEmpty()){
            String interesesSinCorchetes = intereses.replace("[","").replace("]","");
            List<String> listaIntereses = Arrays.asList(interesesSinCorchetes.split(", "));
            for(String interes : listaIntereses){
                switch(interes){
                    case "Comida": checkBoxComida.setChecked(true); break;
                    case "Ropa": checkBoxRopa.setChecked(true); break;
                    case "Tecnologia": checkBoxTecnologia.setChecked(true); break;
                    case "Salud": checkBoxSalud.setChecked(true); break;
                    case "Entretenimiento": checkBoxEntrenimiento.setChecked(true); break;
                    case "Deportes": checkBoxDeportes.setChecked(true); break;
                    case "Videojuegos": checkBoxVideojuegos.setChecked(true); break;
                    case "Consultorias": checkBoxConsultorias.setChecked(true); break;
                    case "Transportes": checkBoxTransportes.setChecked(true); break;
                    case "Marketing": checkBoxMarketing.setChecked(true); break;
                    case "Finanzas": checkBoxFinanzas.setChecked(true); break;
                    case "Hogar": checkBoxHogar.setChecked(true);
                }
            }
        }
    }

    private int calcularDiasRegistrado(String fechaRegistroStr){
        int diasRegistrado = 0;
        SimpleDateFormat sdfFechaRegistro = new SimpleDateFormat("dd/MM/yyyy");
        try{
            Date fechaRegistro = sdfFechaRegistro.parse(fechaRegistroStr);
            Date hoy = new Date();
            diasRegistrado = (int)( (hoy.getTime() - fechaRegistro.getTime()) / (1000 * 60 * 60 * 24));

        } catch (Exception e){
            e.printStackTrace();
        }
        return diasRegistrado;
    }

    private void registrarEmprendedor(final View v) {

        if (validarCampos()) {
            mStorage = FirebaseStorage.getInstance().getReference();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Emprendedor");
            mProgresDialog = new ProgressDialog(getActivity());
            mProgresDialog.setMessage("Registrando Emprendedor");
            mProgresDialog.setCancelable(false);
            mProgresDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (mImageUri != null) {

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
                                        DatabaseReference newPost;
                                        String user_id = mAuth.getCurrentUser().getUid();

                                        if (!mActualizarEmprendedor) {
                                            newPost = mDatabase.push();
                                            newPost.child("id_emprendedor").setValue(user_id);
                                        } else {
                                            newPost = mDatabase.child(mEmprendedor.getKey());
                                        }

                                        newPost.child("edt_nombres_emprendedor").setValue(edt_nombres_emprendedor.getText().toString().trim());
                                        newPost.child("dedicas").setValue(edt_a_que_te_dedicas_emprendedor.getText().toString().trim());
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
                                        newPost.child("edt_facebook").setValue(edt_facebook.getText().toString().trim());
                                        newPost.child("edt_instagram").setValue(edt_instagram.getText().toString().trim());
                                        newPost.child("edt_twitter").setValue(edt_twitter.getText().toString().trim());
                                        newPost.child("imagen").setValue(downloadUrl.toString());
                                        newPost.child("grado_academico").setValue(spinner_grado_academico.getSelectedItem().toString());
                                        newPost.child("intereses").setValue(items.toString());
                                        prefs_notificacion.edit().putString("intereses",items.toString()).commit();

                                        SimpleDateFormat sdfFechaRegistro = new SimpleDateFormat("dd/MM/yyyy");
                                        Date todayDate = new Date();
                                        newPost.child("fechaRegistro").setValue(sdfFechaRegistro.format(todayDate));

                                        mProgresDialog.dismiss();
                                        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.next_action_to_lista);
                                    }
                                });


                            }

                        });
                    } else {
                        DatabaseReference newPost;
                        String user_id = mAuth.getCurrentUser().getUid();
                        if (!mActualizarEmprendedor) {
                            newPost = mDatabase.push();
                            newPost.child("id_emprendedor").setValue(user_id);
                        } else {
                            newPost = mDatabase.child(mEmprendedor.getKey());
                        }

                        newPost.child("edt_nombres_emprendedor").setValue(edt_nombres_emprendedor.getText().toString().trim());
                        newPost.child("dedicas").setValue(edt_a_que_te_dedicas_emprendedor.getText().toString().trim());
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
                        newPost.child("edt_facebook").setValue(edt_facebook.getText().toString().trim());
                        newPost.child("edt_instagram").setValue(edt_instagram.getText().toString().trim());
                        newPost.child("edt_twitter").setValue(edt_twitter.getText().toString().trim());
                        newPost.child("grado_academico").setValue(spinner_grado_academico.getSelectedItem().toString());
                        newPost.child("intereses").setValue(items.toString());
                        prefs_notificacion.edit().putString("intereses",items.toString()).commit();

                        //newPost.child("imagen").setValue("Vacio");

                        SimpleDateFormat sdfFechaRegistro = new SimpleDateFormat("dd/MM/yyyy");
                        Date todayDate = new Date();
                        newPost.child("fechaRegistro").setValue(sdfFechaRegistro.format(todayDate));
                        mProgresDialog.dismiss();
                        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.next_action_to_lista);


                    }

                }
            }, 1000);
        }


    }


    private boolean validarCampos() {
       items = new ArrayList<String>();
        int counter = 0;

        if (checkBoxComida.isChecked()) {
            counter++;
            items.add(checkBoxComida.getText().toString());
        }
        if (checkBoxRopa.isChecked()){
            counter++;
            items.add(checkBoxRopa.getText().toString());
        }
        if (checkBoxTecnologia.isChecked()){
            counter++; items.add(checkBoxTecnologia.getText().toString());
        }
        if (checkBoxSalud.isChecked()){
            counter++; items.add(checkBoxSalud.getText().toString());
        }
        if (checkBoxEntrenimiento.isChecked()) {
            counter++; items.add(checkBoxEntrenimiento.getText().toString());
        }
        if (checkBoxDeportes.isChecked()) {
            counter++; items.add(checkBoxDeportes.getText().toString());
        }
        if (checkBoxVideojuegos.isChecked()) {
            counter++; items.add(checkBoxVideojuegos.getText().toString());
        }
        if (checkBoxConsultorias.isChecked()) {
            counter++; items.add(checkBoxConsultorias.getText().toString());
        }
        if (checkBoxTransportes.isChecked()) {
            counter++; items.add(checkBoxTransportes.getText().toString());
        }
        if (checkBoxMarketing.isChecked()) {
            counter++; items.add(checkBoxMarketing.getText().toString());
        }
        if (checkBoxFinanzas.isChecked()) {
            counter++; items.add(checkBoxFinanzas.getText().toString());
        }
        if (checkBoxHogar.isChecked()) {
            counter++; items.add(checkBoxHogar.getText().toString());
        }

        if (counter < 4) {
            showSnackBar("Registrado");
            System.out.println(items);
        }
        // ...more than 2 CheckBoxes checked
        else {
            showSnackBar("No puedes seleccionar más de 3 intereses");

            return false;
        }

        String s = spinner_dia.getSelectedItem().toString();
//                && !spinner_ocupacion.getSelectedItem().toString().equals("¿Cuál es tu ocupación?")
        if (!(validarEditText(edt_nombres_emprendedor)
                && validarEditText(edt_apellidos_emprendedor)
                && validarEditText(edt_num_emprendedor)
                && validarEditText(edt_a_que_te_dedicas_emprendedor)
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


    public Timestamp getFecha() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        return timestamp;
    }

    private void mostrarOpcionesSubir() {
        final CharSequence[] opciones = {"Subir foto", "Cancelar"};
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
        switch (opcion) {
            case "F":
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
                break;
            case "V":

        }
    }

    private void initListSpinner(View root) {
        Spinner spinner1, spinner2, spinner3, spinner4, spinner5, spinner6,spinner7,spinner8;
        spinner1 = (Spinner) root.findViewById(R.id.spinner_dia);
        spinner2 = (Spinner) root.findViewById(R.id.spinner_mes);
        spinner3 = (Spinner) root.findViewById(R.id.spinner_anio);
        spinner4 = (Spinner) root.findViewById(R.id.spinner_genero);
        spinner5 = (Spinner) root.findViewById(R.id.spinner_pais);
        spinner6 = (Spinner) root.findViewById(R.id.spinner_ciudad);
        spinner7 = (Spinner) root.findViewById(R.id.spinner_grado_academico);
        spinner8 = (Spinner) root.findViewById(R.id.spinner_intereses);

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


        ArrayAdapter<CharSequence> adapterGrado = ArrayAdapter.createFromResource(getActivity(),
                R.array.gradoAcademico, android.R.layout.simple_spinner_item);
        adapterGrado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_grado_academico.setAdapter(adapterGrado);

        ArrayAdapter<CharSequence> adapterIntereses = ArrayAdapter.createFromResource(getActivity(),
                R.array.intereses, android.R.layout.simple_spinner_item);
        adapterIntereses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_intereses.setAdapter(adapterIntereses);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            mImageUri = data.getData();

            //mPostImageSelect.setImageURI(mImageUri);
            Glide.with(getActivity().getApplicationContext())
                    .load(mImageUri)
                    .into(mImgFoto);

        }
    }

}