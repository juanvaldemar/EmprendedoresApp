package com.valdemar.emprendedores.view.ui.proyectos.lista;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.valdemar.emprendedores.MenuLateralActivity;
import com.valdemar.emprendedores.R;
import com.valdemar.emprendedores.SplashActivity;
import com.valdemar.emprendedores.auth.AccessRelato;
import com.valdemar.emprendedores.util.DownloadTask;
import com.valdemar.emprendedores.view.ui.proyectos.Comentarios;
import com.valdemar.emprendedores.view.ui.proyectos.RelatoViewHolderStructureComentarios;
import com.valdemar.emprendedores.view.ui.proyectos.lista.buscador.IModal;
import com.valdemar.emprendedores.view.ui.proyectos.lista.buscador.SearchPlaceAdapter;
import com.valdemar.emprendedores.view.ui.proyectos.lista.buscador.SearchPlaceAdapter2;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;


public class DescBlankFragment extends Fragment {

    private String mPost_key = null;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseLike, mDatabaseLikeCount;
    private TextView mPostTitleDetails,txt_nombre_proyecto,
            txt_cantidad_socios_suscritos,txt_cantidad_socios_aceptados,
            textoFinalizado,postCategoria,
            postAutor,txt_publicado_por,
            postSocio1,postDesc1
            ,postSocio2,postDesc2
            ,postSocio3,postDesc3
            ,postSocio4,postDesc4
            ,postSocio5,postDesc5,postFecha,postInversion,postBeneficio;
    private ImageView mImage_paralax,img_foto_proyecto,iconoFinalizado;
    private FloatingActionButton mFav_favorite;
    private ImageView mVounn_icon;
    private boolean mProcessLike;
    private LinearLayout linearDeBaja;

    private FirebaseAuth mAuth;

    private ProgressDialog mProgresDialog;
    private Button mPostRemoveDetails;
    private Button mPostBloquear;

    private DatabaseReference databaseUserOff;
    private FirebaseDatabase database;

    private TextToSpeech tts;


    private RecyclerView mRecyclerComentarios;
    private DatabaseReference mDatabaseMisComentarios;
    private ProgressDialog mProgress;

    private VideoView mVideoView;
    private TextView spinnerEstados;
    private Button btnPostular;
    private RecyclerView mRecyclerMisLecturas;
    private ArrayList<ItemFeed> arrayLists = new ArrayList<>();
    private SearchPlaceAdapter2 mAdapter;

    private ImageView downloadVideo;


    private final static int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static String[] PERMISOS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private Toast toast;

