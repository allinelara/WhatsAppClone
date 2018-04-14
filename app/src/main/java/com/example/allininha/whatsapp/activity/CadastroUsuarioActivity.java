package com.example.allininha.whatsapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.allininha.whatsapp.R;
import com.example.allininha.whatsapp.helper.Base64Custom;
import com.example.allininha.whatsapp.helper.ConfigFirebase;
import com.example.allininha.whatsapp.helper.Preferences;
import com.example.allininha.whatsapp.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText nome, email, senha;
    private Button btnCadastrar;
    private Usuario usuario = new Usuario();
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        email = (EditText) findViewById(R.id.email);
        senha = (EditText) findViewById(R.id.senha);
        nome = (EditText) findViewById(R.id.nome);
        btnCadastrar = (Button) findViewById(R.id.cadastrarBtn);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario.setNome(nome.getText().toString());
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());
                if(!usuario.getEmail().equalsIgnoreCase("") && !usuario.getSenha().equalsIgnoreCase(""))
                    cadastrarUsuario();
            }
        });
    }

    private void cadastrarUsuario(){
        firebaseAuth = ConfigFirebase.getFirebaseAutenticacao();
        firebaseAuth.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if( task.isSuccessful() ){
                    Toast.makeText(CadastroUsuarioActivity.this, "Sucesso ao cadastrar usuário", Toast.LENGTH_LONG ).show();
                    String usuarioId = Base64Custom.codificar(usuario.getEmail());
                    new Preferences(CadastroUsuarioActivity.this).salvarDados(usuarioId, usuario.getNome());
                    usuario.setId(usuarioId);
                    usuario.salvar();

                    abrirLoginUssuario();
                }else {
                    String erroExcecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erroExcecao = "Digite uma senha mais forte, com no mínimo 6 caracteres.";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "Email inválido.";

                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExcecao = "Usuário já cadastrado no app.";

                    } catch (Exception e) {
                        erroExcecao = "Erro ao efetuar o cadastro.";
                    }
                    Toast.makeText(CadastroUsuarioActivity.this, erroExcecao, Toast.LENGTH_LONG ).show();
                }
            }
        });
    }

    public void abrirLoginUssuario(){
        Intent i = new Intent(CadastroUsuarioActivity.this, Login2Activity.class);
        startActivity(i);
        finish();
    }

}