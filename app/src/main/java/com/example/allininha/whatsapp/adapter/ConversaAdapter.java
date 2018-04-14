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
import com.example.allininha.whatsapp.model.Conversa;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by allininha on 11/12/17.
 */

public class ConversaAdapter extends ArrayAdapter<Conversa> {
    private ArrayList<Conversa> conversas;
    private Context context;

    public ConversaAdapter(@NonNull Context context, @NonNull ArrayList<Conversa> objects) {
        super(context, 0, objects);
        this.conversas = objects;
        this.context = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;
        if (conversas != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_contato, parent, false);

            Conversa conversa = conversas.get(position);

            TextView nomeContato = view.findViewById(R.id.nomeContato);
            TextView emailContato = view.findViewById(R.id.emailContato);

            nomeContato.setText(conversa.getNome());
            emailContato.setText(conversa.getMensagem());
        }

        return view;

    }
}