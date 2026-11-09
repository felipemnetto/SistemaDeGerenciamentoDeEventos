package sistemaDeGerenciamentoDeEventos.dominio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Evento{

    private int id, capacidadeMax, vendidosTotal;
    private String titulo, descricao, local;
    private LocalDate data;
    private LocalTime horaInicio;
    private BigDecimal precoBase;
    private List<Venda> vendas;

    public Evento(int id, String titulo, String local, int capMax,BigDecimal preco, BigDecimal precoDesc){
        this.id = id;
        this.titulo = titulo;
        this.local = local;
        capacidadeMax = capMax;
        this.precoBase = preco;
    }

    public void addListaVenda(Venda aux){
        vendas.add(aux);
        vendidosTotal = vendidosTotal + aux.QuantIngresso();

    }

    public int calcularDisponibilidade(){
        return capacidadeMax-vendidosTotal;

    }

    public int calcularDisponibilidadePromocional(){
        // placeholder
        return 101;
    }

    public int calcularOcupacao(){
        int lotacao = (vendidosTotal/capacidadeMax)*100;
        return lotacao;
    }

    public BigDecimal getPrecoBase(){
        return precoBase;
    }
}
