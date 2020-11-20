package com.valdemar.emprendedores.view.ui.proyectos.lista;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.valdemar.emprendedores.R;
import com.valdemar.emprendedores.auth.AccessRelato;
import com.valdemar.emprendedores.auth.ForgetPasswordRelato;
import com.valdemar.emprendedores.model.Proyecto;
import com.valdemar.emprendedores.view.ui.proyectos.lista.buscador.IModal;
import com.valdemar.emprendedores.view.ui.proyectos.lista.buscador.SearchPlaceAdapter;
import com.valdemar.emprendedores.view.ui.proyectos.lista.buscador.SearchPlaceAdapter2;
import com.valdemar.emprendedores.view.ui.proyectos.lista.buscador.SearchPlaceAdapter3;

import java.util.ArrayList;
import java.util.Random;

public class DescSuscritosFragment extends Fragment {
    private DatabaseReference mDatabaseLike, mDatabaseLikeCount,mDatabaseCountAceptados;
    private TextView txt_cantidad_socios_suscritos;
    private ArrayList<ItemFeed> arrayLists = new ArrayList<>();
    private Proyecto mDetalleProyecto = new Proyecto();
    private String mPost_key = null;
    private RecyclerView mRecyclerMisLecturas;
    private SearchPlaceAdapter3 mAdapter;
    private int total = 0;
    private int contador = 0;
    private String idSuscriptor = "";
    private String idSuscriptorName = "";
    private String idSuscriptorTitulo = "";

     DatabaseReference mDatabaseAceptadosCount;
     DatabaseReference mDatabaseAceptadosCount2;


