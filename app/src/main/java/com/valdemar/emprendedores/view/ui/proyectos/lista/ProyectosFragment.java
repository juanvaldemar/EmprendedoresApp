package com.valdemar.emprendedores.view.ui.proyectos.lista;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.valdemar.emprendedores.MenuLateralActivity;
import com.valdemar.emprendedores.R;
import com.valdemar.emprendedores.view.ui.proyectos.lista.buscador.IModal;
import com.valdemar.emprendedores.view.ui.proyectos.lista.buscador.SearchPlaceAdapter;

import java.util.ArrayList;

public class ProyectosFragment extends Fragment {

    public ProyectosFragment() {
        // Required empty public constructor
    }


    private SearchView mSearch;
    private RecyclerView rvSearch,mRecyclerEpisodiosPerdidos;
    private DatabaseReference mRef,mDatabase;
    private SearchPlaceAdapter mAdapter;
    ArrayList<ItemFeed> arrayLists = new ArrayList<>();

    private String mPost_categoria;
    private String mModulo;

    private String tmp = "";

    private Spinner spinner5,spinner6;
    private ProgressDialog mProgress;

    private String post_key;
    private SharedPreferences prefs_notificacion = null;
    private DatabaseReference mDatabase2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_search, container, false);
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("Emprendedor");
        FirebaseMessaging.getInstance().subscribeToTopic("sendsuscrito");
        FirebaseMessaging.getInstance().subscribeToTopic("sendsuscrito2");
        prefs_notificacion = getActivity().getSharedPreferences("com.valdemar.spook.intereses", getActivity().MODE_PRIVATE);
        String intereses_emprendedor = prefs_notificacion.getString("intereses","");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(intereses_emprendedor !=null){
            String[] segmentacionCanalSplit = intereses_emprendedor.split(",");

            for (String i : segmentacionCanalSplit) {
                String i_ = i.replace("[","");
                String i__ = i_.replace("]","");
                if(i__.trim().equalsIgnoreCase("")){
                    FirebaseMessaging.getInstance().subscribeToTopic(i__);
                }
            }
        }else{
            mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot itemSpanshot: dataSnapshot.getChildren()) {
                        String idEmprendedor = (String)itemSpanshot.child("id_emprendedor").getValue();
                        String intereses_emprendedor = (String)itemSpanshot.child("intereses").getValue();
                        if(user.getUid().equalsIgnoreCase(idEmprendedor)) {
                            if (intereses_emprendedor != null) {

                                String[] segmentacionCanalSplit = intereses_emprendedor.split(",");

                                for (String i : segmentacionCanalSplit) {
                                    String i_ = i.replace("[","");
                                    String i__ = i_.replace("]","");
                                    if(i__.trim().equalsIgnoreCase("")){
                                        FirebaseMessaging.getInstance().subscribeToTopic(i__);
                                    }
                                }

                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        mProgress = new ProgressDialog(getActivity());
        mProgress.setMessage("Cargando ...");
        mProgress.setCancelable(false);
        mProgress.show();
        spinner5 = view.findViewById(R.id.spinner_pais);
        spinner6 = view.findViewById(R.id.spinner_ciudad);

        IModal listener = new IModal() {
            @Override
            public void modalIniciar(String nombre, String url, String uidUser) {

            }

            @Override
            public void modalIniciarDetail(String id) {
                viewDetails(id,view);

            }

            @Override
            public void modalAceptar(String id) {

            }

        };

        initView(view,listener);





        return view;
    }



    private void initView(final View view, final IModal listener) {
        Bundle datosRecuperados = getArguments();
        if (datosRecuperados == null) {
            // No hay datos, manejar excepción
        }
        //mModulo = datosRecuperados.getString("modulo");
        // mModulo = "Historias";

        // mPost_categoria = datosRecuperados.getString("categoria");
        //mPost_categoria = "EpisodiosPerdidos";


        rvSearch = (RecyclerView) view.findViewById(R.id.rvSearch);
        rvSearch.setHasFixedSize(true);

        /*rvSearch.setLayoutManager(new GridLayoutManager(getActivity(),
                2, LinearLayoutManager.VERTICAL, false));
*/

        LinearLayoutManager layoutManagerMisLecturas
                = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        layoutManagerMisLecturas.setReverseLayout(true);
        layoutManagerMisLecturas.setStackFromEnd(true);

        rvSearch.setLayoutManager(layoutManagerMisLecturas);

        mRef = FirebaseDatabase.getInstance().getReference().child("Proyectos");
        //mRef.keepSynced(true);
        allSpook(mPost_categoria,listener);


        //searchView of place
        mSearch = (SearchView) view.findViewById(R.id.mSearch);

        final EditText txtSearch = ((EditText)mSearch.findViewById(R.id.search_src_text));
        txtSearch.setHintTextColor(Color.WHITE);
        txtSearch.setTextColor(Color.WHITE);
        txtSearch.setLinkTextColor(Color.WHITE);
        txtSearch.setPadding(10,0,0,0);
        initCategoria(view);

        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(final String newText) {
                System.out.println(newText);
                allSpook(mPost_categoria,listener);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(mAdapter!=null){
                            if(!newText.isEmpty()){
                                mAdapter.getFilter().filter(newText);
                            }else{
                                allSpook(mPost_categoria,listener);

                            }
                        }
                    }
                },600);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length() == 0){
                    System.out.println("asdasdasd");
                }
                if(newText.isEmpty()){
                    allSpook(mPost_categoria,listener);

                }
                return false;

            }
        });

        mSearch.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                allSpook(mPost_categoria,listener);
                return false;
            }
        });



        mRecyclerEpisodiosPerdidos = (RecyclerView) view.findViewById(R.id.asd);
        mRecyclerEpisodiosPerdidos.setHasFixedSize(true);


        mDatabase = FirebaseDatabase.getInstance().getReference().child("categorias");

        LinearLayoutManager layoutManagermRecyclerEpisodiosPerdidos
                = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        layoutManagermRecyclerEpisodiosPerdidos.setReverseLayout(true);
        layoutManagermRecyclerEpisodiosPerdidos.setStackFromEnd(true);

        mRecyclerEpisodiosPerdidos.setLayoutManager(layoutManagermRecyclerEpisodiosPerdidos);

        FirebaseRecyclerAdapter<Category, CategoryViewHolder> firebaseRecyclerAdaptermRecyclerEpisodiosPerdidos =
                new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(
                        Category.class,
                        R.layout.album_card,
                        CategoryViewHolder.class,
                        mDatabase
                ) {
                    @Override
                    protected void populateViewHolder(CategoryViewHolder viewHolder, final Category model, int position) {
                        post_key = getRef(position).getKey();
                        viewHolder.setTitle(model.getTitulo());
                        viewHolder.setSendBy(model.getTitulo());

                        viewHolder.setImage(getActivity().getApplicationContext(),
                                model.getImagen());

                        Log.v("Seguimiento","dentro");

                        viewHolder.mViewStructure_h.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                allSpook(mPost_categoria,listener);
                                spinner5.setSelection(0);
                                spinner6.setSelection(0);
                                spinner6.setVisibility(View.GONE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mAdapter.getFilter().filter(model.getCategoria());
                                    }
                                },500);

                            }
                        });

                    }
                };

        mRecyclerEpisodiosPerdidos.setAdapter(firebaseRecyclerAdaptermRecyclerEpisodiosPerdidos);



    }

    private void viewDetails(String post_key, View view){

        Bundle args = new Bundle();
        args.putString("blog_id", post_key);

        Navigation.findNavController(view).navigate(R.id.next_action_desc,args);


    }

    private void initCategoria(final View view) {


        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(getActivity(),
                R.array.pais, android.R.layout.simple_spinner_item);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner5.setAdapter(adapter5);

        ArrayAdapter<CharSequence> adapter6 = ArrayAdapter.createFromResource(getActivity(),
                R.array.ciudad, android.R.layout.simple_spinner_item);
        adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner6.setAdapter(adapter6);

        final IModal listener = new IModal() {
            @Override
            public void modalIniciar(String nombre, String url, String uidUser) {
            }

            @Override
            public void modalIniciarDetail(String id) {
                viewDetails(id,view);
            }

            @Override
            public void modalAceptar(String id) {

            }

        };

        spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {



                String selectedItem = parent.getItemAtPosition(position).toString();
                if(!selectedItem.equalsIgnoreCase("país"))
                {
                    if(!tmp.isEmpty()){
                        if(!tmp.equalsIgnoreCase(selectedItem)){
                            spinner6.setSelection(0);
                        }
                    }

                    tmp = selectedItem;
                    allSpook(mPost_categoria,listener);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.getFilter().filter(spinner5.getSelectedItem().toString());
                            spinner6.setVisibility(View.VISIBLE);
                            spinner6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                            {
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                                {
                                    String selectedItem = parent.getItemAtPosition(position).toString();
                                    if(!selectedItem.equalsIgnoreCase("Ciudad"))
                                    {
                                        allSpook(mPost_categoria,listener);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                mAdapter.getFilter().filter(spinner5.getSelectedItem().toString());
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        mAdapter.getFilter().filter(spinner6.getSelectedItem().toString());
                                                    }
                                                },100);
                                            }
                                        },100);

                                    }else{
                                        allSpook(mPost_categoria,listener);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                mAdapter.getFilter().filter(spinner5.getSelectedItem().toString());
                                            }
                                        },100);
                                    }

                                    //  Toast.makeText(getContext(),"Identificador "+selectedItem,Toast.LENGTH_LONG).show();
                                } // to close the onItemSelected
                                public void onNothingSelected(AdapterView<?> parent)
                                {

                                }
                            });
                        }
                    },600);

                    // filterRecycler(root,spinner5.getSelectedItem().toString(),null,null);

                }else{
                    spinner6.setVisibility(View.GONE);
                    allSpook(mPost_categoria,listener);
                }

                //  Toast.makeText(getContext(),"Identificador "+selectedItem,Toast.LENGTH_LONG).show();

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        Button btnLimpiar = view.findViewById(R.id.btnLimpiar);
        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allSpook(mPost_categoria,listener);
                spinner5.setSelection(0);
                spinner6.setSelection(0);
                spinner6.setVisibility(View.GONE);

            }
        });


    }

    private void allSpook(String mPost_categoria, final IModal listener) {

        //Query queryClasico = mRef.orderByChild("pais").equalTo(mPost_categoria);

        mRef.limitToFirst(50).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayLists.clear();
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    String ids = eventSnapshot.getKey();
                    ItemFeed category = eventSnapshot.getValue(ItemFeed.class);
                    category.setId(ids);
                    if(category.getEstadoTrazabilidad() != null){
                        if(!category.getEstadoTrazabilidad().equalsIgnoreCase("DEBAJA")){
                            arrayLists.add(category);
                        }
                    }

                }


                // Collections.reverse(arrayLists);
                mAdapter = new SearchPlaceAdapter(getContext(), arrayLists,listener);
                rvSearch.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                mProgress.dismiss();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void viewDetailsChatStyle(String post_key){
        /*
            DescBlankFragment descBlankFragment = new DescBlankFragment();

            Bundle datosSend = new Bundle();
            datosSend.putString("blog_id", post_key);
        descBlankFragment.setArguments(datosSend);

            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contenido_dinamico, descBlankFragment)
                    .addToBackStack(null).commit();
        Log.v("id","id"+post_key);*/
    }

}