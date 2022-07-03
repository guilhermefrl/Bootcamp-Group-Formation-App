package pt.ubi.di.pmd.projeto_v1_user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Mostrar_Equipas extends AppCompatActivity {
    private TextView T1;
    public ListView L1;
    private Button B1,B2;
    private String nomeagf;
    private String nomeUser;
    private String nomeEquipa;
    USer user;
    String item;
    ArrayList<String> equipa;
    ArrayList<String> informacoesUser;
    ArrayAdapter adapter;
    ImageView I1;
    private Bitmap my_image;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar__equipas);
        T1 = findViewById(R.id.mostrar_nome_evento_equipas);
        L1 = findViewById(R.id.mostrar_elementos_equipa);
        B1 = findViewById(R.id.voltar);
        B2=findViewById(R.id.foto);
        I1=findViewById(R.id.view_foto);
        Intent X = getIntent();
        nomeagf = X.getStringExtra("Nome");
        I1.setVisibility(View.GONE);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();

        getNomeUser(userid);
        L1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = L1.getItemAtPosition(position).toString();
                L1.setEnabled(false);
                lerInformacoesUser(item);
            }
        });
    }

    public void voltar(View v){
        nomeEquipa();
        B1.setVisibility(View.GONE);
        B2.setVisibility(View.GONE);
        L1.setEnabled(true);
        I1.setVisibility(View.GONE);
    }

    public void MostrarFoto(View v){

        StorageReference ref = FirebaseStorage.getInstance().getReference().child(item);
        try {
            final File localFile = File.createTempFile("Images", "jpeg");
            ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener< FileDownloadTask.TaskSnapshot >() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    my_image = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    I1.setImageBitmap(my_image);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    MSG("ERRO");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        I1.setVisibility(View.VISIBLE);
    }

    public void lerInformacoesUser(String nome){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                informacoesUser = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                   user = dataSnapshot.getValue(USer.class);
                    if(user.getNome().equals(nome)) break;
                }
                String tudo= "Nome: " +user.getNome() +" " + user.getApelido();
                informacoesUser.add(tudo);
                informacoesUser.add("Data de nascimento : " + user.getDn());
                informacoesUser.add("Email : " + user.getEmail());
                informacoesUser.add("Género : " + user.getGenero());
                informacoesUser.add("Distrito : " + user.getDistrito());
                adapter = new ArrayAdapter<>(Mostrar_Equipas.this, android.R.layout.simple_list_item_1, informacoesUser);
                L1.setAdapter(adapter);
                T1.setText("");
                B2.setVisibility(View.VISIBLE);
                B1.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void getNomeUser(String userID){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(userID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                USer user = snapshot.getValue(USer.class);
                nomeUser = user.getNome();
                nomeEquipa();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void nomeEquipa(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Equipas").child(nomeagf);
        ref.addValueEventListener(new ValueEventListener() {
            String nome;
            int i = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                outerloop:
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        String nome = dataSnapshot1.getKey();
                        if(nome.equals(nomeUser)) break outerloop;
                    }
                    i++;
                }
                nomeEquipa = "Equipa " + i;
                T1.setText(nomeEquipa);
                getEquipa();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void getEquipa(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Equipas").child(nomeagf).child(nomeEquipa);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                equipa = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    equipa.add(dataSnapshot.getKey());
                }
                Log.i("EQUIPA",equipa.toString());
                verlista();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void verlista(){
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, equipa);
        L1.setAdapter(adapter);
    }
    private void MSG(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}