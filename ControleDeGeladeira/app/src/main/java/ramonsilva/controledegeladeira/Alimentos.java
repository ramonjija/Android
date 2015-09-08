package ramonsilva.controledegeladeira;

/**
 * Created by ramon.silva on 13/08/2015.
 */
public class Alimentos {
    private String nome;
    private String quantidade;

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    @Override
    public String toString() {
        return nome+" | "+quantidade;
    }
}
