package sistemaDeGerenciamentoDeEventos.dominio;

import java.math.BigDecimal;

public class IngressoVip extends Ingresso{
    private static final BigDecimal multiplicador = BigDecimal.valueOf(2.5);
    public IngressoVip(String nome, String cpf,int id,BigDecimal precoFinal){
        super(nome, cpf, id, precoFinal);
    }


    @Override
    public BigDecimal calcularPreco(){
        //coloquei o preco do do vip em 2.5 do preco original
        return super.getPrecoFinal().multiply(multiplicador);
    }

    @Override
    public boolean permiteEntradaAntecipada(){
        return true;
    };
}
