package com.valdemar.emprendedores.view.ui.empresa;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.VideoView;

import com.valdemar.emprendedores.R;

public class RegistrarEmpresaFragment extends Fragment {

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
    private EditText spinner_comercio_exterior;
    private EditText spinner_contrata_estado;
    private EditText edt_descripcion_actividad;
    private EditText spinner_pais;
    private EditText spinner_ciudad;
    private EditText edt_direccion_empresa;
    private ImageButton btn_subir_foto_video;
    private ImageView img_foto_proyecto;
    private VideoView videoview_proyecto;
    private EditText edt_facebook;
    private EditText edt_instagram;
    private EditText edt_linkedin;


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


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registrar_empresa, container, false);
    }
}