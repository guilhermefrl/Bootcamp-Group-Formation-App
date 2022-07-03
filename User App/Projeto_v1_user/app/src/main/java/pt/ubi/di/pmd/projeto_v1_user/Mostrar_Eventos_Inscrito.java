package pt.ubi.di.pmd.projeto_v1_user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class Mostrar_Eventos_Inscrito extends AppCompatActivity  {

    DatabaseReference mFirebaseRef = FirebaseDatabase.getInstance().getReference();
    public ListView listView;
    private String teste1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar__eventos__inscrito);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("nome").getValue().toString();
                teste1=name;

                Query orderByChild= mFirebaseRef.child("Inscritos").orderByChild(teste1).equalTo("true");

                listView=(ListView)findViewById(R.id.List_view_mostrar_eventos_inscrito);
                final ArrayList<String> arrayList=new ArrayList<>();
                ArrayAdapter arrayAdapter =new ArrayAdapter(Mostrar_Eventos_Inscrito.this,android.R.layout.simple_list_item_1,arrayList);

                orderByChild.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @NonNull String previousChildName) {
                        String value = dataSnapshot.getValue(Inscritos.class).toString();
                        arrayList.add(value);
                        arrayAdapter.notifyDataSetChanged();

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //Espera que seja efetuado um longClick na lista
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                                String agf = (String) arrayAdapter.getItem(pos);  //Vamos buscar o item da posicao que foi com long click
                                Intent Pagina_Inscrever_Eventos = new Intent(Mostrar_Eventos_Inscrito.this, Eventos_Inscrito_Remover_Inscricao.class);
                                Pagina_Inscrever_Eventos.putExtra("Nome",agf);
                                startActivity(Pagina_Inscrever_Eventos);
                            }
                        });
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                listView.setAdapter(arrayAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void MSG(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

}