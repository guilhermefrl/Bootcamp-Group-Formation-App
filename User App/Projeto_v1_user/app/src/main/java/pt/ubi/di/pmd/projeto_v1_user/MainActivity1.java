package pt.ubi.di.pmd.projeto_v1_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.view.Menu;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity1 extends AppCompatActivity implements View.OnClickListener {

   private Button B1,B2,B3;
   private FirebaseAuth mAuth;
   String nomeagf;
    String email;
    public USer us;
    FirebaseDatabase myFirebase1;
    int flag=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        mAuth = FirebaseAuth.getInstance();

            if(null != mAuth.getCurrentUser()) {
                B1 = findViewById(R.id.button);
                B1.setOnClickListener(this);

                B2 = findViewById(R.id.button2);
                B2.setOnClickListener(this);

                B3 = findViewById(R.id.button3);
                B3.setOnClickListener(this);
            }
            else{
                MSG("Sessao Nao Iniciada");
                Intent Pagina_Mostrar_Eventos_Disponiveis = new Intent(MainActivity1.this, MainActivity.class);
                startActivity(Pagina_Mostrar_Eventos_Disponiveis);
            }
        mAuth =FirebaseAuth.getInstance();
        FirebaseUser U =mAuth.getCurrentUser();
        email=U.getEmail();
        VerificaUser();
    }
    private void VerificaUser() {
        myFirebase1 = FirebaseDatabase.getInstance();

        final DatabaseReference ref = myFirebase1.getReference().child("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    us = dataSnapshot.getValue(USer.class);
                    String teste = us.getEmail();
                    if(teste.equals(email)){
                        flag = 1;
                    }
                }
                if(flag==0){
                    MSG("Inicie Sessão como user para acessar a aplicação");
                    startActivity(new Intent(MainActivity1.this, MainActivity.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                Intent Pagina_Mostrar_Eventos_Disponiveis = new Intent(MainActivity1.this, Mostrar_Eventos_Disponiveis.class);
                startActivity(Pagina_Mostrar_Eventos_Disponiveis);
                break;

            case R.id.button2 :
                Intent Pagina_Mostrar_Eventos_Fechados = new Intent(MainActivity1.this, Mostrar_Eventos_Fechados.class);
                startActivity(Pagina_Mostrar_Eventos_Fechados);
                break;

            case R.id.button3 :
                Intent Pagina_Mostrar_Eventos_Inscrito = new Intent(MainActivity1.this, Mostrar_Eventos_Inscrito.class);
                startActivity(Pagina_Mostrar_Eventos_Inscrito);
                break;
        }
    }
    private void MSG(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.perfil){
            Intent Perfil = new Intent(MainActivity1.this, Mostrar_Perfil.class);
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
            startActivity(new Intent(MainActivity1.this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}