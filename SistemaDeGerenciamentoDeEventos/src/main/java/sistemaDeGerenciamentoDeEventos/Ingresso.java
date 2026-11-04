package sistemaDeGerenciamentoDeEventos;

public abstract class Ingresso{

    private int id;
    private String nome, cpf;
    private BigDecimal precoFinal;

    public Ingresso(String nome, String cpf, int id, BigDecimal, precoFinal){
        this.nome = nome;
        this.cpf = cpf;
        this.id = id;
        this.precoFinal = precoFinal;

    }
}
