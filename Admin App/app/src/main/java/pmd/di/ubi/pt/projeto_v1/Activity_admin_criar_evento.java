package pmd.di.ubi.pt.projeto_v1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Activity_admin_criar_evento extends AppCompatActivity implements View.OnClickListener {
    private EditText E1,E2,E3,E4,E5;
    private Button B1;
    private Spinner S1;
    ProgressBar P1;
    private ArrayAdapter<String> adapter;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_criar_evento);
        E1=findViewById(R.id.admin_criar_evento_nome);
        E2=findViewById(R.id.admin_criar_evento_descricao);
        E3=findViewById(R.id.admin_criar_evento_total);
        E4=findViewById(R.id.admin_criar_evento_numpessoas);
        B1=findViewById(R.id.admin_criar_evento_criar);
        S1=findViewById(R.id.spinner);
        P1=findViewById(R.id.PB1);
        B1.setOnClickListener(this);
        P1.setVisibility(View.GONE);

        auth = FirebaseAuth.getInstance();
        String[] bankNames={"Ordem de Inscricao","Aleatorio","Ordenar por Idades","Ordenar pelo ano de nascimento","Ordenar pelo mes de nascimento","Ordenar por género", "Ordenar por Distritos", "Ordenar por Apelido"};
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, bankNames);
        S1.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.admin_criar_evento_criar:
                CriarEvento();
                break;
        }
    }

    private void CriarEvento() {
        final String nome=  E1.getText().toString().trim();
        final String des=  E2.getText().toString().trim();
        final String total=  E3.getText().toString().trim();
        final String numpessoas=  E4.getText().toString().trim();
        final String regra=  S1.getSelectedItem().toString().trim();
     

        if(nome.isEmpty()){
            E1.setError("Insira um nome valido");
            E1.requestFocus();
            return;
        }
        if(des.isEmpty()){
            E2.setError("Insira uma Descricao");
            E2.requestFocus();
            return;
        }
        if(total.isEmpty()){
            E3.setError("Insira um total de Participantes");
            E3.requestFocus();
            return;
        }
        if(numpessoas.isEmpty()){
            E4.setError("Insira um numero maximo de pessoas");
            E4.requestFocus();
            return;
        }
        P1.setVisibility(View.VISIBLE);
        int t=Integer.valueOf(total);
        int n=Integer.valueOf(numpessoas);
        Evento ev= new Evento(nome,des,regra,t,n,0);
       DatabaseReference a= FirebaseDatabase.getInstance().getReference().child("Eventos");
         a.push().setValue(ev).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                         @Override
                                                         public void onComplete(@NonNull Task<Void> task) {
                                                             if(task.isSuccessful()){
                                                                 MSG("Evento registado com Sucesso");
                                                                 P1.setVisibility(View.GONE);
                                                                 // startActivity(new Intent(Activity_admin_criar_evento.this, Activity_admin_pagina_escolha.class));
                                                             }
                                                             else{
                                                                 MSG("Erro no Registo");
                                                                 P1.setVisibility(View.GONE);
                                                             }
                                                         }
         });

    }
    private void MSG(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}
