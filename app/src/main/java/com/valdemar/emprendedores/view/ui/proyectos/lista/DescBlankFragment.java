package com.valdemar.emprendedores.view.ui.proyectos.lista;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.valdemar.emprendedores.MenuLateralActivity;
import com.valdemar.emprendedores.R;
import com.valdemar.emprendedores.SplashActivity;
import com.valdemar.emprendedores.auth.AccessRelato;
import com.valdemar.emprendedores.view.ui.proyectos.Comentarios;
import com.valdemar.emprendedores.view.ui.proyectos.RelatoViewHolderStructureComentarios;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class DescBlankFragment extends Fragment {

    private String mPost_key = null;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseLike, mDatabaseLikeCount;
    private TextView mPostTitleDetails,postCategoria,
            postSocio1,postDesc1
            ,postSocio2,postDesc2
            ,postSocio3,postDesc3
            ,postSocio4,postDesc4
            ,postSocio5,postDesc5;
    private ImageView mImage_paralax;
    private FloatingActionButton mFav_favorite;
    private ImageView mVounn_icon;
    private boolean mProcessLike;

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
        postCategoria = (TextView) root.findViewById(R.id.postCategoria);
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

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Proyectos");
        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String videoSubido_ = (String) dataSnapshot.child("videoSubido").getValue();

                boolean videoSubido = Boolean.parseBoolean(videoSubido_);

                String post_title = (String) dataSnapshot.child("nombre").getValue();
                String post_desc = (String) dataSnapshot.child("descripcion").getValue();
                String post_image = (String) dataSnapshot.child("imagen").getValue();
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

                final String textoCentradoDesc = post_desc;
                String text_string_center = "<html><body style='text-align:justify;'>"+textoCentradoDesc+"<body><html>";
                /*****************************************/
                String justifyTag = "<html><body style='text-align:justify;background:black !important;color:#c1c0c0;font-size:15px;'>%s</body></html>";
                String dataString = String.format(Locale.US, justifyTag, text_string_center);
                WebView webViewDetail = (WebView) root.findViewById(R.id.webViewDetail);
                webViewDetail.loadDataWithBaseURL("", dataString, "text/html", "UTF-8", "");
                /*****************************************/

                shares(root,post_title);

                mPostTitleDetails.setText(post_title);
                postCategoria.setText("Categoria: "+post_categoria);


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
                    Glide.with(getActivity().getApplicationContext())
                            .load(post_image)
                            .into(mImage_paralax);
                }

                initComentarios(root, mPost_key);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("asdasd"+databaseError);
                System.out.println("asdasd"+databaseError);

                Toast.makeText(getActivity(),databaseError.toString(),Toast.LENGTH_LONG).show();

            }
        });

        Button btnActualizarProyecto = root.findViewById(R.id.btn_actualizar_proyecto);
        btnActualizarProyecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("ARG_KEY_PROYECTO", mPost_key);
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_DescBlankFragment_to_crearProyectoFragment, args);
                //Navigation.findNavController().navigate(R.id.,args);
            }
        });
    }

    private void shares(View root, final String post_title) {
        FloatingActionButton fav_favorite;
        fav_favorite = root.findViewById(R.id.fav_favorite);
        fav_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,post_title+" para más detalle te invitamos a descargar la aplicación Emprendedores App https://play.google.com/store");
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
        },1000);


        mRecyclerComentarios.setHasFixedSize(true);
        mRecyclerComentarios.setLayoutManager(layoutManagerMisLecturas);

        Query queryRef = mDatabaseMisComentarios.child(mPost_key);

        FirebaseRecyclerAdapter<Comentarios, RelatoViewHolderStructureComentarios>
                firebaseRecyclerAdapterMyLecturas = new FirebaseRecyclerAdapter<Comentarios, RelatoViewHolderStructureComentarios>(
                Comentarios.class,
                R.layout.view_comentarios,
                RelatoViewHolderStructureComentarios.class,queryRef) {
            @Override
            protected void populateViewHolder(RelatoViewHolderStructureComentarios viewHolder, Comentarios model, int i) {
                 //  final String post_key = getRef(i).getKey();
                    viewHolder.setAutor(model.getNombre());
                    viewHolder.setMensaje(model.getComentario());
                    viewHolder.goneHora();
                    viewHolder.setImage(getActivity().getApplicationContext(), model.getFoto());

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
                                    mDatabaseMisComentarios.child(mPost_key).child(String.valueOf(randomNumber)).child("foto").setValue(user.getPhotoUrl().toString());
                                    mDatabaseMisComentarios.child(mPost_key).child(String.valueOf(randomNumber)).child("comentario").setValue(txtComentario.getText().toString());
                                    mDatabaseMisComentarios.child(mPost_key).child(String.valueOf(randomNumber)).child("nombre").setValue(user.getDisplayName().toString());
                                }
                            }

                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        MyDialog.dismiss();

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







}