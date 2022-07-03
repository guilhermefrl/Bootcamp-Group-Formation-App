package pt.ubi.di.pmd.projeto_v1_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity_user_registo extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    public EditText E1,E2,E3,E4,E5;
    private Button B1;
    public ProgressBar P1;
    private ArrayAdapter<String> adapter1, adapter2;
    private Spinner S1, S2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user_registo);
        mAuth = FirebaseAuth.getInstance();
        E1=findViewById(R.id.registo_apelido);
        E2=findViewById(R.id.registo_nome);
        E3=findViewById(R.id.registo_email);
        E4=findViewById(R.id.registo_data_nascimento);
        E5=findViewById(R.id.registo_password);
        S1=findViewById(R.id.spinner123);
        S2=findViewById(R.id.spinner_distritos);

        B1=findViewById(R.id.registo_button_registo);
        B1.setOnClickListener(this);

         P1=findViewById(R.id.progressBar1);
         P1.setVisibility(View.GONE);

        String[] bankNames1={"Feminino","Masculino","Outro"};
        adapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, bankNames1);
        S1.setAdapter(adapter1);

        String[] Distritos={"Aveiro", "Beja", "Braga", "Bragança", "Castelo Branco", "Coimbra", "Évora", "Faro", "Guarda", "Leiria", "Lisboa", "Portalegre",
        "Porto", "Santarém", "Setúbal", "Viana do Castelo", "Vila Real", "Viseu"};
        adapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, Distritos);
        S2.setAdapter(adapter2);
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
      String apelido=  E1.getText().toString().trim();
      String nome=  E2.getText().toString().trim();
      String email=  E3.getText().toString().trim();
      String datanascimento=  E4.getText().toString().trim();
      String password=  E5.getText().toString().trim();
      String genero= S1.getSelectedItem().toString();
      String distrito = S2.getSelectedItem().toString();
        Pattern pattern = Pattern.compile("[0-9]");
        Matcher match = pattern.matcher(nome);

        if(match.find()){
            E2.setError("Insira um nome somente usando letras");
            E2.requestFocus();
            return;
        }
         if(apelido.isEmpty()){
        E1.setError("Insira um apelido");
        E1.requestFocus();
        return;
         }
        if(nome.isEmpty()){
            E2.setError("Insira um nome");
            E2.requestFocus();
            return;
        }
        if(email.isEmpty()){
            E3.setError("Insira um email");
            E3.requestFocus();
            return;
        }
        if(datanascimento.isEmpty()){
            E4.setError("Insira uma data de nascimento");
            E4.requestFocus();
            return;
        }
        if(password.isEmpty()){
            E5.setError("Insira uma password");
            E5.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        E3.setError("Insira um email valido");
        E3.requestFocus();
        return;
        }
        P1.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            USer user= new USer(nome,apelido,email,password,datanascimento,genero,distrito);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                       MSG("Registo quase Concluido, Insira uma foto Agora");
                                        P1.setVisibility(View.GONE);
                                        Intent Enviar = new Intent(MainActivity_user_registo.this, FotoPerfil.class);
                                        Enviar.putExtra("teste",nome);
                                        startActivity(Enviar);
                                    }
                                    else{
                                       MSG("Erro no Registo");
                                        P1.setVisibility(View.GONE);

                                    }
                                }
                            });
                        }else{
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