package com.valdemar.emprendedores.view.ui.empresa;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

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
import com.google.android.material.snackbar.Snackbar;
import com.valdemar.emprendedores.R;

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
    private Spinner spinner_comercio_exterior;
    private Spinner spinner_contrata_estado;
    private EditText edt_descripcion_actividad;
    private Spinner spinner_pais;
    private Spinner spinner_ciudad;
    private EditText edt_direccion_empresa;
    private ImageButton btn_subir_foto_video;
    private ImageView img_foto_proyecto;
    private VideoView mVideoView;
    private EditText edt_facebook;
    private EditText edt_instagram;
    private EditText edt_linkedin;
    private Button btn_registrar_empresa;

    private Uri mImageUri = null;
    private Uri mVideoUri = null;
    private boolean mFotoSubida;
    private boolean mVideoSubido;

    public RegistrarEmpresaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        edt_nro_documento_empresa = (EditText) view.findViewById(R.id.edt_nro_documento_empresa);;
        spinner_categoria_empresa = (Spinner) view.findViewById(R.id.spinner_categoria_empresa);
        edt_correo_electronico = (EditText) view.findViewById(R.id.edt_correo_electronico);
        edt_telefono_empresa =  (EditText) view.findViewById(R.id.edt_telefono_empresa);
        edt_celular_empresa = (EditText) view.findViewById(R.id.edt_celular_empresa);
        edt_contacto_empresa = (EditText) view.findViewById(R.id.edt_contacto_empresa);
        edt_sitio_web = (EditText) view.findViewById(R.id.edt_sitio_web);
        spinner_comercio_exterior = (Spinner) view.findViewById(R.id.spinner_comercio_exterior);
        spinner_contrata_estado = (Spinner) view.findViewById(R.id.spinner_contrata_estado);
        edt_descripcion_actividad = (EditText) view.findViewById(R.id.edt_descripcion_actividad);
        spinner_pais = (Spinner) view.findViewById(R.id.spinner_pais);
        spinner_ciudad = (Spinner) view.findViewById(R.id.spinner_ciudad);
        edt_direccion_empresa = (EditText) view.findViewById(R.id.edt_direccion_empresa);
        btn_subir_foto_video = (ImageButton) view.findViewById(R.id.btn_subir_foto_video);
        img_foto_proyecto = (ImageView) view.findViewById(R.id.img_foto_empresa);
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
        if(resultCode == RESULT_OK){
            switch (requestCode){
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
                    double duracionVideoSegundos = duracionVideoMiliSegundos/1000.0;
                    retriever.release();
                    //Toast.makeText(getActivity(),"Duracion en s: " + duracionVideoSegundos, Toast.LENGTH_LONG).show();
                    if(duracionVideoSegundos > 60.0) {
                        showSnackBar("El video excede el tiempo limite - 60 segundos");
                        return;
                    }
                    mImgFoto.setVisibility(View.GONE);
                    mVideoView.setVisibility(View.VISIBLE);
                    mImageUri = mVideoUri;
                    MediaController mediaController= new MediaController(getActivity());
                    mVideoView.setVideoURI(mVideoUri);
                    mVideoView.setMediaController(mediaController);
                    mVideoView.start();
                    mVideoSubido = true;
                    mFotoSubida = true;
            }
        }
    }

    public void showSnackBar(String msg) {
        Snackbar
                .make(getActivity().findViewById(R.id.constraint_crear_proyecto), msg, Snackbar.LENGTH_SHORT)
                .show();
    }

    private void registrarEmpresa(View v) {
        hideSoftKeyboard();
        if (validarCampos()) {

        }

    }

    public void hideSoftKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    private boolean validarCampos() {
        return true;
    }
}