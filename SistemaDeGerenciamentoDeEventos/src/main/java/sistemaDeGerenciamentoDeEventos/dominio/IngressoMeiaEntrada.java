package sistemaDeGerenciamentoDeEventos.dominio;

import java.math.BigDecimal;

public class IngressoMeiaEntrada extends Ingresso {
    private String justificativa;
    private static final BigDecimal multiplicador = BigDecimal.valueOf(0.5);

    public IngressoMeiaEntrada(String nome, String cpf,int id,BigDecimal precoFinal, String justificativa, Evento evento){
        super(nome, cpf, id, evento);
        this.justificativa=justificativa;
    }

     @Override
    public BigDecimal calcularPreco(){
        return getEvento().getPrecoBase().multiply(multiplicador);
    }

    @Override
    public boolean permiteEntradaAntecipada(){
        return false;
    }

}
