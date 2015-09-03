package ramonsilva.controledegeladeira;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;


public class CadastroUsuarioActivity extends ActionBarActivity implements View.OnClickListener {

    private Button botaoSalvarUsuario = null;
    private String nome = null;
    private String senha = null;
    private RadioGroup radioGroupUsuarios = null;
    private boolean cadastroUsuario = true;
    private Usuario usuario;
    public static List<Usuario> listaDeUsuariosRecebidos;
    ArrayList<Usuario> listaDeUsuarios;
    private SharedPreferences idUsuario;
    private SharedPreferences idListaAmigos;
    private SharedPreferences.Editor prefsEditor;
    private SharedPreferences.Editor prefsEditorListaAmigos;
    private RadioButton rdoBtnSalvarUsuario;
    private RadioButton rdoBtnSalvarAmigos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);
        //TODO: Verificar o erro ao cadastrar amigo pela primeira vez;

        rdoBtnSalvarUsuario = (RadioButton) findViewById(R.id.idRadBtnUsuario);
        rdoBtnSalvarAmigos = (RadioButton) findViewById(R.id.idRadBtnCadastroAmigo);
        idUsuario = getSharedPreferences("Usuario", MODE_PRIVATE);
        prefsEditor = idUsuario.edit();
        idListaAmigos = getSharedPreferences("ListaAmigos", MODE_PRIVATE);
        prefsEditorListaAmigos = idListaAmigos.edit();


        if (!idUsuario.getString("idUsuario", "Inexistente").equals("Inexistente")) {
            rdoBtnSalvarUsuario.setEnabled(false);
            rdoBtnSalvarAmigos.setChecked(true);
        }

        botaoSalvarUsuario = (Button) findViewById(R.id.idBtnSalvarUsuario);

        listaDeUsuarios = (ArrayList) listaDeUsuariosRecebidos;
        listaDeUsuariosRecebidos = null;

        radioGroupUsuarios = (RadioGroup) findViewById(R.id.idRadioGroupCadastroUsuario);
        radioGroupUsuarios.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.idRadBtnCadastroAmigo) {
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

        EditText txtNome = (EditText) findViewById(R.id.idEditTextNomeUsuario);
        nome = txtNome.getText().toString();

        EditText txtSenha = (EditText) findViewById(R.id.idEditTextSenhaUsuario);
        senha = txtSenha.getText().toString();

        final Intent i = new Intent(getApplicationContext(), MainActivity.class);


        if (rdoBtnSalvarUsuario.isChecked()) {
            try {


                ParseQuery<ParseObject> queryUsuario = ParseQuery.getQuery("Usuario");
                queryUsuario.whereEqualTo("nome", nome);
                queryUsuario.findInBackground(new FindCallback<ParseObject>() {
                                                  @Override
                                                  public void done(List<ParseObject> list, ParseException e) {
                                                      if (e != null) {

                                                          Toast.makeText(getApplicationContext(), "Um erro ocorreu", Toast.LENGTH_SHORT).show();
                                                          e.printStackTrace();

                                                          //Toast.makeText(getApplicationContext(), "Usuario ja cadastrado", Toast.LENGTH_SHORT).show();

                                                      } else if (list.isEmpty()) {
                                                          //Usuario não cadastrado
                                                          final ParseObject usuarioCadastrado = new ParseObject("Usuario");

                                                          usuarioCadastrado.put("nome", nome);
                                                          usuarioCadastrado.put("senha", senha);
                                                          //usuarioCadastrado.saveInBackground();
                                                          usuarioCadastrado.saveInBackground(
                                                                  new SaveCallback() {
                                                                      @Override
                                                                      public void done(ParseException e) {
                                                                          prefsEditor.putString("idUsuario", usuarioCadastrado.getObjectId());
                                                                          prefsEditor.commit();
                                                                          Toast.makeText(getApplicationContext(), "Usuario cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                                                                          rdoBtnSalvarUsuario.setEnabled(false);
                                                                          rdoBtnSalvarUsuario.setChecked(false);
                                                                          cadastroUsuario = false;
                                                                      }
                                                                  }

                                                          );
                                                      } else {
                                                          //Usuario já cadastrado
                                                          String idUsuario = null;
                                                          for (ParseObject objeto : list) {
                                                              idUsuario = objeto.getObjectId();
                                                          }
                                                          prefsEditor.putString("idUsuario", idUsuario).commit();
                                                          Toast.makeText(getApplicationContext(), "Usuario ja cadastrado", Toast.LENGTH_SHORT).show();
                                                      }
                                                      //rdoBtnSalvarUsuario.setEnabled(false);

                                                  }
                                              }
                );

                txtNome.setText("");
                txtSenha.setText("");


            } catch (Exception ex) {
                ex.getMessage();
                Toast.makeText(getApplicationContext(), "Nao foi possivel cadastrar esse usuario", Toast.LENGTH_SHORT).show();
            }

        } else {
            usuario = new Usuario(nome, senha);
            listaDeUsuarios.add(usuario);

            //Codigo Anterior
            /*
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
            */
            /*Bundle params = new Bundle();

            String resposta = nome;
            params.putString("Nome", resposta);
            MainIntentActivity.putExtras(params);

            //startActivity(MainIntentActivity);*/

            //Inicio da verificação de autoziação de usuario e cadastro de lista
            //Passo 1 - Verificar se o user e senha batem com os da lista de usuario
            //Passo 2 - Se existirem, inserir no prefs acima
            /*Passo 2.1 - Inserir a lista em json na tabela ListaDeAmigos no parse utilizando o json
            como Dados e o id do usuario cadastrado obtido acima, como IdUsuario
             */

            //Passo 1:
            final ParseQuery<ParseObject> queryAmigo = ParseQuery.getQuery("Usuario");
            queryAmigo.whereEqualTo("nome", nome);
            queryAmigo.whereEqualTo("senha", senha);

            queryAmigo.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(final List<ParseObject> list, ParseException e) {
                    if (e != null) {
                        Log.i("CadastroAmigo", "Ocorreu um erro no cadastro de amigo " + e.getMessage());
                        e.printStackTrace();
                    } else if (list.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Amigo não encontrado", Toast.LENGTH_SHORT).show();

                    } else {
                        //Encontrou o amigo
                        //Passo 2:
                        SharedPreferences mPrefs = getSharedPreferences("prefListaUsuarios", MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = mPrefs.edit();

                        JSONArray array = new JSONArray();
                        JSONObject obj;

                        for (Usuario usuario : listaDeUsuarios) {
                            obj = new JSONObject();
                            try {
                                obj.put("nomeUsuario", usuario.getNome());
                                obj.put("senhaUsuario", usuario.getSenha());

                                array.put(obj);
                            } catch (JSONException je) {
                                je.printStackTrace();
                            }
                        }
                        final String arrayStr = array.toString();
                        prefsEditor.putString("usuarios", arrayStr).commit();

                        //Passo 2.1.1 : Verificar se existe lista de amigos

                        if (idListaAmigos.getString("idListaAmigo", "ListaInexistente").equals("ListaInexistente")) {

                            final ParseObject listaDeAmigos = new ParseObject("ListaDeAmigos");

                            listaDeAmigos.put("IdUsuario",  idUsuario.getString("idUsuario","Inexistente"));// idUsuario.getString("IdUsuario", "Inexistente"));

                            listaDeAmigos.put("Dados", arrayStr);
                            listaDeAmigos.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        prefsEditorListaAmigos.putString("idListaAmigo", listaDeAmigos.getObjectId());
                                        Toast.makeText(getApplicationContext(), "Amigo cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                                        startActivity(i);

                                    }
                                }
                            });
                        } else {

                            //Lista Existe dar update

                            String listObjectId = null;
                            for (ParseObject objetos : list) {
                                listObjectId = objetos.getObjectId();
                            }

                            queryAmigo.getInBackground(listObjectId, new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject parseObject, ParseException e) {
                                    if (e == null) {
                                        parseObject.put("Dados", arrayStr);
                                        parseObject.saveInBackground();
                                        Toast.makeText(getApplicationContext(), "Lista de amigos atualizada com sucesso", Toast.LENGTH_SHORT).show();
                                        startActivity(i);
                                    }
                                }
                            });

                        }


                    }
                }
            });


        }
        txtNome.setText("");
        txtSenha.setText("");

       //Intent i = new Intent(v.getContext(), MainActivity.class);
        //startActivity(i);
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
