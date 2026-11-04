package sistemaDeGerenciamentoDeEventos.dominio;

import java.math.BigDecimal;

public class IngressoPromocional extends Ingresso{
    public IngressoPromocional(String nome, String cpf,int id,BigDecimal preco){
        super(nome, cpf, id, preco);
    }

    
    @Override
    public boolean permiteEntradaAntecipada(){
        return false;
    }

}
