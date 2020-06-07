package com.valdemar.emprendedores.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.valdemar.emprendedores.R;
import com.valdemar.emprendedores.SplashActivity;

public class ViewSpook extends AppCompatActivity {

    private Button mBtnCerrarSesion;
    private TextView mTxtName;
    private ImageView mMenu_profile_image;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_spook);

        mBtnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        mTxtName = findViewById(R.id.welcome);
        mMenu_profile_image = findViewById(R.id.menu_profile_image);
        if(user != null) {
            Uri photoUrl = user.getPhotoUrl();
            String name = user.getDisplayName();

            mTxtName.setText("Bienvenido: "+name);


            Glide.with(ViewSpook.this)
                    .load(photoUrl)
                    .thumbnail(Glide.with(ViewSpook.this)
                            .load(R.color.black))
                    .into(mMenu_profile_image);

        }

        mBtnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(ViewSpook.this, SplashActivity.class);
                startActivity(i);
            }
        });

    }
}