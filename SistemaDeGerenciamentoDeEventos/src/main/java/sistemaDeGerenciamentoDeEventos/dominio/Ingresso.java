package sistemaDeGerenciamentoDeEventos.dominio;

import java.math.BigDecimal;

public abstract class Ingresso{

    private String id, id_venda, nomeParticipante, cpfParticipante;
    private Evento evento;

    public Ingresso(String nome, String cpf, String id, String id_venda,Evento evento){
        this.nomeParticipante = nome;
        this.cpfParticipante = cpf;
        this.id = id;
        this.id_venda = id_venda;
        this.evento = evento;
    }

    public String getId() {
        return id;
    }

    public String getIdVenda(){
        return id_venda;
    }

    public String getNomeParticipante() {
        return nomeParticipante;
    }

    public String getCpfParticipante() {
        return cpfParticipante;
    }

    public Evento getEvento(){
        return evento;
    }

    public abstract BigDecimal calcularPreco();
    public abstract boolean permiteEntradaAntecipada();
}
