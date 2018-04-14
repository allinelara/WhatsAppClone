package com.example.allininha.whatsapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.allininha.whatsapp.R;
import com.example.allininha.whatsapp.helper.Base64Custom;
import com.example.allininha.whatsapp.helper.ConfigFirebase;
import com.example.allininha.whatsapp.helper.Preferences;
import com.example.allininha.whatsapp.model.Conversa;
import com.example.allininha.whatsapp.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class Login2Activity extends AppCompatActivity {

    private EditText email, senha;
    private TextView cadastarUser;
    private Button logar;
    private DatabaseReference databaseReference;
    private Usuario usuario = new Usuario();
    private FirebaseAuth firebaseAuth;
    private ValueEventListener valueEventListenerUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        email = (EditText) findViewById(R.id.email);
        senha = (EditText) findViewById(R.id.senha);
        cadastarUser = (TextView) findViewById(R.id.textoCadastrar);
        logar = (Button) findViewById(R.id.btnLogar);

        databaseReference = ConfigFirebase.getFirebase();

        verificarUsuarioLogado();


       // databaseReference.child("pontos").setValue(900);

        cadastarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login2Activity.this, CadastroUsuarioActivity.class);
                startActivity(i);
            }
        });

        logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario.setSenha(senha.getText().toString());
                usuario.setEmail(email.getText().toString());
                if(!usuario.getSenha().equalsIgnoreCase("")&& !usuario.getEmail().equalsIgnoreCase(""))
                    validarLogin();
            }
        });

    }

    private void validarLogin(){
        firebaseAuth = ConfigFirebase.getFirebaseAutenticacao();
        firebaseAuth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            final String emailCodificado = Base64Custom.codificar(email.getText().toString());

                            databaseReference = ConfigFirebase.getFirebase().child("usuarios").child(emailCodificado);
                            valueEventListenerUsuario = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Usuario usuarioRecup = dataSnapshot.getValue(Usuario.class);
                                    new Preferences(Login2Activity.this).salvarDados(emailCodificado, usuarioRecup.getNome());

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            };
                            databaseReference.addListenerForSingleValueEvent(valueEventListenerUsuario);
                            Toast.makeText(Login2Activity.this, "Sucesso!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Login2Activity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }else
                            Toast.makeText(Login2Activity.this, "Erro ao logar!", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void verificarUsuarioLogado(){
        firebaseAuth = ConfigFirebase.getFirebaseAutenticacao();
        if (firebaseAuth.getCurrentUser()!=null){
            Intent i = new Intent(Login2Activity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

    }
}
