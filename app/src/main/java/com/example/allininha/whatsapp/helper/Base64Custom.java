package com.example.allininha.whatsapp.helper;

import android.util.Base64;

/**
 * Created by allininha on 08/12/17.
 */

public class Base64Custom {

    public static String codificar(String texto){
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)","");
    }

    public static String decodificar(String textoCodificado){
        return new String(Base64.decode(textoCodificado, Base64.DEFAULT));
    }
}
