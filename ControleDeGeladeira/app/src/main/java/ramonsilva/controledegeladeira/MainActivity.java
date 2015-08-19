package ramonsilva.controledegeladeira;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener{

    private List<Alimentos> alimentos = new ArrayList<Alimentos>();
    private  AlimentoAdapter adapter = null;
    private Alimentos alimentoSelecionado = null;
    private int qnt = 0;
    private int posicaoDoALimento = 0;
    private Button botaoMenos = null;
    private Button botaoExcluir = null;
    private Button botaoMais = null;
    private String nomeAlimento = null;
    private String qntAlimento = null;
    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.idBtnSalvar:

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

                break;

            case R.id.idBtnMinus:

                qnt = 0;
                qnt = Integer.valueOf(alimentoSelecionado.getQuantidade());
                if(qnt >= 0) {
                    adapter.remove(alimentoSelecionado);
                    alimentoSelecionado.setQuantidade(String.valueOf(qnt - 1));
                    adapter.insert(alimentoSelecionado, posicaoDoALimento);
                }
                break;

            case R.id.idBtnPlus:

                qnt = 0;
                qnt = Integer.valueOf(alimentoSelecionado.getQuantidade());
                if(qnt >= 0) {
                    adapter.remove(alimentoSelecionado);
                    alimentoSelecionado.setQuantidade(String.valueOf(qnt + 1));
                    adapter.insert(alimentoSelecionado, posicaoDoALimento);

                }
                break;

            case R.id.idBtnExclude:

               adapter.remove(alimentoSelecionado);
               break;

            default:
                break;
        }

    }

    private class AlimentoAdapter extends ArrayAdapter{
            public AlimentoAdapter(){
                super(MainActivity.this, android.R.layout.simple_list_item_1, alimentos);
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

        ListView lista = (ListView)findViewById(R.id.idListViewAlimentos);
        lista.setSelector(R.drawable.selected);
        adapter = new AlimentoAdapter();
        lista.setAdapter(adapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                posicaoDoALimento = position;
                alimentoSelecionado = (Alimentos)parent.getItemAtPosition(posicaoDoALimento);

            }
        });



        Button salvar = (Button) findViewById(R.id.idBtnSalvar);
        salvar.setOnClickListener(this);
        botaoMenos = (Button)findViewById(R.id.idBtnMinus);
        botaoMenos.setOnClickListener(this);

        botaoMais = (Button)findViewById(R.id.idBtnPlus);
        botaoMais.setOnClickListener(this);

        botaoExcluir = (Button)findViewById(R.id.idBtnExclude);
        botaoExcluir.setOnClickListener(this);



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
}
