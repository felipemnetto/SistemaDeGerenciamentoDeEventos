package sistemaDeGerenciamentoDeEventos.dominio;

import java.math.BigDecimal;

public class IngressoVip extends Ingresso{
    public IngressoVip(String nome, String cpf,int id,BigDecimal preco){
        super(nome, cpf, id, preco);
    }


    @Override
    public BigDecimal calcularPreco(){
        //coloquei o preco do do vip em 2.5 do preco original
        return preco.multiply(2.5);
    }

    @Override
    public boolean permiteEntradaAntecipada(){
        return true;

    };
}
