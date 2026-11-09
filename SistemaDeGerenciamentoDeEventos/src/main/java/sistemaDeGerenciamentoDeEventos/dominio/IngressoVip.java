package sistemaDeGerenciamentoDeEventos.dominio;

import java.math.BigDecimal;

public class IngressoVip extends Ingresso{
    private static final BigDecimal multiplicador = BigDecimal.valueOf(1.5);
    public IngressoVip(String nome, String cpf,int id, Evento evento){
        super(nome, cpf, id, evento);
    }


    @Override
    public BigDecimal calcularPreco(){
        return getEvento().getPrecoBase().multiply(multiplicador);
    }

    @Override
    public boolean permiteEntradaAntecipada(){
        return true;
    };
}
