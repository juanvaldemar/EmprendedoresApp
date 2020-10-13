package com.valdemar.emprendedores;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.valdemar.emprendedores.auth.AccessRelato;
import com.valdemar.emprendedores.auth.ViewSpook;

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


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(user != null) {
                    startActivity(new Intent(SplashActivity.this, MenuLateralActivity.class));
                }else{
                    startActivity(new Intent(SplashActivity.this, AccessRelato.class));
                }
                finish();
            }
        },200);

    }

}
