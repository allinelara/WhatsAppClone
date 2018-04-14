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
import com.example.allininha.whatsapp.helper.Preferences;
import com.example.allininha.whatsapp.model.Mensagem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by allininha on 11/12/17.
 */

public class MensagemAdapter extends ArrayAdapter<Mensagem> {
    private ArrayList<Mensagem> mensagems;
    private Context context;
    public MensagemAdapter(@NonNull Context context, @NonNull ArrayList<Mensagem> objects) {
        super(context, 0, objects);
        mensagems = objects;
        this.context = context;

    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;
        if(mensagems !=null){

            //recupera dados do usuario remetente
            Preferences preferences = new Preferences(context);
            String usuarioREmetente = preferences.getIdentificado();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.item_mensagem_esquerda, parent, false);

            Mensagem mensagem = mensagems.get(position);

            if (usuarioREmetente.equalsIgnoreCase(mensagem.getIdUsuario())) {
                view = inflater.inflate(R.layout.item_mensagem_direita, parent, false);

            }else{
                view = inflater.inflate(R.layout.item_mensagem_esquerda, parent, false);
            }

            TextView mensagemText = view.findViewById(R.id.idMensagem);

            mensagemText.setText(mensagem.getMensagem());
        }

        return view;

    }

}
