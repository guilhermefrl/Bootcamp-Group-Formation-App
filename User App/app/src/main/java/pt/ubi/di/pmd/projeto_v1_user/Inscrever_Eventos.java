package pt.ubi.di.pmd.projeto_v1_user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Inscrever_Eventos extends AppCompatActivity implements View.OnClickListener {
    private TextView T1,T2,T3,T4,T5;
    private Button B1;
    private Evento ev;
    private String nomeagf;
    private DatabaseReference mFirebaseRef1 = FirebaseDatabase.getInstance().getReference();
    FirebaseDatabase myFirebase;
    private FirebaseAuth mAuth;
    private String teste;
    ArrayList<String> listaInscritos;
    private int count;
    private int total;
    public int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscrever__eventos);
        T1 = findViewById(R.id.Evento_nome);
        T2 = findViewById(R.id.Evento_des);
        T3 = findViewById(R.id.Evento_Participantes);
        T4 = findViewById(R.id.Evento_equipas);
        B1 = findViewById(R.id.Evento_Inscrever);
        T5=findViewById(R.id.inscritos_eventos);
        B1.setOnClickListener(this);
        Intent X = getIntent();
        nomeagf = X.getStringExtra("Nome");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();
        ContarInscritos();


        Query orderByChild= mFirebaseRef1.child("Eventos").orderByChild("nome").equalTo(nomeagf);
        orderByChild.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Evento ev =snapshot.getValue(Evento.class);
                String s = ev.getNome();
                String des = ev.getDes();
                total=ev.getTotal();
                int nump=ev.getNumpessoas();
                T1.setText("Nome: "+s);
                T2.setText("Descricao: "+des);
                T3.setText("Total de Participantes: "+total);
                T4.setText("Grupos de "+nump +" pessoas");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("nome").getValue().toString();
               teste= name;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Evento_Inscrever :
                RealizarInscricao();
                break;
        }
    }

    public void ContarInscritos(){
        myFirebase = FirebaseDatabase.getInstance();
        DatabaseReference ref = myFirebase.getReference().child("Inscritos").child(nomeagf);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaInscritos = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String nome = dataSnapshot.getKey();
                    String map = dataSnapshot.getValue(String.class);
                    listaInscritos.add(nome);
                }
            count=listaInscritos.size();
                if(count==0){

                }else{
                    count=count-1;
                }
                T5.setText("Total de Inscritos: "+count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }

    private void RealizarInscricao() {
        if(count >= total){
         MSG("Erro, Evento Cheio");
         return;
        }
        Task b = FirebaseDatabase.getInstance().getReference().child("Inscritos").child(nomeagf).child(teste).setValue("true");
        Task c = FirebaseDatabase.getInstance().getReference().child("Inscritos").child(nomeagf).child("nome").setValue(nomeagf);
        MSG("Inscrito no Evento: "+nomeagf);
    }



    private void MSG(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}