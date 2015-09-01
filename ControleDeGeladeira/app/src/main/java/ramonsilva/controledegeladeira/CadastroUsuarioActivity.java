package ramonsilva.controledegeladeira;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CadastroUsuarioActivity extends ActionBarActivity implements View.OnClickListener{

    private Button botaoSalvarUsuario = null;
    private String nome = null;
    private String senha = null;
    private RadioGroup radioGroupUsuarios = null;
    private boolean cadastroUsuario = true;
    private Usuario usuario;
    public static  List<Usuario> listaDeUsuariosRecebidos;
    ArrayList<Usuario> listaDeUsuarios;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        botaoSalvarUsuario = (Button)findViewById(R.id.idBtnSalvarUsuario);

        listaDeUsuarios = (ArrayList)listaDeUsuariosRecebidos;
        listaDeUsuariosRecebidos = null;

        radioGroupUsuarios = (RadioGroup)findViewById(R.id.idRadioGroupCadastroUsuario);
        radioGroupUsuarios.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == R.id.idRadBtnCadastroAmigo){
                    cadastroUsuario = false;
                }

            }
        });

        /*EditText txtNome = (EditText)findViewById(R.id.idEditTextNomeUsuario);
        nome = txtNome.getText().toString();

        EditText txtSenha = (EditText)findViewById(R.id.idEditTextSenhaUsuario);
        senha = txtSenha.getText().toString();*/

        botaoSalvarUsuario.setOnClickListener(this);



    }


    @Override
    public void onClick(View v) {

        EditText txtNome = (EditText)findViewById(R.id.idEditTextNomeUsuario);
        nome = txtNome.getText().toString();

        EditText txtSenha = (EditText)findViewById(R.id.idEditTextSenhaUsuario);
        senha = txtSenha.getText().toString();

        if(!cadastroUsuario){

            usuario = new Usuario(nome,senha);
            listaDeUsuarios.add(usuario);


            SharedPreferences mPrefs = getSharedPreferences("prefListaUsuarios", MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = mPrefs.edit();

            JSONArray array = new JSONArray();
            JSONObject obj;

            for(Usuario usuario : listaDeUsuarios ){
                obj = new JSONObject();
                try{
                    obj.put("nomeUsuario", usuario.getNome());
                    obj.put("senhaUsuario", usuario.getSenha());

                    array.put(obj);
                }catch(JSONException je){
                    je.printStackTrace();
                }
            }
            String arrayStr = array.toString();
            prefsEditor.putString("usuarios", arrayStr).commit();

            Intent MainIntentActivity = new Intent(v.getContext(), MainActivity.class);
            /*Bundle params = new Bundle();

            String resposta = nome;
            params.putString("Nome", resposta);
            MainIntentActivity.putExtras(params);*/

            startActivity(MainIntentActivity);
        }else {
            Toast.makeText(getApplicationContext(), "Nao pode cadastrar esse usuario", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cadastro_usuario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
