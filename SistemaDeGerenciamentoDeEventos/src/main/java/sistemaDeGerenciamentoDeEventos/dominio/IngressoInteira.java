package sistemaDeGerenciamentoDeEventos.dominio;

import java.math.BigDecimal;

public class IngressoInteira extends Ingresso {
    public IngressoInteira(String nome, String cpf,int id,BigDecimal preco){
        super(nome, cpf, id, preco);
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
