package ramonsilva.controledegeladeira;

/**
 * Created by ramon.silva on 31/08/2015.
 */
public class Usuario {

    private String nome;
    private String senha;

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

    @Override
    public String toString() {
        return nome;
    }
}
