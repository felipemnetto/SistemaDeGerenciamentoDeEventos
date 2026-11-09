package sistemaDeGerenciamentoDeEventos.dominio;

import java.math.BigDecimal;

public class IngressoPromocional extends Ingresso{
    private static final BigDecimal multiplicador = BigDecimal.valueOf(0.75);
    public IngressoPromocional(String nome, String cpf,int id,BigDecimal preco, Evento evento){
        super(nome, cpf, id, evento);
    }

    @Override
    public boolean permiteEntradaAntecipada(){
        return false;
    }

    @Override
    public BigDecimal calcularPreco() {
        return getEvento().getPrecoBase().multiply(multiplicador);

    }

}
