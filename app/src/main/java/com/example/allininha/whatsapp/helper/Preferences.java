package com.example.allininha.whatsapp.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by allininha on 05/12/17.
 */

public class Preferences {

    private Context context;
    private SharedPreferences preferences;
    private String NOME_ARQUIVO = "whatsapp.preferencias";
    private int MODE = 0; //somente meu app tem acesso ao arquivo
    private SharedPreferences.Editor editor;

    private String CHAVE_IDENTIFICADOR = "identificadorUsuarioLogado";
    private String CHAVE_NOME = "nomeUSuario";


    public Preferences (Context contextParametro){
        context = contextParametro;
        preferences = context.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();
    }

    public void salvarDados (String identificadorUsuarioLogado, String nome){

        editor.putString(CHAVE_IDENTIFICADOR, identificadorUsuarioLogado);
        editor.putString(CHAVE_NOME, nome);
        editor.commit();
    }

    public String getIdentificado (){
        return preferences.getString(CHAVE_IDENTIFICADOR, null);
    }
    public String getNome (){
        return preferences.getString(CHAVE_NOME, null);
    }
}
