package com.valdemar.emprendedores.view.ui.empresa;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.valdemar.emprendedores.R;
import com.valdemar.emprendedores.model.Empresa;
import com.valdemar.emprendedores.util.Validaciones;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class RegistrarEmpresaFragment extends Fragment {

    private static final int FOTO_GALLERY_REQUEST = 0;
    private static final int VIDEO_GALLERY_REQUEST = 1;

    private ImageView mImgFoto;
    private EditText edt_nombre_empresa;
    private Spinner spinner_tipo_doc_empresa;
    private EditText edt_nro_documento_empresa;
    private Spinner spinner_categoria_empresa;
    private EditText edt_correo_electronico;
    private EditText edt_telefono_empresa;
    private EditText edt_celular_empresa;
    private EditText edt_contacto_empresa;
    private EditText edt_sitio_web;
    private Spinner spinner_modalidad_empresa;
    private Spinner spinner_comercio_exterior;
    private Spinner spinner_contrata_estado;
    private EditText edt_descripcion_actividad;
    private Spinner spinner_pais;
    private Spinner spinner_ciudad;
    private EditText edt_direccion_empresa;
    private ImageButton btn_subir_foto_video;
    private VideoView mVideoView;
    private EditText edt_facebook;
    private EditText edt_instagram;
    private EditText edt_linkedin;
    private Button btn_registrar_empresa;

    private Uri mImageUri = null;
    private Uri mVideoUri = null;
    private boolean mFotoSubida;
    private boolean mVideoSubido;

    private DatabaseReference mDatabaseEmpresa = FirebaseDatabase.getInstance().getReference().child("Empresa");
    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private StorageReference mStorage;
    private View mRoot;
    private ProgressDialog mProgress;

    public RegistrarEmpresaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgress = new ProgressDialog(getActivity());
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registrar_empresa, container, false);
        mImgFoto = (ImageView) view.findViewById(R.id.img_foto_empresa);
        edt_nombre_empresa = (EditText) view.findViewById(R.id.edt_nombre_empresa);
        spinner_tipo_doc_empresa = (Spinner) view.findViewById(R.id.spinner_tipo_doc_empresa);
        edt_nro_documento_empresa = (EditText) view.findViewById(R.id.edt_nro_documento_empresa);
        ;
        spinner_categoria_empresa = (Spinner) view.findViewById(R.id.spinner_categoria_empresa);
        edt_correo_electronico = (EditText) view.findViewById(R.id.edt_correo_electronico);
        edt_telefono_empresa = (EditText) view.findViewById(R.id.edt_telefono_empresa);
        edt_celular_empresa = (EditText) view.findViewById(R.id.edt_celular_empresa);
        edt_contacto_empresa = (EditText) view.findViewById(R.id.edt_contacto_empresa);
        edt_sitio_web = (EditText) view.findViewById(R.id.edt_sitio_web);
        spinner_comercio_exterior = (Spinner) view.findViewById(R.id.spinner_comercio_exterior);
        spinner_modalidad_empresa = (Spinner) view.findViewById(R.id.spinner_modalidad_empresa);
        spinner_contrata_estado = (Spinner) view.findViewById(R.id.spinner_contrata_estado);
        edt_descripcion_actividad = (EditText) view.findViewById(R.id.edt_descripcion_actividad);
        spinner_pais = (Spinner) view.findViewById(R.id.spinner_pais);
        spinner_ciudad = (Spinner) view.findViewById(R.id.spinner_ciudad);
        edt_direccion_empresa = (EditText) view.findViewById(R.id.edt_direccion_empresa);
        btn_subir_foto_video = (ImageButton) view.findViewById(R.id.btn_subir_foto_video);
        mVideoView = (VideoView) view.findViewById(R.id.videoview_empresa);
        edt_facebook = (EditText) view.findViewById(R.id.edt_facebook);
        edt_instagram = (EditText) view.findViewById(R.id.edt_instagram);
        edt_linkedin = (EditText) view.findViewById(R.id.edt_linkedin);
        btn_registrar_empresa = (Button) view.findViewById(R.id.btn_registrar_empresa);

        ArrayAdapter<CharSequence> spnTipoDocumento = ArrayAdapter.createFromResource(getActivity(),
                R.array.tipoDocumento, android.R.layout.simple_spinner_item);
        spnTipoDocumento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tipo_doc_empresa.setAdapter(spnTipoDocumento);

        ArrayAdapter<CharSequence> spnCategoriaEmpresa = ArrayAdapter.createFromResource(getActivity(),
                R.array.categoriaEmpresa, android.R.layout.simple_spinner_item);
        spnCategoriaEmpresa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_categoria_empresa.setAdapter(spnCategoriaEmpresa);

        ArrayAdapter<CharSequence> spnModalidadEmpresa = ArrayAdapter.createFromResource(getActivity(),
                R.array.modalidadEmpresa, android.R.layout.simple_spinner_item);
        spnModalidadEmpresa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_modalidad_empresa.setAdapter(spnModalidadEmpresa);

        ArrayAdapter<CharSequence> spnComercioExterior = ArrayAdapter.createFromResource(getActivity(),
                R.array.comercioExterior, android.R.layout.simple_spinner_item);
        spnComercioExterior.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_comercio_exterior.setAdapter(spnComercioExterior);

        ArrayAdapter<CharSequence> spnContrataEstado = ArrayAdapter.createFromResource(getActivity(),
                R.array.contrataEstado, android.R.layout.simple_spinner_item);
        spnComercioExterior.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_contrata_estado.setAdapter(spnContrataEstado);

        ArrayAdapter<CharSequence> spnPaisAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.pais, android.R.layout.simple_spinner_item);
        spnPaisAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_pais.setAdapter(spnPaisAdapter);

        ArrayAdapter<CharSequence> spnCiudadAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.ciudad, android.R.layout.simple_spinner_item);
        spnCiudadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_ciudad.setAdapter(spnCiudadAdapter);

        btn_subir_foto_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarOpcionesSubir();
            }
        });

        btn_registrar_empresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarEmpresa(v);
            }
        });

        mRoot = view;
        return view;
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
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        switch (opcion) {
            case "F":
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, FOTO_GALLERY_REQUEST);
                break;
            case "V":
                galleryIntent.setType("video/*");
                startActivityForResult(galleryIntent, VIDEO_GALLERY_REQUEST);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case FOTO_GALLERY_REQUEST:
                    mVideoView.setVisibility(View.GONE);
                    mImgFoto.setVisibility(View.VISIBLE);
                    mImageUri = data.getData();
                    //mPostImageSelect.setImageURI(mImageUri);
                    Glide.with(getActivity().getApplicationContext())
                            .load(mImageUri)
                            .into(mImgFoto);
                    mFotoSubida = true;
                    mVideoSubido = false;
                    break;
                case VIDEO_GALLERY_REQUEST:
                    mVideoUri = data.getData();
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(getActivity(), mVideoUri);
                    String duracionVideoString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    long duracionVideoMiliSegundos = Long.parseLong(duracionVideoString);
                    double duracionVideoSegundos = duracionVideoMiliSegundos / 1000.0;
                    retriever.release();
                    //Toast.makeText(getActivity(),"Duracion en s: " + duracionVideoSegundos, Toast.LENGTH_LONG).show();
                    if (duracionVideoSegundos > 60.0) {
                        showSnackBar("El video excede el tiempo limite - 60 segundos");
                        return;
                    }
                    mImgFoto.setVisibility(View.GONE);
                    mVideoView.setVisibility(View.VISIBLE);
                    mImageUri = mVideoUri;
                    MediaController mediaController = new MediaController(getActivity());
                    mVideoView.setVideoURI(mVideoUri);
                    mVideoView.setMediaController(mediaController);
                    mVideoView.start();
                    mVideoSubido = true;
                    mFotoSubida = false;
            }
        }
    }

    public void showSnackBar(String msg) {
        Snackbar
                .make(getActivity().findViewById(R.id.container_registrar_empresa), msg, Snackbar.LENGTH_LONG)
                .show();
    }

    private void registrarEmpresa(View v) {
        hideSoftKeyboard();
        if (validarCampos()) {
            final Empresa nuevoRegistroEmpresa = initDataEmpresa();
            if (mFotoSubida || mVideoSubido) {
                mStorage = FirebaseStorage.getInstance().getReference();
                final StorageReference filepath = mStorage.child("Empresas_images").child(mImageUri.getLastPathSegment());
                filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String uploadUrl = uri.toString();
                                nuevoRegistroEmpresa.setImagen(uploadUrl);
                                guardarEmpresa(nuevoRegistroEmpresa);
                            }
                        });
                    }
                });
            } else {
                guardarEmpresa(nuevoRegistroEmpresa);
            }

        }

    }

    private Empresa initDataEmpresa() {
        final Empresa empresa = new Empresa();
        empresa.setCategoria(spinner_categoria_empresa.getSelectedItem().toString());
        empresa.setCelular(edt_celular_empresa.getText().toString());
        empresa.setCiudad(spinner_ciudad.getSelectedItem().toString());
        empresa.setComercioExterior(spinner_comercio_exterior.getSelectedItem().toString());
        empresa.setContacto(edt_contacto_empresa.getText().toString());
        empresa.setCorreoElectronico(edt_correo_electronico.getText().toString());
        empresa.setContrataEstado(spinner_contrata_estado.getSelectedItem().toString());
        empresa.setModalidadEmpresa(spinner_modalidad_empresa.getSelectedItem().toString());
        empresa.setDescripcion(edt_descripcion_actividad.getText().toString());
        empresa.setDireccion(edt_direccion_empresa.getText().toString());
        empresa.setEdt_facebook(edt_facebook.getText().toString());
        empresa.setEdt_instagram(edt_instagram.getText().toString());
        empresa.setEdt_linkedin(edt_linkedin.getText().toString());
        empresa.setNombre(edt_nombre_empresa.getText().toString());
        empresa.setNumeroDocumento(edt_nro_documento_empresa.getText().toString());
        empresa.setPais(spinner_pais.getSelectedItem().toString());
        empresa.setSitioWeb(edt_sitio_web.getText().toString());
        empresa.setTelefono(edt_telefono_empresa.getText().toString());
        empresa.setTipoDocumento(spinner_tipo_doc_empresa.getSelectedItem().toString());
        empresa.setVideoSubido(mVideoSubido ? "true" : "false");
        return empresa;
    }

    private void guardarEmpresa(Empresa nuevoRegistroEmpresa) {
        DatabaseReference nuevaEmpresaRef = mDatabaseEmpresa.push();
        nuevaEmpresaRef.setValue(nuevoRegistroEmpresa);
        mProgress.show();
        Navigation.findNavController(mRoot).navigate(R.id.next_action_to_lista_empresas);
        mProgress.dismiss();
    }

    public void hideSoftKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    private boolean validarCampos() {
        if (!(Validaciones.validarEditText(edt_nombre_empresa)
                && Validaciones.validarEditText(edt_nro_documento_empresa)
                && Validaciones.validarEditText(edt_telefono_empresa) && Validaciones.validarEditText(edt_celular_empresa)
                && Validaciones.validarEditText(edt_contacto_empresa)
                && Validaciones.validarEditText(edt_descripcion_actividad)
                && Validaciones.validarEditText(edt_direccion_empresa)
                && !spinner_pais.getSelectedItem().toString().equals("País")
                && !spinner_ciudad.getSelectedItem().toString().equals("Ciudad")
                && (mFotoSubida || mVideoSubido))) {
            showSnackBar("Campos incompletos y/o falta subir foto o video");
            return false;
        }

        if (!Validaciones.validarTelefono("TEL", edt_telefono_empresa.getText().toString())) {
            showSnackBar("Telefono formato: (0 + codigo ciudad) + número, ejem: 014337889");
            return false;
        }
        if (!Validaciones.validarTelefono("CEL", edt_celular_empresa.getText().toString())) {
            showSnackBar("Celular formato: longitud e inicio con 9 ");
            return false;
        }
        String tipoDocumentoEmpresa = spinner_tipo_doc_empresa.getSelectedItem().toString();
        if (!Validaciones.validarDocumento(tipoDocumentoEmpresa
                , edt_nro_documento_empresa.getText().toString())) {
            if(tipoDocumentoEmpresa.equalsIgnoreCase("DNI")){
                showSnackBar("DNI formato: 8 dígitos");
                return false;
            }

            if(tipoDocumentoEmpresa.equalsIgnoreCase("RUC")){
                showSnackBar("RUC formato: 11 dígitos y empezar con 15 o 20");
                return false;
            }
        }

        if(!Validaciones.validarCorreo(edt_correo_electronico.getText().toString())){
            showSnackBar("Correo electrónico incorrecto");
            return false;
        }

        if(!Validaciones.validarSitioWeb(edt_sitio_web.getText().toString())){
            showSnackBar("URL Sitio web incorrecto");
            return false;
        }

        return true;
    }


}