package ramonsilva.controledegeladeira;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class MainActivity extends Activity {


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
