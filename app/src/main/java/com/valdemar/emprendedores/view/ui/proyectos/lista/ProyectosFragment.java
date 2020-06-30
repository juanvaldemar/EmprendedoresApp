package com.valdemar.emprendedores.view.ui.proyectos.lista;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    private RecyclerView rvSearch;
    private DatabaseReference mRef;
    private SearchPlaceAdapter mAdapter;
    ArrayList<ItemFeed> arrayLists = new ArrayList<>();

    private String mPost_categoria;
    private String mModulo;

    private String tmp = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);



        IModal listener = new IModal() {
            @Override
            public void modalIniciar(String nombre, String url, String uidUser) {
            }

            @Override
            public void modalIniciarDetail(String id) {
                viewDetailsChatStyle(id);
            }

        };

        initView(view,listener);

        return view;
    }

    private void initView(View view, final IModal listener) {
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
            public boolean onQueryTextSubmit(String query) {
                System.out.println(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println(newText);
                if(mAdapter!=null){
                    if(!newText.isEmpty()){
                        mAdapter.getFilter().filter(newText);
                    }else{
                        allSpook(mPost_categoria,listener);

                    }
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

    }

    private void initCategoria(View view) {
        final Spinner spinner5,spinner6;

        spinner5 = view.findViewById(R.id.spinner_pais);
        spinner6 = view.findViewById(R.id.spinner_ciudad);

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
                viewDetailsChatStyle(id);
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
                                    if(!selectedItem.equalsIgnoreCase("ciudad"))
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



                                    }

                                    //  Toast.makeText(getContext(),"Identificador "+selectedItem,Toast.LENGTH_LONG).show();

                                } // to close the onItemSelected
                                public void onNothingSelected(AdapterView<?> parent)
                                {

                                }
                            });
                        }
                    },100);

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
                    arrayLists.add(category);
                }


                // Collections.reverse(arrayLists);
                mAdapter = new SearchPlaceAdapter(getContext(), arrayLists,listener);
                rvSearch.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

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