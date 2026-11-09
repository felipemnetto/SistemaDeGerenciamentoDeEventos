package sistemaDeGerenciamentoDeEventos.dominio;

import java.math.BigDecimal;

public class IngressoInteira extends Ingresso {
    public IngressoInteira(String nome, String cpf,int id, Evento evento){
        super(nome, cpf, id, evento);
    }

    @Override
    public BigDecimal calcularPreco(){
        return getEvento().getPrecoBase();
    }

    @Override
    public boolean permiteEntradaAntecipada(){
        return false;
    }
}
