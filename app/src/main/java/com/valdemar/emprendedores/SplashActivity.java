package com.valdemar.emprendedores;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.valdemar.emprendedores.auth.AccessRelato;
import com.valdemar.emprendedores.auth.ViewSpook;
import com.valdemar.emprendedores.view.ui.ClasificacionActivity;

public class SplashActivity extends AppCompatActivity {
    private TextView mTitle;
    private SharedPreferences prefs_notificacion = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FirebaseApp.initializeApp(this);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
  /*
      prefs_notificacion = getSharedPreferences("com.valdemar.spook.intereses", MODE_PRIVATE);
        String intereses_emprendedor = prefs_notificacion.getString("intereses","");
        String[] segmentacionCanalSplit = intereses_emprendedor.split(",");
        for (String i : segmentacionCanalSplit) {
            String i_ = i.replace("[","");
            String i__ = i_.replace("]","");
            if(i__.trim().equalsIgnoreCase("")){
                FirebaseMessaging.getInstance().subscribeToTopic(i__);
            }
        }*/




        if(user != null) {
            verificarRegistroTipoUsuario("Emprendedor", "id_emprendedor");
            //startActivity(new Intent(SplashActivity.this, MenuLateralActivity.class));
        }else{
            startActivity(new Intent(SplashActivity.this, AccessRelato.class));
        }
        finish();

    }

    public void verificarRegistroTipoUsuario(final String nodo, final String idTipoUsuario){
        final DatabaseReference mEmprendedorReference;
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mEmprendedorReference = FirebaseDatabase.getInstance().getReference().child(nodo);
        mEmprendedorReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot itemSpanshot: dataSnapshot.getChildren()) {
                    String idUser = (String)itemSpanshot.child(idTipoUsuario).getValue();
                    if(idUser!=null && idUser.equals(user.getUid())){
                        redireccionarListaProyectos(nodo);
                        return;
                    }
                }
                if(nodo.equals("Empresa")){
                    redireccionarMenuCondicional();
                }
                else
                    verificarRegistroTipoUsuario("Empresa", "id_user");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void redireccionarMenuCondicional(){
        Intent i = new Intent(this, ClasificacionActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
        finish();
    }

    private void redireccionarListaProyectos(String opcion){
        Intent i = new Intent(this, MenuLateralActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.putExtra("REQUIERE_REGISTRO", opcion);
        i.putExtra("TIPO_ACCESO", opcion);
        startActivity(i);
        finish();
    }

}
