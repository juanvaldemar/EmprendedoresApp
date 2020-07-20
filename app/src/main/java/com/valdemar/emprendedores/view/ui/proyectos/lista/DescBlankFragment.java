package com.valdemar.emprendedores.view.ui.proyectos.lista;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.valdemar.emprendedores.R;
import com.valdemar.emprendedores.view.ui.proyectos.Comentarios;
import com.valdemar.emprendedores.view.ui.proyectos.RelatoViewHolderStructureComentarios;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class DescBlankFragment extends Fragment {

    private String mPost_key = null;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseLike, mDatabaseLikeCount;
    private TextView mPostTitleDetails;
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

    private void initView(final View root) {

        Bundle datosRecuperados = getArguments();
        if (datosRecuperados == null) {
            // No hay datos, manejar excepción
            return;
        }
        mPost_key = datosRecuperados.getString("blog_id");

        initComentarios(root, mPost_key);

        mPostTitleDetails = (TextView) root.findViewById(R.id.postTitleDetails);
        mImage_paralax = (ImageView) root.findViewById(R.id.image_paralax);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Proyectos");
        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title = (String) dataSnapshot.child("nombre").getValue();
                String post_desc = (String) dataSnapshot.child("descripcion").getValue();
                String post_image = (String) dataSnapshot.child("imagen").getValue();
                final String textoCentradoDesc = post_desc;
                String text_string_center = "<html><body style='text-align:justify;'>"+textoCentradoDesc+"<body><html>";
                /*****************************************/
                String justifyTag = "<html><body style='text-align:justify;background:black !important;color:#c1c0c0;font-size:15px;'>%s</body></html>";
                String dataString = String.format(Locale.US, justifyTag, text_string_center);
                WebView webViewDetail = (WebView) root.findViewById(R.id.webViewDetail);
                webViewDetail.loadDataWithBaseURL("", dataString, "text/html", "UTF-8", "");
                /*****************************************/
                mPostTitleDetails.setText(post_title);
                Glide.with(getActivity().getApplicationContext())
                        .load(post_image)
                        .into(mImage_paralax);
                Toast.makeText(getActivity(),post_title.toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("asdasd"+databaseError);
                System.out.println("asdasd"+databaseError);

                Toast.makeText(getActivity(),databaseError.toString(),Toast.LENGTH_LONG).show();

            }
        });

    }


    private void initComentarios(View root, final String mPost_key) {
        mProgress = new ProgressDialog(getContext());

        mDatabaseMisComentarios = FirebaseDatabase.getInstance().getReference().child("HistoriasDetalle").child("comentarios");
        mDatabaseMisComentarios.keepSynced(true);

        LinearLayoutManager layoutManagerMisLecturas
                = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        layoutManagerMisLecturas.setReverseLayout(true);
        layoutManagerMisLecturas.setStackFromEnd(true);

        mRecyclerComentarios = (RecyclerView) root.findViewById(R.id.recyclerComentarios);
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
                //final String post_key = getRef(position).getKey();
                if(model !=null){
                    viewHolder.setAutor(model.getNombre());
                    viewHolder.setMensaje(model.getComentario());
                    viewHolder.goneHora();
                    viewHolder.setImage(getActivity().getApplicationContext(), model.getFoto());
                }



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

                Button btnModalAcessoRelato = MyDialog.findViewById(R.id.modal_need_inicia_sesion);
                Button btnModalCancel = MyDialog.findViewById(R.id.modal_need_cancel);
                final TextInputEditText txtComentario = MyDialog.findViewById(R.id.comentarioTextInput);

                btnModalAcessoRelato.setEnabled(true);

                btnModalAcessoRelato.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mDatabaseMisComentarios.addValueEventListener(new ValueEventListener() {
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if(user != null){
                                    Log.v("TAG_LIKE","Favorito");
                                    mDatabaseMisComentarios.child(mPost_key).child(mAuth.getCurrentUser().getUid()).child("foto").setValue(user.getPhotoUrl().toString());
                                    mDatabaseMisComentarios.child(mPost_key).child(mAuth.getCurrentUser().getUid()).child("comentario").setValue(txtComentario.getText().toString());
                                    mDatabaseMisComentarios.child(mPost_key).child(mAuth.getCurrentUser().getUid()).child("nombre").setValue(user.getDisplayName().toString());
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