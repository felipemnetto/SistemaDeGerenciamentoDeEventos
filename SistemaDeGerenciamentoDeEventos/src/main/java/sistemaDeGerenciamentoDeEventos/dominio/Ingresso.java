package sistemaDeGerenciamentoDeEventos.dominio;

import java.math.BigDecimal;

public abstract class Ingresso{

    private int id;
    private String nomeParticipante, cpfParticipante;
    private Evento evento;

    public Ingresso(String nome, String cpf, int id, Evento evento){
        this.nomeParticipante = nome;
        this.cpfParticipante = cpf;
        this.id = id;
        this.evento = evento;
    }

    public int getId() {
        return id;
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
