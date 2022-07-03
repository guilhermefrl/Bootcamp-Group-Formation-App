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
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity_admin_registo extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth admin_Auth;
    public EditText E1,E2,E3,E4,E5,E6;
    private Button B1;
    public ProgressBar P1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin_registo);

        admin_Auth = FirebaseAuth.getInstance();
        E1=findViewById(R.id.registo_nome);
        E2=findViewById(R.id.registo_email);
        E3=findViewById(R.id.apelido);
        E4=findViewById(R.id.registo_password);
        B1=findViewById(R.id.registo_button_registo);
        B1.setOnClickListener(this);
        P1=findViewById(R.id.progressBar1);
        P1.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registo_button_registo:
                registarUser();

                break;
        }
    }
    private int f;
    private void registarUser() {
        final String nome=  E1.getText().toString().trim();
        final String apelido=  E3.getText().toString().trim();
        final String email=  E2.getText().toString().trim();
        final String password=  E4.getText().toString().trim();

        if(nome.isEmpty()){
            E1.setError("Insira um nome");
            E1.requestFocus();
            return;
        }
        if(email.isEmpty()){
            E2.setError("Insira um email");
            E2.requestFocus();
            return;
        }
        if(apelido.isEmpty()){
            E3.setError("Insira um apelido");
            E3.requestFocus();
            return;
        }
        if(password.isEmpty()){
            E5.setError("Insira uma password");
            E5.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            E2.setError("Insira um email valido");
            E2.requestFocus();
            return;
        }
        P1.setVisibility(View.VISIBLE);
        admin_Auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Admin admin= new Admin(nome,apelido,email,password);
                            FirebaseDatabase.getInstance().getReference("Admins")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(admin).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        MSG("Registo com Sucesso");
                                        P1.setVisibility(View.GONE);
                                        startActivity(new Intent(MainActivity_admin_registo.this, MainActivity_admin_login.class));
                                    }
                                    else{
                                        MSG("Erro no Registo 1");
                                        P1.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }else{
                            MSG("Erro no Registo 1");
                            P1.setVisibility(View.GONE);
                        }
                    }
                });
    }
    private void MSG(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}