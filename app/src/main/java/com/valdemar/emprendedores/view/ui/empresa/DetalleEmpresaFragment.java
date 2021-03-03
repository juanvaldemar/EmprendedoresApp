package com.valdemar.emprendedores.view.ui.empresa;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.valdemar.emprendedores.R;
import com.valdemar.emprendedores.model.Emprendedor;
import com.valdemar.emprendedores.model.Empresa;

import java.util.Locale;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetalleEmpresaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetalleEmpresaFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    private String mPost_key = null;
    private boolean mEmprendedorRegistrado;
    private Emprendedor mEmprendedorActual;
    private DatabaseReference mDatabase;
    private Empresa mEmpresa = new Empresa();

    private TextView txt_nombre_empresa;
    private TextView txt_descripcion_empresa;
    private TextView txt_tipo_numero_documento;
    private TextView txt_categoria_empresa;
    private TextView txt_direccion_empresa;
    private TextView txt_telefonos_empresa;
    private TextView txt_correo_empresa;
    private TextView txt_contacto_autorizado;
    private TextView txt_tipo_local_empresa;
    private TextView txt_importa_estado_empresa;

    private VideoView mVideoView;
    private ImageView mImage_paralax;

    private ImageButton btn_facebook;
    private ImageButton btn_instagram;
    private ImageButton btn_linkedin;

    public DetalleEmpresaFragment() {
        // Required empty public constructor
    }

    public static DetalleEmpresaFragment newInstance(String param1, String param2) {
        DetalleEmpresaFragment fragment = new DetalleEmpresaFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detalle_empresa, container, false);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        verificarRegistroEmprendedor();
        initView(root);
        return root;
    }

    private void initView(final View root) {

        Bundle datosRecuperados = getArguments();
        if (datosRecuperados == null) {
            // No hay datos, manejar excepción
            return;
        }
        mPost_key = datosRecuperados.getString("key_empresa");
        txt_nombre_empresa = (TextView) root.findViewById(R.id.txt_nombre_empresa);
        txt_descripcion_empresa = (TextView) root.findViewById(R.id.txt_descripcion_empresa);
        txt_tipo_numero_documento = (TextView) root.findViewById(R.id.txt_tipo_numero_documento);
        txt_categoria_empresa = (TextView) root.findViewById(R.id.txt_categoria_empresa);
        txt_direccion_empresa = (TextView) root.findViewById(R.id.txt_direccion_empresa);
        txt_telefonos_empresa = (TextView) root.findViewById(R.id.txt_telefonos_empresa);
        txt_correo_empresa = (TextView) root.findViewById(R.id.txt_correo_empresa);
        txt_contacto_autorizado = (TextView) root.findViewById(R.id.txt_contacto_autorizado);
        txt_tipo_local_empresa = (TextView) root.findViewById(R.id.txt_tipo_local_empresa);
        txt_importa_estado_empresa = (TextView) root.findViewById(R.id.txt_importa_estado_empresa);

        mVideoView = (VideoView) root.findViewById(R.id.videoview_proyecto);
        mImage_paralax = (ImageView) root.findViewById(R.id.image_paralax);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Empresa");
        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {


                String videoSubido_ = (String) dataSnapshot.child("videoSubido").getValue();
                boolean videoSubido = Boolean.parseBoolean(videoSubido_);

                String nombreEmpresa = (String) dataSnapshot.child("nombre").getValue();
                mEmpresa.setNombre(nombreEmpresa);
                txt_nombre_empresa.setText(nombreEmpresa);

                String descripcionEmpresa = (String) dataSnapshot.child("descripcion").getValue();
                mEmpresa.setDescripcion(descripcionEmpresa);
                txt_descripcion_empresa.setText(descripcionEmpresa);

                final String imagen_url = (String) dataSnapshot.child("imagen").getValue();
                mEmpresa.setImagen(imagen_url);

                String categoria = (String) dataSnapshot.child("categoria").getValue();
                mEmpresa.setCategoria(categoria);
                txt_categoria_empresa.setText("Categoría: " + categoria);

                String numeroDocumento = (String) dataSnapshot.child("numeroDocumento").getValue();
                mEmpresa.setNumeroDocumento(numeroDocumento);
                String tipoDocumento = (String) dataSnapshot.child("tipoDocumento").getValue();
                mEmpresa.setTipoDocumento(tipoDocumento);
                txt_tipo_numero_documento.setText(tipoDocumento + ": " + numeroDocumento);

                String telefono = (String) dataSnapshot.child("telefono").getValue();
                mEmpresa.setTelefono(telefono);
                String celular = (String) dataSnapshot.child("celular").getValue();
                mEmpresa.setCelular(celular);
                String telefonos = "";
                if (telefono != null && !telefono.isEmpty())
                    telefonos = "Tel: " + telefono;
                if (celular != null && !celular.isEmpty())
                    if (!telefonos.isEmpty())
                        telefonos += " / Cel: " + celular;
                    else
                        telefonos += "Cel: " + celular;
                txt_telefonos_empresa.setText(telefonos);

                String pais = (String) dataSnapshot.child("pais").getValue();
                mEmpresa.setPais(pais);
                String ciudad = (String) dataSnapshot.child("ciudad").getValue();
                mEmpresa.setCiudad(ciudad);
                String direccion = (String) dataSnapshot.child("direccion").getValue();
                mEmpresa.setCelular(direccion);
                txt_direccion_empresa.setText(direccion + " - " + ciudad + " - " + pais);

                String comercioExterior = (String) dataSnapshot.child("comercioExterior").getValue();
                mEmpresa.setComercioExterior(comercioExterior);
                String contrataEstado = (String) dataSnapshot.child("contrataEstado").getValue();
                mEmpresa.setContrataEstado(contrataEstado);
                txt_importa_estado_empresa.setText("Tipo de Actividad: " + comercioExterior + " - " + contrataEstado);

                String correoElectronico = (String) dataSnapshot.child("correoElectronico").getValue();
                mEmpresa.setCorreoElectronico(correoElectronico);
                txt_correo_empresa.setText("Email: " + correoElectronico);

                String contactoAutorizado = (String) dataSnapshot.child("contacto").getValue();
                mEmpresa.setCorreoElectronico(correoElectronico);
                txt_contacto_autorizado.setText("Nombre del Contacto: " + contactoAutorizado);

                String modadlidadEmpresa = (String) dataSnapshot.child("modadlidadEmpresa").getValue();
                mEmpresa.setModalidadEmpresa(modadlidadEmpresa);
                txt_tipo_local_empresa.setText("Modalidad de Ventas: " + contactoAutorizado);

                String modalidadEmpresa = (String) dataSnapshot.child("modalidadEmpresa").getValue();
                mEmpresa.setCorreoElectronico(correoElectronico);
                txt_tipo_local_empresa.setText("Tipo de Local: " + modalidadEmpresa);

                String id_user = (String) dataSnapshot.child("id_user").getValue();
                mEmpresa.setId_user(id_user);

                dataSnapshot.child("imagen").getValue();


                if (videoSubido) {
                    MediaController mediaController = new MediaController(getActivity());
                    mImage_paralax.setVisibility(View.GONE);
                    mVideoView.setVisibility(View.VISIBLE);
                    mVideoView.setVideoURI(Uri.parse(imagen_url));
                    mVideoView.setMediaController(mediaController);
                    mVideoView.start();

                } else {
                    mImage_paralax.setVisibility(View.VISIBLE);
                    mVideoView.setVisibility(View.GONE);
                    if (getActivity() != null) {
                        if (getActivity().getApplicationContext() != null) {
                            Glide.with(getActivity().getApplicationContext())
                                    .load(imagen_url)
                                    .into(mImage_paralax);


                        } else {
                            Log.v("Msg", "Error al guardar");
                            return;
                        }
                    } else {
                        Log.v("Msg", "Error al guardar");
                        return;
                    }
                }

                Button btnActualizarProyecto = root.findViewById(R.id.btn_editar_empresa);
                btnActualizarProyecto.setVisibility(View.GONE);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user.getUid().equalsIgnoreCase(id_user)) {
                    btnActualizarProyecto.setVisibility(View.VISIBLE);
                    btnActualizarProyecto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle args = new Bundle();
                            args.putString("ARG_KEY_EMPRESA", mPost_key);
                            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_DescBlankFragment_to_crearProyectoFragment, args);
                        }
                    });

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("asdasd" + databaseError);
                System.out.println("asdasd" + databaseError);

                Toast.makeText(getActivity(), databaseError.toString(), Toast.LENGTH_LONG).show();

            }
        });
    }

    public void verificarRegistroEmprendedor() {
        final DatabaseReference mEmprendedorReference;
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mEmprendedorReference = FirebaseDatabase.getInstance().getReference().child("Empresa");
        mEmprendedorReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot itemSpanshot : dataSnapshot.getChildren()) {
                    String idUser = (String) itemSpanshot.child("id_user").getValue();
                    if (idUser != null && idUser.equals(user.getUid())) {
                        mEmprendedorActual = itemSpanshot.getValue(Emprendedor.class);
                        mEmprendedorRegistrado = true;
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

}