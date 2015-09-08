package ramonsilva.controledegeladeira;

import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by ramon.silva on 27/08/2015.
 */
public class Amigos {

    private String id;
    private String GMCID;
    private String nome;

    public String getGMCID() {
        return GMCID;
    }

    public void setGMCID(String GMCID) {
        this.GMCID = GMCID;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    @Override
    public String toString() {
        return nome;
    }
}