    public DescBlankFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_desc_blank, container, false);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        initView(root);
        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        String mensaje = "";
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mensaje = "Permiso Concedido";
        }else{
            mensaje = "Permiso no concedido";
        }
        toast = Toast.makeText(getActivity(), mensaje, Toast.LENGTH_LONG);
        toast.show();
    }
    private void permisos(){
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionCheck!= PackageManager.PERMISSION_GRANTED){ //No tiene el permiso
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        PERMISOS,
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initView(final View root) {



        Bundle datosRecuperados = getArguments();
        if (datosRecuperados == null) {
            // No hay datos, manejar excepción
            return;
        }
        mPost_key = datosRecuperados.getString("blog_id");


        mPostTitleDetails = (TextView) root.findViewById(R.id.postTitleDetails);
        downloadVideo = root.findViewById(R.id.downloadVideo);
        postFecha = (TextView) root.findViewById(R.id.postFecha);
        postInversion = (TextView) root.findViewById(R.id.postInversion);
        postBeneficio = (TextView) root.findViewById(R.id.postBeneficio);



        txt_nombre_proyecto = (TextView) root.findViewById(R.id.txt_nombre_proyecto);



        txt_cantidad_socios_suscritos = (TextView) root.findViewById(R.id.txt_cantidad_socios_suscritos);
        txt_cantidad_socios_aceptados = (TextView) root.findViewById(R.id.txt_cantidad_socios_aceptados);


        postCategoria = (TextView) root.findViewById(R.id.postCategoria);
        postAutor = (TextView) root.findViewById(R.id.postAutor);
        txt_publicado_por = (TextView) root.findViewById(R.id.txt_publicado_por);
        postSocio1 = (TextView) root.findViewById(R.id.postSocio1);
        postDesc1 = (TextView) root.findViewById(R.id.postDesc1);

        postSocio2 = (TextView) root.findViewById(R.id.postSocio2);
        postDesc2 = (TextView) root.findViewById(R.id.postDesc2);

        postSocio3 = (TextView) root.findViewById(R.id.postSocio3);
        postDesc3 = (TextView) root.findViewById(R.id.postDesc3);

        postSocio4 = (TextView) root.findViewById(R.id.postSocio4);
        postDesc4 = (TextView) root.findViewById(R.id.postDesc4);

        postSocio5 = (TextView) root.findViewById(R.id.postSocio5);
        postDesc5 = (TextView) root.findViewById(R.id.postDesc5);

        mVideoView = (VideoView)root.findViewById(R.id.videoview_proyecto);

        mImage_paralax = (ImageView) root.findViewById(R.id.image_paralax);
        img_foto_proyecto = (ImageView) root.findViewById(R.id.img_foto_proyecto);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Proyectos");
        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                String videoSubido_ = (String) dataSnapshot.child("videoSubido").getValue();

                boolean videoSubido = Boolean.parseBoolean(videoSubido_);

                String post_autor = (String) dataSnapshot.child("autor").getValue();

                String post_fecha = (String) dataSnapshot.child("fecha").getValue();
                String post_inversion = (String) dataSnapshot.child("inversion").getValue();
                String post_beneficio = (String) dataSnapshot.child("beneficio").getValue();

                String post_title = (String) dataSnapshot.child("nombre").getValue();
                String post_desc = (String) dataSnapshot.child("descripcion").getValue();
                final String post_image = (String) dataSnapshot.child("imagen").getValue();
                String post_categoria = (String) dataSnapshot.child("categoria").getValue();
                String post_socio1 = (String) dataSnapshot.child("socio1").getValue();
                String post_desc1 = (String) dataSnapshot.child("descripcionSocio1").getValue();

                String post_socio2 = (String) dataSnapshot.child("socio2").getValue();
                String post_desc2 = (String) dataSnapshot.child("descripcionSocio2").getValue();
                String post_socio3 = (String) dataSnapshot.child("socio3").getValue();
                String post_desc3 = (String) dataSnapshot.child("descripcionSocio3").getValue();
                String post_socio4 = (String) dataSnapshot.child("socio4").getValue();
                String post_desc4 = (String) dataSnapshot.child("descripcionSocio4").getValue();
                String post_socio5 = (String) dataSnapshot.child("socio5").getValue();
                String post_desc5 = (String) dataSnapshot.child("descripcionSocio5").getValue();

                String id_emprendedor = (String) dataSnapshot.child("id_emprendedor").getValue();
                String estadoTrazabilidad = (String) dataSnapshot.child("estadoTrazabilidad").getValue();


                if(videoSubido){
                    downloadVideo.setVisibility(View.VISIBLE);
                    downloadVideo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Iniciar descarga");
                            builder.setMessage("Si presiona aceptar se iniciará la descarga.");
                            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                    StrictMode.setThreadPolicy(policy);

                                    permisos();
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                        //Verifica permisos para Android 6.0+
                                        int permissionCheck = ContextCompat.checkSelfPermission(
                                                getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                                            Log.i("Mensaje", "No se tiene permiso para leer.");
                                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 225);
                                        } else {
                                            Log.i("Mensaje", "Se tiene permiso para leer!");
                                        }
                                    }

                                    final File file = new File (Environment
                                            .getExternalStorageDirectory()
                                            .getPath() + "/Android/data/"
                                            + getActivity().getPackageName()
                                            + "/files/sdcard/DirName/const/const.html");

                                    Log.i("playy", "Folder "+file);

                                    DownloadTask downloadTask = new DownloadTask(getActivity());
                                    String url = "https://firebasestorage.googleapis.com/v0/b/app-emprendedores.appspot.com/o/Proyectos_images%2Fvideo%3A258112?alt=media&token=7a42882e-9030-4866-aee4-73cc648b1c76";
                                    downloadTask.execute(url,"valdemar","valdemar.mp4");
                                }
                            });

                            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });


                            builder.show();



                        }


                    });
                }

                spinnerEstados = root.findViewById(R.id.spinnerEstados);

                textoFinalizado = root.findViewById(R.id.textoFinalizado);
                iconoFinalizado = root.findViewById(R.id.iconoFinalizado);


                linearDeBaja = root.findViewById(R.id.linearDeBaja);

                if(estadoTrazabilidad != null){
                    spinnerEstados.setText(estadoTrazabilidad.toString());

                    if(estadoTrazabilidad.equalsIgnoreCase("DEBAJA")){
                        linearDeBaja.setVisibility(View.VISIBLE);
                    }

                    if(estadoTrazabilidad.equalsIgnoreCase("FINALIZADO")){
                        textoFinalizado.setVisibility(View.VISIBLE);
                        iconoFinalizado.setVisibility(View.VISIBLE);
                    }
                    if(estadoTrazabilidad.equalsIgnoreCase("ACTIVO")){
                        shares(root,post_title,post_image);
                        initComentarios(root, mPost_key);

                    }

                }
                dataSnapshot.child("imagen").getValue();


                final String textoCentradoDesc = post_desc;
                String text_string_center = "<html><body style='text-align:justify;'>"+textoCentradoDesc+"<body><html>";
                /*****************************************/
                String justifyTag = "<html><body style='text-align:justify;background:black !important;color:#c1c0c0;font-size:15px;'>%s</body></html>";
                String dataString = String.format(Locale.US, justifyTag, text_string_center);
                WebView webViewDetail = (WebView) root.findViewById(R.id.webViewDetail);
                webViewDetail.loadDataWithBaseURL("", dataString, "text/html", "UTF-8", "");
                /*****************************************/


                mPostTitleDetails.setText(post_title);

                    postFecha.setText("Fecha: "+post_fecha);
                    postInversion.setText("Inversion: "+post_inversion);
                    postBeneficio.setText("Beneficio: "+post_beneficio);


                txt_nombre_proyecto.setText(post_title);

                postCategoria.setText("Categoria: "+post_categoria);

                postAutor.setText("Proyecto publicado por: \n"+post_autor);



                postAutor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String idEmprendedor = (String)dataSnapshot.child("id_emprendedor").getValue();
                        Bundle args = new Bundle();
                        args.putString("idEmprendedor", idEmprendedor);
                        args.putBoolean("visitante", true);

                        Navigation.findNavController(view).navigate(R.id.next_action_desc,args);

                    }
                });


                txt_publicado_por.setText("Proyecto publicado por: \n"+post_autor);

                postSocio1.setText("- "+post_socio1);
                postDesc1.setText(post_desc1);

                if(post_socio2.isEmpty()){
                    postSocio2.setVisibility(View.GONE);
                    postDesc2.setVisibility(View.GONE);
                }
                postSocio2.setText("- "+post_socio2);
                postDesc2.setText(post_desc2);


                if(post_socio3.isEmpty()){
                    postSocio3.setVisibility(View.GONE);
                    postDesc3.setVisibility(View.GONE);
                }
                postSocio3.setText("- "+post_socio3);
                postDesc3.setText(post_desc3);

                if(post_socio4.isEmpty()){
                    postSocio4.setVisibility(View.GONE);
                    postDesc4.setVisibility(View.GONE);
                }
                postSocio4.setText("- "+post_socio4);
                postDesc4.setText(post_desc4);

                if(post_socio5.isEmpty()){
                    postSocio5.setVisibility(View.GONE);
                    postDesc5.setVisibility(View.GONE);
                }
                postSocio5.setText("- "+post_socio5);
                postDesc5.setText(post_desc5);

                if(videoSubido){
                    MediaController mediaController= new MediaController(getActivity());
                    mImage_paralax.setVisibility(View.GONE);
                    mVideoView.setVisibility(View.VISIBLE);
                    mVideoView.setVideoURI(Uri.parse(post_image));
                    mVideoView.setMediaController(mediaController);
                    mVideoView.start();

                } else{
                    mImage_paralax.setVisibility(View.VISIBLE);
                    mVideoView.setVisibility(View.GONE);
                    Glide.with(getActivity().getApplicationContext())
                            .load(post_image)
                            .into(mImage_paralax);

                    Glide.with(getActivity().getApplicationContext())
                            .load(post_image)
                            .into(img_foto_proyecto);
                }


                FloatingActionButton btnActualizarProyecto = root.findViewById(R.id.btn_actualizar_proyecto);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(user.getUid().equalsIgnoreCase(id_emprendedor)){
                    btnActualizarProyecto.setVisibility(View.VISIBLE);
                    btnPostular.setVisibility(View.GONE);
                    btnActualizarProyecto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle args = new Bundle();
                            args.putString("ARG_KEY_PROYECTO", mPost_key);
                           // Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_DescBlankFragment_to_crearProyectoFragment, args);
                            //Navigation.findNavController().navigate(R.id.,args);
                        }
                    });

                }

                Random r = new Random();
                int valorDado = r.nextInt(12);
                txt_cantidad_socios_suscritos.setText(valorDado+"");
                txt_cantidad_socios_aceptados.setText("");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("asdasd"+databaseError);
                System.out.println("asdasd"+databaseError);

                Toast.makeText(getActivity(),databaseError.toString(),Toast.LENGTH_LONG).show();

            }
        });

        btnPostular = root.findViewById(R.id.btnPostular);
        initPostulacion(root);
        initCount(root,mPost_key);
        initListado(root);
    }
    public void initListado(View root){



        mDatabaseLikeCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                txt_cantidad_socios_suscritos.setText(dataSnapshot.getChildrenCount()+" ");
                String asd = dataSnapshot.child(getId()+"").toString();
                arrayLists.clear();
                //llMain.removeView(textView);

                for (final DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    final String ids = eventSnapshot.getKey();
                   //TextView textView = new TextView(getActivity());
                    Object category = eventSnapshot.getValue(Object.class);

                    String convertedToString = String.valueOf(category);

                    String[] segmentacionCanalSplit = convertedToString.split(",");
                    ItemFeed data_ = new ItemFeed();
                    data_.setId_emprendedor(segmentacionCanalSplit[0]);
                    data_.setNombre(segmentacionCanalSplit[1]);

                    arrayLists.add(data_);


                }




                txt_cantidad_socios_suscritos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        iniProfileModal(view,arrayLists);

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void initPostulacion(final View root) {

        //mVounn_icon =  root.findViewById(R.id.vounn_icon);
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
        mDatabaseLike.keepSynced(true);
        mDatabaseLikeCount = FirebaseDatabase.getInstance().getReference().child("HistoriasDetalle").child("count").child(mPost_key);
        mDatabaseLikeCount.keepSynced(true);

        btnPostular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProcessLike = true;
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){

                    mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final String nombre = (String) dataSnapshot.child("nombre").getValue();
                            final String imagen = (String) dataSnapshot.child("imagen").getValue();
                            final String categoria = (String) dataSnapshot.child("categoria").getValue();
                            final String descripcion = (String) dataSnapshot.child("descripcion").getValue();
                            final String id_emprendedor = (String) dataSnapshot.child("id_emprendedor").getValue();

                            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (mProcessLike){

                                        if(dataSnapshot.child(mAuth.getCurrentUser().getUid()).hasChild(mPost_key)){
                                            Log.v("TAG_LIKE","LINE NO");
                                            mDatabaseLike.child(mAuth.getCurrentUser().getUid()).child(mPost_key).removeValue();
                                            showSnackBar("Eliminando Suscripción",root);
                                            //btnPostular.setText("Eliminado");
                                            mProcessLike = false;
                                        }else{
                                            mDatabaseLike.child(mAuth.getCurrentUser().getUid()).child(mPost_key).child("nombre").setValue(nombre);
                                            mDatabaseLike.child(mAuth.getCurrentUser().getUid()).child(mPost_key).child("imagen").setValue(imagen);
                                            mDatabaseLike.child(mAuth.getCurrentUser().getUid()).child(mPost_key).child("categoria").setValue(categoria);
                                            mDatabaseLike.child(mAuth.getCurrentUser().getUid()).child(mPost_key).child("descripcion").setValue(descripcion);
                                            mDatabaseLike.child(mAuth.getCurrentUser().getUid()).child(mPost_key).child("id_emprendedor").setValue(id_emprendedor);

                                            //btnPostular.setText("favoritos");

                                            mProcessLike = false;
                                            showSnackBar("Suscrito", root);
                                        }
                                        if(dataSnapshot.child(mPost_key).hasChild(mAuth.getCurrentUser().getUid())){
                                            showSnackBar("Dislike", root);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.v("TAG_LIKE","LINE onCancelled");

                                }
                            });
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    mDatabaseLikeCount.addListenerForSingleValueEvent(new ValueEventListener(){

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Boolean status = false;
                            if(dataSnapshot.child(user.getUid()).getValue() != null){
                                String data_ = dataSnapshot.child(user.getUid()).getValue().toString();

                                if(!data_.isEmpty()){
                                    status = true;
                                }
                            }
                            if(status){
                                mDatabaseLikeCount.child(mAuth.getCurrentUser().getUid()).removeValue();
                             //   showSnackBar("I love", root);
                                btnPostular.setText("Postular");

                            }else{
                                mDatabaseLikeCount.child(user.getUid()).setValue(user.getUid() +" , "+user.getDisplayName());
                               // showSnackBar("I don't love", root);
                                //btnPostular.setText("Ya haz postulado");

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }else{
                    showSnackBar("Necesitas Iniciar Sesión", root);
                }
            }
        });

        mDatabaseLikeCount.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Boolean status = false;
                if(dataSnapshot.child(user.getUid()).getValue() != null){
                    if(dataSnapshot.child(user.getUid()).getValue().equals(true)){
                        status = true;
                    }
                }
                if(status){
                   // mDatabaseLikeCount.child(mAuth.getCurrentUser().getUid()).removeValue();
                   // showSnackBar("I love", root);
                    //btnPostular.setText("Ya haz postulado");

                }else{
                    //mDatabaseLikeCount.child(user.getUid()).setValue(true);
                   // showSnackBar("I don't love", root);
                    btnPostular.setText("Postular");

                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showSnackBar(String msg, View root) {
        Snackbar
                .make(root.findViewById(R.id.coordinator), msg, Snackbar.LENGTH_LONG)
                .show();
    }

    private void initCount(final View root, final String mPost_key){
        mDatabaseLikeCount = FirebaseDatabase.getInstance().getReference().child("HistoriasDetalle").child("count").child(mPost_key);
        mDatabaseLikeCount.keepSynced(true);
        mDatabaseLikeCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                txt_cantidad_socios_suscritos.setText(dataSnapshot.getChildrenCount()+" ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void shares(View root, final String post_title, final String post_image) {
        FloatingActionButton fav_favorite;
        fav_favorite = root.findViewById(R.id.fav_favorite);
        fav_favorite.setVisibility(View.VISIBLE);
        fav_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,"Hola, estoy creando el Emprendimiento : \""+post_title+"\" ,"+"\n"+post_image);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
    }


    private void initComentarios(View root, final String mPost_key) {
        mProgress = new ProgressDialog(getContext());

        mDatabaseMisComentarios = FirebaseDatabase.getInstance().getReference().child("HistoriasDetalle").child("comentarios");


        LinearLayoutManager layoutManagerMisLecturas
                = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        layoutManagerMisLecturas.setReverseLayout(true);
        layoutManagerMisLecturas.setStackFromEnd(true);

        mRecyclerComentarios = (RecyclerView) root.findViewById(R.id.recyclerComentarios);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerComentarios.setVisibility(View.VISIBLE);

            }
        },500);


        mRecyclerComentarios.setHasFixedSize(true);
        mRecyclerComentarios.setLayoutManager(layoutManagerMisLecturas);

        Query queryRef = mDatabaseMisComentarios.child(mPost_key);

        FirebaseRecyclerAdapter<Comentarios, RelatoViewHolderStructureComentarios>
                firebaseRecyclerAdapterMyLecturas = new FirebaseRecyclerAdapter<Comentarios, RelatoViewHolderStructureComentarios>(
                Comentarios.class,
                R.layout.view_comentarios,
                RelatoViewHolderStructureComentarios.class,queryRef) {
            @Override
            protected void populateViewHolder(RelatoViewHolderStructureComentarios viewHolder, final Comentarios model, int i) {
                 //  final String post_key = getRef(i).getKey();
                    viewHolder.setAutor(model.getNombre());
                    viewHolder.setMensaje(model.getComentario());
                    viewHolder.goneHora();
                    viewHolder.setImage(getActivity().getApplicationContext(), model.getFoto());
                    viewHolder.mViewStructure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            System.out.println("model: "+model);
                            final Dialog MyDialog;
                            MyDialog = new Dialog(getActivity());
                            MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            MyDialog.setContentView(R.layout.comentario_opcion);
                            final Button btnEliminar = MyDialog.findViewById(R.id.eliminar);
                            final Button btnEditar = MyDialog.findViewById(R.id.editar);
                            final Button btnEditar_ = MyDialog.findViewById(R.id.editar_);
                            final EditText editarComentario = MyDialog.findViewById(R.id.editarComentario);
                            btnEliminar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mDatabaseMisComentarios.child(mPost_key).child(model.getIdss()).removeValue();
                                    MyDialog.dismiss();
                                }
                            });
                            btnEditar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    btnEliminar.setVisibility(View.GONE);
                                    editarComentario.setVisibility(View.VISIBLE);
                                    editarComentario.setText(model.getComentario());

                                    btnEditar.setVisibility(View.GONE);
                                    btnEditar_.setVisibility(View.VISIBLE);

                                }
                            });

                            btnEditar_.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    editarComentario.setVisibility(View.VISIBLE);

                                    Map<String, Object> proyectoHashMap = new HashMap<>();
                                    model.setComentario(editarComentario.getText().toString());
                                    proyectoHashMap.put(model.getIdss(), model);

                                    mDatabaseMisComentarios.child(mPost_key).updateChildren(proyectoHashMap);
                                    MyDialog.dismiss();
                                }
                            });

                            MyDialog.show();
                        }
                    });
            }

        };

        mRecyclerComentarios.setAdapter(firebaseRecyclerAdapterMyLecturas);

        TextView mTxtComentarios = root.findViewById(R.id.txtComentarios);

        mTxtComentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog MyDialog;
                MyDialog = new Dialog(getActivity());
                MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                MyDialog.setContentView(R.layout.comentario_add);
                MyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                MyDialog.setCancelable(false);
                Button btnModalAcessoRelato = MyDialog.findViewById(R.id.modal_need_inicia_sesion);
                Button btnModalCancel = MyDialog.findViewById(R.id.modal_need_cancel);
                final TextInputEditText txtComentario = MyDialog.findViewById(R.id.comentarioTextInput);

                btnModalAcessoRelato.setEnabled(true);
                btnModalAcessoRelato.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDatabaseMisComentarios.addListenerForSingleValueEvent(new ValueEventListener() {
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if(user != null){
                                    Random random = new Random();
                                    int randomNumber = random.nextInt(9999999);
                                    if(!txtComentario.getText().toString().isEmpty()){
                                        DatabaseReference newPost = mDatabaseMisComentarios.child(mPost_key).push();

                                        newPost.child("foto").setValue(user.getPhotoUrl().toString());
                                        newPost.child("comentario").setValue(txtComentario.getText().toString());
                                        newPost.child("nombre").setValue(user.getDisplayName().toString());
                                        newPost.child("idss").setValue(newPost.getKey());

                                        mRecyclerComentarios.setVisibility(View.GONE);
                                    }else{
                                        Toast.makeText(getActivity(),"Por favor ingrese un texto válido.",Toast.LENGTH_LONG).show();

                                    }

                                   /* mDatabaseMisComentarios.child(mPost_key).child(String.valueOf(randomNumber)).child("foto").setValue(user.getPhotoUrl().toString());
                                    mDatabaseMisComentarios.child(mPost_key).child(String.valueOf(randomNumber)).child("comentario").setValue(txtComentario.getText().toString());
                                    mDatabaseMisComentarios.child(mPost_key).child(String.valueOf(randomNumber)).child("nombre").setValue(user.getDisplayName().toString());
                                    mRecyclerComentarios.setVisibility(View.GONE);
                                    mRecyclerComentarios.setVisibility(View.VISIBLE);*/
                                }
                            }

                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        MyDialog.dismiss();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerComentarios.setVisibility(View.VISIBLE);

                            }
                        },500);
                    }
                });

                btnModalCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        MyDialog.dismiss();
                    }
                });

                MyDialog.show();

            }
        });


    }

    private void iniProfileModal(final View v, ArrayList<ItemFeed> arrayLists){
        final Dialog MyDialog;
        MyDialog = new Dialog(getActivity());
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.profile_modal);
        MyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btnModalCancel = MyDialog.findViewById(R.id.modal_need_cancel);

        LinearLayoutManager layoutManagerMisLecturas
                = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        layoutManagerMisLecturas.setReverseLayout(true);
        layoutManagerMisLecturas.setStackFromEnd(true);
        mRecyclerMisLecturas = (RecyclerView) MyDialog.findViewById(R.id.asdasd);
        mRecyclerMisLecturas.setHasFixedSize(true);
        mRecyclerMisLecturas.setLayoutManager(layoutManagerMisLecturas);

        final IModal listener = new IModal() {
            @Override
            public void modalIniciar(String nombre, String url, String uidUser) {

            }

            @Override
            public void modalIniciarDetail(String id) {
                //viewDetails(id,view);
                //Toast.makeText(getContext(),id,Toast.LENGTH_LONG).show();
                String idEmprendedor = id;
                Bundle args = new Bundle();
                args.putString("idEmprendedor", idEmprendedor);

                Navigation.findNavController(v).navigate(R.id.next_action_desc,args);
                MyDialog.dismiss();

            }
        };

        mAdapter = new SearchPlaceAdapter2(getContext(), arrayLists,listener);
        mRecyclerMisLecturas.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        System.out.println(arrayLists);

        btnModalCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.dismiss();
            }
        });

        MyDialog.show();


        }



}