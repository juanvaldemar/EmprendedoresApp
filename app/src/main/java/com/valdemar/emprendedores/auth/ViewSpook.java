package com.valdemar.emprendedores.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.valdemar.emprendedores.R;
import com.valdemar.emprendedores.SplashActivity;

public class ViewSpook extends AppCompatActivity {

    private Button mBtnCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_spook);

        mBtnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        mBtnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(ViewSpook.this, SplashActivity.class);
                startActivity(i);
            }
        });

    }
}