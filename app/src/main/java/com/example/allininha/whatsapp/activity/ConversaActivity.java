package com.example.allininha.whatsapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.allininha.whatsapp.R;
import com.example.allininha.whatsapp.adapter.MensagemAdapter;
import com.example.allininha.whatsapp.helper.Base64Custom;
import com.example.allininha.whatsapp.helper.ConfigFirebase;
import com.example.allininha.whatsapp.helper.Preferences;
import com.example.allininha.whatsapp.model.Contato;
import com.example.allininha.whatsapp.model.Conversa;
import com.example.allininha.whatsapp.model.Mensagem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ConversaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listViewConversas;
    private EditText mensagemEdittext;
    private String nomeDestinatario;
    private String emailDestinatario;
    private ImageButton btnEnviar;
    private String idUserRemetente;
    private DatabaseReference databaseReference;
    private ArrayList<Mensagem> mensagens;
    private MensagemAdapter adapter;
    private String idDestinatario;
    private ValueEventListener valueEventListenerConversas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mensagemEdittext = (EditText) findViewById(R.id.mensagem);
        listViewConversas = (ListView) findViewById(R.id.listViewConversas);
        btnEnviar = (ImageButton) findViewById(R.id.bntEnviar);
        toolbar.setTitle("Usuário");
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        // dados do usuário logado
        Preferences preferencias = new Preferences(ConversaActivity.this);
        idUserRemetente = preferencias.getIdentificado();

        Bundle extra = getIntent().getExtras();

        if( extra != null ){
            nomeDestinatario = extra.getString("nome");
            String emailDestinatario = extra.getString("email");
            idDestinatario = Base64Custom.codificar( emailDestinatario );
        }

        // Configura toolbar
        toolbar.setTitle( nomeDestinatario );
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        // Monta listview e adapter
        mensagens = new ArrayList<>();
        adapter = new MensagemAdapter(
                ConversaActivity.this,
                mensagens
        );
        listViewConversas.setAdapter( adapter );

        // Recuperar mensagens do Firebase
        databaseReference = ConfigFirebase.getFirebase()
                .child("mensagens")
                .child( idUserRemetente )
                .child( idDestinatario );

        // Cria listener para mensagens
        valueEventListenerConversas = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Limpar mensagens
                mensagens.clear();

                // Recupera mensagens
                for ( DataSnapshot dados: dataSnapshot.getChildren() ){
                    Mensagem mensagem = dados.getValue( Mensagem.class );
                    mensagens.add( mensagem );
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        databaseReference.addValueEventListener( valueEventListenerConversas );


        // Enviar mensagem
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textoMensagem = mensagemEdittext.getText().toString();

                if( textoMensagem.isEmpty() ){
                    Toast.makeText(ConversaActivity.this, "Digite uma mensagem para enviar!", Toast.LENGTH_LONG).show();
                }else{

                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUsuario( idUserRemetente );
                    mensagem.setMensagem( textoMensagem );

                    boolean retornoMenREmetente = salvarMensagem(idUserRemetente, idDestinatario , mensagem );

                    if(!retornoMenREmetente){
                        Toast.makeText(ConversaActivity.this, "Problema ao salvar mensagem, tente novamente!", Toast.LENGTH_LONG).show();
                    }else {

                        boolean retornoMenDesti = salvarMensagem(idDestinatario, idUserRemetente, mensagem);
                        if(!retornoMenDesti){
                            Toast.makeText(ConversaActivity.this, "Problema ao enviar mensagem, tente novamente!", Toast.LENGTH_LONG).show();
                        }
                    }
                    Conversa conversa = new Conversa();
                    conversa.setIdUsuario(idDestinatario);
                    conversa.setNome(nomeDestinatario);
                    conversa.setMensagem(mensagem.getMensagem());

                    boolean salvarConversaDest= salvarConversa(idUserRemetente, idDestinatario, conversa);
                    if(!salvarConversaDest){
                        Toast.makeText(ConversaActivity.this, "Problema ao enviar mensagem, tente novamente!", Toast.LENGTH_LONG).show();
                    }else{
                        conversa = new Conversa();
                        conversa.setIdUsuario(idUserRemetente);
                        conversa.setNome(new Preferences(ConversaActivity.this).getNome());
                        conversa.setMensagem(mensagem.getMensagem());
                        boolean salvarConversaRemet = salvarConversa(idDestinatario, idUserRemetente, conversa);
                        if(!salvarConversaRemet){
                            Toast.makeText(ConversaActivity.this, "Problema ao enviar mensagem, tente novamente!", Toast.LENGTH_LONG).show();
                        }
                    }
                    mensagemEdittext.setText("");


                }

            }
        });



    }

    private boolean salvarMensagem(String idRemetente, String idDestinatario, Mensagem mensagem){
        try {

            databaseReference = ConfigFirebase.getFirebase().child("mensagens");

            databaseReference.child( idRemetente )
                    .child( idDestinatario )
                    .push()
                    .setValue( mensagem );

            return true;

        }catch ( Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListenerConversas);
    }

    private boolean salvarConversa(String remetente, String destinatario, Conversa conversa){
        try {
            databaseReference = ConfigFirebase.getFirebase().child("conversas");
            databaseReference.child(remetente)
                    .child(destinatario)
                    .setValue(conversa);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