    public DescSuscritosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_desc_suscritos, container, false);

        initView(view);

        mDatabaseAceptadosCount = FirebaseDatabase.getInstance().getReference().child("HistoriasDetalle").child("count_aceptados").child(mPost_key);
        mDatabaseAceptadosCount.keepSynced(true);

        mDatabaseAceptadosCount2 = FirebaseDatabase.getInstance().getReference().child("HistoriasDetalle").child("count_aceptados_2").child(mPost_key);
        mDatabaseAceptadosCount2.keepSynced(true);

        initListado(view);
        return view;
    }

    private void initView(View view) {
        Bundle datosRecuperados = getArguments();
        if (datosRecuperados == null) {
            // No hay datos, manejar excepción
            return;
        }
        mPost_key = datosRecuperados.getString("blog_id");
        LinearLayoutManager layoutManagerMisLecturas
                = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        layoutManagerMisLecturas.setReverseLayout(true);
        layoutManagerMisLecturas.setStackFromEnd(true);

        mRecyclerMisLecturas = (RecyclerView) view.findViewById(R.id.fragmento_mis_lecturas);
        mRecyclerMisLecturas.setHasFixedSize(true);

        mRecyclerMisLecturas.setLayoutManager(layoutManagerMisLecturas);


        mDatabaseLikeCount = FirebaseDatabase.getInstance().getReference().child("HistoriasDetalle").child("count").child(mPost_key);
        mDatabaseLikeCount.keepSynced(true);

        mDatabaseCountAceptados = FirebaseDatabase.getInstance().getReference().child("HistoriasDetalle").child("count_aceptados").child(mPost_key);
        mDatabaseCountAceptados.keepSynced(true);

        txt_cantidad_socios_suscritos = (TextView) view.findViewById(R.id.txt_cantidad_socios_suscritos);

    }

    public void initListado(final View view){

        final IModal listener = new IModal() {
            @Override
            public void modalIniciar(String nombre, String url, String uidUser) {

            }

            @Override
            public void modalIniciarDetail(String id) {
             //   viewDetails(id,view);
                String idEmprendedor = id;
                Bundle args = new Bundle();
                args.putString("idEmprendedor", idEmprendedor);
                args.putBoolean("visitante", true);
                Navigation.findNavController(view).navigate(R.id.next_action_desc,args);

            }

            @Override
            public void modalAceptar(String id) {
                //Toast.makeText(getActivity(),"hola",Toast.LENGTH_LONG).show();
                dialogAceptar(id);
            }
        };

        mDatabaseLikeCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                txt_cantidad_socios_suscritos.setText(dataSnapshot.getChildrenCount()+" ");

                total = (int) dataSnapshot.getChildrenCount();

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

                    //idSuscriptor = segmentacionCanalSplit[0];
                    //idSuscriptorName = segmentacionCanalSplit[1];


                    int catidad = segmentacionCanalSplit.length;
                    if(catidad == 4){
                        idSuscriptorTitulo= segmentacionCanalSplit[3];
                        data_.setCategoria(segmentacionCanalSplit[0]);
                        data_.setCiudad(segmentacionCanalSplit[1]);
                        data_.setDescripcion(idSuscriptorTitulo);

                    }
                    data_.setNombre(segmentacionCanalSplit[1]);

                    arrayLists.add(data_);


                }


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                //if(user.getUid().equalsIgnoreCase(mDetalleProyecto.getId_emprendedor())){
                   // iniProfileModal(view,arrayLists);

                    mAdapter = new SearchPlaceAdapter3(getContext(), arrayLists,listener);
                    mRecyclerMisLecturas.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                //}


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabaseAceptadosCount2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                txt_cantidad_socios_suscritos.setText(dataSnapshot.getChildrenCount()+" ");

                contador = (int) dataSnapshot.getChildrenCount();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void dialogAceptar(final String id) {
        final Dialog MyDialog;
        MyDialog = new Dialog(getActivity());
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.modal_aceptar);
        MyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final TextView modal_need_text_body = MyDialog.findViewById(R.id.modal_need_text_body);
        final TextView title_pop = MyDialog.findViewById(R.id.title_pop);

        final Button btnAceptar = MyDialog.findViewById(R.id.modal_aceptar);
        final Button btnCancelar = MyDialog.findViewById(R.id.modal_cancelar);




        btnAceptar.setEnabled(true);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.dismiss();

                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Random r = new Random();
                int valorDado = r.nextInt(999999);

                for (int i= 0; i<arrayLists.size();i++){
                    if(arrayLists.get(i).getId_emprendedor().equalsIgnoreCase(id)){
                        mDatabaseAceptadosCount.child(id).setValue(id.trim() +", "+arrayLists.get(i).getCiudad().trim()+","+arrayLists.get(i).getDescripcion());
                        break;
                    }
                }


                DatabaseReference newPost = mDatabaseAceptadosCount2.push();
                newPost.setValue(user.getUid());



                contador++;
                if(contador == total){
                    felicitar();
                }
            }
        });


        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.dismiss();
                startActivity(new Intent(getActivity(), AccessRelato.class));
            }
        });


        MyDialog.show();




    }

    private void felicitar() {
        final Dialog MyDialog;
        MyDialog = new Dialog(getActivity());
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.modal_aceptar);
        MyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final TextView modal_need_text_body = MyDialog.findViewById(R.id.modal_need_text_body);
        final TextView title_pop = MyDialog.findViewById(R.id.title_pop);

        final Button btnAceptar = MyDialog.findViewById(R.id.modal_aceptar);
        final Button btnCancelar = MyDialog.findViewById(R.id.modal_cancelar);

        title_pop.setText("Felicidades");
        modal_need_text_body.setText("Haz cumplido");
        btnCancelar.setText("Cerrar");
        btnAceptar.setVisibility(View.GONE);
      //  Toast.makeText(getActivity(),"FELICIDADES",Toast.LENGTH_LONG).show();


        btnAceptar.setEnabled(true);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.dismiss();

                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                mDatabaseAceptadosCount.child(idSuscriptor).setValue(idSuscriptor.trim() +", "+idSuscriptorName.trim()+","+idSuscriptorTitulo);



                DatabaseReference newPost = mDatabaseAceptadosCount2.push();
                newPost.setValue(user.getUid());

                contador++;

            }
        });


        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.dismiss();
                startActivity(new Intent(getActivity(), AccessRelato.class));
            }
        });


        MyDialog.show();




    }

}