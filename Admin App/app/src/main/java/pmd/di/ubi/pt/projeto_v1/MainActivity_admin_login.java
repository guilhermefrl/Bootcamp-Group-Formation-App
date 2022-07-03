package pmd.di.ubi.pt.projeto_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity_admin_login extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth admin_Auth;
    EditText E1,E2;
    Button B1,B2;
    ProgressBar P1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin_login);
        admin_Auth = FirebaseAuth.getInstance();
        E1=findViewById(R.id.login_email);
        E2=findViewById(R.id.login_password);
        Button B1=findViewById(R.id.login_button_login);
        B1.setOnClickListener(this);
        Button B2=findViewById(R.id.login_button_registo);
        B2.setOnClickListener(this);
        P1=findViewById(R.id.progressBar2);
        P1.setVisibility(View.GONE);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = admin_Auth.getCurrentUser();
        // updateUI(currentUser);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_button_login :
                RealizarLogin();
                break;
            case R.id.login_button_registo :
                startActivity(new Intent(MainActivity_admin_login.this, MainActivity_admin_registo.class));
                break;
        }

    }

    private void RealizarLogin() {
        String email=  E1.getText().toString().trim();
        String pass=  E2.getText().toString().trim();
        if(email.isEmpty()){
            E1.setError("Insira um email");
            E1.requestFocus();
            return;
        }
        if(pass.isEmpty()){
            E2.setError("Insira uma password");
            E2.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            E1.setError("Insira um email valido");
            E1.requestFocus();
            return;
        }
        P1.setVisibility(View.VISIBLE);
        admin_Auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    P1.setVisibility(View.GONE);
                    startActivity(new Intent(MainActivity_admin_login.this, Activity_admin_pagina_escolha.class));
                }
                else{
                    MSG("Erro no Login, Tente de Novo");
                    P1.setVisibility(View.GONE);
                }
            }
        });

    }

    private void MSG(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}