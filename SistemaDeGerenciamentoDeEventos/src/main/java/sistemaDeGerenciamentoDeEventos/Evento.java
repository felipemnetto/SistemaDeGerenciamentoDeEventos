package sistemaDeGerenciamentoDeEventos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.BiConsumer;

public class Evento{

    private int id, capacidadeMax, limitePromo;
    private String titulo, descricao, local;
    private LocalDate data;
    private LocalTime hora;
    private BigDecimal preco, precoDesc;
    private List<Vendas> vendas;

    public Evento(int id, String titulo, String local, int capMax,BigDecimal preco, BigDecimal precoDesc){
        this.id = id;
        this.titulo = titulo;
        this.local = local;
        capacidadeMax = capMax;
        this.preco = preco;
        this.precoDesc = precoDesc;
}
}
