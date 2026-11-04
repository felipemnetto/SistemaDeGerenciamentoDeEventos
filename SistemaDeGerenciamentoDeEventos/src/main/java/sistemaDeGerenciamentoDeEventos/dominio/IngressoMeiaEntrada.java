package sistemaDeGerenciamentoDeEventos.dominio;

import java.math.BigDecimal;

public class IngressoMeiaEntrada extends Ingresso {
    private String justificativa;

    public IngressoMeiaEntrada(String nome, String cpf,int id,BigDecimal preco, String justificativa){
        super(nome, cpf, id, preco);
        this.justificativa=justificativa;
    }
    
     @Override
    public BigDecimal calcularPreco(){
        return preco;
    }

    @Override
    public boolean permiteEntradaAntecipada(){
        return false;

    }

}
