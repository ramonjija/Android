package ramonsilva.controledegeladeira;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class MainActivity extends Activity implements View.OnClickListener{

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

    //private GoogleCloudMessaging gcm = null;
    private String regid = null;
    private String PROJECT_NUMBER = "560915181661";
    private Button botaoObterGcm = null;
    private GcmUtils utilidadesGcm = null;

    private TextView txtViewGcm = null;

    private List<Usuario> amigos = new ArrayList<Usuario>();
    private Usuario usuario;

    private Button botaoCadastrarUsuario = null;

    private Context contexto = null;

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.idBtnSalvar:

                InserirAlimento();

                break;

            case R.id.idBtnSalvarLista:

                SalvarLista();

                break;

            case R.id.idBtnMinus:

                AlterarQuantidade(-1);

                break;

            case R.id.idBtnPlus:

                AlterarQuantidade(+1);

                break;

            case R.id.idBtnExclude:

                ExcluirLista();

               break;

            case R.id.idBtnObterGCM:

                ParseObject testObject = new ParseObject("TestObject");
                testObject.put("foo", "bar");
                testObject.saveInBackground();

                //txtViewGcm = (TextView)findViewById(R.id.idTxtViewGCM);
               // utilidadesGcm.ObtemId(PROJECT_NUMBER, contexto, gcm, txtViewGcm);

                break;

            case R.id.idBtnCadastroUsuario:

                Intent cadastroActivity = new Intent(this, CadastroUsuarioActivity.class);
                CadastroUsuarioActivity.listaDeUsuariosRecebidos = amigos;
                startActivity(cadastroActivity);

                break;

            default:
                break;
        }

    }




 /*
    protected void ObtemId(){
        new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(Void... params) {
                String idDoAparelho = "";
                try{
                    if(gcm == null){
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(PROJECT_NUMBER);
                    idDoAparelho = regid;
                    Log.i("GCM", idDoAparelho);
                }catch (IOException ex){
                    idDoAparelho = "Erro: "+ex.getMessage();
                }

                return idDoAparelho;
            }

           /* @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }.execute(null,null,null);
    }
*/
    protected void SalvarLista(){

            //SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
            //SharedPreferences de Alimentos
            SharedPreferences mPrefs = getSharedPreferences("prefListaAlimentos", MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            //SharedPreferences de Usuario
            SharedPreferences idUsuarioPref = getSharedPreferences("Usuario", MODE_PRIVATE);
            SharedPreferences.Editor prefEditorUser = idUsuarioPref.edit();

            JSONArray array = new JSONArray();
            JSONObject obj;
        final String[] objId = new String[1];
        final ParseObject alimentosParse = new ParseObject("ListaDeAlimentos");
            final ParseQuery<ParseObject> query;

            for(Alimentos item : alimentos){
                obj = new JSONObject();
                //alimentosParse = new ParseObject("Alimentos");
                try {
                    obj.put("nome", item.getNome());
                    //alimentosParse.put("nome", item.getNome());
                    obj.put("quantidade", item.getQuantidade());
                    //alimentosParse.put("quantidade", item.getQuantidade());
                    //alimentosParse.saveInBackground();
                    array.put(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            final String arrayStr = array.toString();
        //
        //Parse
            final String idUsuario = idUsuarioPref.getString("idUsuario","Inexistente");

            query = ParseQuery.getQuery("ListaDeAlimentos");
            //query.whereEqualTo("IdUsuario", "Xn9ZXg3qcu");
            query.whereEqualTo("IdUsuario", idUsuario);

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    if(e != null) {
                        e.printStackTrace();
                    }
                    else if(list.isEmpty()){
                            //Lista de alimentos não encontrada
                            //alimentosParse = new ParseObject("ListaDeAlimentos");
                            alimentosParse.put("Alimentos", arrayStr);
                            alimentosParse.put("IdUsuario", idUsuario);
                            //alimentosParse.put("IdUsuario", "Xn9ZXg3qcu");
                            alimentosParse.saveInBackground();
                            Toast.makeText(getApplicationContext(), "Lista salva",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            //Lista de alimentos encontrada
                            String objectId = null;
                            for(ParseObject objetos : list){
                                objectId = objetos.getObjectId();
                            }

                            query.getInBackground(objectId, new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject parseObject, ParseException e) {
                                    if(e == null){
                                        parseObject.put("Alimentos", arrayStr);
                                        parseObject.saveInBackground();
                                        Toast.makeText(getApplicationContext(), "Lista atualizada",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
            });

            /*query.getInBackground("Xn9ZXg3qcu", new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    if(e == null){
                        parseObject.put("Alimentos", arrayStr);
                        parseObject.saveInBackground();
                    }
                }
            });*/

            //alimentosParse = new ParseObject("ListaDeAlimentos");
           // alimentosParse.put("Alimentos", arrayStr);
           // alimentosParse.put("IdUsuario","Xn9ZXg3qcu");
           // alimentosParse.saveInBackground();


        //


        prefsEditor.putString("alimentos", arrayStr).commit();





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
            }
        }

    }

    protected void ExcluirLista(){
        if(alimentoSelecionado != null)
        {

            adapter.remove(alimentoSelecionado);
            alimentoSelecionado = null;
        }
    }

    //protected void RecuperarListaDoJson(SharedPreferences mPrefs){
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

    protected void RecuperarListaDeAmigosJson(){
            try{
                //JSONArray array = new JSONArray(mPrefs.getString("usuarios","usuarios"));
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

    protected void ChecarUsuarioCadastrado(){
        SharedPreferences idUsuarioPref = getSharedPreferences("Usuario", MODE_PRIVATE);
        SharedPreferences.Editor prefEditorUser = idUsuarioPref.edit();

        if(idUsuarioPref.getString("idUsuario","Inexistente").equals("Inexistente"))
        {
            //CadastroUsuarioActivity.listaDeUsuariosRecebidos = amigos;
            Intent i = new Intent(this, CadastroUsuarioActivity.class);
            startActivity(i);
            Log.i("UsuarioCadastrado","Usuario não cadastrado. Iniciando o cadastro");
        }
        else{
            Log.i("UsuarioCadastrado","Usuario Cadastrado id = " + idUsuarioPref.getString("idUsuario","Inexistente"));
        }
    }

    /*protected void LimparListaDeAmigosJson(){
        amigos.clear();
    }*/


    private void InitParse(){

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            try {
                //Parse.enableLocalDatastore(getApplicationContext());
                //Parse.initialize(getApplication(), "OxsN4cdSYTNtg2qyJykqelYMsA1CpQauyvKxThlg", "pc4XHC2JdJx1OpcrpLKi99CAucVR68XhBtxfc4v1");
                Parse.enableLocalDatastore(this);
                Parse.initialize(this, "OxsN4cdSYTNtg2qyJykqelYMsA1CpQauyvKxThlg", "pc4XHC2JdJx1OpcrpLKi99CAucVR68XhBtxfc4v1");
                //ParseInstallation.getCurrentInstallation().saveInBackground();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //String value = extras.getString("ParseIniciado");
        }
    }

    private class AlimentoAdapter extends ArrayAdapter{
            public AlimentoAdapter(){
                super(MainActivity.this, android.R.layout.simple_list_item_1, alimentos);
            }
    }

    private class AmigosAdapter extends ArrayAdapter{
        public AmigosAdapter(){
            super(MainActivity.this, android.R.layout.simple_list_item_1, amigos);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabHost tab = (TabHost)findViewById(R.id.tabHost);
        tab.setup();

        TabHost.TabSpec descritor = tab.newTabSpec("aba1");
        descritor.setContent(R.id.idListViewAlimentos);
        descritor.setIndicator(getString(R.string.titutoApp));
        tab.addTab(descritor);

        descritor = tab.newTabSpec("aba2");
        descritor.setContent(R.id.Cadastro);
        descritor.setIndicator(getString(R.string.cadastro));
        tab.addTab(descritor);

        descritor = tab.newTabSpec("aba3");
        descritor.setContent(R.id.idListViewAmigos);
        descritor.setIndicator("Lista de amigos");
        tab.addTab(descritor);


        //Inicio da configuracao do Parse
        //Parse.enableLocalDatastore(this);
        //Parse.initialize(this, "OxsN4cdSYTNtg2qyJykqelYMsA1CpQauyvKxThlg", "pc4XHC2JdJx1OpcrpLKi99CAucVR68XhBtxfc4v1");

        InitParse();


        //Verifica se o usuario foi cadastrado



        final SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);

        //utilidadesGcm = new GcmUtils();

        contexto = getApplicationContext();

        ChecarUsuarioCadastrado();
        //RecuperarListaDoJson(mPrefs);
        RecuperarListaDoJson();
        RecuperarListaDeAmigosJson();




        /*
        try {
            JSONArray array = new JSONArray(mPrefs.getString("alimentos","alimentos"));
            JSONObject obj;
            Alimentos alimento;
            for(int i =0; i<array.length(); i++){
                obj = array.getJSONObject(i);
                alimento = new Alimentos();
                alimento.setNome(obj.getString("nome"));
                alimento.setQuantidade(obj.getString("quantidade"));

                alimentos.add(alimento);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/



        ListView lista = (ListView)findViewById(R.id.idListViewAlimentos);
        lista.setSelector(R.drawable.selected);
        adapter = new AlimentoAdapter();
        lista.setAdapter(adapter);

        //Teste da lista de Amigos//



        final ListView listaDeAmigos = (ListView)findViewById(R.id.idListViewAmigos);
        listaDeAmigos.setSelector(R.drawable.selected);
        adapterAmigos = new AmigosAdapter();
        listaDeAmigos.setAdapter(adapterAmigos);







        /*amigo.setGMCID("123455");
        amigo.setId("1");
        amigo.setNome("AAA");*/

        //amigos.add(amigo);

        /*Intent intent = getIntent();
        Bundle params = intent.getExtras();

        if(params != null){
            Amigos amigo = new Amigos();
            amigo.setNome(params.getString("Nome"));
            adapterAmigos.add(amigo);
        }&*/




        //
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                posicaoDoALimento = position;
                adaptadorPai = parent;
                viewAl = view;
                alimentoSelecionado = (Alimentos) parent.getItemAtPosition(posicaoDoALimento);


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

        botaoObterGcm = (Button)findViewById(R.id.idBtnObterGCM);
        botaoObterGcm.setOnClickListener(this);

        botaoCadastrarUsuario = (Button)findViewById(R.id.idBtnCadastroUsuario);
        botaoCadastrarUsuario.setOnClickListener(this);

        tab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
                                        @Override
                                        public void onTabChanged(String tabId) {
                                            if (tabId.equals("aba1")) {
                                                botaoMais.setVisibility(View.VISIBLE);
                                                botaoMenos.setVisibility(View.VISIBLE);
                                                botaoExcluir.setVisibility(View.VISIBLE);
                                                botaoSalvarLista.setVisibility(View.VISIBLE);

                                                botaoObterGcm.setVisibility(View.INVISIBLE);
                                                botaosalvarAlimento.setVisibility(View.INVISIBLE);

                                                botaoCadastrarUsuario.setVisibility(View.INVISIBLE);

                                                EscondeTeclado();
                                            }
                                            if(tabId.equals("aba2")){
                                                botaoMais.setVisibility(View.INVISIBLE);
                                                botaoMenos.setVisibility(View.INVISIBLE);
                                                botaoExcluir.setVisibility(View.INVISIBLE);
                                                botaoSalvarLista.setVisibility(View.INVISIBLE);
                                                botaoObterGcm.setVisibility(View.INVISIBLE);
                                                botaoCadastrarUsuario.setVisibility(View.INVISIBLE);

                                                botaosalvarAlimento.setVisibility(View.VISIBLE);

                                            }
                                            if(tabId.equals("aba3")){

                                                botaoMais.setVisibility(View.INVISIBLE);
                                                botaoMenos.setVisibility(View.INVISIBLE);
                                                botaoExcluir.setVisibility(View.INVISIBLE);
                                                botaoSalvarLista.setVisibility(View.INVISIBLE);
                                                botaoObterGcm.setVisibility(View.INVISIBLE);

                                                botaosalvarAlimento.setVisibility(View.INVISIBLE);

                                                botaoObterGcm.setVisibility(View.VISIBLE);
                                                botaoCadastrarUsuario.setVisibility(View.VISIBLE);





                                            }
                                        }
                                    }
        );
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        //SalvarLista();
        super.onPause();
    }
}


