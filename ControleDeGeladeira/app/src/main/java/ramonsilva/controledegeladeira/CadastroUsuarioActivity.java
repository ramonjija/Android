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
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CadastroUsuarioActivity extends ActionBarActivity implements View.OnClickListener {

    private Button botaoSalvarUsuario = null;
    private Button botaoVoltar = null;
    private String nome = null;
    private String senha = null;
    private RadioGroup radioGroupUsuarios = null;
    private boolean cadastroUsuario = true;
    private boolean logarUsuario = false;
    private Usuario usuario;
    public static List<Usuario> listaDeUsuariosRecebidos;
    ArrayList<Usuario> listaDeUsuarios;
    private SharedPreferences idUsuario;
    private SharedPreferences idListaAmigos;
    private SharedPreferences.Editor prefsEditor;
    private SharedPreferences.Editor prefsEditorListaAmigos;
    private RadioButton rdoBtnSalvarUsuario;
    private RadioButton rdoBtnSalvarAmigos;
    private RadioButton rdoBtnLogarUsuario;
    private String mensagem = "Aguarde...";
    private String idUsuarioLogado = null;
    private boolean cadastrouAmigo = false;


    private void PassarParametroEiniciarActivity(Intent i){
        i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("ParseIniciado","sim");
        finish();
        startActivity(i);
    }

    private boolean VerificarSeAmigoEstaCadastrado(List<Usuario> listaDeAmigos, String nome){
        boolean cadastrado = false;

        if(listaDeAmigos != null)
        {
            if (!listaDeAmigos.isEmpty())
            {

                for (Usuario amigo : listaDeAmigos)
                {
                    if ((amigo.getNome()).equals(nome))
                    {
                        cadastrado = true;
                        break;
                    }
                }

            }
        }
        return cadastrado;
    }

    private void RecuperarListaAmigosLogado(String idUsuarioLogado){
        SharedPreferences mPrefs = getSharedPreferences("prefListaUsuarios", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();

        ParseQuery<ParseObject> queryAmigos = ParseQuery.getQuery("ListaDeAmigos");
        queryAmigos.whereEqualTo("IdUsuario", idUsuarioLogado);
        List<ParseObject> objUsuario = null;
        String arrayStrAmigos = null;
        try {
            objUsuario = queryAmigos.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for(ParseObject obj : objUsuario){
           //prefsEditorListaAmigos.putString("Id") = obj.getObjectId();
            prefsEditorListaAmigos.putString("idListaAmigo", obj.getObjectId());
            prefsEditorListaAmigos.commit();
            prefsEditor.putString("usuarios", obj.get("Dados").toString()).commit();
        }


    }

    protected void RecuperarListaDeAlimentos(String idUsuarioLogado){
        //SharedPreferences de Alimentos
        SharedPreferences mPrefs = getSharedPreferences("prefListaAlimentos", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditorAlimentos = mPrefs.edit();


        ParseQuery<ParseObject> queryListaAlimento = ParseQuery.getQuery("ListaDeAlimentos");
        queryListaAlimento.whereEqualTo("IdUsuario",idUsuarioLogado);
        String idAlimentosListaAmigo = null;
        List<ParseObject> objLista = null;
        try {
            objLista = queryListaAlimento.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for(ParseObject obj : objLista)
        {
            prefsEditorAlimentos.putString("alimentos", obj.getString("Alimentos")).commit();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);
        //TODO: Verificar o erro ao cadastrar amigo pela primeira vez;

        rdoBtnSalvarUsuario = (RadioButton) findViewById(R.id.idRadBtnUsuario);
        rdoBtnSalvarAmigos = (RadioButton) findViewById(R.id.idRadBtnCadastroAmigo);
        rdoBtnLogarUsuario = (RadioButton) findViewById(R.id.idRadBtnLogar);
        idUsuario = getSharedPreferences("Usuario", MODE_PRIVATE);
        prefsEditor = idUsuario.edit();
        idListaAmigos = getSharedPreferences("ListaAmigos", MODE_PRIVATE);
        prefsEditorListaAmigos = idListaAmigos.edit();

        botaoVoltar = (Button) findViewById(R.id.idBtnVoltar);

        if (!idUsuario.getString("idUsuario", "Inexistente").equals("Inexistente")) {
            rdoBtnSalvarUsuario.setEnabled(false);
            rdoBtnLogarUsuario.setEnabled(false);
            rdoBtnSalvarAmigos.setEnabled(true);
            rdoBtnSalvarAmigos.setChecked(true);
            botaoVoltar.setVisibility(View.VISIBLE);
            botaoVoltar.setClickable(true);
            botaoVoltar.setOnClickListener(this);
        }else{
            rdoBtnSalvarAmigos.setChecked(false);
            rdoBtnSalvarAmigos.setEnabled(false);
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
                    logarUsuario = false;
                } else if (checkedId == R.id.idRadBtnLogar) {
                    cadastroUsuario = false;
                    logarUsuario = true;
                }

            }
        });

        botaoSalvarUsuario.setOnClickListener(this);

    }


    @Override
    public void onBackPressed() {

        //Intent intent = new Intent(this, MainActivity.class);
        //startActivity(intent);
        if(!rdoBtnLogarUsuario.isEnabled() && !rdoBtnSalvarUsuario.isEnabled()){
                Intent i = new Intent(this, MainActivity.class);
                finish();
                startActivity(i);
                super.onBackPressed();

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.idBtnVoltar:
                finish();
                Intent VoltarMain = new Intent(this,MainActivity.class);
                PassarParametroEiniciarActivity(VoltarMain);
                break;
            case R.id.idBtnCadastroUsuario:


                Toast.makeText(getApplicationContext(), "Aguarde...",Toast.LENGTH_SHORT).show();

                EditText txtNome = (EditText) findViewById(R.id.idEditTextNomeUsuario);
                EditText txtSenha = (EditText) findViewById(R.id.idEditTextSenhaUsuario);
                nome = txtNome.getText().toString();
                senha = txtSenha.getText().toString();
                if(!nome.equals("") && !senha.equals("")) {
                    mensagem = "Aguarde...";
                    final Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    if (rdoBtnSalvarUsuario.isChecked()) {
                        try {
                            ParseQuery<ParseObject> queryUsuario = ParseQuery.getQuery("Usuario");
                            queryUsuario.whereEqualTo("nome", nome);
                            queryUsuario.findInBackground(new FindCallback<ParseObject>() {
                                                              @Override
                                                              public void done(List<ParseObject> list, ParseException e) {
                                                                  if (e != null) {
                                                                      //TODO: Retornar para a main após cadastro bem sucedido
                                                                      Toast.makeText(getApplicationContext(), "Um erro ocorreu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                                                                      rdoBtnLogarUsuario.setEnabled(false);
                                                                                      rdoBtnLogarUsuario.setChecked(false);
                                                                                      rdoBtnSalvarAmigos.setChecked(true);
                                                                                      cadastroUsuario = false;
                                                                                      PassarParametroEiniciarActivity(i);
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

                    } else if (rdoBtnSalvarAmigos.isChecked()) {
                        usuario = new Usuario(nome, senha);
                        if (listaDeUsuarios == null) {
                            listaDeUsuarios = new ArrayList<Usuario>();
                        }
                        if (!VerificarSeAmigoEstaCadastrado(listaDeUsuarios, nome)) {
                            listaDeUsuarios.add(usuario);

                            //Passo 1:
                            final ParseQuery<ParseObject> queryAmigo = ParseQuery.getQuery("Usuario");
                            queryAmigo.whereEqualTo("nome", nome);
                            queryAmigo.whereEqualTo("senha", senha);

                            queryAmigo.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(final List<ParseObject> list, ParseException e) {
                                    if (e != null) {
                                        Log.i("CadastroAmigo", "Ocorreu um erro na adição de amigo " + e.getMessage());
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
                                        final String arrayStr = array.toString();//VERIFICAR
                                        prefsEditor.putString("usuarios", arrayStr).commit();

                                        //Passo 2.1.1 : Verificar se existe lista de amigos

                                        if (idListaAmigos.getString("idListaAmigo", "ListaInexistente").equals("ListaInexistente")) {

                                            final ParseObject listaDeAmigos = new ParseObject("ListaDeAmigos");

                                            listaDeAmigos.put("IdUsuario", idUsuario.getString("idUsuario", "Inexistente"));// idUsuario.getString("IdUsuario", "Inexistente"));
                                            listaDeAmigos.put("Dados", arrayStr);
                                            listaDeAmigos.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if (e == null) {
                                                        prefsEditorListaAmigos.putString("idListaAmigo", listaDeAmigos.getObjectId());
                                                        prefsEditorListaAmigos.commit();
                                                        Toast.makeText(getApplicationContext(), "Amigo adicionado com sucesso", Toast.LENGTH_SHORT).show();
                                                        PassarParametroEiniciarActivity(i);

                                                    }
                                                }
                                            });
                                        } else {

                                            //Lista Existe dar update
                            /*
                            String listObjectId = null;
                            for (ParseObject objetos : list) {
                                listObjectId = objetos.getObjectId();
                            }
                            */
                                            String idListaAmigo = idListaAmigos.getString("idListaAmigo", "Inexistente");//TODO: Melhorar a forma de verificação, utiliza 2x a msm função
                                            ParseQuery<ParseObject> queryAmigoUpdate = ParseQuery.getQuery("ListaDeAmigos");
                                            queryAmigoUpdate.getInBackground(idListaAmigo, new GetCallback<ParseObject>() {
                                                @Override
                                                public void done(ParseObject parseObject, ParseException e) {
                                                    if (e == null) {
                                                        parseObject.put("Dados", arrayStr);
                                                        parseObject.saveInBackground();
                                                        Toast.makeText(getApplicationContext(), "Lista de amigos atualizada com sucesso", Toast.LENGTH_SHORT).show();
                                                        PassarParametroEiniciarActivity(i);
                                                    }
                                                }
                                            });

                                        }


                                    }
                                }
                            });


                        } else {
                            mensagem = "Amigo já adicionado!";
                        }
                    } else if (rdoBtnLogarUsuario.isChecked()) {
                        ParseQuery<ParseObject> queryLogin = ParseQuery.getQuery("Usuario");
                        queryLogin.whereEqualTo("nome", nome);
                        queryLogin.whereEqualTo("senha", senha);
                        List<ParseObject> objUsuario = null;
                        try {
                            objUsuario = queryLogin.find();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        for (ParseObject obj : objUsuario) {
                            idUsuarioLogado = obj.getObjectId();

                        }
                        if (idUsuarioLogado != null) {
                            String idListaAlimento = null;
                            prefsEditor.putString("idUsuario", idUsuarioLogado);
                            prefsEditor.commit();
                            mensagem = "Logado! ;)";

                            RecuperarListaAmigosLogado(idUsuarioLogado);
                            RecuperarListaDeAlimentos(idUsuarioLogado);

                            rdoBtnSalvarUsuario.setEnabled(false);
                            rdoBtnSalvarUsuario.setChecked(false);
                            rdoBtnLogarUsuario.setEnabled(false);
                            rdoBtnLogarUsuario.setChecked(false);
                            rdoBtnSalvarAmigos.setEnabled(true);
                            rdoBtnSalvarAmigos.setChecked(true);
                            cadastroUsuario = false;
                            logarUsuario = false;
                            PassarParametroEiniciarActivity(i);
                            //objUsuario = null;
                        } else {
                            mensagem = "Nome ou Senha incorretos";
                        }
                    }
                }else{
                    mensagem = "Nome e Senha devem ser preenchidos";
                }
                txtNome.setText("");
                txtSenha.setText("");
                Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_SHORT).show();
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
