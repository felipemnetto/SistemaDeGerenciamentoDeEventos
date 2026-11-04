package sistemaDeGerenciamentoDeEventos.dominio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public class Evento{

    private int id, capacidadeMax, limitePromo;
    private String titulo, descricao, local;
    private LocalDate data;
    private LocalTime horaInicio;
    private BigDecimal precoBase, percentDescontoProm;
    private List<Venda> vendas;

    public Evento(int id, String titulo, String local, int capMax,BigDecimal preco, BigDecimal precoDesc){
        this.id = id;
        this.titulo = titulo;
        this.local = local;
        capacidadeMax = capMax;
        this.precoBase = preco;
        this.percentDescontoProm = precoDesc;
    }
}
