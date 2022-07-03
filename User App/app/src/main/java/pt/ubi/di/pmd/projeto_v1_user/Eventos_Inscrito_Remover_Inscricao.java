package pt.ubi.di.pmd.projeto_v1_user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Eventos_Inscrito_Remover_Inscricao extends AppCompatActivity implements View.OnClickListener {
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
        setContentView(R.layout.activity_eventos__inscrito__remover__inscricao);
        T1=findViewById(R.id.nome_evento_remover_inscricao);
        T2=findViewById(R.id.mostrar_descricao_remover_eventos);
        T3=findViewById(R.id.mostrar_total_participantes);
        B1=findViewById(R.id.butao_remove_inscricao);
        B1.setOnClickListener(this);
        Intent X = getIntent();
        nomeagf = X.getStringExtra("Nome");
        mAuth = FirebaseAuth.getInstance();
        ContarInscritos();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("nome").getValue().toString();
                teste = name;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        Query orderByChild= mFirebaseRef1.child("Eventos").orderByChild("nome").equalTo(nomeagf);
        orderByChild.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Evento ev =snapshot.getValue(Evento.class);
                String s = ev.getNome();
                String des = ev.getDes();
                T1.setText("Nome: "+s);
                T2.setText("Descricao: "+des);
                total=ev.getFechado();

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.butao_remove_inscricao :
                RemoverInscricao();
                break;
        }
    }

    private void RemoverInscricao() {
        if(total==1){
            MSG("Evento já fechado, Não é possivel remover Inscrição");
            return;
        }
        Task b = FirebaseDatabase.getInstance().getReference().child("Inscritos").child(nomeagf).child(teste).removeValue();
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
                T3.setText("Total de Inscritos: "+count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    private void MSG(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}