package com.example.allininha.whatsapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.allininha.whatsapp.R;
import com.example.allininha.whatsapp.activity.ConversaActivity;
import com.example.allininha.whatsapp.adapter.ContatoAdapter;
import com.example.allininha.whatsapp.helper.ConfigFirebase;
import com.example.allininha.whatsapp.helper.Preferences;
import com.example.allininha.whatsapp.model.Contato;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatosFragment extends Fragment {


    private ListView contatos;
   // private ArrayAdapter arrayAdapter;
    private ArrayList<Contato> listacontatos = new ArrayList<>();
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private ContatoAdapter contatoAdapter;

    public ContatosFragment() {
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
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);
        contatos = (ListView) view.findViewById(R.id.listaContatos);
       // arrayAdapter = new ArrayAdapter(getActivity(), R.layout.lista_contato, listacontatos);
        contatoAdapter = new ContatoAdapter(getActivity(), listacontatos);
        contatos.setAdapter(contatoAdapter);
        String idUSusarioLogado = new Preferences(getContext()).getIdentificado();
        databaseReference = ConfigFirebase.getFirebase().child("contatos").child(idUSusarioLogado);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //limpar lista
                listacontatos.clear();

                //lista de contatos
                for(DataSnapshot dados: dataSnapshot.getChildren()){
                    Contato contato = dados.getValue(Contato.class);
                    listacontatos.add(contato);
                }
                contatoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        contatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Contato contato = listacontatos.get(i);
                Intent intent  = new Intent(getActivity(), ConversaActivity.class);
                intent.putExtra("nome", contato.getNome());
                intent.putExtra("email", contato.getEmail());
                startActivity(intent);
            }
        });
        return view;
    }

}
