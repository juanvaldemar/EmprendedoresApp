package com.valdemar.emprendedores;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.valdemar.emprendedores.view.CategoriasFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
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
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_registrar_empresa, R.id.nav_directorio_empresas)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavInflater navInflater = navController.getNavInflater();
        NavGraph graph = navInflater.inflate(R.navigation.mobile_navigation);
        Bundle extras = getIntent().getExtras();
        boolean registrarse = false;
        String tipoAcceso = "";
        if (extras != null) {
            registrarse = extras.getBoolean("registrarse");
            tipoAcceso = getIntent().getExtras().getString("TIPO_ACCESO");
        }
        if (registrarse) {
            graph.setStartDestination(R.id.nav_categorias);
        } else {
            switch (tipoAcceso) {
                case "EMPRESA":
                    graph.setStartDestination(R.id.nav_directorio_empresas);
                    break;
                default:
                    graph.setStartDestination(R.id.nav_gallery);
            }
        }
        navController.setGraph(graph);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        verificarRegistroEmprendedor();
    }


    public void verificarRegistroEmprendedor() {
        final DatabaseReference mEmprendedorReference;
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mEmprendedorReference = FirebaseDatabase.getInstance().getReference().child("Emprendedor");
        mEmprendedorReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot itemSpanshot : dataSnapshot.getChildren()) {
                    String idEmprendedor = (String) itemSpanshot.child("id_emprendedor").getValue();
                    if (idEmprendedor != null && idEmprendedor.equals(user.getUid())) {
                        MenuItem registrarEmprendedorMenuItem = navigationView.getMenu().findItem(R.id.nav_categorias);
                        registrarEmprendedorMenuItem.setVisible(false);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}