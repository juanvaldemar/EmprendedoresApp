package com.valdemar.emprendedores.view.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.valdemar.emprendedores.MenuLateralActivity;
import com.valdemar.emprendedores.R;
import com.valdemar.emprendedores.auth.AccessRelato;

public class ClasificacionActivity extends AppCompatActivity {

    private Button btn_ingreso_emprendedor;
    private Button btn_ingreso_empresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clasificacion);
        initView();
    }

    private void initView() {
        btn_ingreso_emprendedor = findViewById(R.id.btn_ingreso_emprendedor);
        btn_ingreso_empresa = findViewById(R.id.btn_ingreso_empresa);
        btn_ingreso_emprendedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ClasificacionActivity.this, MenuLateralActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                i.putExtra("TIPO_ACCESO", "EMPRENDEDOR");
                startActivity(i);
                finish();
            }
        });

        btn_ingreso_empresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ClasificacionActivity.this, MenuLateralActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                i.putExtra("TIPO_ACCESO", "EMPRESA");
                startActivity(i);
                finish();
            }
        });
    }

}