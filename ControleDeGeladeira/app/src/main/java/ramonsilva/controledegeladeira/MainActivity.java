package ramonsilva.controledegeladeira;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.os.Handler;




public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    private List<Alimentos> alimentos = new ArrayList<Alimentos>();
    private AlimentoAdapter adapter = null;
    private AmigosAdapter adapterAmigos = null;
    private Alimentos alimentoSelecionado = null;
    private int qnt = 0;
    private int posicaoDoALimento = 0;
    private Alimentos AlimentoAnterior = null;
    private AdapterView adaptadorPai = null;
    private View viewAl = null;
    private Button botaoMenos = null;
    private Button botaoExcluir = null;
    private Button botaoMais = null;
    private String nomeAlimento = null;
    private String qntAlimento = null;
    private Button botaoSalvarLista = null;

    private Button botaosalvarAlimento = null;

    private Button botaoObterListaAmigo = null;

    private List<Usuario> amigos = new ArrayList<Usuario>();
    private Usuario amigoSelecionado = null;
    private Usuario usuario;

    private Button botaoCadastrarUsuario = null;

    private Context contexto = null;

    private Button botaoExcluirAmigo = null;

    private RadioGroup rdoGroupModal = null;
    private RadioButton rdoBtnKg = null;
    private RadioButton rdoBtnG = null;
    private RadioButton rdoBtnLitro = null;
    private RadioButton rdoBtnUnidade = null;
    private String tipo = "Unidade(s)";

    private ListView lista = null;

    private String idUsuario = null;

    private TextView txtViewUsuario = null;
    private TextView txtViewIdUsuario = null;

    private boolean VerificarConexao(){
        boolean conectado = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getActiveNetworkInfo() != null) {
            if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()){

                conectado = true;
            }else if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()){
                conectado = true;
            }
        }
        return conectado;
    }

    protected static JSONArray ObtemListaAlimentosUsuario(String idListaUsuario) throws ParseException, JSONException {
        JSONArray ListaAlimentosObtidos = null;//
        ParseQuery<ParseObject> queryListaAlimento = ParseQuery.getQuery("ListaDeAlimentos");
        queryListaAlimento.whereEqualTo("objectId",idListaUsuario);
        List<ParseObject> objLista = queryListaAlimento.find();
        for(ParseObject obj : objLista)
        {
            ListaAlimentosObtidos = new JSONArray((String)obj.get("Alimentos"));
        }
        return ListaAlimentosObtidos;
    }

    protected void SalvarLista(final boolean mostraMsg){


            SharedPreferences mPrefs = getSharedPreferences("prefListaAlimentos", MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            SharedPreferences idUsuarioPref = getSharedPreferences("Usuario", MODE_PRIVATE);
            SharedPreferences.Editor prefEditorUser = idUsuarioPref.edit();

            JSONArray array = new JSONArray();
            JSONObject obj;
            final String[] objId = new String[1];
            final ParseObject alimentosParse = new ParseObject("ListaDeAlimentos");
            final ParseQuery<ParseObject> query;

            for(Alimentos item : alimentos){
                obj = new JSONObject();
                try {
                    obj.put("nome", item.getNome());
                    obj.put("quantidade", item.getQuantidade());
                    obj.put("tipo", item.getTipo());
                    array.put(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            final String arrayStr = array.toString();

            final String idUsuario = idUsuarioPref.getString("idUsuario", "Inexistente");
            //idUsuario = idUsuarioPref.getString("idUsuario","Inexistente");

            //Parse

            query = ParseQuery.getQuery("ListaDeAlimentos");
            query.whereEqualTo("IdUsuario", idUsuario);

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    if (e != null) {
                        e.printStackTrace();
                    } else if (list.isEmpty()) {

                        alimentosParse.put("Alimentos", arrayStr);
                        alimentosParse.put("IdUsuario", idUsuario);
                        alimentosParse.saveInBackground();
                        if (mostraMsg) {
                            Toast.makeText(getApplicationContext(), "Lista salva", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        String objectId = null;
                        for (ParseObject objetos : list) {
                            objectId = objetos.getObjectId();
                        }

                        query.getInBackground(objectId, new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject parseObject, ParseException e) {
                                if (e == null) {
                                    parseObject.put("Alimentos", arrayStr);
                                    parseObject.saveInBackground();
                                    if (mostraMsg) {
                                        Toast.makeText(getApplicationContext(), "Lista atualizada", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                    }
                }
            });

        prefsEditor.putString("alimentos", arrayStr).commit();

    }

    protected String SalvarListaOffline(boolean mostraMsg){
        String arrayStr = null;
        SharedPreferences mPrefs = getSharedPreferences("prefListaAlimentos", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        SharedPreferences idUsuarioPref = getSharedPreferences("Usuario", MODE_PRIVATE);
        SharedPreferences.Editor prefEditorUser = idUsuarioPref.edit();

        JSONArray array = new JSONArray();
        JSONObject obj;

        for(Alimentos item : alimentos){
            obj = new JSONObject();
            try {
                obj.put("nome", item.getNome());
                obj.put("quantidade", item.getQuantidade());
                obj.put("tipo", item.getTipo());
                array.put(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        arrayStr = array.toString();
        idUsuario = idUsuarioPref.getString("idUsuario", "Inexistente");
        prefsEditor.putString("alimentos", arrayStr).commit();
        if(mostraMsg) {
            Toast.makeText(getApplicationContext(), "Lista Local Salva", Toast.LENGTH_SHORT).show();
        }
        return arrayStr;
    }

    protected void SalvarListaParse(String idUsuario, String arrayStr, boolean mostraMsg){
        CadastroUsuarioActivity userActivity = new CadastroUsuarioActivity();
        if(VerificarConexao()) {
            ParseObject alimentosParse = new ParseObject("ListaDeAlimentos");
            ParseQuery<ParseObject> query = ParseQuery.getQuery("ListaDeAlimentos");
            query.whereEqualTo("IdUsuario", idUsuario);
            List<ParseObject> listaAlimento = null;
            try {
                listaAlimento = query.find();
                if (listaAlimento.isEmpty()) {
                    alimentosParse.put("Alimentos", arrayStr);
                    alimentosParse.put("IdUsuario", idUsuario);
                    alimentosParse.save();
                    if (mostraMsg) {
                        Toast.makeText(getApplicationContext(), "Lista Criada", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String objectId = null;
                    for (ParseObject obj : listaAlimento) {
                        objectId = obj.getObjectId();
                    }
                    alimentosParse = query.get(objectId);
                    alimentosParse.put("Alimentos", arrayStr);
                    alimentosParse.save();
                    if (mostraMsg) {
                        Toast.makeText(getApplicationContext(), "Lista atualizada", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "Não há conexão com a internet. Lista local salva.",Toast.LENGTH_LONG).show();
        }
    }

    protected void InserirAlimento(){

        Alimentos alimento = new Alimentos();
        EditText nome = (EditText)findViewById(R.id.idNome);
        EditText quantidade = (EditText)findViewById(R.id.idQuantidade);
        nomeAlimento = nome.getText().toString();
        qntAlimento = quantidade.getText().toString();
        if(!nomeAlimento.isEmpty() && !qntAlimento.isEmpty()) {
            alimento.setNome(nomeAlimento);
            alimento.setQuantidade(qntAlimento);
            alimento.setTipo(tipo);
            adapter.add(alimento);
            nome.setText("");
            quantidade.setText("");
        }


    }

    protected void AlterarQuantidade(int valor){

        if(alimentoSelecionado != null)
        {

            qnt = 0;
            qnt = Integer.valueOf(alimentoSelecionado.getQuantidade());
            if (qnt > 0) {
                adapter.remove(alimentoSelecionado);
                alimentoSelecionado.setQuantidade(String.valueOf(qnt + valor));
                adapter.insert(alimentoSelecionado, posicaoDoALimento);
            }else if(valor > 0){
                adapter.remove(alimentoSelecionado);
                alimentoSelecionado.setQuantidade(String.valueOf(qnt + valor));
                adapter.insert(alimentoSelecionado, posicaoDoALimento);
            }
        }

    }

    protected void ExcluirAlimentoLista(){
        if(alimentoSelecionado != null)
        {

            adapter.remove(alimentoSelecionado);
            adapter.notifyDataSetChanged();
            lista.clearChoices();
            alimentoSelecionado = null;
        }
    }

    protected void ExcluirAmigoLista(){
        if(amigoSelecionado != null){
            if(VerificarConexao()){
            adapterAmigos.remove(amigoSelecionado);
            amigos.remove(amigoSelecionado);
            AtualizaListaDeAmigos(amigos);
            amigoSelecionado = null;
            }else{
                Toast.makeText(getApplicationContext(),"Não há conexão com a internet.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //TODO: Criar metodo para salvar a lista (Atualizada) de amigos após exclusão

    private void ObterAlimentosAmigos () {
        new AlertDialog.Builder(this)
                .setMessage("Você tem certeza que deseja substituir sua lista?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                               /**/

                        JSONArray arrayAlimentos;
                        if (amigoSelecionado != null) {
                            String message = "Lista Atualizada com sucesso: " + amigoSelecionado.getNome();
                            String idListaUsuario = null;
                            try {
                                idListaUsuario = Usuario.obtemIdListaUsuario(amigoSelecionado.getNome().toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (idListaUsuario != null) {
                                try {
                                    arrayAlimentos = ObtemListaAlimentosUsuario(idListaUsuario);
                                    alimentos.clear();
                                    JSONObject obj;
                                    Alimentos alimento;
                                    for (int i = 0; i < arrayAlimentos.length(); i++) {
                                        try {
                                            obj = arrayAlimentos.getJSONObject(i);
                                            alimento = new Alimentos();
                                            alimento.setNome(obj.getString("nome"));
                                            alimento.setQuantidade(obj.getString("quantidade"));
                                            alimento.setTipo(obj.getString("tipo"));
                                            alimentos.add(alimento);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } else {

                                message = "O usuario " + amigoSelecionado.getNome() + " nao possui lista!";

                            }
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                        }
                    }
                })
                .setNegativeButton("Não", null)
                .show();
    }

    protected void RecuperarListaDoJson(){
        try {
            //JSONArray array = new JSONArray(mPrefs.getString("alimentos","alimentos"));
            SharedPreferences pref = getSharedPreferences("prefListaAlimentos", MODE_PRIVATE);
            JSONArray array = new JSONArray(pref.getString("alimentos","alimentos"));
            //JSONArray array = new JSONArray();
            JSONObject obj;
            Alimentos alimento;
            for(int i =0; i<array.length(); i++){
                obj = array.getJSONObject(i);
                alimento = new Alimentos();
                alimento.setNome(obj.getString("nome"));
                alimento.setQuantidade(obj.getString("quantidade"));
                alimento.setTipo(obj.getString("tipo"));
                alimentos.add(alimento);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void EscondeTeclado() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void AtualizaListaDeAmigos(List<Usuario> listaDeUsuarios){

        CadastroUsuarioActivity userActivity = new CadastroUsuarioActivity();
        if(VerificarConexao()) {

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


            String idUsuario = ObterIdUsuario();
            if (!idUsuario.equals("Inexistente")) {

                ParseQuery<ParseObject> query = ParseQuery.getQuery("ListaDeAmigos");
                List<ParseObject> listaDeAmigos = null;
                query.whereEqualTo("IdUsuario", idUsuario);
                try {
                    listaDeAmigos = query.find();
                    if (!listaDeAmigos.isEmpty()) {
                        for (ParseObject objParse : listaDeAmigos) {
                            objParse.put("Dados", arrayStr);
                            objParse.save();
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }else{
            Toast.makeText(getApplicationContext(), "Não há conexão com a internet.",Toast.LENGTH_SHORT).show();
        }

    }

    protected void RecuperarListaDeAmigosJson(){
            try{
                SharedPreferences aPrefs = getSharedPreferences("prefListaUsuarios", MODE_PRIVATE);
                JSONArray array = new JSONArray(aPrefs.getString("usuarios","usuarios"));
                JSONObject obj;
                for(int i = 0; i < array.length(); i++){
                    obj = array.getJSONObject(i);
                    usuario = new Usuario();
                    usuario.setNome(obj.getString("nomeUsuario"));
                    usuario.setSenha(obj.getString("senhaUsuario"));

                    amigos.add(usuario);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    protected String ObterIdUsuario(){
        SharedPreferences idUsuarioPref = getSharedPreferences("Usuario", MODE_PRIVATE);
        SharedPreferences.Editor prefEditorUser = idUsuarioPref.edit();
        return idUsuarioPref.getString("idUsuario","Inexistente");
    }

    protected String ObterNomeUsuario(String idUsuario){
        String nomeUsuario = "Inexistente";
        ParseQuery<ParseObject> queryNomeUser = ParseQuery.getQuery("Usuario");
        queryNomeUser.whereEqualTo("objectId",idUsuario);
        try {
            ParseObject objetoUser = queryNomeUser.getFirst();
            nomeUsuario = objetoUser.get("nome").toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return nomeUsuario;
    }

    protected void ChecarUsuarioCadastrado(){
        SharedPreferences idUsuarioPref = getSharedPreferences("Usuario", MODE_PRIVATE);
        SharedPreferences.Editor prefEditorUser = idUsuarioPref.edit();

        if(idUsuarioPref.getString("idUsuario","Inexistente").equals("Inexistente") && !CadastroUsuarioActivity.EntrouSemLogar)
        {
            //CadastroUsuarioActivity.listaDeUsuariosRecebidos = amigos;
            Intent i = new Intent(this, CadastroUsuarioActivity.class);
            finish();
            startActivity(i);
            Log.i("UsuarioCadastrado","Usuario não cadastrado. Iniciando o cadastro");
        }
        else{
            Log.i("UsuarioCadastrado","Usuario Cadastrado id = " + idUsuarioPref.getString("idUsuario","Inexistente"));
        }
    }

    private class AlimentoAdapter extends ArrayAdapter  {
            public AlimentoAdapter(){
                super(MainActivity.this, R.layout.list_item, alimentos);
            }
    }

    private class AmigosAdapter extends ArrayAdapter{
        public AmigosAdapter(){
            super(MainActivity.this, R.layout.list_item, amigos);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.idBtnSalvar:

                InserirAlimento();

                break;

            case R.id.idBtnSalvarLista:
                Toast.makeText(getApplicationContext(),"Aguarde...",Toast.LENGTH_SHORT).show();
                String idUsuario = ObterIdUsuario();
                if(!idUsuario.equals("Inexistente")){
                    SalvarListaParse(idUsuario,SalvarListaOffline(false),true);
                }else {
                    SalvarListaOffline(true);
                }
                break;

            case R.id.idBtnMinus:

                AlterarQuantidade(-1);

                break;

            case R.id.idBtnPlus:

                AlterarQuantidade(+1);

                break;

            case R.id.idBtnExclude:

                ExcluirAlimentoLista();

                break;

            case R.id.idBtnObterListaAmigo:

                if(amigoSelecionado != null) {
                    Toast.makeText(getApplicationContext(),"Aguarde...",Toast.LENGTH_SHORT).show();
                    if(VerificarConexao()) {
                        ObterAlimentosAmigos();
                    }else{
                        Toast.makeText(getApplicationContext(),"Não há conexão com a internet.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.idBtnCadastroUsuario:
                //SalvarLista(false);
                SalvarListaOffline(false);
                Intent cadastroActivity = new Intent(this, CadastroUsuarioActivity.class);
                CadastroUsuarioActivity.listaDeUsuariosRecebidos = amigos;
                finish();
                startActivity(cadastroActivity);
                break;

            case R.id.idBtnExcluirAmigo:
                ExcluirAmigoLista();
                break;
            default:
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        //actionBar.setDisplayShowHomeEnabled(true);
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setIcon(R.mipmap.ic_launcher_geladeira_verde);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);



        /*String idUsuarioLogado = ObterIdUsuario();
        txtViewUsuario = (TextView)findViewById(R.id.txtViewUsuarioLogado);
        txtViewUsuario.setText(ObterNomeUsuario(idUsuarioLogado));
        txtViewIdUsuario = (TextView)findViewById(R.id.txtViewUsuarioID);
        txtViewIdUsuario.setText(idUsuarioLogado);*/

        TabHost tab = (TabHost)findViewById(R.id.tabHost);
        tab.setup();

        TabHost.TabSpec descritor = tab.newTabSpec("aba1");
        descritor.setContent(R.id.idListViewAlimentos);
        descritor.setIndicator(getString(R.string.titutoApp));
        tab.addTab(descritor);

        descritor = tab.newTabSpec("aba2");
        descritor.setContent(R.id.Cadastro);
        //descritor.setIndicator(getString(R.string.cadastro));
        descritor.setIndicator("Adicionar Alimento");
        tab.addTab(descritor);

        descritor = tab.newTabSpec("aba3");
        descritor.setContent(R.id.idListViewAmigos);
        descritor.setIndicator("Área do Usuário");
        tab.addTab(descritor);

        rdoBtnKg = (RadioButton)findViewById(R.id.rdoBtnKg);
        rdoBtnG = (RadioButton)findViewById(R.id.rdoBtnG);
        rdoBtnLitro = (RadioButton)findViewById(R.id.rdoBtnLitro);
        rdoBtnUnidade = (RadioButton)findViewById(R.id.rdoBtnUnidade);

        rdoGroupModal = (RadioGroup)findViewById(R.id.idRdoGroupModal);
        rdoGroupModal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rdoBtnKg){
                    tipo = "Kilo(s)";
                }else if(checkedId == R.id.rdoBtnG){
                    tipo = "Grama(s)";
                }else if(checkedId == R.id.rdoBtnLitro){
                    tipo = "Litro(s)";
                }else{
                    tipo = "Unidade(s)";
                }

            }
        });



        final SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);


        contexto = getApplicationContext();

        ChecarUsuarioCadastrado();
        RecuperarListaDoJson();
        RecuperarListaDeAmigosJson();

        lista = (ListView)findViewById(R.id.idListViewAlimentos);
        lista.setSelector(R.drawable.selected);
        adapter = new AlimentoAdapter();
        lista.setAdapter(adapter);

        //Teste da lista de Amigos//



        final ListView listaDeAmigos = (ListView)findViewById(R.id.idListViewAmigos);
        listaDeAmigos.setSelector(R.drawable.selected);
        adapterAmigos = new AmigosAdapter();
        listaDeAmigos.setAdapter(adapterAmigos);
        //
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                posicaoDoALimento = position;
                adaptadorPai = parent;
                viewAl = view;

                alimentoSelecionado = (Alimentos) parent.getItemAtPosition(posicaoDoALimento);

            }

        });


        listaDeAmigos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                amigoSelecionado = (Usuario) parent.getItemAtPosition(position);

            }
        });


        botaosalvarAlimento = (Button) findViewById(R.id.idBtnSalvar);
        botaosalvarAlimento.setOnClickListener(this);

        botaoMenos = (Button)findViewById(R.id.idBtnMinus);
        botaoMenos.setOnClickListener(this);

        botaoMais = (Button)findViewById(R.id.idBtnPlus);
        botaoMais.setOnClickListener(this);

        botaoExcluir = (Button)findViewById(R.id.idBtnExclude);
        botaoExcluir.setOnClickListener(this);

        botaoSalvarLista = (Button)findViewById(R.id.idBtnSalvarLista);
        botaoSalvarLista.setOnClickListener(this);

        botaoObterListaAmigo = (Button)findViewById(R.id.idBtnObterListaAmigo);
        botaoObterListaAmigo.setOnClickListener(this);

        botaoCadastrarUsuario = (Button)findViewById(R.id.idBtnCadastroUsuario);
        botaoCadastrarUsuario.setOnClickListener(this);

        botaoExcluirAmigo = (Button)findViewById(R.id.idBtnExcluirAmigo);
        botaoExcluirAmigo.setOnClickListener(this);

        tab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
                                        @Override
                                        public void onTabChanged(String tabId) {
                                            if (tabId.equals("aba1")) {
                                                botaoMais.setVisibility(View.VISIBLE);
                                                botaoMenos.setVisibility(View.VISIBLE);
                                                botaoExcluir.setVisibility(View.VISIBLE);
                                                botaoSalvarLista.setVisibility(View.VISIBLE);
                                                botaoObterListaAmigo.setVisibility(View.INVISIBLE);
                                                botaosalvarAlimento.setVisibility(View.INVISIBLE);
                                                botaoCadastrarUsuario.setVisibility(View.INVISIBLE);
                                                botaoExcluirAmigo.setVisibility(View.INVISIBLE);

                                                EscondeTeclado();
                                            }
                                            if (tabId.equals("aba2")) {
                                                botaoMais.setVisibility(View.INVISIBLE);
                                                botaoMenos.setVisibility(View.INVISIBLE);
                                                botaoExcluir.setVisibility(View.INVISIBLE);
                                                botaoSalvarLista.setVisibility(View.INVISIBLE);
                                                botaoObterListaAmigo.setVisibility(View.INVISIBLE);
                                                botaoCadastrarUsuario.setVisibility(View.INVISIBLE);
                                                botaosalvarAlimento.setVisibility(View.VISIBLE);
                                                botaoExcluirAmigo.setVisibility(View.INVISIBLE);

                                            }
                                            if (tabId.equals("aba3")) {

                                                botaoMais.setVisibility(View.INVISIBLE);
                                                botaoMenos.setVisibility(View.INVISIBLE);
                                                botaoExcluir.setVisibility(View.INVISIBLE);
                                                botaoSalvarLista.setVisibility(View.INVISIBLE);
                                                botaoObterListaAmigo.setVisibility(View.INVISIBLE);
                                                botaosalvarAlimento.setVisibility(View.INVISIBLE);
                                                botaoObterListaAmigo.setVisibility(View.VISIBLE);
                                                if (ObterIdUsuario().equals("Inexistente")) {
                                                    botaoCadastrarUsuario.setText("LOGIN");
                                                } else {
                                                    botaoCadastrarUsuario.setText("ADICIONAR AMIGO");
                                                }
                                                botaoCadastrarUsuario.setVisibility(View.VISIBLE);
                                                botaoExcluirAmigo.setVisibility(View.VISIBLE);

                                            }
                                        }
                                    }
        );


        botaoMenos.setOnTouchListener(new View.OnTouchListener() {
            private Handler mHandler;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mHandler != null)
                            return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 300);
                        break;

                    case MotionEvent.ACTION_UP:
                        if (mHandler == null)
                            return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                        break;
                }

                return false;
            }

            Runnable mAction = new Runnable() {
                @Override
                public void run() {
                    AlterarQuantidade(-1);
                    mHandler.postDelayed(this, 300);
                }
            };
        });

        botaoMais.setOnTouchListener(new View.OnTouchListener() {
            private Handler mHandler;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if(mHandler != null)
                            return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 300);
                        break;
                    case MotionEvent.ACTION_UP:
                        if(mHandler == null)
                            return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                        break;
                }

                return false;
            }

            Runnable mAction = new Runnable() {
                @Override
                public void run() {
                    AlterarQuantidade(+1);
                    mHandler.postDelayed(this, 300);
                };
            };
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/
        switch(id){
            case android.R.id.home:{
                //aparece e desaparece o menu
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}



