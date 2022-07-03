package pmd.di.ubi.pt.projeto_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Mostrar_Perfil extends AppCompatActivity implements View.OnClickListener {
    public ListView L1;
    ArrayList<String> info;
    ArrayAdapter adapter;

    String nomeagf;
    String nome;
    ImageView I1;

    Button B1;
    EditText E1,E2;
    Admin a;
    ProgressBar P1;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar__perfil);
        L1=findViewById(R.id.view_perfil);
        FirebaseAuth  mAuth = FirebaseAuth.getInstance();
        FirebaseUser  us= mAuth.getCurrentUser();
        nomeagf=us.getEmail();

        MostrarInfo();
        P1=findViewById(R.id.po2);
        B1=findViewById(R.id.confirm_pass);
        B1.setOnClickListener(this);
        E1=findViewById(R.id.new_pass);
        E2=findViewById(R.id.new_pass2);
        P1.setVisibility(View.GONE);
    }

    private void MostrarInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Admins");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                info = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    a = dataSnapshot.getValue(Admin.class);
                    if(a.getEmail().equals(nomeagf)) break;
                }
                nome=a.getNome();
                String tudo= "Nome: " +a.getNome() +" " + a.getApelido();
                info.add(tudo);
                info.add("Email : " + a.getEmail());
                adapter = new ArrayAdapter<>(Mostrar_Perfil.this, android.R.layout.simple_list_item_1, info);
                L1.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void MSG(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_pass :
                String pass=E1.getText().toString().trim();
                String pass1=E2.getText().toString().trim();
                if(pass.isEmpty()){
                    E1.setError("Insira uma nova Palavra Passe");
                    E1.requestFocus();
                    return;
                }
                if(pass1.isEmpty()){
                    E2.setError("Confirme a nova Palavra Passe");
                    E2.requestFocus();
                    return;
                }
                if(pass.equals(pass1)){
                    P1.setVisibility(View.VISIBLE);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.updatePassword(pass)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        MSG("Sucesso a fazer update da Palavra passe");
                                        P1.setVisibility(View.GONE);
                                    }
                                }
                            });
                    P1.setVisibility(View.GONE);
                }
                else{
                    E2.setError("As Palavras Passes não correspondem");
                    E2.requestFocus();
                }


                break;
        }

    }
}