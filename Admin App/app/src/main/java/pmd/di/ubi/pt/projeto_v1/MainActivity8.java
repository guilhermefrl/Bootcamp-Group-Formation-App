package pmd.di.ubi.pt.projeto_v1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity8 extends AppCompatActivity {
    TextView T1,T2,T3,T4,T5;
    String user;
    FirebaseDatabase myFirebase;
    User us;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8);
        T1= findViewById(R.id.nomeUser);
        T2= findViewById(R.id.Apelido);
        T3= findViewById(R.id.emailUser);
        T4= findViewById(R.id.dnUser);
        T5= findViewById(R.id.generoUser);
        Intent iCameFromMain8=getIntent();
        user=iCameFromMain8.getStringExtra("User");
        myFirebase = FirebaseDatabase.getInstance();
        final DatabaseReference ref3 = myFirebase.getReference();
        final DatabaseReference ref = myFirebase.getReference().child("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    us = dataSnapshot.getValue(User.class);
                    String teste = us.getNome();
                    String apelido= us.getApelido();
                    String email = us.getEmail();
                    String data = us.getDn();
                    String genero= us.getGenero();
                    String distrito= us.getDistrito();
                    if (user.equals(teste)) {
                        String s = "Nome: "+ teste +" "+ apelido;
                        T1.setText(s);
                        s = "Data Nascimento: "+ data;
                        T2.setText(s);
                        s = "Email: "+ email;
                        T3.setText(s);
                        s = "Género: "+ genero;
                        T4.setText(s);
                        s = "Distrito: "+ distrito;
                        T5.setText(s);
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
    }
}
