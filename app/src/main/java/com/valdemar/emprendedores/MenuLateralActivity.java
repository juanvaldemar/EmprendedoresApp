package com.valdemar.emprendedores;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MenuLateralActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DatabaseReference mDatabase;
    NavigationView navigationView;
    private boolean mBackPressedActivado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_menu_lateral);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_categorias,
                R.id.nav_perfil_emprendedor,
                R.id.nav_home,
                R.id.nav_gallery,
                R.id.nav_mis_proyectos_creados,
                R.id.nav_mis_proyectos_suscritos,
                R.id.nav_mis_interesados,
                R.id.nav_registrar_empresa,
                R.id.nav_perfil_empresa,
                R.id.nav_chat,
                R.id.nav_directorio_empresas)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavInflater navInflater = navController.getNavInflater();
        NavGraph graph = navInflater.inflate(R.navigation.mobile_navigation);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String requiereRegistro = "";
            requiereRegistro = getIntent().getExtras().getString("REQUIERE_REGISTRO");
            switch (requiereRegistro) {
                case "EMPRESA":
                    graph.setStartDestination(R.id.nav_registrar_empresa);
                    break;
                case "EMPRENDEDOR":
                    graph.setStartDestination(R.id.nav_categorias);
                    break;
                default:
                    String tipoAcceso = "";
                    tipoAcceso = getIntent().getExtras().getString("TIPO_ACCESO");
                    graph.setStartDestination(R.id.nav_gallery);
                    if(tipoAcceso.equalsIgnoreCase("Emprendedor"))
                        ocultarOpcionRegistroEmprendedor();
                    else
                        ocultarOpcionRegistroEmpresa();
            }
        }
        navController.setGraph(graph);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    public void ocultarOpcionRegistroEmprendedor() {
        MenuItem registrarEmprendedorMenuItem = navigationView.getMenu().findItem(R.id.nav_categorias);
        registrarEmprendedorMenuItem.setVisible(false);
    }

    public void ocultarOpcionRegistroEmpresa() {
        MenuItem registrarEmpresaMenuItem = navigationView.getMenu().findItem(R.id.nav_registrar_empresa);
        registrarEmpresaMenuItem.setVisible(false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if(mBackPressedActivado){
            super.onBackPressed();
            mBackPressedActivado = false;
        }

        //create a dialog to ask yes no question whether or not the user wants to exit
    }

    public void activarBackPressed(){
        mBackPressedActivado = true;
    }

}