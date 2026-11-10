package sistemaDeGerenciamentoDeEventos.dominio;

import java.math.BigDecimal;

public class IngressoInteira extends Ingresso {
    public IngressoInteira(String nome, String cpf,String id, String id_venda, Evento evento){
        super(nome, cpf, id, id_venda, evento);
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
