package sistemaDeGerenciamentoDeEventos.dominio;

import java.math.BigDecimal;

public abstract class Ingresso{

    private int id;
    private String nomeParticipante, cpfParticipante;
    private BigDecimal precoFinal;

    public Ingresso(String nome, String cpf, int id, BigDecimal precoFinal){
        this.nomeParticipante = nome;
        this.cpfParticipante = cpf;
        this.id = id;
        this.precoFinal = precoFinal;
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

    public BigDecimal getPrecoFinal() {
        return precoFinal;
    }

    public abstract BigDecimal calcularPreco();
    public abstract boolean permiteEntradaAntecipada();
}
