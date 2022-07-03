package pmd.di.ubi.pt.projeto_v1;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class Activity_admin_pagina_escolha extends AppCompatActivity implements View.OnClickListener {

    private Button B1,B2,B3;
    private FirebaseAuth mAuth;
    String email;
    public Admin us;
    FirebaseDatabase myFirebase1;
    int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pagina_escolha);
        B1=findViewById(R.id.admin_pagina_escolha_criar_evento);
        B2=findViewById(R.id.admin_pagina_escolha_equipas);
        B3=findViewById(R.id.admin_pagina_escolha_ver_eventos);
        B1.setOnClickListener(this);
        B2.setOnClickListener(this);
        B3.setOnClickListener(this);
        mAuth =FirebaseAuth.getInstance();
        FirebaseUser U =mAuth.getCurrentUser();
        email=U.getEmail();

        VerificaAdmin();
    }

    private void VerificaAdmin() {
        myFirebase1 = FirebaseDatabase.getInstance();

        final DatabaseReference ref = myFirebase1.getReference().child("Admins");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    us = dataSnapshot.getValue(Admin.class);
                    String teste = us.getEmail();
                    if(teste.equals(email)){
                        flag = 1;
                    }
                }
            if(flag==0){
                MSG("Inicie Sessão como administrador para acessar a aplicação");
                startActivity(new Intent(Activity_admin_pagina_escolha.this, MainActivity_admin_login.class));
            }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.admin_pagina_escolha_criar_evento:
                startActivity(new Intent(Activity_admin_pagina_escolha.this, Activity_admin_criar_evento.class));
                break;

            case R.id.admin_pagina_escolha_equipas:
                startActivity(new Intent(Activity_admin_pagina_escolha.this, MainActivity6.class));
                break;
            case R.id.admin_pagina_escolha_ver_eventos:
                startActivity(new Intent(Activity_admin_pagina_escolha.this, MainActivity4.class));
                break;
        }
    }
    private void MSG(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.perfil){
            Intent Perfil = new Intent(Activity_admin_pagina_escolha.this, Mostrar_Perfil.class);
            FirebaseUser u= mAuth.getCurrentUser();
            startActivity(Perfil);
        }
        if(item.getItemId()==R.id.sair){
            FirebaseUser u=mAuth.getCurrentUser();
            mAuth.signOut();
            finishAffinity();
        }
        if(item.getItemId()==R.id.terminar){
            FirebaseUser u=mAuth.getCurrentUser();
            mAuth.signOut();
            startActivity(new Intent(Activity_admin_pagina_escolha.this, MainActivity_admin_login.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
