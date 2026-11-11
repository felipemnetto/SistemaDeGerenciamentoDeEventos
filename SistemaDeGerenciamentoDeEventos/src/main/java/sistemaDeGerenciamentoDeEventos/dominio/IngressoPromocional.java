package sistemaDeGerenciamentoDeEventos.dominio;

import java.math.BigDecimal;

public class IngressoPromocional extends Ingresso{
    public IngressoPromocional(String nome, String cpf,String id, String id_venda, Evento evento){
        super(nome, cpf, id, id_venda, evento);
    }

    @Override
    public boolean permiteEntradaAntecipada(){
        return false;
    }

    @Override
    public BigDecimal calcularPreco() {
        return getEvento()
              .getPrecoBase()
              .multiply(super.getEvento()
              .getPercentDescontoPromocional()
              .divide(BigDecimal.valueOf(100)));

    }

}
