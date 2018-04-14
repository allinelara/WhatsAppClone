package com.example.allininha.whatsapp.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.allininha.whatsapp.R;
import com.example.allininha.whatsapp.adapter.TabAdapter;
import com.example.allininha.whatsapp.helper.Base64Custom;
import com.example.allininha.whatsapp.helper.ConfigFirebase;
import com.example.allininha.whatsapp.helper.Preferences;
import com.example.allininha.whatsapp.helper.SlidingTabLayout;
import com.example.allininha.whatsapp.model.Contato;
import com.example.allininha.whatsapp.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private Toolbar toolbar;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private String idContato;
    private FirebaseAuth usuarioFirebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewPage);
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.slTabLayout);
        toolbar.setTitle("WhatsApp");
        setSupportActionBar(toolbar);

        databaseReference = ConfigFirebase.getFirebase();
        usuarioFirebase = ConfigFirebase.getFirebaseAutenticacao();
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.corMarcador));
        //Confirgurar Adapter
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        slidingTabLayout.setViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sair:
                deslogar();
                return true;
            case R.id.addPessoa:
                abrirCadastroContato();
                return true;
            case R.id.configuracoes:
                return true;
            case R.id.pesquisa:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void deslogar(){
        firebaseAuth = ConfigFirebase.getFirebaseAutenticacao();
        firebaseAuth.signOut();
        Intent i = new Intent(MainActivity.this, Login2Activity.class);
        startActivity(i);
        finish();
    }
    public void abrirCadastroContato(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Contato");
        alertDialog.setMessage("Email do contato.");
        alertDialog.setCancelable(false);

        final EditText email = new EditText(MainActivity.this);
        alertDialog.setView(email);
        alertDialog.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String emailContato = email.getText().toString();

                if(emailContato.isEmpty()){
                    Toast.makeText(MainActivity.this, "Informe um email.", Toast.LENGTH_SHORT).show();
                }else{
                    idContato = Base64Custom.codificar(emailContato);

                    databaseReference = ConfigFirebase.getFirebase().child("usuarios").child(idContato);

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue()!=null){
                                Usuario contatoUsuario = dataSnapshot.getValue(Usuario.class);
                                String idUsuarioLogado = new Preferences(MainActivity.this).getIdentificado();
                                databaseReference = ConfigFirebase.getFirebase().child("contatos")
                                        .child(idUsuarioLogado)
                                        .child(idContato);

                                Contato contato = new Contato();
                                contato.setEmail(contatoUsuario.getEmail());
                                contato.setNome(contatoUsuario.getNome());
                                contato.setIdentificador(idContato);

                                databaseReference.setValue(contato);
                                Toast.makeText(MainActivity.this, "Contato adicionado com sucesso.", Toast.LENGTH_SHORT).show();


                            }else{
                                Toast.makeText(MainActivity.this, "Usuário nāo possui cadastro.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }
}
