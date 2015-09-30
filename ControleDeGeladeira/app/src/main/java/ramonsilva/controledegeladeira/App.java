package ramonsilva.controledegeladeira;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by ramon.silva on 30/09/2015.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "OxsN4cdSYTNtg2qyJykqelYMsA1CpQauyvKxThlg", "pc4XHC2JdJx1OpcrpLKi99CAucVR68XhBtxfc4v1");
    }
}
