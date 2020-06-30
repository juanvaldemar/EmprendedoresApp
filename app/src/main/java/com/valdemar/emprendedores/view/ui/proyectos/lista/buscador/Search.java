package com.valdemar.emprendedores.view.ui.proyectos.lista.buscador;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.valdemar.emprendedores.R;
import com.valdemar.emprendedores.view.ui.proyectos.lista.Category;
import com.valdemar.emprendedores.view.ui.proyectos.lista.ItemFeed;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class Search extends Fragment {

    public Search() {
        // Required empty public constructor
    }


    private SearchView mSearch;
    private RecyclerView rvSearch;
    private DatabaseReference mRef;
    private SearchPlaceAdapter mAdapter;
    ArrayList<ItemFeed> arrayLists = new ArrayList<>();

    private String mPost_categoria;
    private String mModulo;

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
        mRef.keepSynced(true);
        allSpook(mPost_categoria,listener);


        //searchView of place
        mSearch = (SearchView) view.findViewById(R.id.mSearch);


        final EditText txtSearch = ((EditText)mSearch.findViewById(R.id.search_src_text));
        txtSearch.setHintTextColor(Color.WHITE);
        txtSearch.setTextColor(Color.WHITE);
        txtSearch.setLinkTextColor(Color.WHITE);
        txtSearch.setPadding(10,0,0,0);


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