package ramonsilva.controledegeladeira;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

//import com.google.android.gms.gcm.GoogleCloudMessaging;


import org.w3c.dom.Text;

import java.io.IOException;

/**
 * Created by ramon.silva on 28/08/2015.
 */
public class GcmUtils {

   /* protected void ObtemId(final String PROJECT_NUMBER, final Context Contexto, final GoogleCloudMessaging gcmEnviado, final TextView txtView){

        new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(Void... params) {
                GoogleCloudMessaging gcm = gcmEnviado;
                String idDoAparelho = "";
                String regid = "";
                try{
                    if(gcm == null){
                        gcm = GoogleCloudMessaging.getInstance(Contexto);
                    }
                    regid = gcm.register(PROJECT_NUMBER);
                    idDoAparelho = regid;
                    Log.i("GCM", idDoAparelho);
                }catch (IOException ex){
                    idDoAparelho = "Erro: "+ex.getMessage();
                }

                return idDoAparelho;
            }

            /*@Override
            protected void onPostExecute(String s) {
                txtView.setText(s);
            }

        }.execute(null,null,null);

    }*/


}
