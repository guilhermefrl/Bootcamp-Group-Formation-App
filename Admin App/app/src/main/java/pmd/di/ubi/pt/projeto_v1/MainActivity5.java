package pmd.di.ubi.pt.projeto_v1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class MainActivity5 extends AppCompatActivity{
    User us;
    Evento evento;
    TextView nome;
    TextView descricao;
    TextView totalParticipantes;
    TextView regra;
    Button criarEquipas;
    FirebaseDatabase myFirebase,myFirebase1;
    String nomeEvento;
    int total, pessoasGrupo;
    ArrayList<String> listaInscritos;
    ArrayList<String> listaApelidos;
    ArrayList<UserMes> listaMes;
    ArrayList<UserIdade> listaIdades;
    UserMes U1;
    String RegraEvento;
    String teste;
    String userid;
    public UserMes U2,U5;
    public UserIdade U6;
    private DatabaseReference mFirebaseRef1 = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        U2=new UserMes();
        Intent iCameFromActivity4 = getIntent();
        Bundle extras = iCameFromActivity4.getExtras();
        nomeEvento = extras.getString("EVENTO");
        criarEquipas = (Button) findViewById(R.id.criarEquipas);
        nome = (TextView) findViewById(R.id.nome);
        descricao = (TextView) findViewById(R.id.descricao);
        totalParticipantes = (TextView) findViewById(R.id.totalParticipantes);
        regra = (TextView) findViewById(R.id.regrasEquipa);

        preencherDadosEvento();
    }

    public void criarEquipas(View v){
        fazerEquipas();
    }

    public int determinarNumeroEquipas(){ return total/pessoasGrupo;}


    public void fazerEquipas(){
        myFirebase = FirebaseDatabase.getInstance();
        DatabaseReference ref = myFirebase.getReference().child("Inscritos").child(nomeEvento);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaInscritos = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String nome = dataSnapshot.getKey();
                    if(nome.equals("nome") || nome.equals("total")) {} else {
                        listaInscritos.add(nome);
                    }
                }

                adicionarEquipas();

                switch (RegraEvento){
                    case "Ordem de Inscricao":
                        adicionarElementosPorOrdem();
                        break;
                    case "Aleatorio":
                        adicionarElementosAleatorio();
                        break;
                    case "Ordenar por Idades":
                    adicionarElementosIdades();
                        break;
                    case "Ordenar pelo ano de nascimento":
                        adicionarElementosPorAno();

                        break;
                    case "Ordenar pelo mes de nascimento":
                        adicionarElementosPorMes();
                        break;
                    case "Ordenar por género":
                        adicionarElementosPorGenero();
                        break;
                    case "Ordenar por Distritos":
                        adicionarElementosPorDistrito();
                        break;
                    case "Ordenar por Apelido":
                        adicionarElementosPorApelido();
                        break;
                }
                fecharEquipas();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }

    private void adicionarElementosIdades() {
        myFirebase1 = FirebaseDatabase.getInstance();
        final DatabaseReference ref3 = myFirebase1.getReference();
        listaIdades = new ArrayList<>();
        U6=new UserIdade();
        final DatabaseReference ref = myFirebase1.getReference().child("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    us = dataSnapshot.getValue(User.class);
                    String teste = us.getNome();
                    String data1= us.getDn();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    if (listaInscritos.contains(teste)) {
                        try {
                            Date date1 = sdf.parse(data1);
                            UserIdade U2 = new UserIdade(teste,date1);
                            listaIdades.add(U2);
                        } catch (Exception exception) {
                            Toast.makeText(getApplicationContext(), "Unable to parse", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                Collections.sort(listaIdades,new ComparadorDeIdades());
                int nGrupos = determinarNumeroEquipas();
                int j = 0;
                outerloop:
                for (int i = 0; i < nGrupos; i++) {
                    for (int k = 0; k < pessoasGrupo; k++) {
                        U6 = listaIdades.get(j);
                        String a =  U6.getUsername();
                        ref3.child("Equipas").child(nomeEvento).child("Equipa " + i).child(a).setValue(true);
                        j++;
                        if(j == listaInscritos.size()) break outerloop;
                    }
                }
                MSG("Equipas criadas com sucesso");
                startActivity(new Intent(MainActivity5.this, Activity_admin_pagina_escolha.class));

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
    }

    private void adicionarElementosPorMes() {
        myFirebase1 = FirebaseDatabase.getInstance();
        final DatabaseReference ref3 = myFirebase1.getReference();
        listaMes = new ArrayList<>();
        U5=new UserMes();
        final DatabaseReference ref = myFirebase1.getReference().child("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    us = dataSnapshot.getValue(User.class);
                    String teste = us.getNome();
                    String data1= us.getDn();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    if (listaInscritos.contains(teste)) {
                        try {
                            Date date1 = sdf.parse(data1);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date1);
                            int month = cal.get(Calendar.MONTH);
                            month = month + 1;
                            UserMes U2 = new UserMes(teste,month);
                            listaMes.add(U2);
                        } catch (Exception exception) {
                            Toast.makeText(getApplicationContext(), "Unable to parse", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                Collections.sort(listaMes,new ComparadorDeMes());
                int nGrupos = determinarNumeroEquipas();
                int j = 0;
                outerloop:
                for (int i = 0; i < nGrupos; i++) {
                    for (int k = 0; k < pessoasGrupo; k++) {
                        U5 = listaMes.get(j);
                        String a =  U5.getUsername();
                       ref3.child("Equipas").child(nomeEvento).child("Equipa " + i).child(a).setValue(true);
                        j++;
                        if(j == listaInscritos.size()) break outerloop;
                    }
                }
                MSG("Equipas criadas com sucesso");
                startActivity(new Intent(MainActivity5.this, Activity_admin_pagina_escolha.class));

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
    }
    private void adicionarElementosPorAno() {
        myFirebase1 = FirebaseDatabase.getInstance();
        final DatabaseReference ref3 = myFirebase1.getReference();
        listaMes = new ArrayList<>();
        U1=new UserMes();
        final DatabaseReference ref = myFirebase1.getReference().child("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    us = dataSnapshot.getValue(User.class);
                    String teste = us.getNome();
                    String data1= us.getDn();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    if (listaInscritos.contains(teste)) {
                        try {
                            Date date1 = sdf.parse(data1);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date1);
                            int month = cal.get(Calendar.YEAR);
                            UserMes U2 = new UserMes(teste,month);
                            listaMes.add(U2);
                        } catch (Exception exception) {
                            Toast.makeText(getApplicationContext(), "Unable to parse", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                Collections.sort(listaMes,new ComparadorDeMes());
                int nGrupos = determinarNumeroEquipas();
                int j = 0;
                outerloop:
                for (int i = 0; i < nGrupos; i++) {
                    for (int k = 0; k < pessoasGrupo; k++) {
                        U1 = listaMes.get(j);
                        String a =  U1.getUsername();
                        ref3.child("Equipas").child(nomeEvento).child("Equipa " + i).child(a).setValue(true);
                        j++;
                        if(j == listaInscritos.size()) break outerloop;
                    }
                }
                MSG("Equipas criadas com sucesso");
                startActivity(new Intent(MainActivity5.this, Activity_admin_pagina_escolha.class));

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });



    }
    private void adicionarElementosPorGenero() {
        myFirebase1 = FirebaseDatabase.getInstance();
        final DatabaseReference ref3 = myFirebase1.getReference();
        listaMes = new ArrayList<>();
        U1=new UserMes();
        final DatabaseReference ref = myFirebase1.getReference().child("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int flag=3;
                    us = dataSnapshot.getValue(User.class);
                    String teste = us.getNome();
                    String data1= us.getGenero();
                    if (listaInscritos.contains(teste)) {
                         if(data1.equals("Masculino")){
                             flag=1;
                         }
                         if(data1.equals("Feminino")){
                             flag=2;
                         }
                            UserMes U2 = new UserMes(teste,flag);
                            listaMes.add(U2);
                    }
                }
                Collections.sort(listaMes,new ComparadorDeMes());
                int nGrupos = determinarNumeroEquipas();
                int j = 0;
                outerloop:
                for (int i = 0; i < nGrupos; i++) {
                    for (int k = 0; k < pessoasGrupo; k++) {
                        U1 = listaMes.get(j);
                        String a =  U1.getUsername();
                        ref3.child("Equipas").child(nomeEvento).child("Equipa " + i).child(a).setValue(true);
                        j++;
                        if(j == listaInscritos.size()) break outerloop;
                    }
                }
                MSG("Equipas criadas com sucesso");
                startActivity(new Intent(MainActivity5.this, Activity_admin_pagina_escolha.class));

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });

    }

    private void adicionarElementosPorDistrito() {
        myFirebase1 = FirebaseDatabase.getInstance();
        final DatabaseReference ref3 = myFirebase1.getReference();
        listaMes = new ArrayList<>();
        U1=new UserMes();
        final DatabaseReference ref = myFirebase1.getReference().child("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int flag;
                    us = dataSnapshot.getValue(User.class);
                    String teste = us.getNome();
                    String distrito= us.getDistrito();
                    if (listaInscritos.contains(teste)) {
                        String[] Distritos={"Aveiro", "Beja", "Braga", "Bragança", "Castelo Branco", "Coimbra", "Évora", "Faro", "Guarda", "Leiria", "Lisboa", "Portalegre",
                                "Porto", "Santarém", "Setúbal", "Viana do Castelo", "Vila Real", "Viseu"};

                        for (int i = 0; i < 18; i++) {
                            if (distrito.equals(Distritos[i])) {
                                flag = i;
                                UserMes U2 = new UserMes(teste, flag);
                                listaMes.add(U2);
                            }
                        }
                    }
                }
                Collections.sort(listaMes,new ComparadorDeMes());
                int nGrupos = determinarNumeroEquipas();
                int j = 0;
                outerloop:
                for (int i = 0; i < nGrupos; i++) {
                    for (int k = 0; k < pessoasGrupo; k++) {
                        U1 = listaMes.get(j);
                        String a =  U1.getUsername();
                        ref3.child("Equipas").child(nomeEvento).child("Equipa " + i).child(a).setValue(true);
                        j++;
                        if(j == listaInscritos.size()) break outerloop;
                    }
                }
                MSG("Equipas criadas com sucesso");
                startActivity(new Intent(MainActivity5.this, Activity_admin_pagina_escolha.class));

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
    }

    private void adicionarElementosPorApelido(){
        myFirebase1 = FirebaseDatabase.getInstance();
        final DatabaseReference ref3 = myFirebase1.getReference();
        listaMes = new ArrayList<>();
        listaApelidos = new ArrayList<>();
        U1=new UserMes();

        final DatabaseReference ref = myFirebase1.getReference().child("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    us = dataSnapshot.getValue(User.class);
                    String apelido= us.getApelido();
                    if(!listaApelidos.contains(apelido)) {
                        listaApelidos.add(apelido);
                    }
                }

                final DatabaseReference ref1 = myFirebase1.getReference().child("Users");
                ref1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            int flag;
                            us = dataSnapshot.getValue(User.class);
                            String teste = us.getNome();
                            String apelido1= us.getApelido();
                            
                            if (listaInscritos.contains(teste)) {
                                for (int i = 0; i < listaApelidos.size(); i++) {
                                    if((listaApelidos.get(i)).equals(apelido1)) {
                                        flag = i;
                                        UserMes U2 = new UserMes(teste, flag);
                                        listaMes.add(U2);
                                    }
                                }
                            }
                        }
                        Collections.sort(listaMes,new ComparadorDeMes());
                        int nGrupos = determinarNumeroEquipas();
                        int j = 0;
                        outerloop:
                        for (int i = 0; i < nGrupos; i++) {
                            for (int k = 0; k < pessoasGrupo; k++) {
                                U1 = listaMes.get(j);
                                String a =  U1.getUsername();
                                ref3.child("Equipas").child(nomeEvento).child("Equipa " + i).child(a).setValue(true);
                                j++;
                                if(j == listaInscritos.size()) break outerloop;
                            }
                        }
                        MSG("Equipas criadas com sucesso");
                        startActivity(new Intent(MainActivity5.this, Activity_admin_pagina_escolha.class));

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }

                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
    }

    public void adicionarEquipas(){
        myFirebase = FirebaseDatabase.getInstance();
        DatabaseReference ref = myFirebase.getReference();

        int nGrupos = determinarNumeroEquipas();

        for (int i = 0; i < nGrupos;i++){
            ref.child("Equipas").child(nomeEvento).child("Equipa " + i);
        }
    }

    public void adicionarElementosPorOrdem() {
        myFirebase = FirebaseDatabase.getInstance();
        DatabaseReference ref = myFirebase.getReference();
        int nGrupos = determinarNumeroEquipas();
        int j = 0;
        outerloop:
        for (int i = 0; i < nGrupos; i++) {
            for (int k = 0; k < pessoasGrupo; k++) {
                ref.child("Equipas").child(nomeEvento).child("Equipa " + i).child(listaInscritos.get(j)).setValue(true);
                j++;
                if(j == listaInscritos.size()) break outerloop;
            }
        }
    }
    public void adicionarElementosAleatorio() {
        myFirebase = FirebaseDatabase.getInstance();
        DatabaseReference ref = myFirebase.getReference();

        int nGrupos = determinarNumeroEquipas();
        int j = 0;
        Collections.shuffle(listaInscritos);
        outerloop:
        for (int i = 0; i < nGrupos; i++) {
            for (int k = 0; k < pessoasGrupo; k++) {
                ref.child("Equipas").child(nomeEvento).child("Equipa " + i).child(listaInscritos.get(j)).setValue(true);
                j++;
                if(j == listaInscritos.size()) break outerloop;
            }
        }
    }

    public void fecharEquipas(){
        myFirebase = FirebaseDatabase.getInstance();
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Eventos");

        ref.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String nome = snapshot.child("nome").getValue().toString();
                    if(nomeEvento.equals(nome)){
                        String keyid = snapshot.getKey();
                        ref.child(keyid).child("fechado").setValue(1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void preencherDadosEvento(){
        myFirebase = FirebaseDatabase.getInstance();
        DatabaseReference ref = myFirebase.getReference().child("Eventos");
        final ArrayList<Evento> listaEventos = new ArrayList<>();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    evento = dataSnapshot.getValue(Evento.class);
                    if(evento.getNome().equals(nomeEvento)){
                        nome.setText(evento.getNome());
                        descricao.setText(evento.getDes());
                        totalParticipantes.setText(String.valueOf(evento.getTotal()));
                        RegraEvento= evento.getRegra();
                        regra.setText(evento.getRegra());
                        total = evento.getTotal();
                        pessoasGrupo = evento.getNumpessoas();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void Eliminarevento(final View view){
        myFirebase = FirebaseDatabase.getInstance();
        final DatabaseReference ref = myFirebase.getReference().child("Eventos");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    evento = dataSnapshot.getValue(Evento.class);
                    if(evento.getNome().equals(nomeEvento)){
                        Task b = ref.child(dataSnapshot.getKey()).removeValue();
                        //if(b.isSuccessful()) {
                        EliminarInscritos();
                        MSG("Evento removido!");
                        Goback(view);
                        //}
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void EliminarInscritos() {
        Task b = FirebaseDatabase.getInstance().getReference().child("Inscritos").child(evento.getNome()).removeValue();
    }
    public void GoHome(View view){
        startActivity(new Intent(MainActivity5.this, Activity_admin_pagina_escolha.class));
    }
    private void MSG(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
    public void Goback(View view){
        startActivity(new Intent(MainActivity5.this, MainActivity4.class));
    }
}
