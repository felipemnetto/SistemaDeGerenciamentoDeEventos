package sistemaDeGerenciamentoDeEventos.dominio;

import java.math.BigDecimal;

public class IngressoInteira extends Ingresso {
    public IngressoInteira(String nome, String cpf,int id,BigDecimal precoFinal){
        super(nome, cpf, id, precoFinal);
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
