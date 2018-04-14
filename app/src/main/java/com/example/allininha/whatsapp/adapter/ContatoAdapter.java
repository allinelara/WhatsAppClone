package com.example.allininha.whatsapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.allininha.whatsapp.R;
import com.example.allininha.whatsapp.model.Contato;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by allininha on 08/12/17.
 */

public class ContatoAdapter extends ArrayAdapter<Contato> {

    private ArrayList<Contato>contatos;
    private Context context;
    public ContatoAdapter(@NonNull Context context, @NonNull ArrayList<Contato> objects) {
        super(context, 0, objects);
        this.contatos = objects;
        this.context = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;
        if(contatos !=null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_contato, parent, false);

            Contato contato = contatos.get(position);

            TextView nomeContato = view.findViewById(R.id.nomeContato);
            TextView emailContato = view.findViewById(R.id.emailContato);

            nomeContato.setText(contato.getNome());
            emailContato.setText(contato.getEmail());
        }

        return view;

    }
}
