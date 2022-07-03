package pmd.di.ubi.pt.projeto_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity7 extends AppCompatActivity {

    ArrayList<ArrayList<String>> Equipas;
    String nomeEvento;
    FirebaseDatabase myFirebase;
    TextView nomeEquipa;
    ListView myListView;
    ArrayAdapter adapter;
    int equipaAtual = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);

        nomeEquipa = (TextView)findViewById(R.id.equipa);
        myListView = (ListView) findViewById(R.id.lista);

        Intent iCameFromActivity6 = getIntent();
        Bundle extras = iCameFromActivity6.getExtras();
        nomeEvento = extras.getString("EVENTO");

        getEquipas();

        nomeEquipa.setText("Equipa " + equipaAtual);

    }

    public void verlista(){
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Equipas.get(0));
        myListView.setAdapter(adapter);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Intent it = new Intent(MainActivity7.this, MainActivity8.class);
                it.putExtra("User",item);
                startActivity(it);
            }
        });
    }

    public void nextTeam(View v){
        int i = equipaAtual;
        i++;
        if(i >= Equipas.size()){} else {
            equipaAtual++;
            nomeEquipa.setText("Equipa " + equipaAtual);
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Equipas.get(equipaAtual));
            myListView.setAdapter(adapter);
        }
    }

    public void previousTeam(View v){
        int i = equipaAtual;
        i--;
        if(i < 0) {} else {
            equipaAtual--;
            nomeEquipa.setText("Equipa " + equipaAtual);
            adapter =  new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Equipas.get(equipaAtual));
            myListView.setAdapter(adapter);
        }
    }

    public void getEquipas(){

        myFirebase = FirebaseDatabase.getInstance();
        DatabaseReference ref = myFirebase.getReference().child("Equipas").child(nomeEvento);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                Equipas = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String nomeEquipa = dataSnapshot.getKey();
                    getElementos(i);
                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }

    public void getElementos(final int nrEquipa){

        final ArrayList<String> Elementos = new ArrayList<>();
        myFirebase = FirebaseDatabase.getInstance();
        DatabaseReference ref = myFirebase.getReference().child("Equipas").child(nomeEvento).child("Equipa " + nrEquipa);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String nomeEquipa = dataSnapshot.getKey();
                    Elementos.add(nomeEquipa);
                    Log.i("EQUIPAS",Elementos.toString());
                }
                Equipas.add(Elementos);
                Log.i("EQUIPAS",Equipas.toString());
                verlista();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}