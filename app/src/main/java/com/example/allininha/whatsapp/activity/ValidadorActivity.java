package com.example.allininha.whatsapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.allininha.whatsapp.R;
import com.example.allininha.whatsapp.helper.Preferences;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.HashMap;

public class ValidadorActivity extends AppCompatActivity {

    private EditText codigo;
    private Button btnValidar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validador);

        codigo = (EditText) findViewById(R.id.codigo);
        btnValidar = (Button) findViewById(R.id.validarBtn);

        SimpleMaskFormatter simpleMaskDdd = new SimpleMaskFormatter("NNNN");
        MaskTextWatcher maskTextWatcherTDdd = new MaskTextWatcher(codigo, simpleMaskDdd);
        codigo.addTextChangedListener(maskTextWatcherTDdd);

        btnValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Recuperar dados das preferencias do usuario
              //  Preferences preferences = new Preferences(ValidadorActivity.this);
               // HashMap<String, String> dadosUsuario = preferences.getDadosUsuarios();
               // String token = dadosUsuario.get("token");
                String tokenDigitado = codigo.getText().toString();

               // if(token.equalsIgnoreCase(tokenDigitado)){
              //      Toast.makeText(ValidadorActivity.this, "Validado", Toast.LENGTH_SHORT).show();
              //  }else{
              //      Toast.makeText(ValidadorActivity.this, " nao Validado", Toast.LENGTH_SHORT).show();
//
               // }
            }
        });
    }
}
