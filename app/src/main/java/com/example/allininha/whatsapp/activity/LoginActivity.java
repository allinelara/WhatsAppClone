package com.example.allininha.whatsapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.allininha.whatsapp.Manifest;
import com.example.allininha.whatsapp.R;
import com.example.allininha.whatsapp.helper.Permissao;
import com.example.allininha.whatsapp.helper.Preferences;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.HashMap;
import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    private EditText telefone, ddi, ddd, nome;
    private Button btnCadastrar;
    private String[] permissoesNecessarios = new String[]{
            android.Manifest.permission.SEND_SMS,
            android.Manifest.permission.INTERNET
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Permissao.validaPermissoes(1, LoginActivity.this, permissoesNecessarios);

        telefone = (EditText) findViewById(R.id.telefone);
        ddd = (EditText) findViewById(R.id.ddd);
        ddi = (EditText) findViewById(R.id.ddi);
        nome = (EditText) findViewById(R.id.nome);
        btnCadastrar = (Button) findViewById(R.id.cadastrarBtn);

        SimpleMaskFormatter simpleMaskTel = new SimpleMaskFormatter("NNNN-NNNNN");
        MaskTextWatcher maskTextWatcherTel = new MaskTextWatcher(telefone, simpleMaskTel);
        telefone.addTextChangedListener(maskTextWatcherTel);

        SimpleMaskFormatter simpleMaskDdi = new SimpleMaskFormatter("+NN");
        MaskTextWatcher maskTextWatcherTDDi = new MaskTextWatcher(ddi, simpleMaskDdi);
        ddi.addTextChangedListener(maskTextWatcherTDDi);

        SimpleMaskFormatter simpleMaskDdd = new SimpleMaskFormatter("NN");
        MaskTextWatcher maskTextWatcherTDdd = new MaskTextWatcher(ddd, simpleMaskDdd);
        ddd.addTextChangedListener(maskTextWatcherTDdd);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomeUsuario = nome.getText().toString();
                String telefoneCompleto = ddi.getText().toString()+ddd.getText().toString()+telefone.getText().toString();

                String telefoneSemFormatacao = telefoneCompleto.replace("+", "");
                telefoneSemFormatacao = telefoneCompleto.replace("-", "");

                //Gerar Token
                Random random = new Random();
                int numRandom = random.nextInt((9999 - 1000)+1000);

                String token = String.valueOf(numRandom);

                Log.i("token", token);

                Preferences preferences = new Preferences(LoginActivity.this);
              //  preferences.salvarUsuarioPreferencias(nomeUsuario, telefoneSemFormatacao, token);

                //15207952713
                //Envio de SMS
                boolean enviadoSMS = enviaSMS(telefoneSemFormatacao, "Token: "+ token);

                if(enviadoSMS){
                    Intent i = new Intent(LoginActivity.this, ValidadorActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "Problema ao enviar o SMS, tente novamente", Toast.LENGTH_SHORT).show();
                }
               // HashMap<String, String> dadosUsuarios = preferences.getDadosUsuarios();

               // Log.i("token", dadosUsuarios.get("token"));


            }
        });
    }

    private boolean enviaSMS(String telefone, String mensagem){
        try{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(telefone, null, mensagem, null, null);
            return true;
        }catch (Exception ee){
            return false;
        }

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissoes, int[]grantResults){
        super.onRequestPermissionsResult(requestCode, permissoes, grantResults);
        for(int result:grantResults){
            if(result== PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }
    }
    private void alertaValidacaoPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissoes negadas");
        builder.setMessage("Para utilizar este app é necessário aceitar as permissoes.");

        builder.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
