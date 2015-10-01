package ramonsilva.controledegeladeira;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by ramon.silva on 31/08/2015.
 */
public class Usuario {

    private String nome;
    private String senha;
    private int idLista;

    public int getIdLista() {
        return idLista;
    }

    public void setIdLista(int idLista) {
        this.idLista = idLista;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Usuario(){};

    public Usuario(String nome, String senha){
        this.nome = nome;
        this.senha = senha;
    }


    public static String obtemIdListaUsuario(String nome) throws ParseException {
         String idListaUsuario = null;
         String idUsuario = null;

        //TODO: Exemplo de metodo sincrono
        ParseQuery<ParseObject> queryUsuario = ParseQuery.getQuery("Usuario");
        queryUsuario.whereEqualTo("nome", nome);
        List<ParseObject> objUsuario = queryUsuario.find();
        for(ParseObject obj : objUsuario){
            idUsuario = obj.getObjectId();
        }
        ParseQuery<ParseObject> queryLista = ParseQuery.getQuery("ListaDeAlimentos");
        queryLista.whereEqualTo("IdUsuario", idUsuario);
        List<ParseObject> objLista = queryLista.find();
        for(ParseObject obj : objLista) {
            idListaUsuario = obj.getObjectId();
        }

        return idListaUsuario;
    }

    @Override
    public String toString() {
        return nome;
    }
}
