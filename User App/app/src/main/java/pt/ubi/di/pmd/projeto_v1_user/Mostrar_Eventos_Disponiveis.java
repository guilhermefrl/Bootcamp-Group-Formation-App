package pt.ubi.di.pmd.projeto_v1_user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class Mostrar_Eventos_Disponiveis extends AppCompatActivity {

    DatabaseReference mFirebaseRef = FirebaseDatabase.getInstance().getReference();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar__eventos__disponiveis);

        Query orderByChild= mFirebaseRef.child("Eventos").orderByChild("fechado").equalTo(0);

        listView=(ListView)findViewById(R.id.List_view_mostrar_eventos_disponiveis);
        final ArrayList<String> arrayList=new ArrayList<>();
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);

        orderByChild.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @NonNull String previousChildName) {
                String value = dataSnapshot.getValue(Eventos.class).toString();
                arrayList.add(value);
                arrayAdapter.notifyDataSetChanged();
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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { //Espera que seja efetuado um longClick na lista
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int pos, long id) {
                String agf = (String) arrayAdapter.getItem(pos);  //Vamos buscar o item da posicao que foi com long click
                Intent Pagina_Inscrever_Eventos = new Intent(Mostrar_Eventos_Disponiveis.this, Inscrever_Eventos.class);
                Pagina_Inscrever_Eventos.putExtra("Nome",agf);
                startActivity(Pagina_Inscrever_Eventos);
                return false;
            }
        });


    }
    private void MSG(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}