package com.example.allininha.whatsapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.allininha.whatsapp.R;
import com.example.allininha.whatsapp.activity.ConversaActivity;
import com.example.allininha.whatsapp.adapter.ContatoAdapter;
import com.example.allininha.whatsapp.adapter.ConversaAdapter;
import com.example.allininha.whatsapp.helper.Base64Custom;
import com.example.allininha.whatsapp.helper.ConfigFirebase;
import com.example.allininha.whatsapp.helper.Preferences;
import com.example.allininha.whatsapp.model.Contato;
import com.example.allininha.whatsapp.model.Conversa;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversasFragment extends Fragment {


    private ListView listaConversas;
    // private ArrayAdapter arrayAdapter;
    private ArrayList<Conversa> conversas = new ArrayList<>();
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private ConversaAdapter conversaAdapter;
    public ConversasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(valueEventListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListener);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conversas, container, false);
        listaConversas = (ListView) view.findViewById(R.id.listaConversas);
        // arrayAdapter = new ArrayAdapter(getActivity(), R.layout.lista_contato, listacontatos);
        conversaAdapter = new ConversaAdapter(getActivity(), conversas);
        listaConversas.setAdapter(conversaAdapter);
        String idUSusarioLogado = new Preferences(getContext()).getIdentificado();
        databaseReference = ConfigFirebase.getFirebase().child("conversas").child(idUSusarioLogado);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //limpar lista
                conversas.clear();

                //lista de contatos
                for(DataSnapshot dados: dataSnapshot.getChildren()){
                    Conversa conversa = dados.getValue(Conversa.class);
                    conversas.add(conversa);
                }
                conversaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        listaConversas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Conversa conversa = conversas.get(i);
                Intent intent  = new Intent(getActivity(), ConversaActivity.class);
                intent.putExtra("nome", conversa.getNome());
                String email = Base64Custom.decodificar(conversa.getIdUsuario());
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });
        return view;
    }

}
