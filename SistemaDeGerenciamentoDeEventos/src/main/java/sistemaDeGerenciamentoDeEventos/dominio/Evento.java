package sistemaDeGerenciamentoDeEventos.dominio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Evento{

    private int id, capacidadeMax, limiteProm;
    private String titulo, descricao, local;
    private LocalDate data;
    private LocalTime horaInicio;
    private BigDecimal precoBase;
    private List<Venda> vendas;

    public Evento(int id, String titulo, String local, int capacidadeMax,BigDecimal preco, int limiteProm){
        this.id = id;
        this.titulo = titulo;
        this.local = local;
        this.capacidadeMax = capacidadeMax;
        this.precoBase = preco;
    }

    public void addListaVenda(Venda aux){
        vendas.add(aux);
    }

    public int calcularDisponibilidade(){
        long qtdIngressos = vendas.stream()
                                  .count();

        return  capacidadeMax - (int) qtdIngressos;
    }

    public boolean disponibilidadePromocional(){
        long qtdProm = vendas.stream()
                             .flatMap(venda -> venda.getIngressos().stream())
                             .filter(i -> i instanceof IngressoPromocional)
                             .count();

        return qtdProm < limiteProm;
    }

    public BigDecimal getPrecoBase(){
        return precoBase;
    }

    public int getId() {
        return id;
    }

    public int getCapacidadeMax() {
        return capacidadeMax;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getLocal() {
        return local;
    }

    public LocalDate getData() {
        return data;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public List<Venda> getVendas() {
        return List.copyOf(vendas);
    }
}
