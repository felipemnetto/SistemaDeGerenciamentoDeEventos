package sistemaDeGerenciamentoDeEventos.dominio;

import java.math.BigDecimal;

public class IngressoMeiaEntrada extends Ingresso {
    private String justificativa;

    public IngressoMeiaEntrada(String nome, String cpf,int id,BigDecimal precoFinal, String justificativa){
        super(nome, cpf, id, precoFinal);
        this.justificativa=justificativa;
    }

     @Override
    public BigDecimal calcularPreco(){
        return super.getPrecoFinal();
    }

    @Override
    public boolean permiteEntradaAntecipada(){
        return false;
    }

}
